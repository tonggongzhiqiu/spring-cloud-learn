package com.wufu.cloud.service;

import com.wufu.cloud.common.CommonResult;
import com.wufu.cloud.common.User;
import com.wufu.cloud.service.impl.UserFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 通过 @FeignClient(value = "user-service")，
 * 我们可以直接调用 user-service 接口服务调用客户端
 * @author wufu
 */
@FeignClient(value = "user-service", fallback = UserFallbackService.class)
public interface UserService {

    @PostMapping("/user/create")
    CommonResult create(@RequestBody User user);

    @GetMapping("/user/{id}")
    CommonResult<User> getUser(@PathVariable Long id);

    @GetMapping("/user/getByUsername")
    CommonResult<User> getByUsername(@RequestParam String username);

    @PostMapping("/user/update")
    CommonResult update(@RequestBody User user);

    @PostMapping("/user/delete/{id}")
    CommonResult delete(@PathVariable Long id);
}
