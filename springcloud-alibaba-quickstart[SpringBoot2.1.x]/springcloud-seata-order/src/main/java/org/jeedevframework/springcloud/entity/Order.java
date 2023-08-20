package org.jeedevframework.springcloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("t_order")
public class Order {
    @TableId(type=IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("status")
    private Integer status;

    @TableField("product_id")
    private Long productId;

    @TableField("money")
    private Integer money;

    @TableField("count")
    private Integer count;
}
