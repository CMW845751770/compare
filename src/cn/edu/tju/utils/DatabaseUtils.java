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

    static PreparedStatement pst_function = null;
    static PreparedStatement pst_file = null;
    static ResultSet rs = null;
    static int file_ID = 0;

    public static void main(String[] args) throws Exception {
        int i = 0;
        String sql_file = "insert into file(`name`, pro_ID, `language`) values(?,?,?)";
        String sql_function = "insert into `function`(`name`, file_ID, content) values(?,?,?)";
        pst_file = conn.prepareStatement(sql_file, Statement.RETURN_GENERATED_KEYS);
        pst_function = conn.prepareStatement(sql_function);
        String path = "D:\\GraduationProject\\code_data\\elasticsearch-master";
        List<File> files = FileUtils.getJavaFileList(path);
        for (File file : files) {
            pst_file.setString(1, file.getName());
            pst_file.setInt(2, 2);
            pst_file.setString(3, "java");
            pst_file.executeUpdate();
            rs = pst_file.getGeneratedKeys();
            if (rs.next()) file_ID = rs.getInt(1);
            List<List<String>> functions = FileUtils.getFunctionFromJavaFile(file);
//            System.out.println(file.getName());
            for (List<String> function : functions) {
                pst_function.setString(1, function.get(0));
                pst_function.setInt(2, file_ID);
                pst_function.setString(3, function.get(1));
                pst_function.executeUpdate();
                if (i % 100 == 0) System.out.println(i);
                i++;
//                System.out.println(file.getName());
//                ResultSet rs = pst_function.getGeneratedKeys();
//                if (rs.next()) {
//                    System.out.println(rs.getInt(1));
//                }
            }

//
//            pst.setString(1, "1testMain");
//            pst.setString(2, "main.java");
//            pst.setString(3, "1test");
//            pst.setString(4, code);
//            System.out.println(file.getName());
        }
        System.out.println(i);



//        ResultSet rst = pst.executeQuery();
//        while (rst.next()) {
//            System.out.println(rst.getInt("ID"));
//        }
    }
}
