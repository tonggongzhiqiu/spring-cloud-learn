package com.wufu.cloud.service;

import com.wufu.cloud.common.User;

import java.util.List;

public interface UserService {

    void create(User user);

    User getUser(Long id);

    void update(User user);

    void delete(Long id);

    User getByUsername(String username);

    List<User> getUserByIds(List<Long> ids);
}
