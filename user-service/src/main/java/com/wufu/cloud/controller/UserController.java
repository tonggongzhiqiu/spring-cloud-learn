package com.wufu.cloud.controller;

import com.wufu.cloud.common.CommonResult;
import com.wufu.cloud.common.User;
import com.wufu.cloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wufu
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public CommonResult create(@RequestBody User user) {
        userService.create(user);
        return new CommonResult("success", 200);
    }

    @GetMapping("/{id}")
    public CommonResult<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return new CommonResult("get user failed", 404);
        }
        logger.info("根据 id 获取用户信息，用户名称为：{}", user.getUsername());
        return new CommonResult(user, "get user success!", 200);
    }

    @GetMapping("/getUserByIds")
    public CommonResult<List<User>> getUserByIds(@RequestParam List<Long> ids) {
        List<User> userList = userService.getUserByIds(ids);
        logger.info("根据 id 获取用户信息，用户列表为：{}", userList);
        return new CommonResult<>(userList);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestParam User user) {
        userService.update(user);
        return new CommonResult("update success", 200);
    }

    @PostMapping("/delete/{id}")
    public CommonResult deleteById(@PathVariable Long id) {
        userService.delete(id);
        return new CommonResult("delete success", 200);
    }
}
