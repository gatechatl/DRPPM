package Integration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import PhosphoTools.SummaryTools;
import Statistics.General.MathTools;

/**
 * Integration of multiple layer of expression data
 * @author tshaw
 *
 */
public class IntegrateExpressionMatrix {
	
	public static String parameter_info() {
		return "[phosphoFile] [proteomeFile] [rnaFile (put NA if not available)] [weightFile] [outputMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String phosphoFile = args[0];
			String proteomeFile = args[1];
			String rnaFile = args[2]; // can be placed with a NA
			String weightFile = args[3];
			String unannotated_weights = args[4];
			String outputMatrix = args[5];
			String outputFinalMatrix = args[6];
			
			double unannotated_phospho_weight = new Double(unannotated_weights.split(",")[0]);
			double unannotated_proteome_weight = new Double(unannotated_weights.split(",")[1]);
			double unannotated_rna_weight = new Double(unannotated_weights.split(",")[2]);
			
			HashMap accession2Gene = accession2GeneName(phosphoFile, proteomeFile);
			HashMap geneWeights = interpretGeneWeight(weightFile);			
			HashMap phosphoMap = grabPhosphoExpressionValues(phosphoFile);
			HashMap proteomeMap = grabWholeProteomeExpressionValues(proteomeFile);
			
			HashMap summaryWeight = new HashMap();
			HashMap[] summaryTotal = new HashMap[10]; // assume there are 10 samples
			for (int i = 0; i < summaryTotal.length; i++) {
				summaryTotal[i] = new HashMap();
			}
			FileWriter fwriter = new FileWriter(outputMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_final = new FileWriter(outputFinalMatrix);
			BufferedWriter out_final = new BufferedWriter(fwriter_final);			
									
			out.write("Accession\tGeneName\tSiteInfo\tDataType\tWeight\tWeightedLogFC_Sample1\tWeightedLogFC_Sample2\tWeightedLogFC_Sample3\tWeightedLogFC_Sample4\tWeightedLogFC_Sample5\tWeightedLogFC_Sample6\tWeightedLogFC_Sample7\tWeightedLogFC_Sample8\tWeightedLogFC_Sample9\tWeightedLogFC_Sample10\tLogFC_Sample1\tLogFC_Sample2\tLogFC_Sample3\tLogFC_Sample4\tLogFC_Sample5\tLogFC_Sample6\tLogFC_Sample7\tLogFC_Sample8\tLogFC_Sample9\tLogFC_Sample10\tOrig_Sample1\tOrig_Sample2\tOrig_Sample3\tOrig_Sample4\tOrig_Sample5\tOrig_Sample6\tOrig_Sample7\tOrig_Sample8\tOrig_Sample9\tOrig_Sample10\n");
			if (rnaFile.equals("NA")) {								
				Iterator itr = accession2Gene.keySet().iterator();
				while (itr.hasNext()) {
					String accession = (String)itr.next();
					
					String accession_col = accession; // accession is the output
					String geneName_col = (String)accession2Gene.get(accession); // geneName is the output
					String siteInfo_col = ""; // provide site information if available
					String data_type_col = ""; // the data_type of the expression information
					String weight_col = "-9999"; 
					String weighted_logFC_col = "";
					String logFC_col = ""; // expression information
					String raw_col = ""; // raw fold change relative to average																				
										 					
					LinkedList activeList = new LinkedList();
					LinkedList inhibitList = new LinkedList();
					LinkedList generateList = new LinkedList();
					
					boolean foundPhospho = false;
					if (phosphoMap.containsKey(accession)) {
						HashMap site = (HashMap)phosphoMap.get(accession);
						boolean foundActivity = false;
						foundPhospho = true;
						Iterator itr2 = site.keySet().iterator();
						while (itr2.hasNext()) {
							String siteLoc = (String)itr2.next();
							if (geneWeights.containsKey(accession + "\t" + siteLoc)) {
								double weight = (Double)geneWeights.get(accession + "\t" + siteLoc);
								if (weight == 1.0 || weight == -1.0) {
									foundActivity = true;
								}
							}
						}
						itr2 = site.keySet().iterator();
						while (itr2.hasNext()) {
							String siteLoc = (String)itr2.next();
							// check what's the weight for the site
							siteInfo_col = siteLoc;
							data_type_col = "PhosphoSite";
							String expr_str = (String)site.get(siteLoc);
							raw_col = expr_str;
							String[] expr_split = expr_str.split("\t");
							double total = 0.0;
							for (String expr: expr_split) {
								total += new Double(expr);
							}
							double average = total / expr_split.length;
							double[] logFC = new double[expr_split.length];
							for (int i = 0; i < expr_split.length; i++) {
								logFC[i] = MathTools.log2((new Double(expr_split[i]) / average) + 0.01);								
							}
							logFC_col = "";
							for (int i = 0; i < logFC.length; i++) {
								if (logFC_col.equals("")) {
									logFC_col = logFC[i] + "";
								} else {
									logFC_col += "\t" + logFC[i];
								}								
							}
							if (foundActivity) {
								weight_col = "0.0";
							} else {
								weight_col = unannotated_phospho_weight + "";
							}
							double weight = new Double(weight_col);
							if (geneWeights.containsKey(accession + "\t" + siteLoc)) {
								weight = (Double)geneWeights.get(accession + "\t" + siteLoc);
								if (weight == 1.0 || weight == -1.0) {
									weight_col = weight + "";									
								}
							}
							double[] weighted_logFC = new double[expr_split.length];
							for (int i = 0; i < weighted_logFC.length; i++) {
								weighted_logFC[i] = logFC[i] * weight;								
							}
							
							weighted_logFC_col = "";
							for (int i = 0; i < weighted_logFC.length; i++) {
								if (weighted_logFC_col.equals("")) {
									weighted_logFC_col = weighted_logFC[i] + "";
								} else {
									weighted_logFC_col += "\t" + weighted_logFC[i];
								}
								if (summaryTotal[i].containsKey(accession_col)) {
									double total_weighted_locFC = (Double)summaryTotal[i].get(accession_col);
									
									summaryTotal[i].put(accession_col, total_weighted_locFC + new Double(weighted_logFC[i]));
								} else {
									summaryTotal[i].put(accession_col, new Double(weighted_logFC[i]));
								}
							}
							if (summaryWeight.containsKey(accession_col)) {
								double orig_weight = (Double)summaryWeight.get(accession_col);
								orig_weight += Math.abs(new Double(weight_col));
								summaryWeight.put(accession_col, orig_weight);
							} else {
								double weight_val = new Double(weight_col);
								weight_val = Math.abs(weight_val);
								summaryWeight.put(accession_col, weight_val);
							}
							out.write(accession_col + "\t" + geneName_col + "\t" + siteInfo_col + "\t" + data_type_col + "\t" + weight_col + "\t" + weighted_logFC_col + "\t" + logFC_col + "\t" + raw_col + "\n");							
						}
						
					}	
					if (proteomeMap.containsKey(accession)) {
						siteInfo_col = "NA";
						data_type_col = "Proteome";
						String expr_str = (String)proteomeMap.get(accession);		
						raw_col = expr_str;
						String[] expr_split = expr_str.split("\t");
						double total = 0.0;
						for (String expr: expr_split) {
							total += new Double(expr);
						}
						double average = total / expr_split.length;
						double[] logFC = new double[expr_split.length];
						for (int i = 0; i < expr_split.length; i++) {
							logFC[i] = MathTools.log2((new Double(expr_split[i]) / average) + 0.01);								
						}
						logFC_col = "";
						for (int i = 0; i < logFC.length; i++) {
							if (logFC_col.equals("")) {
								logFC_col = logFC[i] + "";
							} else {
								logFC_col += "\t" + logFC[i];
							}								
						}
						if (foundPhospho) {
							weight_col = "0.0";
						} else {
							weight_col = unannotated_proteome_weight + "";
						}
						double weight = new Double(weight_col);
						
						double[] weighted_logFC = new double[expr_split.length];
						for (int i = 0; i < weighted_logFC.length; i++) {
							weighted_logFC[i] = logFC[i] * weight;								
						}
						
						weighted_logFC_col = "";
						for (int i = 0; i < weighted_logFC.length; i++) {
							if (weighted_logFC_col.equals("")) {
								weighted_logFC_col = weighted_logFC[i] + "";
							} else {
								weighted_logFC_col += "\t" + weighted_logFC[i];
							}
							
							if (summaryTotal[i].containsKey(accession_col)) {
								double total_weighted_locFC = (Double)summaryTotal[i].get(accession_col);
								
								summaryTotal[i].put(accession_col, total_weighted_locFC + new Double(weighted_logFC[i]));
							} else {
								summaryTotal[i].put(accession_col, new Double(weighted_logFC[i]));
							}
						}
						if (summaryWeight.containsKey(accession_col)) {
							double orig_weight = (Double)summaryWeight.get(accession_col);
							orig_weight += Math.abs(new Double(weight_col));
							summaryWeight.put(accession_col, orig_weight);
						} else {
							double weight_val = new Double(weight_col);
							weight_val = Math.abs(weight_val);
							summaryWeight.put(accession_col, weight_val);
						}
						out.write(accession_col + "\t" + geneName_col + "\t" + siteInfo_col + "\t" + data_type_col + "\t" + weight_col + "\t" + weighted_logFC_col + "\t" + logFC_col + "\t" + raw_col + "\n");
					}										
				}
			}
			out.close();
			Iterator itr3 = accession2Gene.keySet().iterator();
			while (itr3.hasNext()) {
				String accession = (String)itr3.next();
				String geneName_col = (String)accession2Gene.get(accession); // geneName is the output
				double totalWeight = (Double)summaryWeight.get(accession);
				out_final.write(accession + "\t" + geneName_col + "\t" + totalWeight);
				for (int i = 0; i < 10; i++) {
					double weightLogFC = (Double)summaryTotal[i].get(accession);
					out_final.write("\t" + (weightLogFC / totalWeight));					
				}
				out_final.write("\n");
			}			
			out_final.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * grab all the geneNames for proteomeFile and phosphoFile
	 * @param proteomeFile
	 * @param phosphoFile
	 * @return
	 */
	public static HashMap accession2GeneName(String phosphoFile, String proteomeFile) {
		HashMap accession2geneName = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(phosphoFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (split[1].split("\\|").length > 1) {
					String accession = split[1].split("\\|")[1];
					String geneName = split[4];
					accession2geneName.put(accession, geneName);
				}
			}
			in.close();
			
			fstream = new FileInputStream(proteomeFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (split[1].split("\\|").length > 1) {
					String accession = split[1].split("\\|")[1];
					String geneName = split[3];
					accession2geneName.put(accession, geneName);
				}
				
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return accession2geneName;
	}
	
	public static HashMap interpretGeneWeight(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				map.put(split[0] + "\t" + split[1], new Double(split[2]));
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;		
	}
	
	/**
	 * This will work only with jumpq result for wholeproteome
	 * @param inputFile
	 * @param sampleNames
	 * @return
	 */
	public static HashMap grabRNAseqExpressionValues(String inputFile) {
		HashMap map = new HashMap();
		try {

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				String remaining = "";
				for (int i = 1; i < split.length; i++) {
					if (remaining.equals("")) {
						remaining = split[i];
					} else {
						remaining += "\t" + split[i];
					}
				}
				map.put(split[0], remaining);				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * This will work only with jumpq result for wholeproteome
	 * @param inputFile
	 * @param sampleNames
	 * @return
	 */
	public static HashMap grabWholeProteomeExpressionValues(String inputFile) {
		HashMap map = new HashMap();
		String[] expression_tags = {"sig126", "sig127N", "sig127C", "sig128N", "sig128C", "sig129N", "sig129C", "sig130N", "sig130C", "sig131"};
		
		try {						
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String modinfo = in.readLine();
			String header = in.readLine();
			int[] expr_index = SummaryTools.header_expr_info(header, expression_tags);
			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
									
				if (split[1].split("\\|").length > 1) {
					String accession = split[1].split("\\|")[1];
					
					String expr = "";
					for (int i = 0; i < expr_index.length; i++) {
						if (expr.equals("")) {
							expr = split[expr_index[i]];
						} else {
							expr += "\t" + split[expr_index[i]];
						}
					}
					//System.out.println(expr);
					map.put(accession, expr);
				}				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * This will work only with jumpq result for phosphoproteome 
	 * @param inputFile
	 * @param sampleNames
	 * @return
	 */
	public static HashMap grabPhosphoExpressionValues(String inputFile) {
		HashMap map = new HashMap();
		String[] expression_tags = {"sig126", "sig127N", "sig127C", "sig128N", "sig128C", "sig129N", "sig129C", "sig130N", "sig130C", "sig131"};
		//String[] Mod_sites = {"Mod sites"};		
		try {						
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String modinfo = in.readLine();
			String header = in.readLine();
			int[] expr_index = SummaryTools.header_expr_info(header, expression_tags);
			//int[] modsite_index = SummaryTools.header_expr_info(header, Mod_sites);
			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
					
				if (split[1].split("\\|").length > 1 && split[1].contains(":")) {
					String accession = split[1].split("\\|")[1];
					String mod_sites = split[1].split(":")[1];
					String[] mod_sites_split = mod_sites.split(",");
					String expr = "";
					for (int i = 0; i < expr_index.length; i++) {
						if (expr.equals("")) {
							expr = split[expr_index[i]];
						} else {
							expr += "\t" + split[expr_index[i]];
						}
					}
					if (map.containsKey(accession)) {
						HashMap site = (HashMap)map.get(accession);
						site.put(mod_sites, expr);
						map.put(accession, site);						
					} else {
						HashMap site = new HashMap();
						site.put(mod_sites, expr);
						map.put(accession, site);
					}
					
				}				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
