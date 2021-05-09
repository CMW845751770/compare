package cn.edu.tju.utils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimilarityCalculator {
    private static SimilarityCalculator getSimilarity = new SimilarityCalculator();
    private LDUtils ld = new LDUtils();
    private static VariablesUtils variablesUtils = new VariablesUtils();

    public static String getPreProcessedCode(String code) {
        //删除所有注释
        code = CommentUtils.clearComments(code);
        code = variablesUtils.delVariables(code);
        //删除所有空格和换行
        code = code.replaceAll("\\s", "");
//        System.out.println(code);
        return code;
    }

    public double similarity(String code1, String code2) {
        // TODO Auto-generated method stub
        code1 = getPreProcessedCode(code1);
        code2 = getPreProcessedCode(code2);
        return 1 - ld.ld(code1, code2) * 1.0 / Math.max(code1.length(), code2.length());
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/Graduate?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String USER = "root";
        String PASS = "Zhao@9295";

        Connection conn;
        PreparedStatement pst = null;
        PreparedStatement pst_update = null;
        List<Integer> IDList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();

        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            String sql = "select ID, content from `function` where ID < 10";
        String sql = "select ID, `name`, content from `function` where ID >= ? and ID < ?";
        String sql_update = "update `function` set token = ? where ID = ?";
        pst_update = conn.prepareStatement(sql_update);
        pst = conn.prepareStatement(sql);
        int count = 417173;
        for (int i = 1; i <= count; i += 1000) {
            int upIndex = i + 100;
            if (upIndex >= count) {
                upIndex = count;
            }
            pst.setInt(1, i);
            pst.setInt(2, upIndex);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                pst_update.setInt(2, rst.getInt("ID"));
                pst_update.setString(1, SimilarityCalculator.getPreProcessedCode(rst.getString("content")));
                pst_update.executeUpdate();
            }
            System.out.println(i);
        }
    }
}
