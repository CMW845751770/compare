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
                if (keyWordSet.contains(str)) {
                    result += str;
                }
            }
            head = tail;
        }
        return result;
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
    }
}
