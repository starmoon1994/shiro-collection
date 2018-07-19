package com.company.testshiro4.shiro.mongo;

import com.company.testshiro4.support.SerializeUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 实现shiro的Cache接口  对授权信息进行缓存
 * Created by hxy on 2018/6/26.
 */
@Component
public class ShiroMongoCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    private String keyPrefix = "shiro_cache_:";

    /**
     * 获得byte[]型的key
     *
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.keyPrefix + key;
            return preKey.getBytes();
        } else {
            return SerializeUtils.serialize(key);
        }
    }

    @Override
    public V get(K k) throws CacheException {

        if (k == null) {
            return null;
        } else {

            byte[] byteKey = getByteKey(k);

            Query query = new Query(new Criteria("key").is(byteKey));

            MyCacheBean myCacheBean = mongoTemplate.findOne(query, MyCacheBean.class);

            if (myCacheBean == null) return null;

            Object deserialize = SerializeUtils.deserialize(myCacheBean.getValue());

            return (V) deserialize;
        }

    }

    @Override
    public V put(K k, V v) throws CacheException {


        byte[] serializeK = SerializeUtils.serialize(k);
        byte[] serializeV = SerializeUtils.serialize(v);

        MyCacheBean myCacheBean = new MyCacheBean();
        myCacheBean.setKey(serializeK);
        myCacheBean.setValue(serializeV);
        mongoTemplate.save(myCacheBean);

        return v;
    }

    @Override
    public V remove(K k) throws CacheException {

        byte[] serializeK = SerializeUtils.serialize(k);

        Query query = new Query(new Criteria("key").is(serializeK));

        MyCacheBean andRemove = mongoTemplate.findAndRemove(query, MyCacheBean.class);

        if (andRemove == null) {
            return null;
        }

        Object deserialize = SerializeUtils.deserialize(andRemove.getValue());

        return (V) deserialize;
    }

    @Override
    public void clear() throws CacheException {
        mongoTemplate.dropCollection(MyCacheBean.class);
        logger.debug("ShiroMongoCache clear() 清除shiro的cache");
    }

    @Override
    public int size() {
        long count = mongoTemplate.count(new Query(), MyCacheBean.class);

        return Integer.parseInt(Long.toString(count));
    }

    @Override
    public Set<K> keys() {

        List<MyCacheBean> all = mongoTemplate.findAll(MyCacheBean.class);

        Set<K> newKeys = new HashSet<K>();

        if (all.size() > 0) {
            for (MyCacheBean singleOne : all) {
                byte[] key = singleOne.getKey();

                newKeys.add((K) key);
            }


        }

        return newKeys;

    }

    @Override
    public Collection<V> values() {

        List<MyCacheBean> all = mongoTemplate.findAll(MyCacheBean.class);

        List<V> values = new ArrayList<V>(all.size());

        if (all.size() > 0) {
            for (MyCacheBean singleOne : all) {
                byte[] key = singleOne.getKey();
                values.add((V) key);
            }
        }

        return values;

    }
}
