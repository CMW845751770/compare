package cn.edu.tju.utils;

import java.util.HashSet;

public class VariablesUtils {
    private static String keyWords = "System|abstract|assert|boolean|break|byte|case|catch|char|class|continue|" +
            "default|do|double|else|enum|extends|final|finally|float|for|if|implements|import|int|" +
            "interface|instanceof|long|native|new|package|private|protected|public|return|short|static" +
            "|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while" +
            "|goto|const|true|false|null|public";

    private HashSet<String> keyWordSet = new HashSet<>();

    public VariablesUtils() {
        String list[] = keyWords.split("\\|");
        for (String keyword : list) {
            keyWordSet.add(keyword);
        }
    }

    public static boolean isChar(char c) {
        if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') return true;
        else return false;
    }

    public String delVariables(String code) {
        String result = "";
        char[] c = code.toCharArray();
        int head = 0, tail = 0;
        while (tail < code.length())  {
            if (!isChar(c[tail])) {
                result += c[tail];
                tail++;
            }
            else {
                while (isChar(c[tail])) {
                    tail++;
                }
                String str = code.substring(head, tail);
//                System.out.println(str);
                if (keyWordSet.contains(str)) {
//                    System.out.println(str);
                    result += str;
                }
            }
            head = tail;
        }
        return result;
//        code = " " + code + " ";
//        int pos1 = 0, pos2 = 0;
//        int len = code.length();
//        boolean isVariables = false;
//        StringBuffer ret = new StringBuffer();
//        while (pos1 < len) {
//            pos2++;
//            if (isVariables) {
//                if (code.substring(pos2, pos2 + 2).replaceAll("[0-9a-zA-Z_][^a-zA-Z_]", "").equals("")) {
//                    isVariables = false;
//                    String vv = code.substring(pos1, pos2 + 1);
//                    if (keyWordSet.contains(vv)) {
//                        ret.append(vv);
//                    }
//                    pos1 = pos2 + 1;
//                }
//            } else {
//                if (code.substring(pos2, pos2 + 2).replaceAll("[^\\._a-zA-Z][_a-zA-Z]", "").equals("")) {
//                    isVariables = true;
//                    ret.append(code.substring(pos1, pos2 + 1));
//                    pos1 = pos2 + 1;
//                }
//            }
//            if (pos2 == len - 2) break;
//        }
//        return ret.toString().trim();
    }

    public static void main(String[] args) {
        VariablesUtils variablesUtils = new VariablesUtils();
        String code = "    public PrintProgress(int scale) {\n" +
                "        if (scale >= 2) {\n" +
                "            this.scale = 2;\n" +
                "        } else {\n" +
                "            this.scale = 1;\n" +
                "        }\n" +
                "    }";

        System.out.println(variablesUtils.delVariables(code));
//        delVariables(code);
    }
}
