package com.tudny.circles.core.tools;

public class ProgressBarNoGui implements ProgressBar {

    private int progressBarCharLength = 100;
    private final String name;

    public ProgressBarNoGui(String name, int progressBarCharLength) {
        this.name = name;
        this.progressBarCharLength = progressBarCharLength;
    }

    public ProgressBarNoGui(String name) {
        this.name = name;
    }

    private void print(String s) {
        System.out.print(s);
    }

    private void printStart(String info) {
        print(name + " " + info + " \t[");
    }

    private void printEnd(double progress) {
        int percent = (int) (progress * 100.0);
        print("] \t" + percent + "%\r");
    }

    private void printProgress(int howMany) {
        print("=".repeat(howMany) + " ".repeat(progressBarCharLength - howMany));
    }

    /**
     * @param progress -> should be from range <0.0; 1.0>
     */
    public void update(double progress, String info) {
        int howManyChars = (int) ((double) progressBarCharLength * progress);
        printStart(info);
        printProgress(howManyChars);
        printEnd(progress);
    }

    public void close() {
        print("");
    }
}
