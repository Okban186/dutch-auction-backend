package com.auction.dutch.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.auction.dutch.config.properties.MediaProperties;
import com.auction.dutch.config.properties.StorageProperties;
import com.auction.dutch.enums.ProductStatus;
import com.auction.dutch.enums.StorageBucket;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.mapper.ProductMapper;
import com.auction.dutch.model.dto.request.CreateProductRequest;
import com.auction.dutch.model.dto.response.ProductDetailResponse;
import com.auction.dutch.model.dto.response.ProductItemResponse;
import com.auction.dutch.model.dto.response.ProductMediaResponse;
import com.auction.dutch.model.entity.Brand;
import com.auction.dutch.model.entity.Category;
import com.auction.dutch.model.entity.Product;
import com.auction.dutch.model.entity.ProductMedia;
import com.auction.dutch.repository.BrandRepository;
import com.auction.dutch.repository.CategoryRepository;
import com.auction.dutch.repository.ProductMediaRepository;
import com.auction.dutch.repository.ProductRepository;
import com.auction.dutch.service.storage.StorageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;
    private final StorageService storageService;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final MediaProperties mediaProperties;

    private String generateProductCode() {
        String productCode = "PRD-" +
                LocalDate.now().getYear() +
                "-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();

        return productCode;
    }

    public Product getActiveProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (product.getStatus() == ProductStatus.DELETED || product.getStatus() == ProductStatus.INACTIVE)
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        return product;
    }

    public List<ProductItemResponse> getProducts() {
        List<Product> products = productRepository.findAllByStatus(ProductStatus.ACTIVE);

        if (products.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        List<ProductMedia> firstMediaList = productMediaRepository.findFirstMediaByProductIds(productIds);
        List<ProductItemResponse> productItemResponses = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            ProductMedia productMedia = firstMediaList.get(i);
            productItemResponses.add(ProductItemResponse.builder()
                    .id(product.getId())
                    .code(product.getCode())
                    .name(product.getName())
                    .stockQuantity(product.getStockQuantity())
                    .viewUrl(storageService.generatePresignedViewUrl(StorageBucket.PRODUCT_MEDIA.getBucketName(),
                            productMedia.getStorageKey(), mediaProperties.presigned().viewTtl()))
                    .build());
        }

        return productItemResponses;

    }

    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = getActiveProduct(productId);
        List<ProductMedia> productMediaList = productMediaRepository.findAllByProductId(productId);
        List<ProductMediaResponse> productMediaResponses = new ArrayList<>();
        for (ProductMedia item : productMediaList) {
            productMediaResponses.add(
                    ProductMediaResponse.builder()
                            .id(item.getId())
                            .mediaType(item.getMediaType())
                            .displayOrder(item.getDisplayOrder())
                            .mediaUrl(
                                    storageService.generatePresignedViewUrl(StorageBucket.PRODUCT_MEDIA.getBucketName(),
                                            item.getStorageKey(), mediaProperties.presigned().viewTtl()))
                            .build());
        }

        ProductDetailResponse pDetailResponse = productMapper.toDetailResponse(product, productMediaResponses);
        return pDetailResponse;
    }

    public ProductDetailResponse createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Brand brand = null;
        if (request.brandId() != null) {
            brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        }

        Product product = Product.builder()
                .code(generateProductCode())
                .name(request.name())
                .category(category)
                .brand(brand)
                .description(request.description())
                .stockQuantity(request.stockQuantity()).build();

        productRepository.save(product);
        return productMapper.toDetailResponse(product, null);
    }

    @Transactional
    public void deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setStatus(ProductStatus.INACTIVE);
    }

    @Transactional
    public void activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setStatus(ProductStatus.ACTIVE);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setStatus(ProductStatus.DELETED);
    }

}
