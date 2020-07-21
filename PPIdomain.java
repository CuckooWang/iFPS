import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PPIdomain {
	public static Map<String,List<Double>> getPPIDomianNum(File f1) throws IOException{
		Map<String,List<Double>> hm = new HashMap<String,List<Double>>();
		File f3 = new File("scanoutdo");
		File f4 = new File("domain_domain.dat");
		File f5 = new File("domain_motif.dat");
		
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		
		BufferedReader br3 = new BufferedReader(new FileReader(f3));
		BufferedReader br4 = new BufferedReader(new FileReader(f4));
		BufferedReader br5 = new BufferedReader(new FileReader(f5));
		
		Set<String> site = new HashSet<String>();
		String s1;
		while((s1 = br1.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			String pos = sp1[2];
			String inf = id + "\t" + pos;
			site.add(inf);
		}
		
		Map<String,Set<String>> hm1 = new HashMap<String,Set<String>>();
		while((s1 = br3.readLine()) != null){
			if(s1.startsWith("#")){
				continue;
			}else{
				String[] sp1 = s1.split("\\s+");
				String domid = sp1[0];
				String proid = sp1[3];
				String from = sp1[17];
				//System.out.println(from);
				String to = sp1[18];
				String dominf = from + ";" + to + ";" + domid;
				putinMap(hm1,proid,dominf);
			}
		}
		
		Map<String,Set<String>> dd = new HashMap<String,Set<String>>();
		while((s1 = br4.readLine()) != null){
			if(s1.startsWith("#=ID")){
				String[] sp1 = s1.split("\t");
				String dom1 = sp1[1].trim();
				String dom2 = sp1[2].trim();
				putinMap(dd,dom1,dom2);
				putinMap(dd,dom2,dom1);
			}
		}
		
		Map<String,Set<String>> dm = new HashMap<String,Set<String>>();
		while((s1 = br5.readLine()) != null){
			if(s1.startsWith("#=ID")){
				String[] sp1 = s1.split("\t");
				String dom = sp1[1].trim();
				String mot = sp1[3].trim();
				putinMap(dm,dom,mot);
				putinMap(dm,mot,dom);
			}
		}
		Iterator<String> it1 = site.iterator();
		while(it1.hasNext()){
			String inf = it1.next();
			String[] sp1 = inf.split("\t");
			String id = sp1[0];
			int index = Integer.parseInt(sp1[1]);
			if(!hm1.containsKey(id)){
				List<Double> l  = new ArrayList<Double>();
				l.add(0.0);
				l.add(0.0);
				l.add(0.0);
				hm.put(inf, l);
			}else{
				Set<String> hs1 = hm1.get(id);
				Iterator<String> it11 = hs1.iterator();
				double num = 0.0;
				double numd = 0.0;
				double numt = 0.0;
				Set<String> ppd = new HashSet<String>();
				Set<String> ppm = new HashSet<String>();
				while(it11.hasNext()){
					String dinf = it11.next();
					String[] sp11 = dinf.split(";");
					int from = Integer.parseInt(sp11[0]);
					int to = Integer.parseInt(sp11[1]);
					String domain = sp11[2];
					if(index >= from && index <= to){
						num ++;
						if(dd.containsKey(domain)){
							ppd.addAll(dd.get(domain));
						}
						if(dm.containsKey(domain)){
							ppm.addAll(dm.get(domain));
						}
					}
				}
				numd = ppd.size();
				numt = ppm.size();
				List<Double> l  = new ArrayList<Double>();
				l.add(num);
				l.add(numd);
				l.add(numt);
				hm.put(inf, l);
			}
		}
		
		return hm;
	}
	
	public static void putinMap(Map<String,Set<String>> hm1, String id1,String id2){
		if(!hm1.containsKey(id1)){
			Set<String> hs1 = new HashSet<String>();
			hs1.add(id2);
			hm1.put(id1, hs1);
		}else{
			Set<String> hs1 = hm1.get(id1);
			hs1.add(id2);
			hm1.put(id1, hs1);
		}
	}
}
