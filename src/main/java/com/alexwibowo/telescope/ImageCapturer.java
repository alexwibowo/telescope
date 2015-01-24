package com.alexwibowo.telescope;

import org.bytedeco.javacpp.opencv_core.IplImage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class ImageCapturer implements Runnable, ImageSource {

    private final LinkedBlockingQueue<IplImage> images = new LinkedBlockingQueue();

    final int INTERVAL=1000;///you may use interval
//    CanvasFrame canvas = new CanvasFrame("Web Cam");
    public ImageCapturer() {
//        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void run() {
      /*  FrameGrabber grabber = new VideoInputFrameGrabber(0);
        int i=0;
        try {
            grabber.start();
            while (true) {
                IplImage img = grabber.grab();
                if (img != null) {
                    cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
                    cvSaveImage((i++)+"-capture.jpg", img);
                    images.offer(img);
//                    canvas.showImage(img);
                }
                Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
        }*/
    }

    @Override
    public BufferedImage getImage() throws InterruptedException {
        try {
            final BufferedImage read = ImageIO.read(new File("C:\\dev\\insight\\telescope\\src\\main\\resources\\20141120_212117.jpg"));
            return compressImage(getScaledImage(read, 640, 480), 0.8f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage compressImage(BufferedImage src, float compressionFactor) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found");
        }

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream);
        ImageWriter writer = (ImageWriter) writers.next();
        writer.setOutput(ios);
        ImageWriteParam param = writer.getDefaultWriteParam();
        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(compressionFactor);
        // appends a complete image stream containing a single image and
        //associated stream and image metadata and thumbnails to the output
        writer.write(null, new IIOImage(src, null, null), param);

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        final BufferedImage read = ImageIO.read(byteArrayInputStream);
        return read;
    }

    private BufferedImage getScaledImage(BufferedImage src, int w, int h){
        int finalw = w;
        int finalh = h;
        double factor = 1.0d;
        if(src.getWidth() > src.getHeight()){
            factor = ((double)src.getHeight()/(double)src.getWidth());
            finalh = (int)(finalw * factor);
        }else{
            factor = ((double)src.getWidth()/(double)src.getHeight());
            finalw = (int)(finalh * factor);
        }


        BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, finalw, finalh, null);
        g2.dispose();
        return resizedImg;
    }
}
