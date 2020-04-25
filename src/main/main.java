package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class main {

    private static Scanner scanner;
    private static String INPUT_FILE_NAME;

    public static final String[] SUPPORTED_EXTENSIONS = new String[]{"jpg", "png"};
    public static String VIDEO_FRAMES_DIRECTORY = "input\\sequenceImages";
    public static String MULTIPLE_IMAGES_DIRECTORY = "input\\multipleImages";
    private static final String FFMPEGCommand1 = "";
    private static final String FFMPEGCommand2 = "";
    private static final String FFMPEGCommand3 = "";

    private static FolderReader folderReader;

    public static void main(String[] args) {
        while (true) {
            scanner = new Scanner(System.in);

            System.out.println("Plot from \n1)image \n2)multiple images \n3)video?");
            String firstChoice = scanner.nextLine();

            if (firstChoice.equals("1")) {
                plotSingleImage();
            } else if (firstChoice.equals("2")) {
                plotMultipleImages();
            } else if (firstChoice.equals("3")) {
                plotVideo();
            } else if (firstChoice.equals(("test"))) {
                testMethod();
            } else if (firstChoice.equals("exit")) {
                System.out.println("Goodbye!");
                System.exit(1);
            } else {
                System.out.println("Invalid input, try again!");
            }
        }
    }




    private static void testMethod() {
        System.out.println("Running Test Method");
//
//
//        try {
////            System.out.println("this runs");
////            Process p2 = Runtime.getRuntime().exec("ffmpeg -i input\\aurora.mp4 -s 960x540 input\\sequenceImages\\video%04d.png -loglevel quiet");
////            System.out.println("this runs");
////            int exitValue1 = p2.waitFor();
////            if(exitValue1 == 0){
////                System.out.println("Video converted to image frames");
////            } else {
////                printRuntimeError(videotoimagep);
////            }
//
//
//            folderReader = new FolderReader(VIDEO_FRAMES_DIRECTORY, new String[]{"jpg", "png"});
//            folderReader.StoreImageNamesInArray();
//            multiThreadPlot();
//            System.out.println("Plotting complete");
//            folderReader.clearWorkFolder();
//
//
//            try {
//                Process image2videoProcess = Runtime.getRuntime().exec(FFMPEGCommand4);
//                int exitValue2 = image2videoProcess.waitFor();
//                if(exitValue2 == 0) {
//                    System.out.println("Plot image frames converted to video");
//                } else {
//                    printRuntimeError(image2videoProcess);
//                }
//
//            } catch (Exception e) {
//                System.out.println("Error in converting video to image frames");
//            }
//            System.out.println("Done!");
//
//
//        } catch (Exception e) {
//            System.out.println("Error in converting video to image frames");
//        }


        Process p = null;
        try {
            p = Runtime.getRuntime().exec("");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // Read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Test method completed");
    }


    private static void plotSingleImage() {
        System.out.println("Enter name of image file");
        INPUT_FILE_NAME = scanner.nextLine();
        String runTimeCommand = "Rscript SinglePlot.R " + "input/" + INPUT_FILE_NAME + " " + INPUT_FILE_NAME;

        try {
            Process child = Runtime.getRuntime().exec(runTimeCommand);
            int code = child.waitFor();
            InputStream childError = child.getErrorStream();
            switch (code) {
                case 0:
                    //normal termination, everything is fine
                    System.out.println("Done! \n \n");
                    break;
                case 1:
                    // Read the error stream then
                    for (int i = 0; i < childError.available(); i++) {
                        System.out.println("" + childError.read());
                    }
                    throw new RuntimeException();
            }
        } catch (Exception e) {
            System.out.println("Error in Rscript SinglePlot");
        }
    }


    private static void plotMultipleImages() {
        folderReader = new FolderReader(MULTIPLE_IMAGES_DIRECTORY, new String[]{"jpg", "png"});
        folderReader.storeImageNamesInArray();

        long startTime = System.currentTimeMillis();
        // MultiThreader.directoryPath = MULTIPLE_IMAGES_DIRECTORY;
        multiThreadPlot(MULTIPLE_IMAGES_DIRECTORY);
        long endTime = System.currentTimeMillis();

        System.out.println("Plot renders complete");
        System.out.println("Took " + (endTime-startTime)/1000 + "s to plot");

        System.out.println("Done! \n \n");

    }




    private static void plotVideo() {
        System.out.println("Enter name of video file");
        INPUT_FILE_NAME = scanner.nextLine();

        try {

            //TODO Manually convert video to image frames and downscale
            //videoToImages(INPUT_FILE_NAME, "output\\sequencePlot\\","png", 0);
            //ImageDownscaler.DownscaleAndCreateImage(inputImagePath);

            // Convert video to image frames using ffmpeg
            String inputCommand = "ffmpeg -i input\\" + INPUT_FILE_NAME
                                    + " -s 960x540 input\\sequenceImages\\video%05d.jpg";
            Process videoToImageProcess = Runtime.getRuntime().exec(inputCommand);
            int exitValue1 = videoToImageProcess.waitFor();
            if(exitValue1 == 0) {
                System.out.println("Plot image frames converted to video");
            } else {
                printRuntimeError(videoToImageProcess);
            }

            folderReader = new FolderReader(VIDEO_FRAMES_DIRECTORY, new String[]{"jpg", "png"});
            folderReader.storeImageNamesInArray();
            multiThreadPlot(VIDEO_FRAMES_DIRECTORY);
            System.out.println("Plotting complete");

            folderReader.clearWorkFolder();

            try {
                String OUTPUT_FILE_NAME = "output\\" + INPUT_FILE_NAME.substring(0, INPUT_FILE_NAME.indexOf(".")) + ".mp4";
                String FFMPEGImageFramesToVideo = "ffmpeg -r 60 -f image2 -s 1080x1080 -i output\\sequencePlot\\video%05d.png -vcodec libx264 -crf 25 -loglevel quiet -pix_fmt yuv420p "
                        + OUTPUT_FILE_NAME;
                Process imageToVideoProcess = Runtime.getRuntime().exec(FFMPEGImageFramesToVideo);
                int exitValue2 = imageToVideoProcess.waitFor();
                if(exitValue2 == 0) {
                    System.out.println("Plot image frames converted to video");
                } else {
                    printRuntimeError(imageToVideoProcess);
                }

            } catch (Exception e) {
                System.out.println("Error in converting plot frames to video");
            }
            System.out.println("Done!");

        } catch (Exception e) {
            System.out.println("Error in converting input video to image frames");
        }



    }

    private static void printRuntimeError(Process video2imageProcess) {
        System.out.println("Failed to execute because:");
        try (final BufferedReader b = new BufferedReader(new InputStreamReader(video2imageProcess.getErrorStream()))) {
            String line;
            if ((line = b.readLine()) != null)
                System.out.println(line);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }


    //Multiple Thread implementation for 4 threads starts here
    private static void multiThreadPlot(String directory) {
        MultiThreader.loopSize = folderReader.imageNameArray.size();
        MultiThreader.directoryPath = directory;

        MultiThreader m1 = new MultiThreader();
        MultiThreader m2 = new MultiThreader();
        MultiThreader m3 = new MultiThreader();
        MultiThreader m4 = new MultiThreader();

        m1.loopStart = 0;
        m1.loopEnd = MultiThreader.loopSize;
        m2.loopStart = 1;
        m2.loopEnd = MultiThreader.loopSize;
        m3.loopStart = 2;
        m3.loopEnd = MultiThreader.loopSize;
        m4.loopStart = 3;
        m4.loopEnd = MultiThreader.loopSize;

        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m2);
        Thread t3 = new Thread(m3);
        Thread t4 = new Thread(m4);

        // System.out.println("this runs");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            System.out.println("Problem with join");
        }
    }

    private static class MultiThreader implements Runnable{
        public static int loopSize;
        public static String directoryPath;
        public int loopStart;
        public int loopEnd;
        public String outputPath;

        @Override
        public void run() {

            if (directoryPath.equals("input\\multipleImages")) {
                outputPath = "output\\multiPlot";
            } else if (directoryPath.equals("input\\sequenceImages")) {
                outputPath = "output\\sequencePlot";
            }

            for(int i = loopStart; i < loopEnd; i = i + 4) {

                String imagePath = directoryPath + "\\" + folderReader.imageNameArray.get(i);
                String command = "Rscript GroupPlot.R" + " " + imagePath + " " + folderReader.imageNameArray.get(i)
                                    + " " + outputPath;
                Process p = null;
                try {
                    p = Runtime.getRuntime().exec(command);
                    p.waitFor();
                } catch (Exception e) {
                    System.out.println("One of the threads died");
                }
            }
        }
    }

}
