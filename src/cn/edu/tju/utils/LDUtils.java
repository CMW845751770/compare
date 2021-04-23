package cn.edu.tju.utils;

public class LDUtils {

    /**
     * 计算矢量距离
     * Levenshtein Distance(LDUtils)
     *
     * @param str1 str1
     * @param str2 str2
     * @return ld
     */
    public int ld(String str1, String str2) {
        int[][] d;
        int n = str1.length();
        int m = str2.length();
        int i; //iterate str1
        int j; //iterate str2
        char ch1; //str1
        char ch2; //str2
        int temp;
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            ch1 = str1.charAt(i - 1);
            for (j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    private int min(int one, int two, int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min) {
            min = three;
        }
        return min;
    }

    public double sim(String str1, String str2) {
        int ld = ld(str1, str2);
        return 1 - (double) ld / Math.max(str1.length(), str2.length());
    }

    public static void main(String[] args) {
        LDUtils ld = new LDUtils();
        double num = ld.ld("中国人民是人才", "人民");
        System.out.println(num);
    }
}