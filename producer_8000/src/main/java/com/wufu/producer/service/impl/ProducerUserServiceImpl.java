package com.wufu.producer.service.impl;

import com.wufu.producer.entity.ProducerUser;
import com.wufu.producer.mapper.ProducerUserMapper;
import com.wufu.producer.service.IProducerUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * user 服务实现类
 * </p>
 *
 * @author wufu
 * @since 2022-06-20
 */
@Service
public class ProducerUserServiceImpl extends ServiceImpl<ProducerUserMapper, ProducerUser> implements IProducerUserService {

}
