package org.violet.restaurantmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class RmaControllerTest {

    @Autowired
    protected MockMvc mockMvc;

}
