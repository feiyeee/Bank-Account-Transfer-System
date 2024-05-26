package com.feiye.bank.exceptions;

/**mc
 * 余额不足异常
 * @author feiyeee
 * @version 1.0
 * @since 1.0
 */
public class MoneyNotEnoughException extends Exception {
    public MoneyNotEnoughException() {}
    public MoneyNotEnoughException(String msg) {
        super(msg);
    }
}
