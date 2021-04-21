package cn.edu.tju.comparator;

public abstract class Compare {
	public abstract String getPreProcessedCode(String filePath);
	public abstract double getSimilarity(String code1,String code2);
}
