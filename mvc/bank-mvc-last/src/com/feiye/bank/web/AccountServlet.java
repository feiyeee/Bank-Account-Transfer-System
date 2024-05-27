package com.feiye.bank.web;

import com.feiye.bank.exceptions.MoneyNotEnoughException;
import com.feiye.bank.service.AccountService;
import com.feiye.bank.service.impl.AccountServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**mc
 * 账户小程序
 * AccountServlet是一个司令官，负责调度其他组件来完成任务
 * @author feiye
 * @version 2.0
 * @since 2.0
 */
@WebServlet("/transfer")
public class AccountServlet extends HttpServlet {
    private AccountService accountService = new AccountServiceImpl();   // 多态

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 接收数据
        String fromActno = request.getParameter("fromActno");
        String toActno = request.getParameter("toActno");
        double money = Double.parseDouble(request.getParameter("money"));
        // 调用业务方法处理业务（调度Model处理业务）
        try {
            accountService.transfer(fromActno, toActno, money);
            // 展示处理结果（调度view做页面展示）
            response.sendRedirect(request.getContextPath() + "/success.jsp");
        } catch (MoneyNotEnoughException e) {
            // 转账失败
            // 展示处理结果（调度view做页面展示）
            response.sendRedirect(request.getContextPath() + "/moneynotenough.jsp");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
