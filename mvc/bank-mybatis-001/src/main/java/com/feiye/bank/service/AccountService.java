package com.feiye.bank.service;

import com.feiye.bank.exceptions.MoneyNotEnoughException;
import com.feiye.bank.exceptions.TransferException;

/**
 * 账户业务类
 * @author feiye
 * @version 1.0
 * @since 1.0
 */
public interface AccountService {

    /**
     * 账户转账业务
     * @param fromActno 转出账号
     * @param toActno 转入账号
     * @param money 转账金额
     */
    void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, TransferException;
}
