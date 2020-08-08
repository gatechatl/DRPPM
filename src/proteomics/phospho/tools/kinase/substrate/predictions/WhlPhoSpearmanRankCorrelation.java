package proteomics.phospho.tools.kinase.substrate.predictions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

public class WhlPhoSpearmanRankCorrelation {

	public static String description() {
		return "Calculate Spearman Rank Correlation between Kinase and all Phosphosites";
	}
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String parameter_info() {
		return "[phosphoMatrix] [wholeMatrix] [numSamples] [kinaseGeneListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String phosphoMatrix = args[0];
			String wholeMatrix = args[1];
			int numSamples = new Integer(args[2]);
			String kinaseGeneListFile = args[3];
			String outputFile = args[4];
			HashMap kinaseList = new HashMap();
			
			FileInputStream fstream = new FileInputStream(kinaseGeneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				kinaseList.put(str.toUpperCase(), str.toUpperCase());
			}
			in.close();
			
			HashMap whl_kinase = new HashMap();
			fstream = new FileInputStream(wholeMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double[] expr = new double[numSamples];
				int index = 0;
				for (int i = split.length - numSamples; i < split.length; i++) {
					expr[index] = new Double(split[i]);
					index++;
				}
				if (kinaseList.containsKey(split[0].toUpperCase())) {
					whl_kinase.put(split[0].toUpperCase(), expr);
				}
			}
			in.close();


			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			String[] header_split = header.split("\t");
			out.write(header_split[0]);
			for (int i = 1; i < numSamples + 1; i++) {
				out.write("\t" + header_split[i]);
			}
			
			Iterator itr = whl_kinase.keySet().iterator();
			while (itr.hasNext()) {
				String kinase = (String)itr.next();
				out.write("\t" + kinase);
			}
			out.write("\n");
			int count = 0;
			fstream = new FileInputStream(phosphoMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				double[] expr = new double[numSamples];
				int index = 0;
				for (int i = split.length - numSamples; i < split.length; i++) {
					expr[index] = new Double(split[i]);
					index++;
					out.write("\t" + split[i]);
				}
				count++;
				if (count % 1000 == 0) {
					System.out.println(count);
				}
				itr = whl_kinase.keySet().iterator();
				while (itr.hasNext()) {
					String kinase = (String)itr.next();
					double[] kinase_expr = (double[])whl_kinase.get(kinase);					
					double spearman_cor = MathTools.SpearmanRank(kinase_expr, expr);
					double spearman_cor_pval = MathTools.SpearmanRankPvalue(kinase_expr, expr);
					spearman_cor = new Double(new Double(spearman_cor * 100).intValue()) / 100;
					//spearman_cor_pval = new Double(new Double(spearman_cor * 100000).intValue()) / 100000;
					out.write("\t" + spearman_cor + ":" + spearman_cor_pval);
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
