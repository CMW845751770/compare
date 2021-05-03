package cn.edu.tju.utils;

import javax.swing.*;
import java.awt.*;

public class FileChooserUtils {

    public String getPath(){
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        Component parent = null;
        int returnVal = jFileChooser.showSaveDialog(parent);


        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String selectPath = jFileChooser.getSelectedFile().getPath();
            return selectPath;
        }

        return  "";
    }

    public static void main(String[] args) {
        FileChooserUtils fileChooserUtils = new FileChooserUtils();
        String path = fileChooserUtils.getPath();
        System.out.println(path);
    }
}
