package com.wufu.cloud.service.impl;

import com.wufu.cloud.common.User;
import com.wufu.cloud.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private List<User> userList;

    @Override
    public void create(User user) {
        userList.add(user);
    }

    @Override
    public User getUser(Long id) {
        for (User user: userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void update(User user) {
        for (User user1 : userList) {
            if (user1.getId().equals(user.getId())) {
                user1.setUsername(user.getUsername());
                user1.setPassword(user.getPassword());
                return ;
            }
        }
    }

    @Override
    public void delete(Long id) {
        User user = getUser(id);
        if (user != null) {
            userList.remove(user);
        }
    }

    @Override
    public User getByUsername(String username) {
        List<User> findUserList = userList.stream().filter(
                user -> user.getUsername().equals(username)
        ).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(findUserList)) {
            return findUserList.get(0);
        }

        return null;
    }

    @Override
    public List<User> getUserByIds(List<Long> ids) {
        return userList.stream().filter(user -> ids.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void initData() {
        userList = new ArrayList<>();
        userList.add(new User(1L, "wufu", "123456"));
        userList.add(new User(2L, "andy", "123456"));
        userList.add(new User(3L, "mark", "123456"));
    }
}
