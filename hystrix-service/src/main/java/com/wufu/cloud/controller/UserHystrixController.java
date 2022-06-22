package com.wufu.cloud.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.wufu.cloud.common.CommonResult;
import com.wufu.cloud.common.User;
import com.wufu.cloud.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author wufu
 */
@RestController
@RequestMapping("/user")
public class UserHystrixController {

    @Resource
    private UserService userService;

    /**
     * 具有服务降级
     */
    @GetMapping("/testFallback/{id}")
    public CommonResult testFallback(@PathVariable Long id) {
        return userService.getUser(id);
    }


    @GetMapping("/testCommand/{id}")
    public CommonResult testCommand(@PathVariable Long id) {
        return userService.getUserCommand(id);
    }

    /**
     * 使用 ignoreExceptions 忽略某些异常的降级
     */
    @GetMapping("/testException/{id}")
    public CommonResult testException(@PathVariable Long id) {
        return userService.getUserException(id);
    }

    /**
     * 请求缓存，
     * 当请求的参数一致时，开启缓存后会直接使用缓存
     */
    @GetMapping("/testCache/{id}")
    public CommonResult testCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.getUserCache(id);
        userService.getUserCache(id);
        return new CommonResult("test cache success!", 200);
    }

    @GetMapping("/testRemoveCache/{id}")
    public CommonResult testRemoveCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.removeUserCache(id);
        userService.getUserCache(id);
        return new CommonResult("test remove cache!", 200);
    }


    @GetMapping("/testCollapser")
    public CommonResult testCollapser() throws ExecutionException, InterruptedException {
        Future<User> future1 = userService.getUserFuture(1L);
        Future<User> future2 = userService.getUserFuture(2L);
        future1.get();
        future2.get();
        ThreadUtil.safeSleep(200);

        Future<User> future3 = userService.getUserFuture(3L);
        future3.get();
        return new CommonResult("test collapser", 200);
    }
}
