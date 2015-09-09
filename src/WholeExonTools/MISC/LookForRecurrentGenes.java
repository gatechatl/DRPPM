package WholeExonTools.MISC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class LookForRecurrentGenes {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String fileName = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\Leventaki\\exon_review\\Leventaki_EXON_REVIEW_20150903_LookForRecurrent.txt";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0].split("_")[0] + "\t" + split[1], split[0].split("_")[0] + "\t" + split[1]);
				/*
				if (map.containsKey(split[0] + "\t" + split[1])) {
					int num = (Integer)map.get(split[0] + "\t" + split[1]) + 1;
					
				} else {
					map.put(split[0] + "\t" + split[1], 1);
				}*/
			}
			in.close();
			
			
			HashMap newMap = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				String[] split = str.split("\t");
				//String sample = (String)map.get(gene);
				String gene = split[1];
				String sample = split[0];
				if (newMap.containsKey(gene)) {
					int num = (Integer)newMap.get(gene) + 1;
					newMap.put(gene, num);
				} else {
					newMap.put(gene, 1);
				}
				//System.out.println(key + "\t" + map.get(key));
			}
			
			itr = newMap.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				int num = (Integer)newMap.get(key);
				System.out.println(key + "\t" + num);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
