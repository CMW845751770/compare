package compare.cplusplus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

import compare.Compare;

import javax.xml.transform.Result;

public class CPlusPlusCompare extends Compare {
    /*C++关键字*/
    private static CPlusPlusCompare cmp = new CPlusPlusCompare();
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/Graduate?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "Zhao@9295";

    private String keyWords = "and|asm|auto|bad_cast|bad_typeid|bool|break|case|catch|char|class|const|const_cast" +
            "|continue|default|delete|do|double|dynamic_cast|else|enum|except|explicit|extern|false|finally|float|for" +
            "|friend|goto|if|inline|int|long|mutable|namespace|new|operator|or|private|protected|public|register|reinterpret_cast" +
            "|return|short|signed|sizeof|static|static_cast|struct|switch|template|this|throw|true|try|type_info|typedef" +
            "|typeid|typename|union|unsigned|using|virtual|void|volatile|wchar_t|while";
    private HashSet<String> keyWordSet = new HashSet<>();
    private LD ld = new LD();

    public CPlusPlusCompare() {
        String list[] = keyWords.split("\\|");
        for (String keyword : list) {
            keyWordSet.add(keyword);
        }
    }

    private String delVariables(String code) {
        code = "   " + code + "  ";
        int pos1 = 0, pos2 = 0;
        int len = code.length();
        boolean isVariables = false;
        StringBuffer ret = new StringBuffer();
        while (pos1 < len) {
            pos2++;
            if (isVariables) {
                if (code.substring(pos2, pos2 + 2).replaceAll("[0-9a-zA-Z_][^a-zA-Z_]", "").equals("")) {
                    isVariables = false;
                    String vv = code.substring(pos1, pos2 + 1);
                    if (this.keyWordSet.contains(vv)) {
                        ret.append(vv);
                    }
                    pos1 = pos2 + 1;
                }
            } else {
                if (code.substring(pos2, pos2 + 2).replaceAll("[^\\._a-zA-Z][_a-zA-Z]", "").equals("")) {
                    isVariables = true;
                    ret.append(code.substring(pos1, pos2 + 1));
                    pos1 = pos2 + 1;
                }
            }
            if (pos2 == len - 2) break;
        }

        return ret.toString().trim();
    }

    @Override
    public String getPreProcessedCode(String filePath) {
        // TODO Auto-generated method stub
        String code = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            StringBuffer buf = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                buf.append(line + "\n");
            }
            //删除所有注释
            code = DelComments.delComments(buf.toString());
            int pos1 = 0, pos2 = 0;
            int len = code.length();
            boolean isString = false;
            StringBuffer ret = new StringBuffer();
            while (pos1 < len) {
                pos2++;
                if (isString) {
                    if (pos2 < len - 1) {
                        if (code.substring(pos2, pos2 + 1).equals("\"") && !code.subSequence(pos2 - 1, pos2).equals("\\")) {
                            isString = false;
                            ret.append(delVariables(code.substring(pos1, pos2 + 1)));
                            pos1 = pos2 + 1;
                        }
                    } else {
                        break;
                    }
                } else {
                    if (pos2 < len - 1) {
                        if (code.substring(pos2, pos2 + 1).equals("\"")) {
                            isString = true;
                            ret.append(delVariables(code.substring(pos1, pos2)));
                            pos1 = pos2;
                        }
                    } else {
                        ret.append(delVariables(code.substring(pos1, code.length())));
                        break;
                    }
                }
            }
            code = ret.toString();
            //删除所有空格和换行
            code = code.replaceAll("\\s", "");

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    @Override
    public double getSimilarity(String code1, String code2) {
        // TODO Auto-generated method stub
        return 1 - ld.ld(code1, code2) * 1.0 / Math.max(code1.length(), code2.length());
    }

    public static void main(String[] args) throws IOException {
        String path1 = "D:\\GraduationProject\\testC\\main.cpp";
        String path2 = "D:\\GraduationProject\\testC\\main1.cpp";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path1), "UTF-8"));
        StringBuffer buf = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            buf.append(line + "\n");
        }

        String code = buf.toString();
        System.out.println(code);
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
//            String sql = "insert into `function` (id, `name`, file_id) values(?,?,?)";
//            String sql = "insert into `function` values(?,?,?,?)";
            String sql = "insert into file values(?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "1main");
            statement.setString(2, "main");
            statement.setInt(3, 1);
            statement.setString(4, "C++");
            statement.executeUpdate(sql);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        ArrayList<String> codeList = new ArrayList<>();
//        codeList.add(cmp.getPreProcessedCode(path1));
//        codeList.add(cmp.getPreProcessedCode(path2));
//        double s = cmp.getSimilarity(codeList.get(0), codeList.get(1));
//        System.out.println(s);

//        CPlusPlusCompare cmp = new CPlusPlusCompare();
//        File dic = new File("D:\\GraduationProject\\testC");
//        String names[] = {"main.cpp", "main1.cpp"};
//
//        for (String name : names) {
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\GraduationProject\\testC\\" + name)));
//            //bw.write("题目："+name);

//            System.out.println("题目：" + name);
//            bw.newLine();
//            ArrayList<String> idList = new ArrayList<>();
//            ArrayList<String> codeList = new ArrayList<>();
//            for (File f1 : dic.listFiles()) {
//                File f2 = new File(f1.getAbsoluteFile() + "\\" + name);
//                if (f2.exists()) {
//                    idList.add(f1.getName());
//                    codeList.add(cmp.getPreProcessedCode(f2.getAbsolutePath()));
//                }
//            }
//            for (int i = 0; i < codeList.size(); i++) {
//                for (int j = i + 1; j < codeList.size(); j++) {
//                    double s = cmp.getSimilarity(codeList.get(i), codeList.get(j));
//                    System.out.println(s);
//                    if (s >= 0.7) {
//                        bw.write(idList.get(i) + "\t" + idList.get(j) + "\t" + s);
//                        bw.newLine();
//                    }
//                }
//            }
//            bw.close();
//        }
    }
}
