package com.wufu.cloud.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.wufu.cloud.common.CommonResult;
import com.wufu.cloud.common.User;
import com.wufu.cloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL;

/**
 * @author wufu
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.user-service}")
    private String userServiceUrl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 当当前服务出现问题时，会将服务降级为获取默认用户：getDefaultUser
     */
    @Override
    @HystrixCommand(fallbackMethod = "getDefaultUser")
    public CommonResult getUser(Long id) {
        return restTemplate.getForObject(
                userServiceUrl + "/user/{1}",
                CommonResult.class,
                id
        );
    }

    /**
     * 设置命令、分组、线程池名称
     */
    @Override
    @HystrixCommand(fallbackMethod = "getDefaultUser",
                    commandKey = "getUserCommand",
                    groupKey = "getUserGroup",
                    threadPoolKey = "getUserThreadPool")
    public CommonResult getUserCommand(Long id) {
        return restTemplate.getForObject(
                userServiceUrl + "/user/{1}",
                CommonResult.class,
                id
        );
    }

    /**
     * 使用 ignoreExceptions 忽略某些异常的降级
     * NullPointerException 不会使用 getDefaultUser2
     */
    @Override
    @HystrixCommand(fallbackMethod = "getDefaultUser2",
                    ignoreExceptions = {NullPointerException.class})
    public CommonResult getUserException(Long id) {
        if (id == 1) {
            throw new IndexOutOfBoundsException();
        } else if (id == 2) {
            throw new NullPointerException();
        }
        return restTemplate.getForObject(
                userServiceUrl + "/user/{1}",
                CommonResult.class,
                id
        );
    }

    @Override
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "getDefaultUser",
                    commandKey = "getUserCache")
    public CommonResult getUserCache(Long id) {
        logger.info("getUserCache id:{}", id);
        return restTemplate.getForObject(
                userServiceUrl + "/user/{1}",
                CommonResult.class,
                id);
    }

    @Override
    @CacheRemove(commandKey = "getUserCache", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public CommonResult removeUserCache(Long id) {
        logger.info("remove cache id:{}", id);
        return restTemplate.postForObject(
                userServiceUrl + "/user/delete/{1}",
                null,
                CommonResult.class,
                id
        );
    }

    @Override
    @HystrixCollapser(
            scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            batchMethod = "getUserByIds",
            collapserProperties = {
                @HystrixProperty(name="timerDelayInMilliseconds", value="100")

    })
    public Future<User> getUserFuture(Long id) {
        return new AsyncResult<User>(){
            @Override
            public User invoke() {
                CommonResult commonResult = restTemplate.getForObject(
                        userServiceUrl + "/user/{1}",
                        CommonResult.class,
                        id);
                Map data = (Map) commonResult.getData();
                User user = BeanUtil.mapToBean(data, User.class, true);
                logger.info("get user future, username:{}", user.getUsername());
                return  user;
            }
        };
    }


    private CommonResult getDefaultUser(Long id) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return new CommonResult(defaultUser);
    }

    private CommonResult getDefaultUser2(Long id, Throwable e) {
        logger.error("getDefaultUser2 id:{}, throwable class:{}", id, e.getClass());
        User defaultUser = new User(-2L, "defaultUser2", "123456");
        return new CommonResult(defaultUser);
    }

    /**
     * 为缓存生成 key 的方法
     */
    private String getCacheKey(Long id) {
        return String.valueOf(id);
    }

    @HystrixCommand
    private List<User> getUserByIds(List<Long> ids) {
        logger.info("get user by ids:{}", ids);
        CommonResult commonResult = restTemplate.getForObject(
                userServiceUrl + "/user/getUserByIds?ids={1}",
                CommonResult.class,
                CollUtil.join(ids, ",")
        );
        return (List<User>) commonResult.getData();
    }
}
