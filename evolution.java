import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.LifeTree;

public class evolution {
	public static Map<String,Double> getRCS(File sites,String msout) throws IOException{
		Set<String> nomsout = new HashSet<String>();
		File f1 = new File("D:\\elegans\\2017年10月\\dbPAF\\eml\\EML_HUMAN.fa");
		File f2 = new File("D:\\elegans\\2017年10月\\dbPAF\\eml\\EML_MOUSE.fa");
		File f3 = new File("D:\\elegans\\2017年10月\\dbPAF\\eml\\EML_RAT.fa");
		File f4 = new File("D:\\elegans\\2017年10月\\dbPAF\\eml\\EML_DROME.fa");
		File f5 = new File("D:\\elegans\\2017年10月\\dbPAF\\eml\\EML_SCHPO.fa");
		File f6 = new File("D:\\elegans\\2017年10月\\dbPAF\\eml\\EML_YEAST.fa");
		
		Map<String,Set<Integer>> hmall = new HashMap<String,Set<Integer>>();
		readEML(f1,hmall,"human");
		readEML(f2,hmall,"mouse");
		readEML(f3,hmall,"rat");
		readEML(f4,hmall,"fly");
		readEML(f5,hmall,"pombe");
		readEML(f6,hmall,"yeast");
		
		Map<String,Double> hm1 = new HashMap<String,Double>();
		BufferedReader br1 = new BufferedReader(new FileReader(sites));
		String s1;
		while((s1 = br1.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			if(nomsout.contains(id)){
				continue;
			}
			String oldseq = sp1[1];
			int pos = Integer.parseInt(sp1[2]);
			String aa = sp1[3];
			String temaa = oldseq.substring(pos-1, pos);
			if(!temaa.equals(aa)){
				System.out.println(id + "\t" + pos + ":位置标记错了");
			}
			BufferedReader br2 = new BufferedReader(new FileReader(msout + id + ".afa"));
			Map<String,String> seqs = getSeq(br2);
			List<String> sps = new ArrayList<String>();
			int newpos = pos;
			String lseq = seqs.get("elegans" + "\t" + id);
			String oseq = lseq.replace("-", "");
			if(oseq.length() < pos){
				System.out.println(id + "\t" + pos);
			}
			String subseq = oseq.substring(0, pos);
			String temsubseq = lseq.substring(0, pos);
			String temseq = lseq;
			if(temsubseq.equals(subseq)){
				newpos = pos;
			}else{
				int num = 0;
				for(int i=0;i<lseq.length()-oseq.length();i++){
					num ++;
					temseq = temseq.replaceFirst("-", "");
					temsubseq = temseq.substring(0, pos);
					if(temsubseq.equals(subseq)){
						newpos = pos + num;
						break;
					}else{
						continue;
					}
				}
			}
			Iterator<String> it1 = seqs.keySet().iterator();
			while(it1.hasNext()){
				String inf = it1.next();
				String[] sp0 = inf.split("\t");
				String sp = sp0[0];
				if(sp.equals("elegans")){
					sps.add(sp);
					continue;
				}
				String seq = seqs.get(inf);
				String newaa = seq.substring(newpos-1, newpos);
				if(!newaa.equals("S") && !newaa.equals("T") && !newaa.equals("Y")){
					continue;
				}
				int oldpos = getOldIndex(seq,newpos);
				if(!hmall.containsKey(inf)){
					continue;
				}else{
					Set<Integer> hs = hmall.get(inf);
					if(hs.contains(oldpos)){
						sps.add(sp);
					}
				}
			}
			Map<String,Double> lifeTree = LifeTree.getTree();
			Map<String,Double> spnum = LifeTree.getSpNum();
			double MBL = 0.0;
			double RCR = 1.0;
			for(int i=0;i<sps.size();i++){
				String spa = sps.get(i);
				for(int j=0;j<sps.size();j++){
					if(j == i){
						continue;
					}else{
						String spb = sps.get(j);
						double temMBL = lifeTree.get(spa + "_" + spb);
						if(temMBL > MBL){
							MBL = temMBL;
							RCR = 1.0 * sps.size()/spnum.get(spa + "_" + spb);
						}
					}
				}
			}
			double RCS = MBL * RCR;
			String inf = id + "\t" + pos /*+ "\t" + newpos*/;
			hm1.put(inf, RCS);
		}
		
		return hm1;
	}
	public static Map<String,String> getSeq(BufferedReader br1) throws IOException{
		Map<String,String> hm1 = new HashMap<String,String>();
		String s1;
		String inf = null;
		StringBuffer sb = new StringBuffer();
		while((s1 = br1.readLine()) != null){
			if(s1.startsWith(">")){
				if(sb.length() == 0 || sb.toString().equals("")){
					String[] sp1 = s1.split("\\s+");
					String sp = sp1[0].substring(1);
					String id = sp1[1];
					inf = sp + "\t" + id;
				}else{
					String seq = sb.toString();
					hm1.put(inf, seq);
					sb = new StringBuffer();
					String[] sp1 = s1.split("\\s+");
					String sp = sp1[0].substring(1);
					String id = sp1[1];
					inf = sp + "\t" + id;
				}
			}else{
				sb.append(s1);
				//System.out.println(s1);
			}
		}
		hm1.put(inf, sb.toString());
		return hm1;
	}
	public static int getOldIndex(String seq,int newindex){
		int oldindex;
		String subseq = seq.substring(0, newindex);
		String oldseq = subseq.replace("-", "");
		oldindex = oldseq.length();
		
		return oldindex;
	}
	public static void readEML(File f1, Map<String,Set<Integer>> hm, String sp) throws NumberFormatException, IOException{
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		String s1;
		while((s1 = br1.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			String inf = sp + "\t" + id;
			int index = Integer.parseInt(sp1[2]);
			if(!hm.containsKey(inf)){
				Set<Integer> hs = new HashSet<Integer>();
				hs.add(index);
				hm.put(inf, hs);
			}else{
				Set<Integer> hs = hm.get(inf);
				hs.add(index);
				hm.put(inf, hs);
			}
		}
	}
}
