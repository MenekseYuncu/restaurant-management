package org.violet.restaurantmanagement.menu.service;

import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.menu.service.command.MenuListCommand;
import org.violet.restaurantmanagement.menu.service.domain.Menu;

public interface MenuService {

    RmaPage<Menu> getAllMenu(MenuListCommand menuListCommand);
}
