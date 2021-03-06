package com.company.testshiro1;

import com.alibaba.fastjson.JSONObject;
import com.company.testshiro1.support.RetVO;
import com.company.testshiro1.support.SecUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 身份认证相关的controller
 */
@RestController
public class AuthenController {

    /**
     * ajaxLogin登录方法
     */
    @RequestMapping(value = "/ajaxLogin.do", method = RequestMethod.POST)
    public RetVO ajaxLogin(HttpServletResponse response, @RequestBody SecUser secUser) {
        RetVO vo = new RetVO();
        JSONObject retJsonObject = new JSONObject();

        // 从SHiro获取处理器 Subject是Shiro的核心对象，基本所有身份验证、授权都是通过Subject完成。
        Subject subject = SecurityUtils.getSubject();

        // 组装安全对象
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(secUser.getUsername(), secUser.getPassword());
        usernamePasswordToken.setRememberMe(true);

        try {
            // 调用Shiro的Subject类去登录
            subject.login(usernamePasswordToken);

            // TODO:此处可做一些登录成功后的处理

            // 从Shiro获取用户信息
            SecUser userLogined = (SecUser) subject.getPrincipal();

            // TODO:生成自定义cookie
            CustomCookie(response, userLogined);

            vo.setMsg("登录成功");

        } catch (IncorrectCredentialsException e) {
            // TODO: 处理异常  建议全局处理 统一返回 此处仅做展示
//            throw new AuthDeniedException("密码错误");
            throw e;
        } catch (LockedAccountException e) {
            vo.setMsg("登录失败，该用户已被冻结");
//            throw new AuthDeniedException("登录失败，该用户已被冻结");
            throw e;
        } catch (AuthenticationException e) {
            vo.setMsg("该用户不存在");
//            throw new AuthDeniedException("该用户不存在");
            throw e;
        } catch (Exception e) {
            // 处理不了的异常 直接抛出  由统一异常处理器兜底
            e.printStackTrace();
            throw e;
        }

        vo.setData(retJsonObject);
        return vo;
    }

    private void CustomCookie(HttpServletResponse response, SecUser userLogined) {
        Cookie tokenCookie = new Cookie("g_tk", String.valueOf(userLogined.getId()));
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(false);
        tokenCookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(tokenCookie);

        Cookie usernameCookie = new Cookie("g_n", userLogined.getUsername());
        usernameCookie.setPath("/");
        usernameCookie.setHttpOnly(false);
        usernameCookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(usernameCookie);
    }


    /**
     * 退出登录
     */
    @RequestMapping(value = "/ajaxLogout.do", method = RequestMethod.POST)
    public RetVO ajaxLogout(HttpServletResponse response) {
        RetVO retVO = new RetVO();

        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        retVO.setMsg("登出成功");
        return retVO;
    }

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     *
     * @return
     */
    @RequestMapping(value = "/unauth.do")
    public RetVO unauth(HttpServletResponse response) {
        RetVO vo = new RetVO();

        vo.setCode(401);
        vo.setMsg("未登录 或 登录信息过期");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return vo;
    }


}
