package cn.edu.tju.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SimilarityCalculator {
    private static SimilarityCalculator getSimilarity = new SimilarityCalculator();
    private LDUtils ld = new LDUtils();
    public static String getPreProcessedCode(String code) {
        //删除所有注释
        System.out.println(code);
        code = CommentUtils.delComments(code);
        System.out.println(code);
        System.out.println("*******************************************");
        code = VariablesUtils.delVariables(code);
        System.out.println(code);
        System.out.println("*******************************************");
        //删除所有空格和换行
        code = code.replaceAll("\\s", "");
        System.out.println(code);
        System.out.println("*******************************************");
        return code;
    }

    public double similarity(String code1, String code2) {
        // TODO Auto-generated method stub
        return 1 - ld.ld(code1, code2) * 1.0 / Math.max(code1.length(), code2.length());
    }

    public static void main(String[] args) throws IOException {
        String path1 = "D:\\GraduationProject\\test\\test1.java";
        String path2 = "D:\\GraduationProject\\test\\test2.java";

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path1), StandardCharsets.UTF_8));
        StringBuffer buf = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            buf.append(line + "\n");
        }

        String code = buf.toString();

        ArrayList<String> codeList = new ArrayList<>();
        codeList.add(getPreProcessedCode(code));

        br = new BufferedReader(new InputStreamReader(new FileInputStream(path2)));
        buf = new StringBuffer();
        while ((line = br.readLine()) != null) {
            buf.append(line + "\n");
        }
        code = buf.toString();
        codeList.add(getPreProcessedCode(code));
        System.out.println(getSimilarity.similarity(codeList.get(0), codeList.get(1)));
    }
}
