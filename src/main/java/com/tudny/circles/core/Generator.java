package com.tudny.circles.core;

import com.github.tudny.tudlogger.Log;
import com.tudny.circles.core.tools.ProgressBarCLI;
import com.tudny.circles.core.tools.ProgressBarNoGui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Generator {
    public static final String TAG = Generator.class.getSimpleName();

    public static final Integer TRIES = 10000;
    public static final Integer TIMEOUT = 10000;
    private static final String REMIX_PREFIX = "remix_";
    private static final Boolean COLOR_MIX = Boolean.TRUE;
    private static final Integer COLOR_MIX_RANGE = 5;

    public static void generate(String path) {
        Log.d(TAG, "generate: " + path);
        File file = new File(path);

        try {
            // Reading file
            BufferedImage originalImage = ImageIO.read(file);
            printImageData(originalImage, file);

            // Creating blank copy
            BufferedImage remixImage = createBlankCopy(originalImage);

            // Remixing image
            remixGrowing(originalImage, remixImage);

            // Setting path to save
            String pathToOutput = createDirectoryForNewFile(file);

            // File to be saved
            File outputFile = new File(pathToOutput);

            boolean isNewFileCreated = outputFile.createNewFile();
            if (!isNewFileCreated) Log.d(TAG + " | Remix picture", "File was overridden.");

            ImageIO.write(remixImage, "png", outputFile);
            Log.d(TAG + " | Remix picture", "Saved file to: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static BufferedImage createBlankCopy(BufferedImage originalImage) {
        int pictureWidth = originalImage.getWidth();
        int pictureHeight = originalImage.getHeight();
        return new BufferedImage(pictureWidth, pictureHeight, BufferedImage.TYPE_INT_RGB);
    }

    private static void printImageData(BufferedImage image, File file) {
        int pictureWidth = image.getWidth();
        int pictureHeight = image.getHeight();
        Log.d(TAG + " | Loaded picture", "Picture name: " + file.getName());
        Log.d(TAG + " | Loaded picture", "Full path: " + file.getAbsolutePath());
        Log.d(TAG + " | Loaded picture", "Picture size: " + pictureWidth + "x" + pictureHeight);
    }

    private static String createDirectoryForNewFile(File file) {
        String folderPath = file.getParent();
        String originalName = file.getName();
        String newName = REMIX_PREFIX + originalName;
        String sep = System.getProperty("file.separator");
        String newFileAbsolutePath = folderPath + sep + newName;
        Log.d(TAG + " | Remix picture", "New file saving path: " + newFileAbsolutePath);
        return newFileAbsolutePath;
    }

    private static void remixGrowing(BufferedImage originalImage, BufferedImage remixImage) {

        ProgressBarNoGui progressBarNoGui = new ProgressBarNoGui("Generating", 100);

        ProgressBarCLI progressBarCLI = new ProgressBarCLI();
        progressBarCLI.setVisible(true);

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int tries = TRIES;

        ArrayList<Circle> circles = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < tries; i++) {

            if (i % 10 == 0) {
                double progress = ((double) (i)) / ((double) (tries));
                progressBarCLI.update(progress, "");
                progressBarNoGui.update(progress, "");
            }

            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            int color = originalImage.getRGB(x, y);

            Circle circle = Circle.factorNewCircle(x, y, color, circles);
            if (circle != null) {
                circles.add(circle);
            }

            // Start growing after placing 10% of circles
            if (i >= tries / 10) {
                for (Circle cir : circles) {
                    if (cir.getRadius() != 0.0) cir.update(circles);
                }
            }
        }

        ArrayList<Circle> stillGrowing = new ArrayList<>();
        for (Circle cir : circles) {
            if (cir.getRadiusVelocity() != 0.0) {
                stillGrowing.add(cir);
            }
        }

        int timeout = TIMEOUT;
        while (stillGrowing.size() > 0 && timeout-- > 0) {

            int iProgress = TIMEOUT - timeout;
            if (iProgress % 10 == 0) {
                double progress = ((double) (iProgress)) / ((double) (TIMEOUT));
                progressBarCLI.update(progress, " filling up");
                progressBarNoGui.update(progress, " filling up");
            }

            ArrayList<Circle> toBeDeleted = new ArrayList<>();
            for (Circle cir : stillGrowing) {
                cir.update(circles);
                if (cir.getRadiusVelocity() == 0.0) {
                    toBeDeleted.add(cir);
                }
            }
            stillGrowing.removeAll(toBeDeleted);
        }

        //// GENERATING IMAGE


        // REVOLUTION using new drawing method taken from https://web.engr.oregonstate.edu/~sllu/bcircle.pdf
        for (Circle cir : circles) {
            drawCircleJohn(remixImage, cir);
        }

        /// closing progress window
        progressBarCLI.setVisible(false);
        progressBarCLI.close();

        progressBarNoGui.close();
    }

    @SuppressWarnings("unused")
    private static void drawCircle(BufferedImage image, Circle cir) {
        int radius = cir.getRadius().intValue();
        int color = cir.getColor();
        if (COLOR_MIX) color = mixColor(color);
        int x = 0;
        int y = radius;
        int xC = cir.getX();
        int yC = cir.getY();
        int dParameter = 3 - 2 * radius;
        putPixel(image, xC, yC, x, y, color);
        while (y >= x) {
            x++;
            if (dParameter >= 0) {
                y--;
                dParameter = dParameter + 4 * (x - y) + 10;
            } else {
                dParameter = dParameter + 4 * x + 6;
            }
            putPixel(image, xC, yC, x, y, color);
        }

    }

    // https://web.engr.oregonstate.edu/~sllu/bcircle.pdf
    private static void drawCircleJohn(BufferedImage image, Circle cir) {
        int radius = cir.getRadius().intValue();
        int color = cir.getColor();
        if (COLOR_MIX) color = mixColor(color);

        int xC = cir.getX();
        int yC = cir.getY();

        int x = radius;
        int y = 0;
        int dx = 1 - 2 * radius;
        int dy = 1;
        int radiusError = 0;
        while (x >= y) {
            // John Kennedy's method goes from 0 degree to 45, but Bresenham's algorithm goes from 90 to 45, so I had to switch x, y in order to make circles filled with pixels
            putPixel(image, xC, yC, y, x, color);
            y++;
            radiusError += dy;
            dy += 2;
            if (2 * radiusError + dx > 0) {
                x--;
                radiusError += dx;
                dx += 2;
            }
        }
    }

    private static int mixColor(int color) {
        int A = (color >> 24) & 0xFF;
        int R = (color >> 16) & 0xFF;
        int G = (color >> 8) & 0xFF;
        int B = (color) & 0xFF;

        int R_mix = mixColorPart(R);
        int G_mix = mixColorPart(G);
        int B_mix = mixColorPart(B);

        return (A << 24) + (R_mix << 16) + (G_mix << 8) + B_mix;
    }

    private static int mixColorPart(int color) {
        Random rand = new Random();
        int plusOrMinus = rand.nextInt(2) == 0 ? 1 : -1;
        int colorMix = color + rand.nextInt(COLOR_MIX_RANGE) * plusOrMinus;
        if (colorMix > 255) colorMix = 255;
        if (colorMix < 0) colorMix = 0;
        return colorMix;
    }

    private static void putPixel(BufferedImage bufferedImage, int xC, int yC, int x, int y, int color) {

        int pos_y = yC + x;
        int tempH = x;
        while (pos_y <= yC + y) {
            sendPixelRequest(xC + x, yC + tempH, color, bufferedImage);
            sendPixelRequest(xC - x, yC + tempH, color, bufferedImage);
            sendPixelRequest(xC - x, yC - tempH, color, bufferedImage);
            sendPixelRequest(xC + x, yC - tempH, color, bufferedImage);

            sendPixelRequest(xC + tempH, yC + x, color, bufferedImage);
            sendPixelRequest(xC + tempH, yC - x, color, bufferedImage);
            sendPixelRequest(xC - tempH, yC - x, color, bufferedImage);
            sendPixelRequest(xC - tempH, yC + x, color, bufferedImage);

            pos_y++;
            tempH++;
        }

    }

    private static void sendPixelRequest(int w1, int w2, int color, BufferedImage image) {
        try {
            image.setRGB(w1, w2, color);
        } catch (Exception ignored) {
        }
    }
}
