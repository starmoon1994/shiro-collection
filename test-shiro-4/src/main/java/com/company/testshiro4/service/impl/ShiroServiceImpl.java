package com.company.testshiro4.service.impl;


import com.company.testshiro4.entity.SecMenu;
import com.company.testshiro4.entity.SecUser;
import com.company.testshiro4.entity.vo.MenuVo;
import com.company.testshiro4.mapper.SecMenuMapper;
import com.company.testshiro4.service.SecMenuService;
import com.company.testshiro4.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * shiro以及安全权限相关的业务
 * Created by hxy on 2018/6/19.
 */

@Service
public class ShiroServiceImpl implements ShiroService {

    @Resource
    private SecMenuMapper secMenuMapper;

    @Autowired
    private SecMenuService secMenuService;

    @Override
    public List<MenuVo> getUserMenuList(SecUser userLogined) {
//        List<SecMenu> secMenuList = secMenuMapper.selectlistModuleByRoleIds(userLogined.getRoleIds());

        // 获取有效菜单
        List<SecMenu> secMenuList = secMenuService.getStringPermissionList(userLogined);

        // 组装菜单
        List<MenuVo> menuVos = secMenuService.handleMenuList(secMenuList);

        return menuVos;
    }

}
