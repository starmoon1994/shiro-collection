package com.company.testshiro4.dao.impl;

import com.company.testshiro4.dao.SecUserDao;
import com.company.testshiro4.entity.SecUser;
import com.company.testshiro4.mapper.SecUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hyp-company on 2018/6/29.
 */

@Repository
public class SecUserDaoImpl implements SecUserDao {


    @Autowired
    private SecUserMapper secUserMapper;

    @Override
    public int addOne(SecUser secUser) {
        return secUserMapper.addOne(secUser);
    }

    @Override
    public int updateOne(SecUser secUser) {
        return secUserMapper.updateOne(secUser);
    }

    @Override
    public SecUser selectOne(long id) {
        return secUserMapper.selectOne(id);
    }

    @Override
    public List<SecUser> selectList(int state) {
        return secUserMapper.selectList(state);
    }

    @Override
    public int removeUser(Integer uid) {
        return secUserMapper.removeUser(uid);
    }

}
