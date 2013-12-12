package com.junerking.skeleton;

import com.junerking.skeleton.DataDef.NodeData;

// ==================================
// 行向量，列矩阵
// a---b---0
// |---|---|
// c---d---0
// |---|---|
// tx--ty--1
// ==================================

public class TransfromUtils {
	private static IMatrix help_matrix1 = new IMatrix();
	private static IMatrix help_matrix2 = new IMatrix();

	public static void transfromPointWithParent(NodeData bone_data, NodeData pare_data) {

		nodeToMatrix(bone_data, help_matrix1);
		nodeToMatrix(pare_data, help_matrix2);

		help_matrix1.mul(help_matrix2.inv());

		matrixToNode(help_matrix1, bone_data);
	}

	public static void nodeToMatrix(NodeData node, IMatrix matrix) {

//		System.out.println("node to Matrix: " + node.x + " " + node.y + " " + node.scale_x + " " + node.scale_y + " " + node.skew_x + " "
//				+ node.skew_y);

		matrix.a = (float) (node.scale_x * Math.cos(node.skew_y));
		matrix.b = (float) (node.scale_x * Math.sin(node.skew_y));
		matrix.c = (float) (-node.scale_y * Math.sin(node.skew_x));
		matrix.d = (float) (node.scale_y * Math.cos(node.skew_x));

		matrix.tx = node.x;
		matrix.ty = node.y;
	}

	public static void matrixToNode(IMatrix matrix, NodeData node) {
//		System.out.println("" + matrix.toString());

		node.skew_x = (float) (Math.atan2(matrix.d, matrix.c) - Math.PI * 0.5f);
		node.skew_y = (float) (Math.atan2(matrix.b, matrix.a));
		node.scale_x = (float) Math.sqrt(matrix.a * matrix.a + matrix.b * matrix.b);
		node.scale_x = (float) Math.sqrt(matrix.c * matrix.c + matrix.d * matrix.d);
		node.x = matrix.tx;
		node.y = matrix.ty;

//		System.out.println(node.x + " " + node.y);
	}
}
