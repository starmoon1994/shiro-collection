package com.company.testshiro4.service;

import com.company.testshiro4.entity.SecUser;

import java.util.List;

/**
 * Created by hyp-company on 2018/6/29.
 */
public interface SecUserService {

    int addUser(SecUser secUser);

    int removeUser(SecUser secUser);

    int updateUser(SecUser secUser);

    SecUser getUser(long id);

    List<SecUser> getList(int state);
}
