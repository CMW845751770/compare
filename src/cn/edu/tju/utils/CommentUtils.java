package cn.edu.tju.utils;

/**
 * @author Insunny
 *
 */
public class CommentUtils {

    private static final char MARK = '"';

    private static final char SLASH = '/';

    private static final char BACKSLASH = '\\';

    private static final char STAR = '*';

    private static final char NEWLINE = '\n';

    //引号
    private static final int TYPE_MARK = 1;

    //斜杠
    private static final int TYPE_SLASH = 2;

    //反斜杠
    private static final int TYPE_BACKSLASH = 3;

    //星号
    private static final int TYPE_STAR = 4;

    // 双斜杠类型的注释
    private static final int TYPE_DSLASH = 5;

    public static char[] del(char[] _target, int _start, int _end) {
        char[] tmp = new char[_target.length - (_end - _start + 1)];
        System.arraycopy(_target, 0, tmp, 0, _start);
        System.arraycopy(_target, _end + 1, tmp, _start, _target.length - _end - 1);
        return tmp;
    }

    public static String delComments(String _target) {
        int preType = 0;
        int mark = -1, cur = -1, token = -1;// 输入字符串
        char[] input =  _target.toCharArray();
        for (cur = 0; cur < input.length; cur++) {
            if (input[cur] == MARK) {// 首先判断是否为转义引号
                if (preType == TYPE_BACKSLASH)
                    continue;// 已经进入引号之内
                if (mark > 0) {// 引号结束
                    mark = -1;
                } else {
                    mark = cur;
                }
                preType = TYPE_MARK;
            } else if (input[cur] == SLASH) {// 当前位置处于引号之中
                if (mark > 0)
                    continue;// 如果前一位是*，则进行删除操作
                if (preType == TYPE_STAR) {
                    input = del(input, token, cur);// 退回一个位置进行处理
                    cur = token - 1;
                    preType = 0;
                } else if (preType == TYPE_SLASH) {
                    token = cur - 1;
                    preType = TYPE_DSLASH;
                } else {
                    preType = TYPE_SLASH;
                }
            } else if (input[cur] == BACKSLASH) {
                preType = TYPE_BACKSLASH;
            } else if (input[cur] == STAR) {// 当前位置处于引号之中
                if (mark > 0)
                    continue;// 如果前一个位置是/,则记录注释开始的位置
                if (preType == TYPE_SLASH) {
                    token = cur - 1;
                }
                preType = TYPE_STAR;
            } else if(input[cur] == NEWLINE)
            {
                if(preType == TYPE_DSLASH)
                {
                    input = del(input, token, cur);// 退回一个位置进行处理
                    cur = token - 1;
                    preType = 0;
                }
            }

        }
        return new String(input);
    }


//    private static final Pattern pattern = Pattern.compile(, Pattern.DOTALL);

    public static String clearComments(String content){
        content = content.replaceAll("//.+\\r\\n", "");
        return RegularExpressionUtils.createMatcherWithTimeout(content, "//[^\\n]*|/\\*([^*^/]*|[*^/]*|[^*/]*)*\\*+/", 2000).replaceAll("");
    }

    public static void main(String[] args) {
        clearComments("{\n" +
                "    Provider<ValidateJsonAgainstSchemaTask> validateRestSpecTask = project.getTasks().register(\"validateRestSpec\", ValidateJsonAgainstSchemaTask.class, task -> {\n" +
                "        task.setInputFiles(Util.getJavaTestAndMainSourceResources(project, filter -> {\n" +
                "            filter.include(DOUBLE_STAR + \"/rest-api-spec/api/\" + DOUBLE_STAR + \"/*.json\");\n" +
                "            filter.exclude(DOUBLE_STAR + \"/_common.json\");\n" +
                "        }));\n" +
                "        // This must always be specified precisely, so that\n" +
                "        // projects other than `rest-api-spec` can use this task.\n" +
                "        task.setJsonSchema(new File(project.getRootDir(), \"rest-api-spec/src/main/resources/schema.json\"));\n" +
                "        task.setReport(new File(project.getBuildDir(), \"reports/validateJson.txt\"));\n" +
                "    });\n" +
                "    Provider<ValidateJsonNoKeywordsTask> validateNoKeywordsTask = project.getTasks().register(\"validateNoKeywords\", ValidateJsonNoKeywordsTask.class, task -> {\n" +
                "        task.setInputFiles(Util.getJavaTestAndMainSourceResources(project, filter -> {\n" +
                "            filter.include(DOUBLE_STAR + \"/rest-api-spec/api/\" + DOUBLE_STAR + \"/*.json\");\n" +
                "            filter.exclude(DOUBLE_STAR + \"/_common.json\");\n" +
                "        }));\n" +
                "        task.setJsonKeywords(new File(project.getRootDir(), \"rest-api-spec/keywords.json\"));\n" +
                "        task.setReport(new File(project.getBuildDir(), \"reports/validateKeywords.txt\"));\n" +
                "        // There's no point running this task if the schema validation fails\n" +
                "        task.mustRunAfter(validateRestSpecTask);\n" +
                "    });\n" +
                "    project.getTasks().named(\"precommit\").configure(t -> t.dependsOn(validateRestSpecTask, validateNoKeywordsTask));\n" +
                "}");
        System.out.println("end...");
    }
}