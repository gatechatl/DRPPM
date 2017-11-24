package PhosphoTools.HongBoProject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class CalculateCorrelationKinaseActivityClusters {

	public static String parameter_info() {
		return "[Kinase Activity] [Clustering]";
	}
	public static void main(String[] args) {
		
		try {
			
			
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\KinaseActivity\\phosProteome_Cluster_trend_consensus_eigengenes.txt";
			String kinaseActivityFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\KinaseActivity\\kinase_activity.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			LinkedList[] list = new LinkedList[header.split("\t").length];
			for (int i = 0; i < list.length; i++) {
				list[i] = new LinkedList();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					list[i - 1].add(split[i]);
				}								
			}
			in.close();

			HashMap map = new HashMap();
			fstream = new FileInputStream(kinaseActivityFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				double[] activity = new double[8];
				for (int i = 1; i < 9; i++) {
					activity[i - 1] = new Double(split[i]);
				}								
				map.put(geneName, activity);
			}
			in.close();
			
			Iterator itr2 = map.keySet().iterator();
			while (itr2.hasNext()) {
				String geneName = (String)itr2.next();
				String result = geneName;
				double[] trend2 = (double[])map.get(geneName);
				for (int i = 0; i < list.length; i++) {
					double[] trend = new double[8];
					int j = 0;
					Iterator itr = list[i].iterator();
					while (itr.hasNext()) {
						double value = new Double((String)itr.next());
						trend[j] = value;
						j++;
					}
					double correl = MathTools.PearsonCorrel(trend2, trend);
					result += "\t" + correl;
				}
				System.out.println(result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
