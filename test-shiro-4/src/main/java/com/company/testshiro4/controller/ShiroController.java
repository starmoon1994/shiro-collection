package com.company.testshiro4.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.testshiro4.entity.SecUser;
import com.company.testshiro4.entity.vo.MenuVo;
import com.company.testshiro4.mapper.SecUserMapper;
import com.company.testshiro4.service.ShiroService;
import com.company.testshiro4.support.RetVO;
import com.company.testshiro4.support.exception.AuthDeniedException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ShiroController {

    @Autowired
    private SecUserMapper secUserMapper;

    @Autowired
    private ShiroService shiroService;

    @RequiresRoles(value = "admin")
    @RequestMapping("/findByUsername.do")
    public RetVO testboot() {
        RetVO retVO = new RetVO();
        SecUser user = secUserMapper.findByUsername("admin");
        retVO.setData(user);
        return retVO;
    }

    /**
     * 登录方法
     */
    @RequestMapping(value = "/ajaxLogin.do", method = RequestMethod.POST)
    public RetVO ajaxLogin(HttpServletResponse response, @RequestBody SecUser secUser) {

        RetVO vo = new RetVO();
        JSONObject retJsonObject = new JSONObject();
        // 从SHiro获取处理器 Subject是Shiro的核心对象，基本所有身份验证、授权都是通过Subject完成。
        Subject subject = SecurityUtils.getSubject();
        // 组装安全对象
        UsernamePasswordToken token = new UsernamePasswordToken(secUser.getUsername(), secUser.getPassword());

        try {
            // 调用Shiro的Subject类去登录
            subject.login(token);
            // 从Shiro获取用户信息
            SecUser userLogined = (SecUser) subject.getPrincipal();

            retJsonObject.put("token", subject.getSession().getId());

            vo.setMsg("登录成功");

            //isAuthenticated()获取登录释放成功的结果
            boolean authenticated = subject.isAuthenticated();
            // TODO:生成菜单列表信息
            List<MenuVo> menuVoList = shiroService.getUserMenuList(userLogined);

            retJsonObject.put("menuVoList", menuVoList);

            // TODO:生成cookie
            Cookie cookie = new Cookie("g_id", String.valueOf(userLogined.getUid()));

            cookie.setMaxAge(7 * 24 * 60 * 60);

            response.addCookie(cookie);

        } catch (IncorrectCredentialsException e) {
            throw new AuthDeniedException("密码错误");
        } catch (LockedAccountException e) {
            vo.setMsg("登录失败，该用户已被冻结");
            throw new AuthDeniedException("登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            vo.setMsg("该用户不存在");
            throw new AuthDeniedException("该用户不存在");
        } catch (Exception e) {
            // 处理不了的异常 直接抛出  由统一异常处理器兜底
            e.printStackTrace();
            throw e;
        }

        vo.setData(retJsonObject);
        return vo;
    }


    /**
     * 退出登录
     */
    @RequestMapping(value = "/ajaxLogout.do", method = RequestMethod.POST)
    public RetVO ajaxLogout(HttpServletResponse response, @RequestBody SecUser secUser) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        subject.isAuthenticated();


        RetVO retVO = new RetVO();
        retVO.setMsg("登出成功");

        return retVO;
    }

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     *
     * @return
     */
    @RequestMapping(value = "/unauth.do")
    public RetVO unauth() {
        RetVO vo = new RetVO();
        vo.setCode(301);
        vo.setMsg("未登录");
        return vo;
    }
}
