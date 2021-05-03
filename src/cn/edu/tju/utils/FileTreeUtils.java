package cn.edu.tju.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FileTreeUtils {
    static String jsonString = "";

    public static List<List<String>> getJavaFileList(String dirPath) {
        List<List<String>> functions = new ArrayList<>();
        List<FileTree> treeNode = new ArrayList<>();
        Queue<FileTree> fileQueue = new LinkedList<>();
        File dir = new File(dirPath);
        FileTree fileTree1 = null;
        FileTree fileTree = new FileTree(0, 0, dir.getName().toString(), "root", dir);
        fileQueue.offer(fileTree);
        int index = 1;
        while (!fileQueue.isEmpty()) {
            fileTree = fileQueue.poll();
            File file = fileTree.getFile();
            if (file.isDirectory()) {
//                treeNode.add(fileTree);
                jsonString = jsonString + fileTree.toString() + ",";
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        fileTree1 = new FileTree(index, fileTree.getId(), files[i].getName(), "root", files[i]);
                    } else {
                        fileTree1 = new FileTree(index, fileTree.getId(), files[i].getName(), "file", files[i]);
                    }
                    fileQueue.offer(fileTree1);
                    index++;
                }
            } else {
                if (file.getName().endsWith(".java")) {
//                    treeNode.add(fileTree);
                    jsonString = jsonString + fileTree.toString() + ",";
                    List<List<String>> function = FileUtils.getFunctionFromJavaFile(fileTree.getFile());
                    for (List<String> f : function) {
                        fileTree1 = new FileTree(index, fileTree.getId(), f.get(2), "function", null);
                        f.add(index + "");
                        treeNode.add(fileTree1);
                        jsonString = jsonString + fileTree1.toString() + ",";
                        index ++;
                    }
                    functions.addAll(function);
                }
            }
        }

        return functions;
    }

    public static void main(String[] args) {
        String path = "D:\\GraduationProject\\test";
        List<List<String>> f = getJavaFileList(path);
//        for (FileTree file : f) {
//            System.out.println(file.toString());
//        }

        System.out.println(f);

//        System.out.println(jsonString);
//        JSONObject jsonObject = new JSONObject();
        jsonString = jsonString.substring(0, jsonString.length() - 1);
        jsonString = "[" + jsonString + "]";
        boolean b = CreateJsonFileUtils.createJsonFile(jsonString, "D:\\GraduationProject\\json", "test");
    }
}
