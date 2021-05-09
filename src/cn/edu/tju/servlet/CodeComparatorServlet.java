package cn.edu.tju.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: CMW天下第一
 * @Date: 2021/5/9 13:47
 */
@WebServlet("/test")
public class CodeComparatorServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        //TODO
        resp.getWriter().write(code);
    }
}
