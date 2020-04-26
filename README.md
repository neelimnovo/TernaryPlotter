# TernaryPlotter

This is a Java CLI application that creates ternary plots from images or video using R scripts.

Requirements - FFMPEG Codec, Rtools, Java

The current functionality includes
1) Single images - Place an image in the /input folder and provide its full name when prompted
Example:
[SingleImageOutput](https://github.com/neelimnovo/TernaryPlotter/blob/master/examples/SingleImageOutput.jpg)

2) Multiple images- Place all images to plot in the /input/multipleImages folder, and run.
Pretty much the singlePlot script, run on multiples images with concurrent approach.

3) Combined images - Place all images to plot in the/input/combinedImages folder
Example:
[CombinedImageOutput](https://github.com/neelimnovo/TernaryPlotter/blob/master/examples/CombinedOutput.png)

4) Video - Place the video file in the /input file and provide its full name when prompted (Requires the FFMPEG codec to be installed)
Example: [VideoOutput](https://github.com/neelimnovo/TernaryPlotter/blob/master/examples/VideoOutput.gif)

Exit using the 'exit' command in the first prompt menu.
