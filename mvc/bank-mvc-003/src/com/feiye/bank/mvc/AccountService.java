package com.feiye.bank.mvc;

import com.feiye.bank.exceptions.AppException;
import com.feiye.bank.exceptions.MoneyNotEnoughException;
import com.feiye.bank.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * service: 业务
 * AccountService: 专门处理Account业务的一个类
 * 业务类一般起名：XxxxService、XxxxBiz……
 *
 * @author feiye
 * @version 1.0
 * @since 1.0
 */
public class AccountService {
    // 方法起名，要体现出处理的是什么业务
    // 一个业务对应一个方法

    // 定义在外面，每一个业务方法中都可能需要连接数据库
    private AccountDao accountDao = new AccountDao();

    /**
     * 完成转账的业务逻辑
     * @param fromActno 转出账号
     * @param toActno 转入账号
     * @param money 转账金额
     */
    public void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, AppException {
        // 在service层控制事务
        try (Connection connection = DBUtil.getConnection()) {
            // 开启事务（需要使用Connection对象）
            connection.setAutoCommit(false);

            // 查询余额是否充足
            Account fromAct = accountDao.selectByActno(fromActno, connection);
            if (fromAct.getBalance()<money) {
                throw new MoneyNotEnoughException("余额不足");
            }
            // 余额充足
            Account toAct = accountDao.selectByActno(toActno, connection);
            // 修改余额（只是修改了内存中Java对象的余额）
            fromAct.setBalance(fromAct.getBalance() - money);
            toAct.setBalance(toAct.getBalance() + money);
            // 更新数据库中的余额
            int count = accountDao.update(fromAct, connection);
            count += accountDao.update(toAct, connection);
            if (count != 2) {
                throw new AppException("账户转账异常！");
            }

            // 提交事务
            connection.commit();
        } catch (SQLException e) {
            throw new AppException("账户转账异常！");
        }
    }
}
