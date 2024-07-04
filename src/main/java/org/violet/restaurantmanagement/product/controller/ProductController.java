package org.violet.restaurantmanagement.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.common.controller.response.RmaPageResponse;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.product.controller.mapper.ProductCreateRequestToCreateCommandMapper;
import org.violet.restaurantmanagement.product.controller.mapper.ProductListRequestToListCommandMapper;
import org.violet.restaurantmanagement.product.controller.mapper.ProductToProductListResponseMapper;
import org.violet.restaurantmanagement.product.controller.mapper.ProductToProductResponseMapper;
import org.violet.restaurantmanagement.product.controller.mapper.ProductUpdateRequestToUpdateCommandMapper;
import org.violet.restaurantmanagement.product.controller.request.ProductCreateRequest;
import org.violet.restaurantmanagement.product.controller.request.ProductListRequest;
import org.violet.restaurantmanagement.product.controller.request.ProductUpdateRequest;
import org.violet.restaurantmanagement.product.controller.response.ProductListResponse;
import org.violet.restaurantmanagement.product.controller.response.ProductResponse;
import org.violet.restaurantmanagement.product.service.ProductService;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    private static final ProductCreateRequestToCreateCommandMapper productCreateRequestToCommandMapper = ProductCreateRequestToCreateCommandMapper.INSTANCE;
    private static final ProductUpdateRequestToUpdateCommandMapper productUpdateRequestToCommandMapper = ProductUpdateRequestToUpdateCommandMapper.INSTANCE;
    private static final ProductToProductListResponseMapper productToProductListResponse = ProductToProductListResponseMapper.INSTANCE;
    private static final ProductToProductResponseMapper productToProductResponse = ProductToProductResponseMapper.INSTANCE;
    private static final ProductListRequestToListCommandMapper productListRequestToListCommandMapper = ProductListRequestToListCommandMapper.INSTANCE;


    @PostMapping("/products")
    public BaseResponse<RmaPageResponse<ProductListResponse>> getAllProducts(
            @Valid @RequestBody ProductListRequest request
    ) {
        RmaPage<Product> productPage = productService.getAllProducts(
                productListRequestToListCommandMapper.map(request)
        );

        RmaPageResponse<ProductListResponse> pageResponse = RmaPageResponse.<ProductListResponse>builder()
                .content(productToProductListResponse.map(productPage.getContent()))
                .page(productPage)
                .build();
        return BaseResponse.successOf(pageResponse);

    }

    @GetMapping("/{id}")
    public BaseResponse<ProductResponse> getProductById(
            @PathVariable String id
    ){
        Product product = productService.getProductById(id);
        ProductResponse productResponse = productToProductResponse.map(product);
        return BaseResponse.successOf(productResponse);
    }

    @PostMapping
    public BaseResponse<Void> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        ProductCreateCommand createCommand = productCreateRequestToCommandMapper.map(request);
        productService.createProduct(createCommand);
        return BaseResponse.SUCCESS;
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        ProductUpdateCommand command = productUpdateRequestToCommandMapper.map(request);
        productService.updateProduct(id, command);
        return BaseResponse.SUCCESS;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteProduct(
            @PathVariable String id
    ) {
        productService.deleteProduct(id);
        return BaseResponse.SUCCESS;
    }
}
