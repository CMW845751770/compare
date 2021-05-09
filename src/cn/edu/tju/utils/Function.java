package cn.edu.tju.utils;

public class Function {
    private int ID;
    private String name;
    private String content;
    private double sim;

    public double getSim() {
        return sim;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSim(double sim) {
        this.sim = sim;
    }

    public int getID() {
        return ID;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public Function(int ID, String name, String content, double sim) {
        this.ID = ID;
        this.name = name;
        this.content = content;
        this.sim = sim;
    }

    public String toSting() {
        return "ID:" + ID + ",name:" + name + ",sim:" + sim;
    }
}
