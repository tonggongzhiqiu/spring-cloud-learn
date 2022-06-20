package com.wufu.consumer.controller;

import com.wufu.producer.entity.ProducerUser;
import com.wufu.common.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


/**
 * @author wufu
 */
@RestController
@RequestMapping("/consumer/user")
public class ConsumerController {

    public static final String PRODUCER_URL = "http://localhost:8000/producer/user";


    @Autowired
    private RestTemplate template;

    @GetMapping("/get/{id}")
    public Result getUser(@PathVariable Integer id) {
        return template.getForObject(PRODUCER_URL + "/get/" + id, Result.class);
    }

    @PostMapping("/create")
    public Result createUser(@RequestBody ProducerUser user) {
        return template.postForObject(PRODUCER_URL + "/create", user, Result.class);
    }
}
