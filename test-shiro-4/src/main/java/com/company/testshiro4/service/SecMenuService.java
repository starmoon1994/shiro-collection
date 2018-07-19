package com.company.testshiro4.service;


import com.company.testshiro4.entity.SecMenu;
import com.company.testshiro4.entity.SecUser;
import com.company.testshiro4.entity.vo.MenuVo;

import java.util.List;

/**
 * Created by hyp-company on 2018/6/26.
 */
public interface SecMenuService {
    List<SecMenu> getStringPermissionList(SecUser secUser);

    /**
     * 组装结构化的菜单列表
     * @param secMenuList
     * @return
     */
    public List<MenuVo> handleMenuList(List<SecMenu> secMenuList);

    List<SecMenu> selectList();

    int addMenu(SecMenu secMenu);

    int removeMenu(SecMenu secMenu);

    int updateMenu(SecMenu secMenu);

    SecMenu getMenu(long id);

    List<SecMenu> getList(int state);
}
