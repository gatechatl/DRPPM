package PhosphoTools.KinaseSubstrateInference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;
import statistics.general.StatisticsConversion;

/**
 * Combine the phosphosite correlation result and calculate a meta pvalue
 * @author tshaw
 *
 */
public class CombinePhosphositeCorrelationResult {


	public static String description() {
		return "Combine Spearman Rank Result";
	}
	public static String type() {
		return "KINASESUBSTRATE";
	}
	public static String parameter_info() {
		return "[inputListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();
			HashMap sites = new HashMap();
			HashMap kinases = new HashMap();
			String inputListFile = args[0];
			String outputFile = args[1];
			FileInputStream fstream = new FileInputStream(inputListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				String fileName = split[1];
				System.out.println(fileName);
				HashMap kinase_site = new HashMap();
				FileInputStream fstream2 = new FileInputStream(fileName);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				String header = in2.readLine();
				String[] header_split = header.split("\t");
				for (int i = 11; i < header_split.length; i++) {
					kinases.put(header_split[i], header_split[i]);
				}
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					HashMap kinase_correlation = new HashMap();
					for (int i = 11; i < header_split.length; i++) {
						kinase_correlation.put(header_split[i], split2[i]);
					}
					sites.put(split2[0], split2[0]);
					kinase_site.put(split2[0], kinase_correlation);
				}
				in2.close();
				map.put(name, kinase_site);
			}
			in.close();

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Site");
			Iterator itr = kinases.keySet().iterator();
			while (itr.hasNext()) {
				String kinase = (String)itr.next();
				out.write("\t" + kinase + "_Score\t" + kinase + "_correl");
			}
			out.write("\n");
			System.out.println("Total Sites: " + sites.size());
			int count = 0;
			Iterator itr2 = sites.keySet().iterator();
			while (itr2.hasNext()) {
				String site = (String)itr2.next();
				out.write(site);
				count++;
				if (count % 1000 == 0) {
					System.out.println(count);
				}
				itr = kinases.keySet().iterator();
				while (itr.hasNext()) {
					String kinase = (String)itr.next();
					
					String value = "";
					LinkedList zscores = new LinkedList();
					LinkedList weights = new LinkedList();
					Iterator itr3 = map.keySet().iterator();
					while (itr3.hasNext()) {
						String name = (String)itr3.next();
						HashMap kinase_site = (HashMap)map.get(name);
						if (kinase_site.containsKey(site)) {
							HashMap kinase_correl = (HashMap)kinase_site.get(site);
							if (kinase_correl.containsKey(kinase)) {
								String correl = (String)kinase_correl.get(kinase);
								double pval = new Double(correl.split(":")[1]);
								double pval_zscore = StatisticsConversion.inverseCumulativeProbability(pval / 2) * -1;
								// pval_zscore = pval2zscore(pval);
								double rho = new Double(correl.split(":")[0]);
								zscores.add(pval_zscore);
								rho = Math.abs(rho);
								/*if (rho > 0.7 || rho < -0.7) {
									weights.add(1.0);
								} else if (rho > 0.5 || rho < -0.5) {
									weights.add(0.5);
								} else {
									weights.add(0.1);
								}*/
								weights.add(rho);
								value += name + ":" + correl + ",";
							} else {
								// do nothing
							}
						} else {
							// do nothing
						}
					}
					if (zscores.size() > 0) {
						double zscore_result = MathTools.stouffer_meta_analysis(zscores, weights);
						out.write("\t" + zscore_result + "\t" + value);
					} else {
						out.write("\tNA\tNA");
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
