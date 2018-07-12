package com.company.testshiro3.shiro.mongo;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 实现shiro的CacheManager接口 对Shiro的Cache进行管理
 * Created by hxy on 2018/6/26.
 */
@Component
public class ShiroMongoCacheManager implements CacheManager {


    private static final Logger logger = LoggerFactory.getLogger(ShiroMongoCacheManager.class);

    // fast lookup by name map
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();


    @Autowired
    private ShiroMongoCache shiroMongoCache;


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        logger.debug("获取名称为: " + name + " 的Cache实例");

        Cache c = caches.get(name);

        if (c == null) {

            c = shiroMongoCache;

            caches.put(name, c);

        }

        return c;
    }
}
