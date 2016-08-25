package com.iclick.knn;

import java.util.ArrayList;

public class KNN {
	// 为4个类别设置权重，默认权重比一致  
	public int[]  classWeightArray=new int[]{1,1,1,1};
	
	//测试数据地址
	private  String  testDataPath;
	
	//训练数据的地址
	
	private  String trainDataPath;
	
	//分类的不同类型
	
	private  ArrayList<String> todo;
}
