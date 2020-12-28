# iFPS & pLiRK
 iFPS (Inference of Functional Phosphorylation Sites) to predict whether a given phosphosite likely exerts a biological impact.  
 pLiRK is a model-based method for the prediction of lifespan-related kinases.

## The description of each source code
### main.java
Model training of iFPS.
### evolution.java
Calculate the RCS of each p-site.
### CrossTalk.java
Reveal the acetylation sites closed to each p-site based on the prediction of GPS-PAIL 2.0 (http://pail.biocuckoo.org/).
### PPIdomain.java
Calculate the number of protein domains and motifs that interate with each protein.
### SecondStructure.java
Get the second structural information of proteins from online service of NetSurfP v1.1 (http://www.cbs.dtu.dk/services/NetSurfP/).
### UpKA.java
Calculate the number of upstream kinase families of each protein based on iGPS 1.0 (http://igps.biocuckoo.org/).
### pLiRK.py
Predict the potential lifespan-related kinases based on the prediction of iGPS 1.0 (http://igps.biocuckoo.org/).
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
All codes can run directly on a "normal" computer with JAVA 1.8.0 and Python 3.7.9 installed, no extra installation is required

## Instruction
### iFPS
For users who want to run iFPS in own computer, files contian the information of different features should be first generated as the form demonstrated in demo/.  
"ELM_HUMAN.fa" contains human p-sites in ELM format collected in dbPAF (http://dbpaf.biocuckoo.org/), and "evolution.java" will use p-sites of 6 eukarytes to calculate Residue Conservation Score (RCS) of p-sites identified in this work.  
"test_all.txt" contains number of kinase families of each p-site, and "UpKA.java" will be used to extract the infromation of kinase families.  
"domain_domain.dat" and "domain_motif.dat" were downloaded from 3DID database (https://3did.irbbarcelona.org/), which contain the known domain-domain and domain-motif interactions, and "PPIdomain.java" will be used to extract the information of protein-protein interactions (PPI) domain.  
"seq_all.sulfation_new.fa" includes acetylation sites of proteins predicted by GPS-PAIL 2.0 (http://pail.biocuckoo.org/), and "CrossTalk.java" will be used to extract the information of crosstalk.  
"result_all.txt" contains the second structural infromation of proteins by NetSurfP v1.1 (http://www.cbs.dtu.dk/services/NetSurfP/), and "SecondStructure.java" will be used to extract the information of relative surface accessibility (RSA) and secondary structure.  
At last, once you have all above files ready, you can open the command prompt in your computer with JAVA 1.8.0 installed, and open the directory
