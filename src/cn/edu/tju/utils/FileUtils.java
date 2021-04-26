package cn.edu.tju.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: CMW天下第一
 * @Date: 2021/4/21 11:15
 */
public class FileUtils {

    /**
     * 从给定的目录路径中读取所有的Java文件
     *
     * @param dirPath Java文件的目录路径
     * @return
     */
    public static List<File> getJavaFileList(String dirPath) {
        List<File> files = new ArrayList<>();
        File dir = new File(dirPath);
        File[] fileList = dir.listFiles();
        if (Objects.isNull(fileList)) {
            return files;
        }
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                files.addAll(getJavaFileList(dirPath + "\\" + fileList[i].getName()));
            } else {
                if (fileList[i].getName().endsWith(".java")) {
                    files.add(fileList[i]);
                }
            }
        }
        return files;
    }


    /**
     * 读取文件中的内容
     *
     * @param file Java文件
     * @return
     */
    public static String printFileContent(File file) {
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }


    /**
     * 从Java文件中读取所有的函数代码块
     *
     * @param file Java文件
     * @return
     */
    public static List<List<String>> getFunctionFromJavaFile(File file) {
        CompilationUnit unit = null;
        List<List<String>> functionList = new ArrayList<>();
        try {
            unit = JavaParser.parse(file);
        } catch (FileNotFoundException | ParseProblemException e) {
            return functionList;
        }
        List<MethodDeclaration> methodDeclarationList = unit.findAll(MethodDeclaration.class);
        for (MethodDeclaration m : methodDeclarationList) {
            List<String> list = new ArrayList<>();
            m.getBody().ifPresent(body -> {
                //为方法名::参数类型格式_个数
                NodeList<Parameter> parameters = m.getParameters();
                //处理参数类型为字符串，并按照升序排列，并统计个数
                List<String> stringList = parameters.stream().map(parameter -> {
                    return parameter.getType().asString();
                }).collect(Collectors.toList());
                Map<String, Integer> countMap = new TreeMap<>();
                for (String type : stringList) {
                    countMap.merge(type, 1, Integer::sum);
                }
                String key = m.getNameAsString();
                for (String type : countMap.keySet()) {
                    key = String.join("::", key, type + "_" + countMap.getOrDefault(type, 0));
                }
                list.add(key);
                list.add(body.toString());
                list.add(m.getName().toString());
                functionList.add(list);
            });
        }
        return functionList;
    }


    public static void main(String[] args) throws Exception {
        String dirPath = "D:\\GraduationProject\\code_data\\elasticsearch-master\\server\\src\\main\\java\\org\\elasticsearch\\bootstrap";
        List<File> fileList = getJavaFileList(dirPath);
        for (File file : fileList) {
            System.out.println(getFunctionFromJavaFile(file));
        }
    }
}
