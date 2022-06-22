package com.wufu.cloud.service;

import com.wufu.cloud.common.CommonResult;
import com.wufu.cloud.common.User;

import java.util.concurrent.Future;

/**
 * @author wufu
 */
public interface UserService {

    public CommonResult getUser(Long id);

    public CommonResult getUserCommand(Long id);

    public CommonResult getUserException(Long id);


    public CommonResult getUserCache(Long id);

    public CommonResult removeUserCache(Long id);

    public Future<User> getUserFuture(Long id);
}
