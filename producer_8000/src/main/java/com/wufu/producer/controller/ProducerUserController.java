package com.wufu.producer.controller;


import com.wufu.producer.common.Result;
import com.wufu.producer.entity.ProducerUser;
import com.wufu.producer.service.IProducerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * user 前端控制器
 * </p>
 *
 * @author wufu
 * @since 2022-06-20
 */
@RestController
@RequestMapping("/producer/user")
public class ProducerUserController {

    @Autowired
    private IProducerUserService userService;

    @GetMapping("/get/{id}")
    public Result getUser(@PathVariable Integer id) {
        ProducerUser user = userService.getById(id);
        if (user == null) {
            return Result.error(false, 404, "data not found");
        }
        return Result.ok(true, 200, "success").data("user", user);
    }

    @PostMapping("/create")
    public Result createUser(@RequestBody ProducerUser user) {
        boolean result = userService.save(user);
        if (!result) {
            return Result.error(false, 404, "create data error");
        }
        return Result.ok(true, 200, "create data success");
    }
}

