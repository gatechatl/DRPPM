package Integration.summarytable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Generate a comprehensive summary table count for each sample
 * @author tshaw
 *
 */
public class IntegratedSummaryTableFrequencyCount {

	public static String type() {
		return "INTEGRATED";
	}
	public static String description() {
		return "Generate summary frequency table";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			HashMap fusion = new HashMap();
			HashMap snv_missense = new HashMap();
			HashMap snv_silent = new HashMap();
			HashMap snv_nonsense = new HashMap();
			HashMap snv_exon = new HashMap();
			HashMap snv_splice = new HashMap();
			HashMap snv_splice_region = new HashMap();
			HashMap snv_UTR_3 = new HashMap();
			HashMap snv_UTR_5 = new HashMap();
			HashMap indel_proteinIns = new HashMap();
			HashMap indel_proteinDel = new HashMap();
			HashMap indel_exon = new HashMap();
			HashMap indel_frameshift = new HashMap();
			HashMap indel_intron = new HashMap();
			HashMap indel_splice = new HashMap();
			HashMap indel_splice_region = new HashMap();
			HashMap indel_UTR_3 = new HashMap();
			HashMap indel_UTR_5 = new HashMap();
			HashMap pdgfra_expression = new HashMap();
			HashMap met_expression = new HashMap();
			HashMap ccnd1_expression = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String sampleNameStr = in.readLine();
			String[] sampleNames = sampleNameStr.split("\t");
			String rnaseqIDStr = in.readLine();
			String[] rnaseqIDs = rnaseqIDStr.split("\t");
			String excapIDStr = in.readLine();
			String[] excapIDs = excapIDStr.split("\t");
			String subtypeIDStr = in.readLine();
			String[] subtypeIDs = subtypeIDStr.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("Pdgfra(Expression[FPKM])")) {
					for (int i = 1; i < split.length; i++) {
						if (!split[i].equals("NA")) {
							double fpkm = new Double(split[i]);
							//if (fpkm > 131) {
							pdgfra_expression.put(sampleNames[i], fpkm);
							//}
						}
					}
				}
				if (split[0].equals("Met(Expression[FPKM])")) {
					for (int i = 1; i < split.length; i++) {
						if (!split[i].equals("NA")) {
							double fpkm = new Double(split[i]);
							//if (fpkm > 131) {
							met_expression.put(sampleNames[i], fpkm);
							//}
						}
					}
				}
				if (split[0].equals("Ccnd1(Expression[FPKM])")) {
					for (int i = 1; i < split.length; i++) {
						if (!split[i].equals("NA")) {
							double fpkm = new Double(split[i]);
							//if (fpkm > 131) {
							ccnd1_expression.put(sampleNames[i], fpkm);
							//}
						}
					}
				}
				if (split[0].contains("Fusion")) {
					for (int i = 1; i < split.length; i++) {
						if (split[i].equals("1")) {
							if (fusion.containsKey(sampleNames[i])) {
								int count = (Integer)fusion.get(sampleNames[i]);
								count++;
								fusion.put(sampleNames[i], count);
							} else {
								fusion.put(sampleNames[i], 1);
							}
						}
					}
				}
				if (split[0].contains("SNV")) {
					for (int i = 1; i < split.length; i++) {
						if (split[i].contains("missense")) {
							if (snv_missense.containsKey(sampleNames[i])) {
								int count = (Integer)snv_missense.get(sampleNames[i]);
								count++;
								snv_missense.put(sampleNames[i], count);
							} else {
								snv_missense.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("silent")) {
							if (snv_silent.containsKey(sampleNames[i])) {
								int count = (Integer)snv_silent.get(sampleNames[i]);
								count++;
								snv_silent.put(sampleNames[i], count);
							} else {
								snv_silent.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("nonsense")) {
							if (snv_nonsense.containsKey(sampleNames[i])) {
								int count = (Integer)snv_nonsense.get(sampleNames[i]);
								count++;
								snv_nonsense.put(sampleNames[i], count);
							} else {
								snv_nonsense.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("exon")) {
							if (snv_exon.containsKey(sampleNames[i])) {
								int count = (Integer)snv_exon.get(sampleNames[i]);
								count++;
								snv_exon.put(sampleNames[i], count);
							} else {
								snv_exon.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("splice")) {
							if (snv_splice.containsKey(sampleNames[i])) {
								int count = (Integer)snv_splice.get(sampleNames[i]);
								count++;
								snv_splice.put(sampleNames[i], count);
							} else {
								snv_splice.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("splice_region")) {
							if (snv_splice_region.containsKey(sampleNames[i])) {
								int count = (Integer)snv_splice_region.get(sampleNames[i]);
								count++;
								snv_splice_region.put(sampleNames[i], count);
							} else {
								snv_splice_region.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("UTR_3")) {
							if (snv_UTR_3.containsKey(sampleNames[i])) {
								int count = (Integer)snv_UTR_3.get(sampleNames[i]);
								count++;
								snv_UTR_3.put(sampleNames[i], count);
							} else {
								snv_UTR_3.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("UTR_5")) {
							if (snv_UTR_5.containsKey(sampleNames[i])) {
								int count = (Integer)snv_UTR_5.get(sampleNames[i]);
								count++;
								snv_UTR_5.put(sampleNames[i], count);
							} else {
								snv_UTR_5.put(sampleNames[i], 1);
							}
						}						
					}
				}
				if (split[0].contains("Indel")) {
					for (int i = 1; i < split.length; i++) {
						if (split[i].contains("proteinIns")) {
							if (indel_proteinIns.containsKey(sampleNames[i])) {
								int count = (Integer)indel_proteinIns.get(sampleNames[i]);
								count++;
								indel_proteinIns.put(sampleNames[i], count);
							} else {
								indel_proteinIns.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("proteinDel")) {
							if (indel_proteinDel.containsKey(sampleNames[i])) {
								int count = (Integer)indel_proteinDel.get(sampleNames[i]);
								count++;
								indel_proteinDel.put(sampleNames[i], count);
							} else {
								indel_proteinDel.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("exon")) {
							if (indel_exon.containsKey(sampleNames[i])) {
								int count = (Integer)indel_exon.get(sampleNames[i]);
								count++;
								indel_exon.put(sampleNames[i], count);
							} else {
								indel_exon.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("frameshift")) {
							if (indel_frameshift.containsKey(sampleNames[i])) {
								int count = (Integer)indel_frameshift.get(sampleNames[i]);
								count++;
								indel_frameshift.put(sampleNames[i], count);
							} else {
								indel_frameshift.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("intron")) {
							if (indel_intron.containsKey(sampleNames[i])) {
								int count = (Integer)indel_intron.get(sampleNames[i]);
								count++;
								indel_intron.put(sampleNames[i], count);
							} else {
								indel_intron.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("splice")) {
							if (indel_splice.containsKey(sampleNames[i])) {
								int count = (Integer)indel_splice.get(sampleNames[i]);
								count++;
								indel_splice.put(sampleNames[i], count);
							} else {
								indel_splice.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("splice_region")) {
							if (indel_splice_region.containsKey(sampleNames[i])) {
								int count = (Integer)indel_splice_region.get(sampleNames[i]);
								count++;
								indel_splice_region.put(sampleNames[i], count);
							} else {
								indel_splice_region.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("UTR_3")) {
							if (indel_UTR_3.containsKey(sampleNames[i])) {
								int count = (Integer)indel_UTR_3.get(sampleNames[i]);
								count++;
								indel_UTR_3.put(sampleNames[i], count);
							} else {
								indel_UTR_3.put(sampleNames[i], 1);
							}
						}
						if (split[i].contains("UTR_5")) {
							if (indel_UTR_5.containsKey(sampleNames[i])) {
								int count = (Integer)indel_UTR_5.get(sampleNames[i]);
								count++;
								indel_UTR_5.put(sampleNames[i], count);
							} else {
								indel_UTR_5.put(sampleNames[i], 1);
							}
						}
					}
				}
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			for (int i = 0; i < sampleNames.length; i++) {
				if (i == 0) {
					out.write(sampleNames[i]);
				} else {
					out.write("\t" + sampleNames[i]);
				}
			}
			out.write("\n");
			
			//pdgfra_expression
			out.write("pdgfra_expression");
			for (int i = 1; i < sampleNames.length; i++) {
				if (pdgfra_expression.containsKey(sampleNames[i])) {
					out.write("\t" + pdgfra_expression.get(sampleNames[i]));
				} else {
					out.write("\tNA");
				}
			}
			out.write("\n");			
			
			//met_expression
			out.write("met_expression");
			for (int i = 1; i < sampleNames.length; i++) {
				if (met_expression.containsKey(sampleNames[i])) {
					out.write("\t" + met_expression.get(sampleNames[i]));
				} else {
					out.write("\tNA");
				}
			}
			out.write("\n");			

			//ccnd1_expression
			out.write("ccnd1_expression");
			for (int i = 1; i < sampleNames.length; i++) {
				if (ccnd1_expression.containsKey(sampleNames[i])) {
					out.write("\t" + ccnd1_expression.get(sampleNames[i]));
				} else {
					out.write("\tNA");
				}
			}
			out.write("\n");			
			
			out.write("fusion");
			for (int i = 1; i < sampleNames.length; i++) {
				if (fusion.containsKey(sampleNames[i])) {
					out.write("\t" + fusion.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
			
			out.write("snv_missense");
			for (int i = 1; i < sampleNames.length; i++) {
				if (snv_missense.containsKey(sampleNames[i])) {
					out.write("\t" + snv_missense.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
			
			out.write("snv_silent");
			for (int i = 1; i < sampleNames.length; i++) {
				if (snv_silent.containsKey(sampleNames[i])) {
					out.write("\t" + snv_silent.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
			
			out.write("snv_nonsense");
			for (int i = 1; i < sampleNames.length; i++) {
				if (snv_nonsense.containsKey(sampleNames[i])) {
					out.write("\t" + snv_nonsense.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
			
			out.write("snv_exon");
			for (int i = 1; i < sampleNames.length; i++) {
				if (snv_exon.containsKey(sampleNames[i])) {
					out.write("\t" + snv_exon.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("snv_splice");
			for (int i = 1; i < sampleNames.length; i++) {
				if (snv_splice.containsKey(sampleNames[i])) {
					out.write("\t" + snv_splice.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("snv_splice_region");
			for (int i = 1; i < sampleNames.length; i++) {
				if (snv_splice_region.containsKey(sampleNames[i])) {
					out.write("\t" + snv_splice_region.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("snv_UTR_3");
			for (int i = 1; i < sampleNames.length; i++) {
				if (snv_UTR_3.containsKey(sampleNames[i])) {
					out.write("\t" + snv_UTR_3.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("snv_UTR_5");
			for (int i = 1; i < sampleNames.length; i++) {
				if (snv_UTR_5.containsKey(sampleNames[i])) {
					out.write("\t" + snv_UTR_5.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("indel_proteinIns");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_proteinIns.containsKey(sampleNames[i])) {
					out.write("\t" + indel_proteinIns.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("indel_proteinDel");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_proteinDel.containsKey(sampleNames[i])) {
					out.write("\t" + indel_proteinDel.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("indel_exon");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_exon.containsKey(sampleNames[i])) {
					out.write("\t" + indel_exon.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("indel_frameshift");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_frameshift.containsKey(sampleNames[i])) {
					out.write("\t" + indel_frameshift.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");

			out.write("indel_intron");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_intron.containsKey(sampleNames[i])) {
					out.write("\t" + indel_intron.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
					
			out.write("indel_splice");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_splice.containsKey(sampleNames[i])) {
					out.write("\t" + indel_splice.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
			
			out.write("indel_splice_region");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_splice_region.containsKey(sampleNames[i])) {
					out.write("\t" + indel_splice_region.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
				
			out.write("indel_UTR_3");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_UTR_3.containsKey(sampleNames[i])) {
					out.write("\t" + indel_UTR_3.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
			
			out.write("indel_UTR_5");
			for (int i = 1; i < sampleNames.length; i++) {
				if (indel_UTR_5.containsKey(sampleNames[i])) {
					out.write("\t" + indel_UTR_5.get(sampleNames[i]));
				} else {
					out.write("\t" + 0);
				}
			}
			out.write("\n");
			
			

			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
