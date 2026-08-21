package com.auction.dutch.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import com.auction.dutch.cache.UploadTrackingService;
import com.auction.dutch.cache.impl.RedisUploadTrackingService;
import com.auction.dutch.config.properties.MediaProperties;
import com.auction.dutch.enums.MediaType;
import com.auction.dutch.enums.ProductStatus;
import com.auction.dutch.enums.StorageBucket;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.model.dto.internal.DetectedMediaInfo;
import com.auction.dutch.model.dto.request.ConfirmMediaItemRequest;
import com.auction.dutch.model.dto.request.ConfirmProductMediaRequest;
import com.auction.dutch.model.dto.request.DeleteProductMediaRequest;
import com.auction.dutch.model.dto.request.MediaOrderUpdate;
import com.auction.dutch.model.dto.request.UpdateProductMediaOrderRequest;
import com.auction.dutch.model.dto.response.ProductMediaResponse;
import com.auction.dutch.model.entity.Product;
import com.auction.dutch.model.entity.ProductMedia;
import com.auction.dutch.repository.ProductMediaRepository;
import com.auction.dutch.repository.ProductRepository;
import com.auction.dutch.service.storage.StorageService;
import com.auction.dutch.util.MediaOrderPolicy;
import com.auction.dutch.validator.ProductMediaValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductMediaService {

  private final ProductRepository productRepository;

  private final ProductMediaRepository productMediaRepository;

  private final ProductMediaValidator productMediaValidator;

  private final StorageService storageService;

  private final UploadTrackingService uploadTrackingService;

  private final Tika tika = new Tika();

  private final MediaProperties mediaProperties;

  private final RedisUploadTrackingService redisUploadTrackingService;

  @Transactional
  public List<ProductMediaResponse> confirmMedia(

      Long sellerId,

      Long productId,

      ConfirmProductMediaRequest request) {

    if (request.items().size() > (mediaProperties.maxImagePerProduct() + mediaProperties.maxVideoPerProduct()))
      throw new AppException(ErrorCode.INVALID_REQUEST);
    for (ConfirmMediaItemRequest item : request.items()) {
      if (redisUploadTrackingService.existsUploadSession(item.storageKey()))
        throw new AppException(ErrorCode.INVALID_MEDIA_REQUEST);
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(
            () -> new AppException(
                ErrorCode.PRODUCT_NOT_FOUND));
    if (product.getStatus() == ProductStatus.DELETED)
      throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

    List<DetectedMediaInfo> detectedMediaList = new ArrayList<>();

    Set<Integer> requestedOrders = request.items()
        .stream()
        .map(ConfirmMediaItemRequest::displayOrder)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    List<Integer> existsDisplayOrders = productMediaRepository.findExistingDisplayOrders(productId, requestedOrders);

    for (ConfirmMediaItemRequest item : request.items()) {

      if (existsDisplayOrders.contains(item.displayOrder()))
        continue;

      try (InputStream is = storageService.getObject(
          StorageBucket.PRODUCT_MEDIA.getBucketName(),
          item.storageKey())) {

        String mimeType = tika.detect(is);

        MediaType actualType = resolveMediaType(
            mimeType);

        long fileSize = storageService.getObjectSize(
            StorageBucket.PRODUCT_MEDIA.getBucketName(),
            item.storageKey());

        detectedMediaList.add(new DetectedMediaInfo(
            item.storageKey(),
            item.mediaType(),
            actualType,
            mimeType,
            item.displayOrder(),
            fileSize));

      } catch (Exception e) {

        throw new AppException(
            ErrorCode.INVALID_MEDIA_FILE);
      }
    }

    productMediaValidator.validateConfirmedMedia(
        productId,
        detectedMediaList);

    List<ProductMedia> entities = new ArrayList<>();

    for (DetectedMediaInfo detected : detectedMediaList) {

      redisUploadTrackingService.saveUploadSession(sellerId, productId, detected.storageKey());
      String finalKey = generateFinalStorageKey(
          productId,
          detected.mimeType());

      storageService.moveObject(
          StorageBucket.PRODUCT_MEDIA.getBucketName(),
          detected.storageKey(),
          finalKey);

      ProductMedia media = ProductMedia.builder()
          .product(product)
          .storageKey(finalKey)
          .mediaType(detected.actualType())
          .displayOrder(detected.displayOrder())
          .build();

      entities.add(media);
    }
    productMediaRepository.saveAll(
        entities);

    uploadTrackingService.decreasePendingUploads(
        sellerId,
        entities.size());

    return entities.stream()
        .map(this::toResponse)
        .toList();
  }

  private MediaType resolveMediaType(
      String mimeType) {

    for (MediaType type : MediaType.values()) {

      if (type.matches(mimeType)) {
        return type;
      }
    }

    throw new AppException(
        ErrorCode.INVALID_MEDIA_FILE);
  }

  private String generateFinalStorageKey(Long productId, String mimeType) {

    return "products/"
        + productId
        + "/"
        + UUID.randomUUID()
            .toString()
            .replace("-", "")
        + resolveExtension(mimeType);
  }

  private ProductMediaResponse toResponse(
      ProductMedia media) {

    return ProductMediaResponse.builder()
        .id(media.getId())
        .mediaType(media.getMediaType())
        .displayOrder(media.getDisplayOrder())
        .mediaUrl(
            storageService.generatePresignedViewUrl(
                StorageBucket.PRODUCT_MEDIA.getBucketName(),
                media.getStorageKey(),
                mediaProperties.presigned().viewTtl()))
        .build();
  }

  private String resolveExtension(String mimeType) {

    return switch (mimeType) {

      case "image/jpeg" -> ".jpg";

      case "image/png" -> ".png";

      case "image/webp" -> ".webp";

      case "video/mp4" -> ".mp4";

      case "video/quicktime" -> ".mov";

      default ->
        throw new AppException(
            ErrorCode.INVALID_MEDIA_FILE);
    };
  }

  @Transactional
  public void deleteProductMedia(Long productId, DeleteProductMediaRequest request) {
    List<ProductMedia> productMediaList = productMediaRepository.findAllByIdInAndProductId(request.mediaIds(),
        productId);
    for (ProductMedia productMedia : productMediaList) {
      storageService.deleteObject(StorageBucket.PRODUCT_MEDIA.getBucketName(), productMedia.getStorageKey());
    }
    productMediaRepository.deleteAll(productMediaList);
  }

  @Transactional
  public void updateOrder(
      Long productId,
      UpdateProductMediaOrderRequest request) {

    Product product = productRepository.findByIdForUpdate(productId)
        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    if (product.getStatus() == ProductStatus.DELETED)
      throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
    validateRequest(request);

    Set<Long> mediaIds = request.media()
        .stream()
        .map(MediaOrderUpdate::mediaId)
        .collect(Collectors.toSet());

    List<ProductMedia> medias = productMediaRepository
        .findAllByProductIdAndIdIn(
            productId,
            mediaIds);

    Map<Long, ProductMedia> mediaMap = medias.stream()
        .collect(Collectors.toMap(ProductMedia::getId, Function.identity()));

    if (mediaMap.size() != mediaIds.size()) {

      throw new AppException(
          ErrorCode.MEDIA_NOT_ASSOCIATED_WITH_PRODUCT);
    }

    int temporaryOrder = -10_000_000;

    for (MediaOrderUpdate update : request.media()) {

      ProductMedia media = mediaMap.get(
          update.mediaId());

      media.setDisplayOrder(temporaryOrder++);
    }

    productMediaRepository.flush();

    for (MediaOrderUpdate update : request.media()) {

      ProductMedia media = mediaMap.get(update.mediaId());

      media.setDisplayOrder(update.displayOrder());
    }

    productMediaRepository.saveAll(mediaMap.values());

    productMediaRepository.flush();
  }

  private void validateRequest(
      UpdateProductMediaOrderRequest request) {

    if (request == null || request.media() == null || request.media().isEmpty()) {

      throw new AppException(ErrorCode.INVALID_MEDIA_ORDER);
    }

    Set<Long> mediaIds = new HashSet<>();

    Set<Integer> displayOrders = new HashSet<>();

    for (MediaOrderUpdate update : request.media()) {

      if (update.mediaId() == null || update.displayOrder() == null) {

        throw new AppException(ErrorCode.INVALID_MEDIA_ORDER);
      }

      if (!mediaIds.add(
          update.mediaId())) {

        throw new AppException(ErrorCode.DUPLICATE_MEDIA_OPERATION);
      }

      int order = update.displayOrder();

      if (order < MediaOrderPolicy.SAFE_MIN || order > MediaOrderPolicy.SAFE_MAX) {

        throw new AppException(ErrorCode.INVALID_MEDIA_ORDER);
      }

      if (!displayOrders.add(order)) {

        throw new AppException(ErrorCode.INVALID_MEDIA_ORDER);
      }
    }
  }

  public List<ProductMediaResponse> getProductMediaList(Long productId) {
    List<ProductMedia> productMediaList = productMediaRepository.findAllByProductId(productId);

    return productMediaList.stream().map(this::toResponse).toList();
  }
}
