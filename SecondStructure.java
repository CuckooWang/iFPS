import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SencondStru {
	public static Map<String,List<Double>> getChance(File f1) throws IOException{
		Map<String,List<Double>> hm = new HashMap<String,List<Double>>();
		File f2 = new File("result_all.txt");
		
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		BufferedReader br2 = new BufferedReader(new FileReader(f2));
		
		Map<String,List<Double>> hm1 = new HashMap<String,List<Double>>();
		String s1;
		while((s1 = br2.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			String pos = sp1[1];
			String inf = id + "\t" + pos;
			double rsa = Double.parseDouble(sp1[2]);
			double asa = Double.parseDouble(sp1[3]);
			double ap = Double.parseDouble(sp1[4]);
			double bp = Double.parseDouble(sp1[5]);
			double cp = Double.parseDouble(sp1[6]);
			List<Double> l = new ArrayList<Double>();
			l.add(rsa);
			l.add(asa);
			l.add(ap);
			l.add(bp);
			l.add(cp);
			hm1.put(inf, l);
		}
		
		while((s1 = br1.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			String pos = sp1[2];
			String inf = id + "\t" + pos;
			List<Double> l = hm1.get(inf);
			hm.put(inf, l);
		}
		
		return hm;
	}
}
