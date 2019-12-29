# load required libraries
library(png)
library(ggtern)
library(jpeg)


initialDirectory = getwd()

setwd("input/combinationImages")

fileList <- list.files(path = ".")

workimage <- 0
dataset <- NULL

for (i in fileList) {
if(grepl(".jpg", i)){
	workimage <- readJPEG(i)}
if(grepl(".png", i)){
	workimage <- readPNG(i)}
RedArray <- workimage[,,1]
GreenArray <- workimage[,,2]
BlueArray <- workimage[,,3]
dataset2 <- data.frame("Blue" = as.numeric(BlueArray),
                                  "Red" = as.numeric(RedArray),
                                  "Green" = as.numeric(GreenArray))
dataset2 <- subset(dataset2, ((Red + Blue + Green) != 0))
    if(is.null(dataset)){
            dataset <- dataset2
	} else {
            dataset <- rbind(dataset,dataset2)

            }
	}


# clear memory
rm(workimage,RedArray,GreenArray,BlueArray)

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


setwd(initialDirectory)

# save output file
ggsave(plot, filename="CombinedPlot.png" ,width=7, height=7, path = "output")






