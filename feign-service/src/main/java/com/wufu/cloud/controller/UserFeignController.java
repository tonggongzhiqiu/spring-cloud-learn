package com.wufu.cloud.controller;

import com.wufu.cloud.common.CommonResult;
import com.wufu.cloud.common.User;
import com.wufu.cloud.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wufu
 */
@RestController
@RequestMapping("/user")
public class UserFeignController {

    @Resource
    private UserService userService;

    @GetMapping("/{id}")
    public CommonResult getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/getByUsername")
    public CommonResult<User> getUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody User user) {
        return userService.create(user);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody User user) {
        return userService.update(user);
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}
