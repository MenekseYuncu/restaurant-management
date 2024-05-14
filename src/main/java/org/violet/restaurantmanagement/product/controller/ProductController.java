package org.violet.restaurantmanagement.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.product.controller.mapper.ProductCreateRequestToCreateCommandMapper;
import org.violet.restaurantmanagement.product.controller.mapper.ProductUpdateRequestToProductUpdateCommandMapper;
import org.violet.restaurantmanagement.product.controller.request.ProductCreateRequest;
import org.violet.restaurantmanagement.product.controller.request.ProductUpdateRequest;
import org.violet.restaurantmanagement.product.service.ProductService;
import org.violet.restaurantmanagement.product.service.command.ProductCreateCommand;
import org.violet.restaurantmanagement.product.service.command.ProductUpdateCommand;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private static final ProductCreateRequestToCreateCommandMapper productCreateRequestToCommandMapper = ProductCreateRequestToCreateCommandMapper.INSTANCE;
    private static final ProductUpdateRequestToProductUpdateCommandMapper productUpdateRequestToCommandMapper = ProductUpdateRequestToProductUpdateCommandMapper.INSTANCE;


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
