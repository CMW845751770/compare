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
    static PreparedStatement pst_project = null;
    static ResultSet rs = null;
    static int file_ID = 0;
    static int project_ID = 0;

    public static void main(String[] args) throws Exception {
        int i = 0;
        String sql_file = "insert into file(`name`, pro_ID, `language`) values(?,?,?)";
        String sql_function = "insert into `function`(`key`, `name`, file_ID, content, token) values(?,?,?,?,?)";
        String sql_project = "insert into `project`(`name`) values(?)";
        pst_project = conn.prepareStatement(sql_project, Statement.RETURN_GENERATED_KEYS);
        pst_file = conn.prepareStatement(sql_file, Statement.RETURN_GENERATED_KEYS);
        pst_function = conn.prepareStatement(sql_function);
        String path = "D:\\GraduationProject\\code_data_test";
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        for (File f : fileList) {
            pst_project.setString(1, f.getName());
            pst_project.executeUpdate();
            rs = pst_project.getGeneratedKeys();
            if (rs.next()) project_ID = rs.getInt(1);
            String path1 = path + "\\" + f.getName();
            List<File> files = FileUtils.getJavaFileList(path1);
            for (File file : files) {
                pst_file.setString(1, file.getName());
                pst_file.setInt(2, project_ID);
                pst_file.setString(3, "java");
                pst_file.executeUpdate();
                rs = pst_file.getGeneratedKeys();
                if (rs.next()) file_ID = rs.getInt(1);
                List<List<String>> functions = FileUtils.getFunctionFromJavaFile(file);
                for (List<String> function : functions) {
                    pst_function.setString(1, function.get(0));
                    pst_function.setString(2, function.get(2));
                    pst_function.setInt(3, file_ID);
                    pst_function.setString(4, function.get(1));
                    pst_function.setString(5, SimilarityCalculator.getPreProcessedCode(function.get(1)));
                    pst_function.executeUpdate();
                    if (i % 1000 == 0) System.out.println(i);
                    i++;
                }
            }
            System.out.println(i);
        }

    }
}

