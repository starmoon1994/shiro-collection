package com.company.testshiro4.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by hyp-company on 2018/6/25.
 */
@Document(collection = "coll_user")
public class TestUserInfo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    // 主键使用此注解
    @Id
    private String id;

    // 字段使用此注解
    @Field
    private String name;

    // 字段还可以用自定义名称
    @Field("myage")
    private int age;

    // 还可以生成索引
    @Indexed(name = "index_birth", direction = IndexDirection.DESCENDING)
    @Field
    private Timestamp birth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Timestamp getBirth() {
        return birth;
    }

    public void setBirth(Timestamp birth) {
        this.birth = birth;
    }

}