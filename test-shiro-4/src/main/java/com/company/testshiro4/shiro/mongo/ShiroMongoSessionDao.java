package com.company.testshiro4.shiro.mongo;

import com.company.testshiro4.support.SerializeUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 实现AbstractSessionDAO接口  提供给Shiro 对Session进行操作
 * Created by hxy on 2018/6/25.
 */
@Component
public class ShiroMongoSessionDao extends AbstractSessionDAO {


    @Autowired
    private ShiroMongoRepository shiroMongoRepository;

    @Override
    protected Serializable doCreate(Session session) {

        // generateSessionId是AbstractSessionDAO提供的生成sessionid的方法 底层就是UUID
        Serializable sessionId = this.generateSessionId(session);

        this.assignSessionId(session, sessionId);

        shiroMongoRepository.saveOne(session);

        return sessionId;

    }

    /**
     * 获取id
     */
    private String getSessionKey(Serializable id) {
        return (String) id;
    }


    @Override
    protected Session doReadSession(Serializable sessionId) {

        Session session = shiroMongoRepository.findOne(sessionId);

        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {

        shiroMongoRepository.update(session);

    }

    @Override
    public void delete(Session session) {
        shiroMongoRepository.delete(session);
    }

    @Override
    public Collection<Session> getActiveSessions() {

        Collection<Session> collection = new ArrayList<Session>();

        List<MySessionBean> all = shiroMongoRepository.findAll();

        for (MySessionBean sessionBean : all) {
            SimpleSession simpleSession = (SimpleSession) SerializeUtils.deserialize(sessionBean.getValue());
            collection.add(simpleSession);
        }

        return collection;
    }

    public Collection<Session> getActiveSessionsByPage() {
        Collection<Session> collection = new ArrayList<Session>();

        List<MySessionBean> all = shiroMongoRepository.findAllByPage();

        for (MySessionBean sessionBean : all) {
            SimpleSession simpleSession = (SimpleSession) SerializeUtils.deserialize(sessionBean.getValue());
            collection.add(simpleSession);
        }

        return collection;
    }
}
