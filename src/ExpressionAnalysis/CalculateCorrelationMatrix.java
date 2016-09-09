package ExpressionAnalysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.MathTools;

/**
 * Given an input matrix file and a consensus profile generate a correlation matrix
 * @author tshaw
 *
 */
public class CalculateCorrelationMatrix {

	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Given an input matrix file and a consensus profile generate a correlation matrix";
	}
	public static String parameter_info() {
		return "[inputMatrix] [consensusProfileFile] [geneListFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrix = args[0];
			String consensusProfile = args[1];
			String geneListFile = args[2];
			
			HashMap geneList= new HashMap();
			FileInputStream fstream = new FileInputStream(geneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (geneList.containsKey(split[0].toUpperCase())) {
					String type = (String)geneList.get(split[0].toUpperCase()) + "," + split[1];
					geneList.put(split[0].toUpperCase(), type);
				} else {
					geneList.put(split[0].toUpperCase(), split[1]);
				}
			}
			in.close();
			
			fstream = new FileInputStream(consensusProfile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println("Header\t" + header);
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
			HashMap map_tag = new HashMap();
			fstream = new FileInputStream(inputMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].toUpperCase();
				String key = geneName.split("_")[0].toUpperCase();
				if (geneList.containsKey(key)) {
					double[] activity = new double[8];
					for (int i = 1; i < 9; i++) {
						activity[i - 1] = new Double(split[i]);
					}								
					map.put(geneName, activity);
					map_tag.put(geneName, geneList.get(key));
				}
			}
			in.close();
			
			Iterator itr2 = map.keySet().iterator();
			while (itr2.hasNext()) {
				String geneName = (String)itr2.next();
				String key = geneName.split("_")[0];
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
				result += "\t" + map_tag.get(geneName);
				System.out.println(result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
