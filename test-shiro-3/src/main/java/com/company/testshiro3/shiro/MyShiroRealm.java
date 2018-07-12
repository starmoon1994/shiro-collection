package com.company.testshiro3.shiro;

import com.company.testshiro3.support.MockUserList;
import com.company.testshiro3.support.SecUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
public class MyShiroRealm extends AuthorizingRealm {


    /**
     * 授权
     * 先取出角色  再取角色对应的权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Object primaryPrincipal = principals.getPrimaryPrincipal();

//        SecUser secUser = (SecUser) primaryPrincipal;
        SecUser secUser =new SecUser();

        BeanUtils.copyProperties(primaryPrincipal ,secUser);


                // 赋予角色信息
        authorizationInfo.addRole(secUser.getRoles());

        // 赋予权限
        authorizationInfo.addStringPermission(secUser.getPermissions());

        return authorizationInfo;
    }


    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();

        SecUser userInfo = MockUserList.getMockUser(username);

        if (userInfo == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户实体
                userInfo.getPassword(), //密码
                ByteSource.Util.bytes(""),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

}