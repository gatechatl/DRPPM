package ProteinFeature.OverlapGeneList;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class OverlapGeneList {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Overlapping gene list";
	}
	public static String parameter_info() {
		return "";
	}
	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String bingexperimentFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\PaulTaylor\\MotifDiscovery\\BingBai_HTPExperiment.fasta\\bing_list.txt";
			FileInputStream fstream = new FileInputStream(bingexperimentFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1], split[1]);
			}
			in.close();
			
			HashMap hit = new HashMap();
			HashMap viability = new HashMap();
			String viabilityFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\PaulTaylor\\MotifDiscovery\\BingBai_HTPExperiment.fasta\\KyungHa_viability_experiment.txt";
			fstream = new FileInputStream(viabilityFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (map.containsKey(str)) {
					System.out.println(str);
					hit.put(str,  str);
					
				}
				viability.put(str, str);
			}
			in.close();
			System.out.println("Overlap: " + hit.size() + "\nBing's Experiment: " + map.size() + "\nViability: " + viability.size());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
