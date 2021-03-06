import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpKA {
	public static Map<String,Double> getKANum(File f1) throws IOException{
		Map<String,Double> hm = new HashMap<String,Double>();
		File f2 = new File("test_all.txt");
		
		
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		BufferedReader br2 = new BufferedReader(new FileReader(f2));
		
		String s1;
		Map<String,Double> out = new HashMap<String,Double>();
		while((s1 = br2.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			String index = sp1[1];
			double num = Double.parseDouble(sp1[2]);
			String inf = id + "\t" + index;
			out.put(inf, num);
			
		}

		while((s1 = br1.readLine()) != null){
			String[] sp1 = s1.split("\t");
			String id = sp1[0];
			String index = sp1[2];
			String inf = id + "\t" + index;
			double num = out.get(inf);
			hm.put(inf, num);
		}
		return hm;
	}
}
