package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;



public class main {

    private static Scanner scanner;
    private static String INPUT_FILE_NAME;

    public static final String[] SUPPORTED_EXTENSIONS = new String[]{"jpg", "png"};
    public static String VIDEO_FRAMES_DIRECTORY = "C:\\Users\\User\\IdeaProjects\\TernaryPlotter\\input\\sequenceImages";
    private static final String FFMPEGCommand1 = "";
    private static final String FFMPEGCommand2 = "";
    private static final String FFMPEGCommand3 = "";
    private static final String FFMPEGCommand4 = "ffmpeg -r 60 -f image2 -s 1080x1080 -i output\\sequencePlot\\video%03d.png -vcodec libx264 -crf 25 -loglevel quiet -pix_fmt yuv420p output.mp4";

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
            }else if (firstChoice.equals("exit")) {
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
            p = Runtime.getRuntime().exec(FFMPEGCommand4);

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

        try {
            Process child = Runtime.getRuntime().exec("Rscript SinglePlot.R " + INPUT_FILE_NAME);
            int code = child.waitFor();
            switch (code) {
                case 0:
                    //normal termination, everything is fine
                    break;
                case 1:
                    //Read the error stream then
                    throw new RuntimeException();

            }

        } catch (Exception e) {
            System.out.println("Error in Rscript");
        }
    }


    private static void plotMultipleImages() {
        folderReader = new FolderReader(VIDEO_FRAMES_DIRECTORY, new String[]{"jpg", "png"});
        folderReader.StoreImageNamesInArray();

        long startTime = System.currentTimeMillis();
        multiThreadPlot();
        long endTime = System.currentTimeMillis();

        System.out.println("Plot renders complete");
        System.out.println("Took " + (endTime-startTime)/1000 + "s to plot");


        System.out.println("Done! \n \n");

    }




    private static void plotVideo() {
        System.out.println("Enter name of video file");
        INPUT_FILE_NAME = scanner.nextLine();

        // Convert video to image frames using ffmpeg
        try {
            System.out.println("this runs");
            Process videotoimagep = Runtime.getRuntime().exec("ffmpeg -i input\\" + INPUT_FILE_NAME + " -s 960x540 input\\sequenceImages\\video%04d.png -loglevel quiet");
            System.out.println("this runs");
            int exitValue1 = videotoimagep.waitFor();
//            if(exitValue1 == 0){
//                System.out.println("Video converted to image frames");
//            } else {
//                printRuntimeError(videotoimagep);
//            }

            //TODO implement and use downscaling
            //ImageDownscaler.DownscaleAndCreateImage(inputImagePath);

            folderReader = new FolderReader(VIDEO_FRAMES_DIRECTORY, new String[]{"jpg", "png"});
            folderReader.StoreImageNamesInArray();

            multiThreadPlot();
            System.out.println("Plotting complete");

            folderReader.clearWorkFolder();


            try {
                Process image2videoProcess = Runtime.getRuntime().exec(FFMPEGCommand4);
                int exitValue2 = image2videoProcess.waitFor();
                if(exitValue2 == 0) {
                    System.out.println("Plot image frames converted to video");
                } else {
                    printRuntimeError(image2videoProcess);
                }

            } catch (Exception e) {
                System.out.println("Error in converting video to image frames");
            }
            System.out.println("Done!");


        } catch (Exception e) {
            System.out.println("Error in converting video to image frames");
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

    private static class MultiThreader implements Runnable{
        public static int loopSize;
        public int loopStart;
        public int loopEnd;

        @Override
        public void run() {
            for(int i = loopStart; i < loopEnd; i = i + 4) {

                String imagePath = "input\\sequenceImages\\" + folderReader.imageNameArray.get(i);
                String command = "Rscript GroupPlot.R" + " " + imagePath + " " + folderReader.imageNameArray.get(i);
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


    //Multiple Thread implementation for 4 threads starts here
    private static void multiThreadPlot() {
        MultiThreader.loopSize = folderReader.imageNameArray.size();

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

}
