package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


public class FolderReader {

    public String videoFramesDirectory;
    public String[] extensionsArray;
    public ArrayList<BufferedImage> ImageArray = new ArrayList<>();
    public ArrayList<String> imageNameArray = new ArrayList<>();
    public File inputImageDirectory;

    public FolderReader(String directory, String[] extensions){
        videoFramesDirectory = directory;
        extensionsArray= extensions;
        inputImageDirectory = new File(videoFramesDirectory);
    }

    //EFFECTS: Stores images in folder as buffered images in array
    public void storeImageNamesInArray() {
        if (inputImageDirectory.isDirectory()) {
            for (final File f : inputImageDirectory.listFiles(IMAGE_FILTER)) {

                imageNameArray.add(f.getName());
            }
        }
    }

    // Clears contents of folder
    public void clearWorkFolder() {
        for(File file: inputImageDirectory.listFiles())
            if (!file.isDirectory())
                file.delete();
    }


    //EFFECTS: Stores images in folder as buffered images in array
    public void StoreImagesInArray() {
        if (inputImageDirectory.isDirectory()) {
            for (final File f : inputImageDirectory.listFiles(IMAGE_FILTER)) {

                imageNameArray.add(f.getName());
//                BufferedImage img = null;
//                try {
//                    img = ImageIO.read(f);
//                    ImageArray.add(img);
//
//                } catch (final IOException e) {
//                    //
//                }
            }
        }
    }

    //EFFECTS: Filters images in folder based on acceptable extensions
    public FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : extensionsArray) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };


}
