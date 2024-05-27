package com.feiye.bank.utils;

import java.sql.*;
import java.util.ResourceBundle;

/**mc
 * JDBC工具类
 * @author feiye
 * @version 1.0
 * @since 1.0
 */
public class DBUtil {
    private static ResourceBundle bundle = ResourceBundle.getBundle("resources/jdbc");
    private static String driver = bundle.getString("driver");
    private static String url = bundle.getString("url");
    private static String user = bundle.getString("user");
    private static String password = bundle.getString("password");

    // 不让创建对象
    // 工具类中的方法都是静态的，不需要创建对象
    // 为了防止创建对象，故将构造方法私有化
    private DBUtil() {
    }

    // DBUtil类加载时注册驱动
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ThreadLocal<Connection> local = new ThreadLocal<>();

    /**
     * 这里没有使用数据库连接池，直接创建连接对象
     * @return 连接对象
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = local.get();
        if (conn == null) {
            conn = DriverManager.getConnection(url, user, password);
            local.set(conn);
        }
        return conn;
    }

    /**cc
     * 关闭资源
     * @param conn 连接对象
     * @param ps 数据库操作对象
     * @param rs 结果集对象
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (conn!=null) {
            try {
                conn.close();
                // 关闭连接后，需要移除
                // Tomcat服务器内置了一个线程池，线程池中很多线程对象；
                // 这些线程对象t1 t2 t3都是提前创建好的
                // 即 t1 t2 t3存在重复使用的现象
                local.remove();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (ps!=null) {
            try {
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
