package org.jeedevframework.springcloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("t_storage")
public class Storage {
    @TableId(type= IdType.AUTO)
    private Long id;

    @TableField("product_id")
    private Long productId;

    @TableField("count")
    private Integer count;
}
