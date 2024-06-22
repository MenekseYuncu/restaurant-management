package org.violet.restaurantmanagement.menu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.enums.RmaCurrency;
import org.violet.restaurantmanagement.menu.service.MenuService;
import org.violet.restaurantmanagement.menu.service.command.MenuListCommand;
import org.violet.restaurantmanagement.product.model.mapper.ProductEntityToDomainMapper;
import org.violet.restaurantmanagement.product.repository.ProductRepository;
import org.violet.restaurantmanagement.product.repository.entity.ProductEntity;
import org.violet.restaurantmanagement.product.service.domain.Product;

import java.util.List;


@Service
@RequiredArgsConstructor
class MenuServiceImpl implements MenuService {

    private final ProductRepository productRepository;
    private final RmaCurrency currency;

    private static final ProductEntityToDomainMapper productEntityToDomainMapper = ProductEntityToDomainMapper.INSTANCE;


    @Override
    public RmaPage<Product> getAllMenu(MenuListCommand menuListCommand) {
        Page<ProductEntity> productEntityPage = productRepository.findAll(
                menuListCommand.toSpecification(ProductEntity.class),
                menuListCommand.toPageable()
        );

        List<Product> products = productEntityToDomainMapper.map(productEntityPage.getContent());
        products.forEach(product -> product.setCurrency(currency));

        return RmaPage.<Product>builder()
                .content(products)
                .page(productEntityPage)
                .sortedBy(menuListCommand.getSorting())
                .filteredBy(menuListCommand.getFilter())
                .build();
    }
}
