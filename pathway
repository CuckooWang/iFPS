import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class pathway {
	public static Map<String,Double> getPWNum(File f1) throws IOException{
		Map<String,Double> hm = new HashMap<String,Double>();
		File f2 = new File("idmapping_result.txt");
		File f3 = new File("cel_pathway.list");
		
		
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		BufferedReader br2 = new BufferedReader(new FileReader(f2));
		BufferedReader br3 = new BufferedReader(new FileReader(f3));
		
		String s1;
		Map<String,String> blout = new HashMap<String,String>();
		while((s1 = br2.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			String newid = sp1[1];
			if(newid.equals("null")){
				continue;
			}else{
				blout.put(id, newid);
			}
		}
		
		Map<String,Set<String>> pw = new HashMap<String,Set<String>>();
		while((s1 = br3.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String[] sp10 = sp1[0].split(":");
			String[] sp11 = sp1[1].split(":");
			String proid = sp10[1];
			String pwid = sp11[1];
			putinMap(pw,proid,pwid);
		}
		
		while((s1 = br1.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			if(!blout.containsKey(id)){
				hm.put(id, 0.0);
			}else{
				String newid = blout.get(id);
				if(!pw.containsKey(newid)){
					hm.put(id, 0.0);
				}else{
					double pwnum = pw.get(newid).size() * 1.0;
					hm.put(id, pwnum);
				}
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
