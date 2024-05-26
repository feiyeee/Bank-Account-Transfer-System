package com.feiye.bank.web.servlet;

import com.feiye.bank.exceptions.AppException;
import com.feiye.bank.exceptions.MoneyNotEnoughException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**mc
 * 在不使用MVC架构模式的前提下完成银行账户转账
 * 分析这个程序存在哪些问题：
 *  1. 代码的复用性太差
 *      原因：没有进行职能分工，没有独立组件的概念，代码和代码之间的耦合度太高，扩展力太差
 *  2. 耦合度太高，导致代码很难扩展
 *  3. 操作数据库的代码和业务逻辑混杂在一起，很容易出错，无法专注业务逻辑的编辑
 *
 * AccountTransferServlet负责了什么？
 *  1. 数据接收
 *  2. 核心的业务处理
 *  3. 数据库表中数据的CRUD操作
 *  4. 页面的数据展示
 *
 * @author feiyeee
 * @version 1.0
 * @since 1.0
 */
@WebServlet("/transfer")
public class AccountTransferServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取响应流对象
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 转账相关信息
        String fromActno = request.getParameter("fromActno");
        String toActno = request.getParameter("toActno");
        double money = Double.parseDouble(request.getParameter("money"));

        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        // 编写转账的逻辑代码，连接数据库，进行转账操作
        // 1. 转账之前，判断转出账户的余额是否充足
        try {
            // 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 获取连接
            String url = "jdbc:mysql://localhost:3306/mvc";
            String user = "root";
            String password = "123456";
            conn = DriverManager.getConnection(url, user, password);
            // 开启事务（不再自动提交，改为手动提交，业务完成之后再提交）
            conn.setAutoCommit(false);
            // 获取预编译的数据库操作对象
            String sql1 = "select balance from t_act where actno=?";
            ps = conn.prepareStatement(sql1);
            ps.setString(1, fromActno);
            // 执行sql语句，返回结果集
            rs = ps.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if(balance<money) {
                    // 余额不足
                    throw new MoneyNotEnoughException("余额不足");
                }
                // 程序能够执行到这里，说明余额一定是充足的
                // 开始转账
                String sql2 = "update t_act set balance = balance - ? where actno = ?";
                ps2 = conn.prepareStatement(sql2);
                ps2.setDouble(1, money);
                ps2.setString(2, fromActno);
                int count = ps2.executeUpdate();

                /*// 模拟异常
                String s = null;
                s.toString();*/

                String sql3 = "update t_act set balance = balance + ? where actno = ?";
                ps3 = conn.prepareStatement(sql3);
                ps3.setDouble(1, money);
                ps3.setString(2, toActno);
                // 累计
                count += ps3.executeUpdate();

                if (count != 2) {
                    throw new AppException("App异常，请联系管理员");
                }

                // 手动提交事务
                conn.commit();
                // 转账成功
                out.print("转账成功！");
            }

        } catch (Exception e) {
            // 回滚事务
            try {
                if (conn!=null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            out.print(e.getMessage());

        } finally {
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(ps2!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(ps3!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
