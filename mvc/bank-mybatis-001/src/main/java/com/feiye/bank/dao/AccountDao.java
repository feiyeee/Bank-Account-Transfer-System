package com.feiye.bank.dao;

import com.feiye.bank.pojo.Account;

/**
 * 账户的DAO对象，负责t_act表中数据的CRUD
 * DAO对象中的任何一个方法和业务不挂钩，没有任何业务逻辑在里面
 *
 * @author feiye
 * @version 1.0
 * @since 1.0
 */
public interface AccountDao {

    /**
     * 根据账户查询账户信息
     *
     * @param arg0
     * @return
     */
    Account selectByActno(String arg0);

    /**
     * 更新账户信息
     * @param arg0
     * @return
     */
    int updateByActno(Account arg0);
}