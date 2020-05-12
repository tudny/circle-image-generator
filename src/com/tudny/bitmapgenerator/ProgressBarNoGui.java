package com.tudny.bitmapgenerator;

import com.tudny.tudnylogger.Log;

public class ProgressBarNoGui {

	private int progressBarCharLength = 100;
	private String name;

	public ProgressBarNoGui(String name, int progressBarCharLength) {
		this.name = name;
		this.progressBarCharLength = progressBarCharLength;
	}

	public ProgressBarNoGui(String name) {
		this.name = name;
	}

	private void print(String s){
		System.out.print(s);
	}

	private void printStart(){
		print(name + " \t[");
	}

	private void printEnd(double progress){
		int percent = (int) (progress * 100.0);
		print("] \t" + percent + "%\r");
	}

	private void printProgress(int howMany){
		for (int i = 0; i < progressBarCharLength; i++) {
			if(howMany --> 0){
				print("=");
			} else {
				print(" ");
			}
		}
	}

	/** @param progress -> should be from range <0.0; 1.0>
	 * */
	public void update(double progress){
		int howManyChars = (int) ((double)progressBarCharLength * progress);
		printStart();
		printProgress(howManyChars);
		printEnd(progress);
	}

	public void close(){
		print("");
	}
}
