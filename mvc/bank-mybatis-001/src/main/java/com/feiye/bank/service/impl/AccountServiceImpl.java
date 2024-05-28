package com.feiye.bank.service.impl;

import com.feiye.bank.dao.AccountDao;
import com.feiye.bank.exceptions.MoneyNotEnoughException;
import com.feiye.bank.exceptions.TransferException;
import com.feiye.bank.pojo.Account;
import com.feiye.bank.service.AccountService;
import com.feiye.bank.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class AccountServiceImpl implements AccountService {

    // private AccountDao accountDao = new AccountDaoImpl();

    // mybatis中实际上采用了代理模式，在内存中生成dao接口的代理类，然后创建代理类的实例
    // 使用mybatis的这种代理机制的前提：SqlMapper.xml文件中namespace必须是dao接口的全限定名称，id必须是dao接口中的方法名
    private AccountDao accountDao = SqlSessionUtil.openSession().getMapper(AccountDao.class);

    @Override
    public void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, TransferException {
        // 添加事务控制代码
        SqlSession sqlSession = SqlSessionUtil.openSession();

        // 1. 判断转出账户的余额是否充足(select)
        Account fromAct = accountDao.selectByActno(fromActno);
        // 2. 如果余额不足，提示用户
        if (fromAct.getBalance() < money) {
            throw new MoneyNotEnoughException("余额不足");
        }
        // 3. 余额充足，更新转出/转入账户余额(update)
        // 先更新内存中Java对象account的余额
        Account toAct = accountDao.selectByActno(toActno);
        fromAct.setBalance(fromAct.getBalance() - money);
        toAct.setBalance(toAct.getBalance() + money);
        int count = accountDao.updateByActno(fromAct);
        count += accountDao.updateByActno(toAct);
        if (count != 2) {
            throw new TransferException("转账异常，未知原因");
        }

        // 提交事务
        sqlSession.commit();
        // 关闭事务
        SqlSessionUtil.close(sqlSession);
    }
}

