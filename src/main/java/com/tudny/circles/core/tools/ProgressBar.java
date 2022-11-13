package com.tudny.circles.core.tools;

public interface ProgressBar {
    void update(double progress, String updateInfo);
    void close();
}
