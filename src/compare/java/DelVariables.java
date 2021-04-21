package compare.java;

import java.io.*;
import java.util.HashSet;

public class DelVariables {
    private String keyWords = "System|abstract|assert|boolean|break|byte|case|catch|char|class|continue|" +
            "default|do|double|else|enum|extends|final|finally|float|for|if|implements|import|int|" +
            "interface|instanceof|long|native|new|package|private|protected|public|return|short|static" +
            "|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while" +
            "|goto|const|true|false|null|public";

    private static HashSet<String> keyWordSet = new HashSet<>();

    public DelVariables() {
        String list[] = keyWords.split("\\|");
        for (String keyword : list) {
            keyWordSet.add(keyword);
        }
    }

    public static String delVariables(String code) {
        code = " " + code + " ";
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
                    if (keyWordSet.contains(vv)) {
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
}
