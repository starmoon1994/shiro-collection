package com.company.testshiro4.shiro.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 * 自定义的shiro对象cache
 * Created by hxy on 2018/6/25.
 */

@Document(collection = "shiro_cache")
public class MyCacheBean  implements Serializable {

    private static final long serialVersionUID = -7125642695178165650L;

    @Id
    private String id;
    @Field
    private byte[] key;
    @Field
    private byte[] value;

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
