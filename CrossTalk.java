import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CrossTalk {
	public static Map<String,Double> ifHas(File f1,int winsize) throws IOException{
		Map<String,Double> hm = new HashMap<String,Double>();
		File f2 = new File("seq_all.sulfation_new.fa");
		
		
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		BufferedReader br2 = new BufferedReader(new FileReader(f2));
		
		String s1;
		String id = null;
		Map<String,Set<Integer>> out = new HashMap<String,Set<Integer>>();
		while((s1 = br2.readLine()) != null){
			if(s1.startsWith("Position")){
				continue;
			}
			if(s1.startsWith(">")){
				id = s1.substring(1);
			}else{
				String[] sp1 = s1.split("\t");
				int index = Integer.parseInt(sp1[0]);
				putinMap(out,id,index);
			}
		}

		while((s1 = br1.readLine()) != null){
			String[] sp1 = s1.split("\t");
			id = sp1[0];
			int index = Integer.parseInt(sp1[2]);
			String inf = id + "\t" + index;
			if(!out.containsKey(id)){
				hm.put(inf, 0.0);
			}else{
				int tem = 0;
				for(int i = index-winsize;i<index+winsize+1;i++){
					if(out.get(id).contains(i)){
						tem = 1;
						hm.put(inf, 1.0);
						break;
					}
				}
				if(tem == 0){
					hm.put(inf, 0.0);
				}
			}
		}
		return hm;
	}
	
	public static void putinMap(Map<String,Set<Integer>> hm1, String id,int index){
		if(!hm1.containsKey(id)){
			Set<Integer> hs1 = new HashSet<Integer>();
			hs1.add(index);
			hm1.put(id, hs1);
		}else{
			Set<Integer> hs1 = hm1.get(id);
			hs1.add(index);
			hm1.put(id, hs1);
		}
	}
}
