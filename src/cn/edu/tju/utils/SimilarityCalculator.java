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

    public static void main(String[] args) throws IOException {
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String DB_URL = "jdbc:mysql://localhost:3306/Graduate?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String USER = "root";
        String PASS = "Zhao@9295";

        Connection conn;
        PreparedStatement pst = null;
        List<Integer> IDList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "select ID, content from `function` where ID < 5";
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                IDList.add(rs.getInt("ID"));
                contentList.add(rs.getString("content"));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        for (int i = 0; i < IDList.size() - 1; i++) {
            for (int j  = i + 1; j < IDList.size(); j++) {
                System.out.println(IDList.get(i) + " " + IDList.get(j) + " " + getSimilarity.similarity(contentList.get(i), contentList.get(j)));
            }
        }
//        System.out.println(getSimilarity.similarity(codeList.get(0), codeList.get(1)));

    }
}
