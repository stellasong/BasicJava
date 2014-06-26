
library("forecast");

# trainingDataPath injected from Java
data <- read.table(trainingDataPath, header=TRUE);
#data <- read.table(file.choose(), header=TRUE);

#data$ID <- as.factor(data$ID);
group <- split(data, data$ID);

#rDataPath <- "rdata.txt";
if (file.exists(rDataPath)) file.remove(rDataPath);

for(i in 1:length(group)){
	
  eachItem = group[[i]];
  timeSeries <- ts(eachItem$Sold, frequency = 12)
  #predictedValue <- forecast(tslm(timeSeries ~ trend + season), h=1);
  predictedValue <- forecast(ets(timeSeries), 1);
  #write.table(predictedValue, rDataPath, append=TRUE, row.names = FALSE, col.names = FALSE);
  #this only writes the ID and the upper 80 to the file
  write.table(cbind(group[[1]]$ID[1], predictedValue$upper[,1]), rDataPath, append=TRUE, row.names = FALSE, col.names = FALSE);
}


#preds <- sapply(group, function(data) forecast(tslm(ts(data$Sold, frequency=12), 1)));
# the model for this step is automatically chosen
#timeSeries <- ts(data$Used, frequency = 12)

# when the data has up-down trend /\_/\, can use ets (Exponential smoothing state space model)
#predictedValue <- forecast(ets(timeSeries), 1)

# to predict linear models, especially those involving trend and seasonality components.
#predictedValue <- forecast(tslm(timeSeries ~ trend + season), h=1)

# ARIMA (integrated autoregressive moving average model) for non-stationary time-series
#predictedValue <- forecast(arima(timeSeries), h=1)

#plot(predictedValue)
#write.table(cpredictedValue, "rdata.txt", sep=";")
