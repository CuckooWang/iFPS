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
Get the sencond structural information of proteins from NetSurfP v1.1 (http://www.cbs.dtu.dk/services/NetSurfP/).
### UpKA.java
Calculate the number of upstream kinase families of each protein based on iGPS 1.0 (http://igps.biocuckoo.org/).

## Contact
Dr. Yu Xue: xueyu@hust.edu.cn  
Dr. Mengqiu Dong: dongmengqiu@nibs.ac.cn  
Wenjun Li: liwenjun@nibs.ac.cn  
Chenwei Wang: wangchenwei@hust.edu.cn  
