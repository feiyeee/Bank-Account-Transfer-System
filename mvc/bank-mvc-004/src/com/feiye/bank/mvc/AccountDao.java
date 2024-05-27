package com.feiye.bank.mvc;

import com.feiye.bank.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountDao负责Account数据的增删改查
 *  1. DAO
 *      Data Access Object(数据访问对象)
 *  2. DAO实际上是一种属于JavaEE的设计模式之一
 *  3. DAO只负责数据库表的CRUD，没有任何业务逻辑在里面
 *  4. 如果处理t_user表，可以叫做：UserDao
 *     如果处理t_student表，可以叫做：StudentDao
 *  5. 一般情况下，一张表对应一个DAO对象
 *
 * @author feiye
 * @version 1.0
 * @since 1.0
 */
public class AccountDao {
    /**
     * 插入账户信息
     * @param act 账户信息
     * @return 1表示插入成功
     */
    public int insert(Account act) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "insert into t_act(actno, balance) values(?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, act.getActno());
            ps.setDouble(2, act.getBalance());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(null, ps, null);
        }
        return count;
    }

    /**
     * 根据主键删除账户
     * @param id 主键
     * @return
     */
    public int deleteById(Long id) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "delete from t_act where id=?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(null, ps, null);
        }
        return count;
    }

    /**
     * 更新账户
     * @param act
     * @return
     */
    public int update(Account act) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "update t_act set balance=?, actno=? where id=?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, act.getBalance());
            ps.setString(2, act.getActno());
            ps.setLong(3, act.getId());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(null, ps, null);
        }
        return count;
    }

    /**
     * 查找账户
     * @param actno
     * @return
     */
    public Account selectByActno(String actno) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Account act = null;
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "select id, balance from t_act where actno=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, actno);
            rs = ps.executeQuery();
            if (rs.next()) {
                Long id = rs.getLong("id");
                Double balance = rs.getDouble("balance");
                // 将结果集封装成Java对象
                act = new Account();
                act.setId(id);
                act.setActno(actno);
                act.setBalance(balance);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(null, ps, rs);
        }
        return act;
    }

    /**
     * 查找所有账户
     * @return
     */
    public List<Account> selectAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Account> list = new ArrayList<>();
        try {
            Connection conn = DBUtil.getConnection();
            String sql = "select id, balance from t_act";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                // 取出数据
                Long id = rs.getLong("id");
                String actno = rs.getString("actno");
                Double balance = rs.getDouble("balance");
                // 封装对象
                Account account = new Account(id, actno, balance);
                // 加到List集合
                list.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(null, ps, rs);
        }
        return list;
    }
}
