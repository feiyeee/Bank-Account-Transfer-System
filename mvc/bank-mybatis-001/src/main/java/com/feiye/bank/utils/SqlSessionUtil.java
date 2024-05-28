package com.feiye.bank.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * MyBatis工具类
 * @author feiye
 * @version 1.0
 * @since 1.0
 */
public class SqlSessionUtil {
    private SqlSessionUtil() {}

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // 全局的，服务器级别的，一个服务器中定义一个即可
    private static ThreadLocal<SqlSession> local = new ThreadLocal<>();

    /**
     * 获取会话对象
     * @return 会话对象
     */
    public static SqlSession openSession() {
        SqlSession sqlSession = local.get();
        if (sqlSession == null) {
            sqlSession = sqlSessionFactory.openSession();
            // 将sqlSession对象绑定到当前线程
            local.set(sqlSession);
        }
        return sqlSession;
    }

    /**
     * 关闭SqlSession对象（从当前线程中移除SqlSession对象）
     * @param sqlSession
     */
    public static void close(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.close();
            // 移除SqlSession对象和当前线程的绑定关系
            local.remove();
        }
    }
}
