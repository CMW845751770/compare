package cn.edu.tju.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if(Objects.isNull(fileList)){
            return files;
        }
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                files.addAll(getJavaFileList(dirPath +  "\\" + fileList[i].getName()));
            } else {
                if (fileList[i].getName().endsWith(".java")) {
                    files.add(fileList[i]);
                }
            }
        }
        return files;
//        File[] files = dir.listFiles();
//        System.out.println(files.length);
//        for (File file : files) {
//            if (file.isDirectory()) {
//                f.addAll(getJavaFileList(files[1]));
//            }
//        }
//        if (Objects.nonNull(files)) {
//            return Arrays.stream(files).filter(file -> file.isFile() && (file.getName().endsWith(".java")))
//                    .collect(Collectors.toList());
//        }
//        return new ArrayList<>();
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
    public static List<List<String>> getFunctionFromJavaFile(File file) throws Exception {
        CompilationUnit unit = JavaParser.parse(file);
        List<MethodDeclaration> methodDeclarationList = unit.findAll(MethodDeclaration.class);
        List<List<String>> functionList = new ArrayList<>();
        for (MethodDeclaration m : methodDeclarationList) {
            List<String> list = new ArrayList<>();
            list.add(m.getName().toString());
            m.getBody().ifPresent(body -> {
                list.add(body.toString());
            });
            functionList.add(list);
        }
        return functionList;
    }


    public static void main(String[] args) throws Exception {
        String dirPath = "D:/CppWorkSpace";
        List<File> fileList = getJavaFileList(dirPath);
        for (File file : fileList) {
            System.out.println(getFunctionFromJavaFile(file));
        }
    }
}
