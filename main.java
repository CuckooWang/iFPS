package github;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tools.GenerateARFF;
import tools.LOO;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class main {
	static Logistic trainModel(File inputFile, double ridge) throws Exception{
		ArffLoader loader = new ArffLoader(); 
		loader.setFile(inputFile);
		Instances insTrain = loader.getDataSet(); 
		insTrain.setClassIndex(insTrain.numAttributes()-1);
		Logistic logic = new Logistic();
		logic.setRidge(ridge);
		logic.buildClassifier(insTrain); 
		return logic;
	}
	
	public static void main(String[] srgs) throws Exception{
		File f1 = new File("EML_pos.txt");
		File f2 = new File("EML_neg.txt");
		String mso = "muscleout\\";
		if(f1.exists() && f2.exists()){
			Map<String,Double> evposp = evolution.getRCS(f1, mso);
			Map<String,Double> evposn = evolution.getRCS(f2, mso);

			BufferedWriter bwev = new BufferedWriter(new FileWriter("conservation.txt"));
			DataDistribution.writeEvo(evposp, evposn, bwev);
			
			Map<String,List<Double>> ppidp = PPIdomain.getPPIDomianNum(f1);
			Map<String,List<Double>> ppidn = PPIdomain.getPPIDomianNum(f2);
			
			BufferedWriter bwdo = new BufferedWriter(new FileWriter("PPIdomain.txt"));
			DataDistribution.writePPIdo(ppidp, ppidn, bwdo);
		
			Map<String,List<Double>> ssp = SencondStru.getChance(f1);
			Map<String,List<Double>> ssn = SencondStru.getChance(f2);
		
			BufferedWriter bwasa = new BufferedWriter(new FileWriter("ASA.txt"));
			BufferedWriter bwrsa = new BufferedWriter(new FileWriter("RSA.txt"));
			BufferedWriter bwss1 = new BufferedWriter(new FileWriter("SencondS1.txt"));
			BufferedWriter bwss2 = new BufferedWriter(new FileWriter("SencondS2.txt"));
			BufferedWriter bwss3 = new BufferedWriter(new FileWriter("SencondS3.txt"));
			DataDistribution.writess(ssp, ssn,bwrsa,bwasa,bwss1, bwss2, bwss3);

			Map<String,Double> ctp = CrossTalk.ifHas(f1, 15);
			Map<String,Double> ctn = CrossTalk.ifHas(f2, 15);
			BufferedWriter bwct = new BufferedWriter(new FileWriter("Acetylation.txt"));
			DataDistribution.writeCT(ctp, ctn, bwct);
			
			Map<String,Double> kap = UpKA.getKANum(f1);
			Map<String,Double> kan = UpKA.getKANum(f2);
			BufferedWriter bwka = new BufferedWriter(new FileWriter("UPKA.txt"));
			DataDistribution.writeUPKA(kap, kan, bwka);

			int fearnum = 8;
			String[] keys = evposn.keySet().toArray(new String[0]);
			int posnum = evposp.keySet().size();
			int fold = 5;
			BufferedWriter bw0 = new BufferedWriter(new FileWriter
					("training_out" + fold + "fold" + "_" + fearnum + ".txt"));
			double totAUC = 0.0;
			List<String> negsforpre = new ArrayList<String>();
			double AUC = 0.0;
			for(int i=1;i<11;i++){
				File allarff = new File("all" + "_" + i + ".arff");
				BufferedWriter bw1 = new BufferedWriter(new FileWriter(allarff));
				bw1.write("@RELATION\tscore" );
				bw1.newLine();
				bw1.newLine();
				for(int j=1;j<=fearnum;j++){
					bw1.write("@ATTRIBUTE\ts_" + j + "\t" + "REAL");
					bw1.newLine();
				}
				bw1.write("@ATTRIBUTE\tclass\t{1,0}");
				bw1.newLine();
				bw1.newLine();
				bw1.write("@DATA");
				bw1.newLine();
				Iterator<String> it1 = evposp.keySet().iterator();
				while(it1.hasNext()){
					String inf = it1.next();
					String[] sp1 = inf.split("\t");
					String id = sp1[0];
					String pos = sp1[1];
					bw1.write(evposp.get(inf).toString());
					List<Double> ppi = ppidp.get(inf);
					double donum = ppi.get(0);
					double pdonum = ppi.get(1);
					double pmtnum = ppi.get(2);
					double totppi = pdonum + pmtnum;
					bw1.write("\t" + totppi);
					List<Double> ss = ssp.get(inf);
					double rsa = ss.get(0);
					double asa = ss.get(1);
					double ap = ss.get(2);
					double bp = ss.get(3);
					double cp = ss.get(4);
					bw1.write("\t" + rsa);
					bw1.write("\t" + ap + "\t" + bp + "\t" + cp);
					bw1.write("\t" + ctp.get(inf).toString());
					bw1.write("\t" + kap.get(inf).toString());
					bw1.write("\t" + 1);
					bw1.newLine();
					
				}
				Random random = new Random();
				List<String> negkey = new ArrayList<String>();
				while(negkey.size() < (posnum*fold)){
					String keya = keys[random.nextInt(keys.length)];
					if(!negkey.contains(keya) ){
						negkey.add(keya);
					}
				}
				for(int j=0;j<negkey.size();j++){
					String inf = negkey.get(j);
					String[] sp1 = inf.split("\t");
					String id = sp1[0];
					String pos = sp1[1];
					bw1.write(evposn.get(inf).toString());
					List<Double> ppi = ppidn.get(inf);
					double donum = ppi.get(0);
					double pdonum = ppi.get(1);
					double pmtnum = ppi.get(2);
					double totppi = pdonum;
					bw1.write("\t" + totppi);
					List<Double> ss = ssn.get(inf);
					double rsa = ss.get(0);
					double asa = ss.get(1);
					double ap = ss.get(2);
					double bp = ss.get(3);
					double cp = ss.get(4);
					bw1.write("\t" + rsa);
					bw1.write("\t" + ap + "\t" + bp + "\t" + cp);
					bw1.write("\t" + ctn.get(inf).toString());
					bw1.write("\t" + kan.get(inf).toString());
					bw1.write("\t" + 0);
					bw1.newLine();
				}
				bw1.flush();
				bw1.close();

				Instances data = new Instances(new BufferedReader(new FileReader(allarff)));
				data.setClassIndex(data.numAttributes()-1);
				double RIDGE = 0.0;
				double auc1 = 0.0;
				for(double ridge = 1E-2; ridge<=1E2; ridge += Math.pow(10, Math.floor((Math.log10(ridge))))){
					System.out.println(i + ":" + ridge);
					Logistic logic1 = new Logistic();
					Evaluation eval = new Evaluation(data);
					logic1.setRidge(ridge);
					eval.crossValidateModel(logic1, data, 10, new Random(1));
					double temauc = eval.areaUnderROC(0);
					if(temauc > auc1){
						RIDGE = ridge;
						auc1 = temauc;
					}else{
						continue;
					}
				}
				
				Logistic logic2 = trainModel(allarff, RIDGE);
				Evaluation eval = new Evaluation(data);
				eval.evaluateModel(logic2, data);
				double auc2 = eval.areaUnderROC(0);
				if(auc2 > AUC){
					AUC = auc2;
					negsforpre = negkey;
				}
				totAUC += auc2;
				
				bw0.write("@RIDGE=" + RIDGE + "\t");
				bw0.write("@AUC1=" + auc1 + "\t");
				bw0.write("@AUC2=" + auc2);
				bw0.newLine();
				for(int y=1;y<logic2.coefficients().length;y++){
					bw0.write(logic2.coefficients()[y][0] + "\t");
				}
				bw0.write(logic2.coefficients()[0][0] + "\t");
				bw0.newLine();
				
				
				
			}
			double aveAUC = totAUC/10.0;
			bw0.write("@Average(AUC)=" + aveAUC);
			bw0.flush();
			bw0.close();

			List<String> poss = new ArrayList<String>();
			List<String> negs = new ArrayList<String>();
			Map<String,Double> sscore = new HashMap<String,Double>();
			
			List<Double> coe = new ArrayList<Double>();
			BufferedReader br1 = new BufferedReader(new FileReader
					("training_out" + fold + "fold" + "_" + fearnum + ".txt"));
			String s1;
			String lasts1 = null;
			double auc = 0;
			while((s1 = br1.readLine()) != null){
				if(s1.startsWith("@RIDGE")){
					String[] sp1 = s1.split("\t");
					String[] sp2 = sp1[2].split("=");
					double temauc = Double.parseDouble(sp2[1]);
					if(temauc <= auc){
						br1.readLine();
					}else{
						auc = temauc;
					}
				}else if(s1.startsWith("@Average(AUC)")){
					continue;
				}else{
					lasts1 = s1;
				}
			}
			System.out.println(lasts1);
			String[] sp0 = lasts1.split("\t");
			for(int i=0;i<sp0.length;i++){
				double a = Double.parseDouble(sp0[i]);
				coe.add(a);
			}
			Iterator<String> it1 = evposp.keySet().iterator();
			while(it1.hasNext()){
				String inf = it1.next();
				poss.add(inf);
				String[] sp1 = inf.split("\t");
				String id = sp1[0];
				String pos = sp1[1];
				List<Double> feature = new ArrayList<Double>();
				feature.add(evposp.get(inf));
				List<Double> ppi = ppidp.get(inf);
				double donum = ppi.get(0);
				double pdonum = ppi.get(1);
				double pmtnum = ppi.get(2);
				double totppi = pdonum + pmtnum;
				feature.add(totppi);
				List<Double> ss = ssp.get(inf);
				double rsa = ss.get(0);
				double asa = ss.get(1);
				double ap = ss.get(2);
				double bp = ss.get(3);
				double cp = ss.get(4);
				feature.add(rsa);
				feature.add(ap);
				feature.add(bp);
				feature.add(cp);
				feature.add(ctp.get(inf));
				feature.add(kap.get(inf));
				double score = 0.0;
				for(int i=0;i<fearnum;i++){
					score += feature.get(i)*coe.get(i);
				}
				sscore.put(inf, score);
			}
			Iterator<String> it2 = evposn.keySet().iterator();
			while(it2.hasNext()){
				String inf = it2.next();
				negs.add(inf);
				String[] sp1 = inf.split("\t");
				String id = sp1[0];
				String pos = sp1[1];
				List<Double> feature = new ArrayList<Double>();
				feature.add(evposn.get(inf));
				List<Double> ppi = ppidn.get(inf);
				double donum = ppi.get(0);
				double pdonum = ppi.get(1);
				double pmtnum = ppi.get(2);
				double totppi = pdonum + pmtnum;
				feature.add(totppi);
				List<Double> ss = ssn.get(inf);
				double rsa = ss.get(0);
				double asa = ss.get(1);
				double ap = ss.get(2);
				double bp = ss.get(3);
				double cp = ss.get(4);
				feature.add(rsa);
				feature.add(ap);
				feature.add(bp);
				feature.add(cp);
				feature.add(ctn.get(inf));
				feature.add(kan.get(inf));
				double score = 0.0;
				for(int i=0;i<fearnum;i++){
					score += feature.get(i)*coe.get(i);
				}
				sscore.put(inf, score);
			}
			
			double SP = 0.8;
			Object[] ob = LOO.getSN(poss, negs, sscore, SP);
			double cutoff = (double)ob[0];
			double sn = (double)ob[1];
			double sp = (double)ob[2];
			int TP = (int)ob[3];
			int TN = (int)ob[4];
			int FP = (int)ob[5];
			int FN = (int)ob[6];
			bw0 = new BufferedWriter(new FileWriter
					("training_out" + fold + "fold" + "_" + fearnum + ".txt",true));
			bw0.write("\t@cutoff=" + cutoff);
			bw0.write("\t@sn=" + sn);
			bw0.write("\t@sp=" + sp);
			bw0.write("\t@TP=" + TP);
			bw0.write("\t@FN=" + FN);
			bw0.write("\t@TN=" + TN);
			bw0.write("\t@FP=" + FP);
			bw0.newLine();
			bw0.flush();
			bw0.close();
			
			BufferedWriter bw1 = new BufferedWriter(new FileWriter
					("traning_dataset.txt"));
			writerROC(bw1,poss,negsforpre,sscore);
		}
	}
	
	public static void writerROC(BufferedWriter bw1,List<String> poss,List<String> negs,Map<String,Double> sscore) throws IOException{
		List<String> allsites = new ArrayList<String>();
		for(int i=0;i<poss.size();i++){
			allsites.add(poss.get(i));
		}
		for(int i=0;i<negs.size();i++){
			allsites.add(negs.get(i));
		}
		
		
		for(int i=0;i<allsites.size();i++){								
			if(poss.contains(allsites.get(i))){
				bw1.write(sscore.get(allsites.get(i)) + "\t" + 1 + "\t" + allsites.get(i));
				bw1.newLine();
			}else if(negs.contains(allsites.get(i))){
				bw1.write(sscore.get(allsites.get(i)) + "\t" + 0 + "\t" + allsites.get(i));
				bw1.newLine();
			}else{
				System.out.println("abnormal：" + allsites.get(i));
			}
		}
		bw1.flush();
		bw1.close();
	}
}
