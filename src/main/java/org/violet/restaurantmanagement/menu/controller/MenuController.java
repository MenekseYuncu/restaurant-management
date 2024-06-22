package org.violet.restaurantmanagement.menu.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.common.controller.response.RmaPageResponse;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.menu.controller.mapper.MenuListRequestToListCommand;
import org.violet.restaurantmanagement.menu.controller.mapper.ProductToMenuListResponseMapper;
import org.violet.restaurantmanagement.menu.controller.request.MenuListRequest;
import org.violet.restaurantmanagement.menu.controller.response.MenuListResponse;
import org.violet.restaurantmanagement.menu.service.MenuService;
import org.violet.restaurantmanagement.product.service.domain.Product;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuService menuService;

    private static final MenuListRequestToListCommand menuListRequestToListCommand = MenuListRequestToListCommand.INSTANCE;
    private static final ProductToMenuListResponseMapper productToMenuListResponse = ProductToMenuListResponseMapper.INSTANCE;


    @PostMapping
    public BaseResponse<RmaPageResponse<MenuListResponse>> getAllMenu(
            @Valid @RequestBody MenuListRequest request
    ) {
        RmaPage<Product> productRmaPage = menuService.getAllMenu(
                menuListRequestToListCommand.map(request)
        );

        RmaPageResponse<MenuListResponse> pageResponse = RmaPageResponse.<MenuListResponse>builder()
                .content(productToMenuListResponse.map(productRmaPage.getContent()))
                .page(productRmaPage)
                .build();

        return BaseResponse.successOf(pageResponse);
    }

}
