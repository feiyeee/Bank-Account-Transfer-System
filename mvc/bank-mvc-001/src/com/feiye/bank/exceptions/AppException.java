package com.feiye.bank.exceptions;

/**mc
 * App异常
 * @author feiyeee
 * @version 1.0
 * @since 1.0
 */
public class AppException extends Exception {
    public AppException() {}
    public AppException(String msg) {
        super(msg);
    }
}
