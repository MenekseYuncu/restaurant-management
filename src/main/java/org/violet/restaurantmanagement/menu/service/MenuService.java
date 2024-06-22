package org.violet.restaurantmanagement.menu.service;

import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.menu.service.command.MenuListCommand;
import org.violet.restaurantmanagement.product.service.domain.Product;

public interface MenuService {

    RmaPage<Product> getAllMenu(MenuListCommand menuListCommand);
}
