package com.tudny.bitmapgenerator;

import com.tudny.bitmapgenerator.core.Generator;

public class Main {
	public static void main(String[] args) throws Exception {
		if(args.length != 1){
			throw new Exception("You should provide path to your photo <C:\\Users\\user\\Pictures\\xx.png>!");
		}

		Generator.generate(args[0]);
	}
}
