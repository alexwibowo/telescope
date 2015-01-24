package com.alexwibowo.telescope;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Telescope {

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final ImageCapturer imageCapturer = new ImageCapturer();
        final ImageProcessor imageProcessor = new ImageProcessor(imageCapturer);
        executorService.execute(imageCapturer);
        executorService.execute(imageProcessor);
        while (true) {
            Thread.sleep(1000);
        }
    }
}
