package com.feiye.bank.service;

import com.feiye.bank.exceptions.AppException;
import com.feiye.bank.exceptions.MoneyNotEnoughException;

public interface AccountService {
    void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, AppException;
}
