package com.company.testshiro3.shiro;

import com.alibaba.fastjson.JSONObject;
import com.company.testshiro3.support.RetVO;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);

    // 针对shiro默认的表单登录成功
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {

        // 可在这里面处理登录成功的操作 如日志记录 cookie信息
        System.out.println("CustomFormAuthenticationFilter onLoginSuccess");

        // 重定向到配置的登录成功接口
        // issueSuccessRedirect(request, response);

        RetVO retVO = new RetVO();
        retVO.setMsg("loginSuccess from CustomFormAuthenticationFilter onLoginSuccess");
        response.setContentType("application/json");
        response.getOutputStream().print(JSONObject.toJSONString(retVO));
        response.getOutputStream().flush();

        //we handled the success redirect directly, prevent the chain from continuing:
        // return false时 不会继续执行chain
        return false;
    }

    // 针对shiro默认的表单登录
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        // 可在这里面处理登录失败的情况 如日志记录
        System.out.println("CustomFormAuthenticationFilter onLoginFailure");

        if (log.isDebugEnabled()) {
            log.debug("Authentication exception", e);
        }
        setFailureAttribute(request, e);
        //login failed, let request continue back to the login page:
        return true;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // TODO:可在这里面处理登录失败的情况 如日志记录 黑名单 试错次数等
        System.out.println("CustomFormAuthenticationFilter onAccessDenied");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        log.info("有人访问{}接口,但未经授权", httpServletRequest.getRequestURI());


        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                log.info("有人访问{}接口,但未经授权,但发现是login请求", httpServletRequest.getRequestURI());
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }


}
