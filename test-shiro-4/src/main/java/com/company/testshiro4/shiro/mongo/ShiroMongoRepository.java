package com.company.testshiro4.shiro.mongo;

import com.company.testshiro4.support.SerializeUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 对MongoDB操作的封装
 * Created by hxy on 2018/6/25.
 */
@Component
public class ShiroMongoRepository {

    private static Logger logger = LoggerFactory.getLogger(ShiroMongoRepository.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 插入保存
     */
    public Serializable saveOne(Session session) {

        MySessionBean bean = new MySessionBean();
        bean.setKey((String) session.getId());
        bean.setValue(SerializeUtils.serialize(session));
        bean.setHost(session.getHost());
        bean.setStartTimestamp(session.getStartTimestamp());
        bean.setLastAccessTime(session.getLastAccessTime());
        mongoTemplate.insert(bean);


        logger.info("ShiroMongoRepository saveOne sessionId:{}", (String) session.getId());
        return session.getId();
    }


    /**
     * 查询操作
     */
    public Session findOne(Serializable sessionId) {

        Query query = new Query(new Criteria("key").is((String) sessionId));

        MySessionBean mySessionBean = mongoTemplate.findOne(query, MySessionBean.class);

        if (mySessionBean == null) {
            return null;
        }

        // TODO: 过期处理

        Session session = (Session) SerializeUtils.deserialize(mySessionBean.getValue());

        if (session == null) {
            return null;
        }

        return session;
    }


    /**
     * 删除操作
     */
    public void delete(Session session) {

        String sessionId = (String) session.getId();

        Query query = new Query(new Criteria("key").is((sessionId)));

//        MySessionBean mySessionBean = mongoTemplate.findAndRemove(query, MySessionBean.class);

        mongoTemplate.remove(query, MySessionBean.class);

        logger.debug("ShiroMongoRepository delete 已删除 sessionId：{}", sessionId);

    }

    public List<MySessionBean> findAll() {

        List<MySessionBean> all = mongoTemplate.findAll(MySessionBean.class);

        return all.isEmpty() ? new ArrayList<>() : all;
    }

    public void update(Session session) {

        // 主要是更新 session本体  也就是value
        MySessionBean bean = new MySessionBean();
        bean.setKey((String) session.getId());
        bean.setValue(SerializeUtils.serialize(session));
        bean.setHost(session.getHost());
        bean.setStartTimestamp(session.getStartTimestamp());
        bean.setLastAccessTime(session.getLastAccessTime());

        Query query = new Query(new Criteria("key").is((String) session.getId()));

        Update update = new Update().set("value", SerializeUtils.serialize(session))
                .set("host", session.getHost())
                .set("startTimestamp", session.getStartTimestamp())
                .set("lastAccessTime", session.getLastAccessTime());

        mongoTemplate.upsert(query, update, MySessionBean.class);
    }

    public List<MySessionBean> findAllByPage() {

        Query query = new Query();
        // TODO：暂时不分页 需完善旧session的删除
//        int pageNum = PageSystemContext.getPageNum();
//        int pageSize = PageSystemContext.getPageSize();
//        if (pageSize != 0) {
//            query.limit(pageSize);
//            query.skip((pageNum - 1) * pageSize);
//        }
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));

        List<MySessionBean> all = mongoTemplate.find(query, MySessionBean.class);

        return all.isEmpty() ? new ArrayList<>() : all;
    }
}
