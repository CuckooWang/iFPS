# iFPS
 iFPS (Inference of Functional Phosphorylation Sites) to predict whether a given phosphosite likely exerts a biological impact.

## The description of each source code
### main.java
Model training of iFPS.
### evolution.java
Calculate the RCS of each p-site.
### CrossTalk.java
Reveal the acetylation sites closed to each p-site based on the prediction of GPS-PAIL 2.0 (http://pail.biocuckoo.org/).
### PPIdomain.java
Calculate the number of protein domains and motifs that interate with each protein.
### SencondStructure.java
Get the sencond structural information of proteins from online service of NetSurfP v1.1 (http://www.cbs.dtu.dk/services/NetSurfP/).
### UpKA.java
Calculate the number of upstream kinase families of each protein based on iGPS 1.0 (http://igps.biocuckoo.org/).
### demo
A small dataset to demo above codes, an example of the output of training is also provided.

## Software Requirements
### OS Requirements
Above codes have been tested on the following systems:
Windows: Windows 7, Windos 10
Linux: CentOS linux 7.8.2003
GPS-PAIL 2.0 and iGPS 1.0 for windows platform were downloaded and adopted before iFPS 
### Hardware Requirements
All codes and softwares could run on a "normal" desktop computer, no non-standard hardware is needed

## Installation guide
All codes can run directly on a "normal" computer with JAVA installed, no extra installation is required

## Additional information
Expected run time is depended on the number of phosphosites used for prediction, it will take about 5 minutes for 2,000 sites.

## Contact
Dr. Yu Xue: xueyu@hust.edu.cn  
Dr. Mengqiu Dong: dongmengqiu@nibs.ac.cn  
Wenjun Li: liwenjun@nibs.ac.cn  
Chenwei Wang: wangchenwei@hust.edu.cn  
