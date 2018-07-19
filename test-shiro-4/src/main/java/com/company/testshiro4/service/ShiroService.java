package com.company.testshiro4.service;


import com.company.testshiro4.entity.SecUser;
import com.company.testshiro4.entity.vo.MenuVo;

import java.util.List;

/**
 * 权限和安全相关的业务
 * Created by hxy on 2018/6/19.
 */
public interface ShiroService {


    // 根据用户角色获取菜单列表
    List<MenuVo> getUserMenuList(SecUser userLogined);

}
