package com.feiye.bank.dao.impl;

import com.feiye.bank.dao.AccountDao;
import com.feiye.bank.pojo.Account;
import com.feiye.bank.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class AccountDaoImpl implements AccountDao {
    @Override
    public Account selectByActno(String arg0) {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // Account account = (Account) sqlSession.selectOne("account.selectByActno", actno);
        return (Account) sqlSession.selectOne("com.feiye.bank.dao.AccountDao.selectByActno", arg0);
    }

    @Override
    public int updateByActno(Account arg0) {
        SqlSession sqlSession = SqlSessionUtil.openSession();
        // int count = sqlSession.update("account.updateByActno", act);
        return sqlSession.update("com.feiye.bank.dao.AccountDao.updateByActno", arg0);
    }
}
