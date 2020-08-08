package genomics.exome.summarize;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class McKinnonGenerateDistribution {

	public static void main(String[] args) {
		
		try {
			
			String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2013_SNV.txt";
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
				String quality = split[1];
				String sampleName = split[2];
				String type = split[5];
				
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
			
			System.out.println("sampleName\tmissense\tsilent\tnonsense\texon\tsplice\tsplice_region\tUTR_3\tUTR_5");
			Iterator itr = sampleType.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String sampeType = (String)map.get(sampleName);
				HashMap mutType = (HashMap)sampleType.get(sampleName);
				int missense = 0;
				int silent = 0;
				int exon = 0;
				int splice = 0;
				int splice_region = 0;
				int UTR_3 = 0;
				int UTR_5 = 0;
				int nonsense = 0;
				if (mutType.containsKey("missense")) {
					missense = (Integer)mutType.get("missense");
				}
				if (mutType.containsKey("silent")) {
					silent = (Integer)mutType.get("silent");
				}
				if (mutType.containsKey("exon")) {
					exon = (Integer)mutType.get("exon");
				}
				if (mutType.containsKey("splice")) {
					splice = (Integer)mutType.get("splice");
				}
				if (mutType.containsKey("splice_region")) {
					splice_region = (Integer)mutType.get("splice_region");
				}
				if (mutType.containsKey("UTR_3")) {
					UTR_3 = (Integer)mutType.get("UTR_3");
				}
				if (mutType.containsKey("UTR_5")) {
					UTR_5 = (Integer)mutType.get("UTR_5");
				}
				if (mutType.containsKey("nonsense")) {
					nonsense = (Integer)mutType.get("nonsense");
				}
				
				System.out.println(sampeType + "_" + sampleName + "\t" + missense + "\t" + silent + "\t" + nonsense + "\t" + exon + "\t" + splice + "\t" + splice_region + "\t" + UTR_3 + "\t" + UTR_5);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
