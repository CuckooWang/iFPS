package github;

import java.awt.font.NumericShaper.Range;
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
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.supervised.instance.StratifiedRemoveFolds;
import weka.filters.Filter;

public class main {
	public static void main(String[] srgs) throws Exception{
		//有6个id没办法跑muscleout在evolution方法中去掉
		
		File f1 = new File("EML_pos.txt");
		File f2 = new File("EML_neg.txt");
		String mso = "muscleout\\";
		if(f1.exists() && f2.exists()){
			//PhC
			Map<String,Double> evposp = evolution.getRCS(f1, mso);
			Map<String,Double> evposn = evolution.getRCS(f2, mso);
			
			//IDM
			Map<String,List<Double>> ppidp = PPIdomain.getPPIDomianNum(f1);
			Map<String,List<Double>> ppidn = PPIdomain.getPPIDomianNum(f2);
			
			//RSA and SS
			Map<String,List<Double>> ssp = SencondStru.getChance(f1);
			Map<String,List<Double>> ssn = SencondStru.getChance(f2);
			
			//ASCs
			Map<String,Double> ctp = CrossTalk.ifHas(f1, 15);
			Map<String,Double> ctn = CrossTalk.ifHas(f2, 15);
			
			//UKFs
			Map<String,Double> kap = UpKA.getKANum(f1);
			Map<String,Double> kan = UpKA.getKANum(f2);
			
			//the num of features used for training
			int fearnum = 8;
			
			String[] keys = evposn.keySet().toArray(new String[0]);
			int posnum = evposp.keySet().size();
			//the ratio of negative data and positive data
			int fold = 5;
			BufferedWriter bw0 = new BufferedWriter(new FileWriter
					("training_out" + fold + "倍" + "_" + fearnum + ".txt"));
			
			List<String> negsforpre = new ArrayList<String>();
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
				
				bw0.write("@RIDGE=" + RIDGE + "\t");
				bw0.write("@AUC=" + auc1 + "\t");
				bw0.newLine();
				
				StratifiedRemoveFolds kFold = new StratifiedRemoveFolds();
				kFold.setInputFormat(data);
				
				Logistic bestlogistic = new Logistic();
				double bestauc = 0.0;
				double totAUC = 0.0;
				
				BufferedWriter bw2 = new BufferedWriter(new FileWriter
						("roc" + i + ".txt"));
				for(int a=1;a<11;a++){
					
					kFold.setOptions(new String[]{
				    "-N", ""+10, "-F", ""+a, "-S", "1"});
					Instances test = Filter.useFilter(data, kFold);
					kFold.setOptions(new String[]{
				    "-N", ""+10, "-F", ""+a, "-S", "1", "-V"});
					Instances train = Filter.useFilter(data, kFold);
					System.out.println(train.size());
					System.out.println(test.size());
					Logistic logic2 = new Logistic();
					logic2.setRidge(RIDGE);
					
					logic2.buildClassifier(train);
					Evaluation eval = new Evaluation(train);
					
					for(int j=0;j<test.size();j++){
						Instance ins = test.get(j); 
						double[] scores = logic2.distributionForInstance(ins);
						double type = ins.classValue();
						bw2.write(scores[1] + "\t" + type);
						bw2.newLine();
						
					}
					eval.evaluateModel(logic2, test);
					double auc2 = eval.areaUnderROC(0);
					totAUC += auc2;
					if(auc2 > bestauc){
						bestauc = auc2;
						bestlogistic = logic2;
					}
					
				}
				bw2.flush();
				bw2.close();
				
				for(int y=1;y<bestlogistic.coefficients().length;y++){
					bw0.write(bestlogistic.coefficients()[y][0] + "\t");
				}
				bw0.write(bestlogistic.coefficients()[0][0] + "\t");
				bw0.newLine();
				
			}
			bw0.flush();
			bw0.close();
		}
	}
	
}
