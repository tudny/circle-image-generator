package com.tudny.bitmapgenerator.core.tools;

import com.sun.deploy.util.WinRegistry;

import javax.swing.*;
import java.awt.*;

public class ProgressBar {

	private final int progressBarWidth = 500;
	private final int progressBarHeight = 50;

	private JFrame jFrame;
	private JPanel rectangle;

	public ProgressBar() {
		String TITLE = "Progress";
		jFrame = new JFrame(TITLE);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.setSize(progressBarWidth, progressBarHeight);

		JPanel jPanel = new JPanel();
		jPanel.setLayout(null);
		Color backgroundColor = isDarkModeOn() ? Color.BLACK : Color.WHITE;
		jPanel.setBackground(backgroundColor);
		jFrame.add(jPanel);

		rectangle = new JPanel();
		rectangle.setBounds(0, 0, 0, progressBarHeight);
		final String progressColorBright = "#54ff93";
		final String progressColorDark = "#328751";
		String progressColor = isDarkModeOn() ? progressColorDark : progressColorBright;
		rectangle.setBackground(Color.decode(progressColor));

		jPanel.add(rectangle);
	}

	public void setVisible(boolean visible){
		jFrame.setVisible(visible);
	}

	public void close(){
		jFrame.dispose();
	}

	/** @param progress -> should be from range <0.0; 1.0>
	 * */
	public void updateProgress(double progress, String info) {
		if(progress < 0.0 || 1.0 < progress) throw new IllegalArgumentException("Progress argument should be from range <0.0; 1.0>, but was" + progress + "\n");
		int currentWidth = (int) (progressBarWidth * progress);
		rectangle.setBounds(0, 0,currentWidth, progressBarHeight);
		int percentProgress = (int) (progress * 100.0);
		jFrame.setTitle("Progress" + info + ": " + percentProgress + "%");
	}

	public static boolean isDarkModeOn(){
		Integer value = WinRegistry.getInteger(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "AppsUseLightTheme");
		return (value == 0);
	}
}
