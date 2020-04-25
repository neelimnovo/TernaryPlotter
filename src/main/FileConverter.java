package main;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class FileConverter {

    public static int plotVideoHeight = 0;
    public static int plotVideoWidth = 0;
    public static double plotVideoFrameRate= 0.0;

    public static void videoToImages(String videoPath, String outputPath, String imgType, int frameSkipNumber) throws IOException
    {
        //initialise objects required for conversion
        Java2DFrameConverter converter = new Java2DFrameConverter();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoPath);
        frameGrabber.start();

        //setup values required for output video
        System.out.println(frameGrabber.getMetadata());
        plotVideoWidth = frameGrabber.getImageWidth();
        plotVideoHeight = frameGrabber.getImageHeight();
        plotVideoFrameRate = frameGrabber.getFrameRate();

        Frame frame;
        double frameRate=frameGrabber.getFrameRate();
        int imgNum=0;
        System.out.println("Video has "+frameGrabber.getLengthInFrames()+" frames and has frame rate of "+frameRate);

        try {
            for(int i=1; i<=frameGrabber.getLengthInFrames(); i++){
                imgNum++;
                frameGrabber.setFrameNumber(i);
                frame = frameGrabber.grab();
                BufferedImage  bi = converter.convert(frame);
                String path = outputPath+File.separator+imgNum+".jpg";
                ImageIO.write(bi,imgType, new File(path));
                i+=frameSkipNumber;
            }
            frameGrabber.stop();
        } catch (Exception e) {
            System.out.println("Error in video to image frame conversion");
            e.printStackTrace();
        }
    }


    public static void convertJPGtoMovie(ArrayList<String> links, String vidPath)
    {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath,640,720);
        try {
            recorder.setFrameRate(1);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
            recorder.setVideoBitrate(9000);
            recorder.setFormat("mp4");
            recorder.setVideoQuality(0); // maximum quality
            recorder.start();
            for (int i=0;i<links.size();i++)
            {
                recorder.record(grabberConverter.convert(cvLoadImage(links.get(i))));
            }
            recorder.stop();
        }
        catch (org.bytedeco.javacv.FrameRecorder.Exception e){
            e.printStackTrace();
        }
    }


}
