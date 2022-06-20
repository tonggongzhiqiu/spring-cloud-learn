package com.wufu.producer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * user
 * </p>
 *
 * @author wufu
 * @since 2022-06-20
 */
@Getter
@Setter
@TableName("producer_user")
public class ProducerUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Name
     */
    private String name;


}
