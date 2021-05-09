package cn.edu.tju.servlet;

import cn.edu.tju.utils.FileTreeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: CMW天下第一
 * @Date: 2021/5/9 13:47
 */
@WebServlet("/test")
public class CodeComparatorServlet extends HttpServlet {
    FileTreeUtils fileTreeUtils = new FileTreeUtils();
    List<List<String>> tree = null;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        if (status.equals("1")) {
            String path = req.getParameter("path");
            System.out.println(path);
            tree = fileTreeUtils.getJavaFileList(path);
            resp.getWriter().write("ok");
        } else {
            String code = req.getParameter("code");
            //TODO
            System.out.println(code);
            resp.getWriter().write("yyy");
        }
    }
}
