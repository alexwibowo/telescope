package com.alexwibowo.telescope;

import java.awt.image.BufferedImage;

public interface ImageSource {
    BufferedImage getImage() throws InterruptedException;
}
