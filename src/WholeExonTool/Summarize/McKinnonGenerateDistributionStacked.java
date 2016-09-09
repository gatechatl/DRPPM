package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class McKinnonGenerateDistributionStacked {

	public static void main(String[] args) {
		
		try {
			
			String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\PsuedoPair\\New_PseudoNormal_tier1_putative_somatic_addedNote.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2015_SNV_compile.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2015_SNV_unfiltered.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2015_SNV_final.txt";
			String sampleFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\samples.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2015_SNV_filtered.txt";
			HashMap sampleType = new HashMap();
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(sampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String type = str.split("-")[1];
				String sampleName = str.split("_")[0];
				map.put(sampleName, type);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String quality = split[2];
				String sampleName = split[3];
				String type = split[0];
				
				if (quality.equals("SJHQ")) {
					if (sampleType.containsKey(sampleName)) {
						HashMap mutType = (HashMap)sampleType.get(sampleName);
						if (mutType.containsKey(type)) {
							int count = (Integer)mutType.get(type);
							mutType.put(type, count + 1);
						} else {
							mutType.put(type, 1);
						}
						sampleType.put(sampleName, mutType);
					} else {
						HashMap mutType = new HashMap();
						if (mutType.containsKey(type)) {
							int count = (Integer)mutType.get(type);
							mutType.put(type, count + 1);
						} else {
							mutType.put(type, 1);
						}
						sampleType.put(sampleName, mutType);
					}
				}
			}
			in.close();
			
			System.out.println("sampleName\trecurrent\tbad_singleton\tgood_singleton");
			Iterator itr = sampleType.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String sampeType = (String)map.get(sampleName);
				HashMap mutType = (HashMap)sampleType.get(sampleName);
				int singleton_bad = 0;
				int recurrent = 0;
				int singleton_good = 0;

				if (mutType.containsKey("singleton_bad")) {
					singleton_bad = (Integer)mutType.get("singleton_bad");
				}
				if (mutType.containsKey("recurrent")) {
					recurrent = (Integer)mutType.get("recurrent");
				}
				if (mutType.containsKey("singleton_good")) {
					singleton_good = (Integer)mutType.get("singleton_good");
				}
				
				
				System.out.println(sampeType + "_" + sampleName + "\t" + recurrent + "\t" + singleton_bad + "\t" + singleton_good);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
