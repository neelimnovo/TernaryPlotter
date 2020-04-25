package tests;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static main.FileConverter.videoToImages;
import static org.junit.jupiter.api.Assertions.fail;

public class FileConverterTest {

    @Test
    public void testVideoToImage(){
        try {
            videoToImages("TL46.mp4", "output\\sequencePlot\\","png", 0);
        } catch (IOException e) {
            fail("Should not throw exception");
        }

    }

}
