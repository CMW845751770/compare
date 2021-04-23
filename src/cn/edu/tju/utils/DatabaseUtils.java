package cn.edu.tju.utils;

import java.io.File;
import java.sql.*;
import java.util.List;

public class DatabaseUtils {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/Graduate?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "Zhao@9295";

    static Connection conn;

    static {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    static PreparedStatement pst = null;

    public static void main(String[] args) throws Exception {
//        String sql = "select * from project";
        String sql = "insert into `function`(`name`, file_ID, content) values(?,?,?)";
        pst = conn.prepareStatement(sql);
        String path = "D:\\GraduationProject\\test";
        List<File> files = FileUtils.getJavaFileList(path);
        for (File file : files) {
            List<List<String>> functions = FileUtils.getFunctionFromJavaFile(file);
            System.out.println(file.getName());
            for (List<String> function : functions) {
                pst.setString(1, function.get(0));
                pst.setString(2, "1test");
                pst.setString(3, function.get(1));
                pst.executeUpdate();
//                System.out.println(file.getName());
            }

//
//            pst.setString(1, "1testMain");
//            pst.setString(2, "main.java");
//            pst.setString(3, "1test");
//            pst.setString(4, code);
//            System.out.println(file.getName());
        }



//        ResultSet rst = pst.executeQuery();
//        while (rst.next()) {
//            System.out.println(rst.getInt("ID"));
//        }
    }
}
