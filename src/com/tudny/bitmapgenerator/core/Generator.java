package com.tudny.bitmapgenerator.core;

import com.tudny.tudnylogger.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Generator {
	public static final String TAG = Generator.class.getSimpleName();

	public static void generate(String path){
		Log.d(TAG, "generate: " + path);
		File file = new File(path);

		try {
			BufferedImage originalImage = ImageIO.read(file);

			int pictureWidth = originalImage.getWidth();
			int pictureHeight = originalImage.getHeight();

			Log.d(TAG, "Picture size: " + pictureWidth + "x" + pictureHeight);

			BufferedImage remixImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

			remix(originalImage, remixImage);

			String directory = file.getParent();
			Log.d(TAG, directory);

			String sep = System.getProperty("file.separator");
			String pathToOutput = directory + sep + ("remix_" + file.getName());

			Log.d(TAG, pathToOutput);

			File outputFile = new File(pathToOutput);
			boolean isNewFileCreated = outputFile.createNewFile();
			Log.d(TAG, "Is new file created: " + isNewFileCreated);
			ImageIO.write(remixImage, "png", outputFile);

			Log.d(TAG, "Saved to file: " + file.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void remix(BufferedImage originalImage, BufferedImage remixImage) {

		final int progress_w = 500;
		final int progress_h = 50;
		JFrame jFrame = new JFrame("Progress");
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jFrame.setSize(progress_w, progress_h);
		jFrame.setResizable(false);
		JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.setBackground(Color.WHITE);
		jFrame.add(jp);
		JPanel rectangle = new JPanel();
		rectangle.setBackground(Color.decode("#54ff93"));
		rectangle.setBounds(0, 0, 0, progress_h);

		jp.add(rectangle);
		jFrame.setVisible(true);


		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int tries = width * height;

		Random rand = new Random();

		for(int i = 0; i < tries; i++){
			if(i % 1000 == 0){
				Log.d(TAG, i + "");
				int w = (int)(500L * (long)i / (long)tries);
				rectangle.setBounds(0, 0, w, progress_h);
				int progress = (int) ((long)i * 100L / (long)tries);
				jFrame.setTitle("Progress " + progress + "%");
			}

			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			int color = originalImage.getRGB(x, y);
			int[] dx = {-1, 0, 1,
					-2, -1, 0, 1, 2,
				-3, -2, -1, 0, 1, 2, 3,
				-3, -2, -1, 0, 1, 2, 3,
				-3, -2, -1, 0, 1, 2, 3,
					-2, -1, 0, 1, 2,
						-1, 0, 1};
			/*
			*          {-1, 0, 1,
					-2, -1, 0, 1, 2,
					-2, -1, 0, 1, 2,
					-2, -1, 0, 1, 2,
						-1, 0, 1};
			* */
			//{0, -1, 0, 1, 0};

			int[] dy = {
						-3, -3, -3,
					-2, -2, -2, -2, -2,
				-1, -1, -1, -1, -1, -1, -1,
				 0,  0,  0,  0,  0,  0,  0,
				 1,  1,  1,  1,  1,  1,  1,
					 2,  2,  2,  2,  2,
						 3,  3,  3};

			/*
			*          {-2, -2, -2,
					-1, -1, -1, -1, -1,
					 0,  0,  0,  0,  0,
					 1,  1,  1,  1,  1,
						 2,  2,  2};
			* */
			//{1, 0, 0, 0, -1};
			for(int k = 0; k < dx.length; k++){
				int nx = x + dx[k];
				int ny = y + dy[k];
				if(nx < 0 || width <= nx || ny < 0 || height <= ny) continue;
				remixImage.setRGB(nx, ny, color);
			}
		}

		jFrame.setVisible(false);
		jFrame.dispose();
	}
}
