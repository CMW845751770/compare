package cn.edu.tju.utils;

import java.io.File;

public class FileTree {
    private int id;
    private int pId;
    private String name;
    private String type;
    private File file;

    public FileTree(int id, int pId, String name, String type, File file) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.type = type;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public void setType(String type) {
        this.type= type;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"parent\":\"" + pId + "\"," +
                "\"text\":\"" + name + "\"," +
                "\"type\":\"" + type + "\"" +
                "}";
    }
}

