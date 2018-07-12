package com.company.testshiro3.support;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = AuthorizationException.class)
    public void unLoginHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        logger.error("授权异常", e);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public void UnauthorizedExceptionHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        logger.error("shiro权限异常", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "权限不足：" + e.getMessage());
    }

    @ExceptionHandler(value = UnauthenticatedException.class)
    public void UnauthenticatedExceptionHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        logger.error("Shiro身份验证异常", e);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "身份验证异常：" + e.getMessage());
    }

    /*@ExceptionHandler(value = BusinessException.class)
    public void businessHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        logger.error("业务异常", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }*/

    @ExceptionHandler(value = Exception.class)
    public void jsonErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        logger.error("其他异常", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
