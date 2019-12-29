package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageDownscaler {

    private static final int IMG_WIDTH = 500;
    private static final int IMG_HEIGHT = 500;
    private static final String DOWNSCALE_SAVE_PATH = "C:\\Users\\User\\IdeaProjects\\TernaryPlotter\\output";

    public static void DownscaleAndCreateImage(String path) {

        try{

            BufferedImage originalImage = ImageIO.read(new File(path));
            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            BufferedImage resizeImageJpg = resizeImage(originalImage, type);
            ImageIO.write(resizeImageJpg, "jpg", new File(DOWNSCALE_SAVE_PATH));

            BufferedImage resizeImagePng = resizeImage(originalImage, type);
            ImageIO.write(resizeImagePng, "png", new File(DOWNSCALE_SAVE_PATH));


        }catch(IOException e){
            System.out.println("Downscale fail");
        }

    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int type){
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }



}
