package org.jeedevframework.springcloud.web;

import io.seata.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Account;
import org.jeedevframework.springcloud.service.AccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description：账户接口
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @RequestMapping("/listAll")
    public R listAll() {
        List<Account> accountList = accountService.list();
        return R.ok(2000, StringUtils.EMPTY,accountList);
    }

    @RequestMapping("/decrease")
    public R decrease(@RequestParam("userId") Long userId, @RequestParam("money") Integer money) {
        return accountService.decrease(userId,money);
    }
}

