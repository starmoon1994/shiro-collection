package com.company.testshiro4.shiro;

import com.company.testshiro4.entity.SecMenu;
import com.company.testshiro4.entity.SecRole;
import com.company.testshiro4.entity.SecUser;
import com.company.testshiro4.mapper.SecUserMapper;
import com.company.testshiro4.service.SecMenuService;
import com.company.testshiro4.service.SecRoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private SecUserMapper secUserMapper;

    @Autowired
    private SecRoleService secRoleService;

    @Autowired
    private SecMenuService secMenuService;


    /**
     * 授权
     * 先取出角色  再取角色对应的权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SecUser secUser = (SecUser) principals.getPrimaryPrincipal();


        // 赋予角色信息
        List<SecRole> secRoleList = secRoleService.getRoleList(secUser);
        for (SecRole singleRole : secRoleList) {
            authorizationInfo.addRole(singleRole.getCode());
        }

        // 赋予权限
        List<SecMenu> secMenuList = secMenuService.getStringPermissionList(secUser);
        for (SecMenu secMenu : secMenuList) {
            List<String> permissionList = secMenu.getPermission();
            if (permissionList != null && permissionList.size() > 0) {
                for (String singleOne : permissionList) {
                    authorizationInfo.addStringPermission(singleOne);
                }
            }
        }

        return authorizationInfo;
    }


    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();

        SecUser userInfo = secUserMapper.findByUsername(username);

        if (userInfo == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        if (userInfo.getState() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户实体
                userInfo.getPassword(), //密码
                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

}