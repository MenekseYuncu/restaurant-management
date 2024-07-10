package org.violet.restaurantmanagement.menu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.menu.service.MenuService;
import org.violet.restaurantmanagement.menu.service.command.MenuListCommand;
import org.violet.restaurantmanagement.menu.service.domain.Menu;
import org.violet.restaurantmanagement.menu.service.mapper.ProductToMenuMapper;
import org.violet.restaurantmanagement.product.model.mapper.ProductEntityToDomainMapper;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.domain.Product;

import java.util.List;


@Service
@RequiredArgsConstructor
class MenuServiceImpl implements MenuService {

    private final ProductRepository productRepository;
    private final String currency;

    private static final ProductEntityToDomainMapper productEntityToDomainMapper = ProductEntityToDomainMapper.INSTANCE;
    private static final ProductToMenuMapper productToMenuMapper = ProductToMenuMapper.INSTANCE;


    @Override
    public RmaPage<Menu> getAllMenu(MenuListCommand menuListCommand) {
        Page<ProductEntity> productEntityPage = productRepository.findAll(
                menuListCommand.toSpecification(ProductEntity.class),
                menuListCommand.toPageable()
        );

        List<Product> products = productEntityToDomainMapper.map(productEntityPage.getContent());
        products.forEach(product -> product.setCurrency(currency));

        return RmaPage.<Menu>builder()
                .content(productToMenuMapper.map(products))
                .page(productEntityPage)
                .sortedBy(menuListCommand.getSorting())
                .filteredBy(menuListCommand.getFilter())
                .build();
    }
}
