package com.junerking.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class InputNumberSort {

	public static void main(String[] args) {

		Scanner cin = new Scanner(System.in);

		while (cin.hasNext()) {
			int n = cin.nextInt();
			ArrayList<Integer> num = new ArrayList<Integer>();
			for (int i = 0; i < n; i++) {
				num.add(cin.nextInt());
			}
			Collections.sort(num);
			for (int i = 0; i < n; i++) {
				System.out.print(num.get(i) + " ");
			}
			System.out.println();
		}

	}

}
