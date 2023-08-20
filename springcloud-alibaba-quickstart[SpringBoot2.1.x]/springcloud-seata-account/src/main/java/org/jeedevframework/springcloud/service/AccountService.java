package org.jeedevframework.springcloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Account;

/**
 * Description:账户接口
 */
public interface AccountService extends IService<Account> {
    R decrease(Long userId, Integer money);
}

