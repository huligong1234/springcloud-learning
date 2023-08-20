package org.jeedevframework.springcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Account;
import org.jeedevframework.springcloud.mapper.AccountMapper;
import org.jeedevframework.springcloud.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * Description:账户接口实现
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Override
    public R decrease(Long userId, Integer money) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        Account account = getOne(queryWrapper);
        if(null == account) {
            return R.error(2001,"账户不存在!");
        }
        if(account.getMoney()>=money) {
            account.setMoney(account.getMoney()-money);
            updateById(account);
        }else {
            return R.error(2002,"账户余额不足!");
        }
        return R.ok(2000,"账户余额更新成功!");
    }
}

