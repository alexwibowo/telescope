package com.alexwibowo.telescope;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor implements Runnable {

    private final ImageSource imageSource;

    public ImageProcessor(ImageSource imageSource) {
        this.imageSource = imageSource;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedImage image = imageSource.getImage();
                ImageIO.write(image, "png", new File("C:/NotBackedUp/Recording/" + System.currentTimeMillis() + ".png"));
            } catch (InterruptedException|IOException e) {
                e.printStackTrace();
            }
        }
    }
}
