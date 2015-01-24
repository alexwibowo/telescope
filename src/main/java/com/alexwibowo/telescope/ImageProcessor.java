package com.alexwibowo.telescope;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class ImageProcessor implements Runnable {

    private final ImageSource imageSource;

    private AtomicReference<BufferedImage> previousImage = new AtomicReference<>();

    public ImageProcessor(ImageSource imageSource) {
        this.imageSource = imageSource;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedImage newImage = imageSource.getImage();
                if (previousImage.get() == null) {
                    System.out.println("Writing image for the first time.");
                    ImageIO.write(newImage, "png", new File("C:/NotBackedUp/Recording/" + System.currentTimeMillis() + ".png"));
                }else {
                    final double percentageDifference = ImgDifferent.getPercentageDifference(newImage, previousImage.get());
                    if (percentageDifference > 10) {
                        System.out.println("Percentage difference is : " + percentageDifference);
                        ImageIO.write(newImage, "png", new File("C:/NotBackedUp/Recording/" + System.currentTimeMillis() + ".png"));
                    }
                }
                previousImage.set(newImage);
            } catch (InterruptedException|IOException e) {
                e.printStackTrace();
            }
        }
    }
}
