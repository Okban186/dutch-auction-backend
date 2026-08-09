package com.auction.dutch.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auction.dutch.enums.ProductStatus;
import com.auction.dutch.exception.AppException;
import com.auction.dutch.exception.ErrorCode;
import com.auction.dutch.mapper.ProductMapper;
import com.auction.dutch.model.dto.request.CreateProductRequest;
import com.auction.dutch.model.dto.response.ProductDetailResponse;
import com.auction.dutch.model.dto.response.ProductItemResponse;
import com.auction.dutch.model.entity.Brand;
import com.auction.dutch.model.entity.Category;
import com.auction.dutch.model.entity.CategoryAttribute;
import com.auction.dutch.model.entity.Product;
import com.auction.dutch.repository.BrandRepository;
import com.auction.dutch.repository.CategoryRepository;
import com.auction.dutch.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

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
        return productMapper.toItemResponses(products);
    }

    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = getActiveProduct(productId);
        ProductDetailResponse pDetailResponse = productMapper.toDetailResponse(product);

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
        return productMapper.toDetailResponse(product);
    }

    public void deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setStatus(ProductStatus.INACTIVE);
    }

    public void activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setStatus(ProductStatus.ACTIVE);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setStatus(ProductStatus.DELETED);
    }

}
