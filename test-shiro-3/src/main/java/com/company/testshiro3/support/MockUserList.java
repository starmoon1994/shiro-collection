package com.company.testshiro3.support;


import org.springframework.util.StringUtils;

public class MockUserList {


    public static SecUser getMockUser(String username) {

        if (!StringUtils.isEmpty(username) && "admin".equals(username)) {
            SecUser secUser = new SecUser();
            secUser.setId(1);
            secUser.setUsername("admin");
            secUser.setPassword("123456");
            secUser.setRoles("admin");
            secUser.setPermissions("admin:get");
            return secUser;
        } else {
            SecUser secUser = new SecUser();
            secUser.setId(2);
            secUser.setUsername("saler");
            secUser.setPassword("123456");
            secUser.setRoles("saler");
            secUser.setPermissions("saler:get");
            return secUser;
        }
    }


}


