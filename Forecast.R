library("forecast")

setwd("C:/Users/leo/Desktop/stat/DS training/Reproducible Research")
dataSet <- read.table("./repdata_data_StormData.csv.bz2", header = TRUE, sep = ",")

timeSeries <- ts(dataSet$Sold, frequency = 12)

predictedValue <- forecast(ets(timeSeries), 1)

plot(predictedValue)
