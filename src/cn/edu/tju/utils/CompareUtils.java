package cn.edu.tju.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompareUtils {
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
    static PreparedStatement pst_function = null;

    public List<Function> compare(String code) throws SQLException {
        List<Function> result = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            result.add(new Function(i, "", "", 0.0));
        }
        SimilarityCalculator sm = new SimilarityCalculator();
        String sql = "select count(1) from `function`";
        pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        int count = 0;
        if (rs.next()) count = rs.getInt(1);
//        System.out.println(count);
        String sql_function = "select ID, `name`, content from `function` where ID >= ? and ID < ?";
        pst_function = conn.prepareStatement(sql_function);
        for (int i = 1; i <= count; i += 100) {
            int upIndex = i + 100;
            if (upIndex >= count) {
                upIndex = count;
            }
            pst_function.setInt(1, i);
            pst_function.setInt(2, upIndex);
            ResultSet rst = pst_function.executeQuery();
            while (rst.next()) {
                String content = rst.getString("content");
                double sim = sm.similarity(code, content);
                for (int j = 0; j < result.size(); j++) {
                    if (sim >= result.get(j).getSim()) {
                        Function f = new Function(rst.getInt("ID"), rst.getString("name"), rst.getString("content"), sim);
                        result.add(j, f);
                        result.remove(result.size() - 1);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws SQLException {
        CompareUtils compareUtils = new CompareUtils();
        String code = "{\n" +
                "    ActionResult<List<Wo>> result = new ActionResult<>();\n" +
                "    EqualsTerms equals = new EqualsTerms();\n" +
                "    equals.put(\"person\", effectivePerson.getDistinguishedName());\n" +
                "    result = this.standardListPrev(Wo.copier, id, count, JpaObject.sequence_FIELDNAME, equals, null, null, null, null, null, null, null, true, DESC);\n" +
                "    return result;\n" +
                "}";
        List<Function> result = compareUtils.compare(code);
        for (Function f: result) {
            System.out.println(f.toSting());
        }
    }
}
