package com.company.testshiro4.entity;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Created by hyp-company on 2018/6/25.
 */
@Document(collection = "shiro_session")

public class ShiroSessionBean   implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    // 主键使用此注解
    @Id
    private String id;

    // 字段使用此注解
    @Field
    private String sessionValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionValue() {
        return sessionValue;
    }

    public void setSessionValue(String sessionValue) {
        this.sessionValue = sessionValue;
    }
}
