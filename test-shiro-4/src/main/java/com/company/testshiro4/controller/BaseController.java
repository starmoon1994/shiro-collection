package com.company.testshiro4.controller;

import com.company.testshiro4.entity.SecUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 基础Controller  此类将存放controller层的公共工具类
 * Created by hyp-company on 2018/6/20.
 */
public class BaseController {

    /**
     * 获取当前用户信息
     *
     * @return SecUser
     */
    public synchronized SecUser getSecUser() {
        Subject subject = SecurityUtils.getSubject();

        SecUser userLogined = (SecUser) subject.getPrincipal();

        return userLogined;
    }
}
