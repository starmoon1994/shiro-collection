package com.company.testshiro4.entity;

import java.io.Serializable;

/**
 * SecUser的子类 多加了一个字段 sessionId
 * 用于展现shiro的在线用户
 */
public class SecUserSO extends SecUser implements Serializable {
    private static final long serialVersionUID = 1456217245177063996L;

    private String sessionId;


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
