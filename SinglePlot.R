# load required libraries

library(png)
library(ggtern)
library(jpeg)


# command line input path
args <- commandArgs(trailingOnly = TRUE)
pathString <- args[1]
outputFileName <- args[2]

# print(outputFileName)

isJPG <- grepl(".jpg", pathString)
isPNG <- grepl(".png", pathString)

print(pathString)

# load image file
if(isJPG) {
  workimage <- readJPEG(pathString)
  outputFileName <- gsub(".jpg", "plot.png", outputFileName)
} else if(isPNG) {
  workimage <- readPNG(pathString)
  outputFileName <- gsub(".png", "plot.png", outputFileName)
}

print(outputFileName)


# parse pixel information from image file
RedArray <- workimage[,,1]
GreenArray <- workimage[,,2]
BlueArray <- workimage[,,3]


# create dataframe from pixel information
dataset <- data.frame("Blue" = as.numeric(BlueArray),"Red" = as.numeric(RedArray), "Green" = as.numeric(GreenArray))


# Filter more pixels based on HSV values
# Create HSV values for respective RGB
HSV <- rgb2hsv(dataset$Red, dataset$Green, dataset$Blue, maxColorValue = 1)


# Keep all H values
dataset$Hue <- HSV[1,]
dataset$Saturation <- HSV[2,]
dataset$Value <- HSV[3,]


# Keep S > 10
dataset <- subset(dataset, Saturation > 0.1)

# Keep V > 40
dataset <- subset(dataset, Value > 0.5)


# Create Color Column
dataset$Color <- rgb(dataset$Red, dataset$Green, dataset$Blue)


# axes separator lines for the ternary plot
lines <- data.frame(x = c(0.5, 0, 0.5), 
                        y = c(0.5, 0.5, 0), 
                        z = c(0, 0.5, 0.5), 
                        xend = c(1, 1, 1)/3, 
                        yend = c(1, 1, 1)/3, 
                        zend = c(1, 1, 1)/3)



# ternaryplot
plot <- ggtern(dataset, aes(Blue,Red,Green)) + geom_point(alpha = 0.15, shape = 16, color = dataset$Color) + 
    geom_segment(data = lines, aes(x, y, z, xend = xend, yend = yend, zend = zend),
                color = 'black', size = 1) + theme_rgbw() + theme_hidegrid()


# save output file
ggsave(plot, filename= outputFileName, width=7, height=7, dpi = 250, path ="output")

    


    

