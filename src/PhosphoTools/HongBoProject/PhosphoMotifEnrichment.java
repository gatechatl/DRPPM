package PhosphoTools.HongBoProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import functional.pathway.enrichment.MapUtilsBig2Small;
import functional.pathway.enrichment.MapUtilsSmall2Big;
import functional.pathway.enrichment.OverRepresentationAnalysisFDR;
import MISC.CommandLine;
import Statistics.General.MathTools;

/**
 * 
 * @author tshaw
 *
 */
public class PhosphoMotifEnrichment {

	public static String type() {
		return "PHOSPHOMOTIF";
	}
	public static String description() {
		return "Calculate the motif enrichment for peptides";
	}
	public static String parameter_info() {
		return "[motifFile] [motifFasta] [backgroundFile] [backgroundFasta] [outputMotifEnrichment] [outputMotifNameEnrichment]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String motifFile = args[0];
			String motifFasta = args[1];
			String backgroundFile = args[2];
			String backgroundFasta = args[3];
			String outputMotifEnrichment = args[4];
			String outputMotifNameEnrichment = args[5];
			
			FileWriter fwriter_motif = new FileWriter(outputMotifEnrichment);
			BufferedWriter out_motif = new BufferedWriter(fwriter_motif);						
			//out_motif.write("ClusterName\tGeneSetName\tPval\tFoldEnrichment\tHit#\tGeneSet#\tModGene#\tTotal#\n");
			
			FileWriter fwriter_motifname = new FileWriter(outputMotifNameEnrichment);
			BufferedWriter out_motif_name = new BufferedWriter(fwriter_motifname);						
			//out_motifname.write("ClusterName\tGeneSetName\tPval\tFoldEnrichment\tHit#\tGeneSet#\tModGene#\tTotal#\n");
			
			HashMap motif_hit = new HashMap();
			HashMap motif_name_hit = new HashMap();
			HashMap motif_hit_protein = new HashMap();
			HashMap motif_name_hit_protein = new HashMap();
			HashMap motif_peptide = new HashMap();
			HashMap background_hit = new HashMap();
			HashMap background_name_hit = new HashMap();
			HashMap background_peptide = new HashMap();
			
			HashMap peptide2protein = new HashMap();
			
			FileInputStream fstream = new FileInputStream(motifFasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				str = str.split("\t")[0];
				if (!str.contains(">")) {
					motif_peptide.put(str, str);
				}				
			}
			in.close();
			
			String protein = "";
			fstream = new FileInputStream(backgroundFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (str.contains(">")) {
					protein = split[0].replaceAll(">", "") + "_" + split[1].replaceAll(",", "");
				} else {
					String peptide = split[1];
					peptide2protein.put(peptide, protein);
					background_peptide.put(peptide, peptide);
				}
				
			}
			in.close();
			
			fstream = new FileInputStream(motifFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String motif = split[4] + "_" + split[5];
				String motif_name = split[5];
				String peptide = split[3];
				String protein_name = (String)peptide2protein.get(peptide);
				//peptide = protein_name;
				if (motif_hit.containsKey(motif)) {
					LinkedList list = (LinkedList)motif_hit.get(motif);
					if (!list.contains(peptide)) {
						list.add(peptide);						
					}					
					motif_hit.put(motif, list);
					
					list = (LinkedList)motif_hit_protein.get(motif);
					if (!list.contains(protein_name)) {
						list.add(protein_name);						
					}
					motif_hit_protein.put(motif, list);
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(peptide)) {
						list.add(peptide);
					}
					motif_hit.put(motif, list);
					
					list = new LinkedList();
					if (!list.contains(protein_name)) {
						list.add(protein_name);
					}
					motif_hit_protein.put(motif, list);
				}
								
				if (motif_name_hit.containsKey(motif_name)) {
					LinkedList list = (LinkedList)motif_name_hit.get(motif_name);
					if (!list.contains(peptide)) {
						list.add(peptide);
					}
					motif_name_hit.put(motif_name, list);
					
					list = (LinkedList)motif_name_hit_protein.get(motif_name);
					if (!list.contains(protein_name)) {
						list.add(protein_name);						
					}
					motif_name_hit_protein.put(motif_name, list);
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(peptide)) {
						list.add(peptide);
					}
					motif_name_hit.put(motif_name, list);
					
					list = new LinkedList();
					if (!list.contains(protein_name)) {
						list.add(protein_name);
					}
					motif_name_hit_protein.put(motif_name, list);
				}								
			}
			in.close();
			
			
			fstream = new FileInputStream(backgroundFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String motif = split[4] + "_" + split[5];
				String motif_name = split[5];
				String peptide = split[3];
				//peptide = protein_name;
				
				if (background_hit.containsKey(motif)) {
					LinkedList list = (LinkedList)background_hit.get(motif);
					if (!list.contains(peptide)) {
						list.add(peptide);
					}
					background_hit.put(motif, list);					
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(peptide)) {
						list.add(peptide);
					}
					background_hit.put(motif, list);
				}
								
				if (background_name_hit.containsKey(motif_name)) {
					LinkedList list = (LinkedList)background_name_hit.get(motif_name);
					if (!list.contains(peptide)) {
						list.add(peptide);
					}
					background_name_hit.put(motif_name, list);					
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(peptide)) {
						list.add(peptide);
					}
					background_name_hit.put(motif_name, list);
				}								
			}
			in.close();
			
			HashMap motif_pvalue = new HashMap();
			HashMap motif_line = new HashMap();
			Iterator itr = background_hit.keySet().iterator();
			while (itr.hasNext()) {
				String motif = (String)itr.next();
				LinkedList background_list = (LinkedList)background_hit.get(motif);
				int a = 0;
				String motif_list_str = "";
				if (motif_hit.containsKey(motif)) {															
					LinkedList motif_list = (LinkedList)motif_hit.get(motif);
					LinkedList motif_list_protein = (LinkedList)motif_hit_protein.get(motif);
					motif_list_str = convertList2Str(motif_list_protein);
					a = motif_list.size();
				}
				int b = background_list.size() - a;
				int c = motif_peptide.size() - a;
				int d = background_peptide.size() - a - b - c;
				double pvalue = MathTools.fisherTest(a, b, c, d);
							
				double ratioA = new Double(a) / motif_peptide.size();
				double ratioB = new Double(background_list.size()) / background_peptide.size();
				double foldenrichment = ratioA / ratioB + 0.01;
				motif_pvalue.put(motif, pvalue);	
				String line = motifFile + "\t" + motif + "\t" + pvalue + "\t" + foldenrichment + "\t" + motif_list_str + "\t" + a + "\t" + motif_peptide.size() + "\t" + background_list.size() + "\t" + background_peptide.size();
				motif_line.put(motif, line);				
				//out_motif.write(line);
				
			}
			//motif_pvalue = (HashMap) MapUtilsBig2Small.sortByValue(motif_pvalue);
			motif_pvalue = (HashMap) MapUtilsSmall2Big.sortByValue(motif_pvalue);
			
			String buffer = UUID.randomUUID().toString();
			String buffer_output = buffer + "_output";
			FileWriter fwriter2 = new FileWriter(buffer);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			Iterator itr_list = motif_pvalue.keySet().iterator();
			while (itr_list.hasNext()) {
				String motif = (String)itr_list.next();
				double pvalue = (Double)motif_pvalue.get(motif);
				out2.write(pvalue + "\n");
			}
			out2.close();
			String script = OverRepresentationAnalysisFDR.generateFDRScript(buffer, buffer_output);
			CommandLine.writeFile(buffer + "pvalue.r", script);
			CommandLine.executeCommand("R --vanilla < " + buffer + "pvalue.r");
			
			LinkedList pvals = new LinkedList();
			fstream = new FileInputStream(buffer_output);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			HashMap motif_fdr = new HashMap();
			itr_list = motif_pvalue.keySet().iterator();
			while (itr_list.hasNext()) {
				String motif = (String)itr_list.next();			
				String str = in.readLine();
				motif_fdr.put(motif, str);
			}
			in.close();
			
			
			itr = motif_pvalue.keySet().iterator();
			while (itr.hasNext()) {
				String motif = (String)itr.next();
				String line = (String)motif_line.get(motif);
				String fdr = (String)motif_fdr.get(motif);
				out_motif.write(line + "\t" + fdr + "\n");
			}			
			out_motif.close();

			File f = new File(buffer);
			if (f.exists()) {
				f.delete();
			}
			f = new File(buffer + "pvalue.r");
			if (f.exists()) {
				f.delete();
			}
			f = new File(buffer_output);
			if (f.exists()) {
				f.delete();
			}
			
			
			
			HashMap motif_name_pvalue = new HashMap();
			HashMap motif_name_line = new HashMap();
			itr = background_name_hit.keySet().iterator();
			while (itr.hasNext()) {
				String motif = (String)itr.next();
				LinkedList background_list = (LinkedList)background_name_hit.get(motif);
				int a = 0;
				String motif_list_str = "";
				if (motif_name_hit.containsKey(motif)) {
					LinkedList motif_list = (LinkedList)motif_name_hit.get(motif);
					LinkedList motif_list_protein = (LinkedList)motif_name_hit_protein.get(motif);
					motif_list_str = convertList2Str(motif_list_protein);
					a = motif_list.size();
				}
				int b = background_list.size() - a;
				int c = motif_peptide.size() - a;
				int d = background_peptide.size() - a - b - c;
				double pvalue = MathTools.fisherTest(a, b, c, d);
				//motif_name_pvalue.put(motif, pvalue);
				double ratioA = new Double(a) / motif_peptide.size();
				double ratioB = new Double(background_list.size()) / background_peptide.size();
				double foldenrichment = ratioA / ratioB + 0.01;
				motif_name_pvalue.put(motif, pvalue);
				
				String line = motifFile + "\t" + motif + "\t" + pvalue + "\t" + foldenrichment + "\t" + motif_list_str + "\t" + a + "\t" + motif_peptide.size() + "\t" + background_list.size() + "\t" + background_peptide.size();
				motif_name_line.put(motif, line);
				//out_motif_name.write(line);				
			}
			motif_name_pvalue = (HashMap) MapUtilsSmall2Big.sortByValue(motif_name_pvalue);
			//motif_name_pvalue = (HashMap) MapUtilsBig2Small.sortByValue(motif_name_pvalue);
			
			
			buffer = UUID.randomUUID().toString();
			buffer_output = buffer + "_output";
			fwriter2 = new FileWriter(buffer);
			out2 = new BufferedWriter(fwriter2);
			
			itr_list = motif_name_pvalue.keySet().iterator();
			while (itr_list.hasNext()) {
				String motif = (String)itr_list.next();
				double pvalue = (Double)motif_name_pvalue.get(motif);
				out2.write(pvalue + "\n");
			}
			out2.close();
			
			script = OverRepresentationAnalysisFDR.generateFDRScript(buffer, buffer_output);
			CommandLine.writeFile(buffer + "pvalue.r", script);
			CommandLine.executeCommand("R --vanilla < " + buffer + "pvalue.r");
			
			pvals = new LinkedList();
			fstream = new FileInputStream(buffer_output);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			HashMap motif_name_fdr = new HashMap();
			
			itr_list = motif_name_pvalue.keySet().iterator();
			while (itr_list.hasNext()) {
				String motif = (String)itr_list.next();			
				String str = in.readLine();
				motif_name_fdr.put(motif, str);
			}
			in.close();
			
			itr = motif_name_pvalue.keySet().iterator();
			while (itr.hasNext()) {
				String motif = (String)itr.next();
				String line = (String)motif_name_line.get(motif);
				String fdr = (String)motif_name_fdr.get(motif);
				out_motif_name.write(line + "\t" + fdr + "\n");
			}			
			out_motif_name.close();
			
			//System.out.println(motif_name_line.size());
			f = new File(buffer);
			if (f.exists()) {
				f.delete();
			}
			f = new File(buffer + "pvalue.r");
			if (f.exists()) {
				f.delete();
			}
			f = new File(buffer_output);
			if (f.exists()) {
				f.delete();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String convertList2Str(LinkedList list) {
		String result = "";
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String protein = (String)itr.next();
			if (result.equals("")) {
				result = protein;
			} else {
				if (protein != null) {
					if (!protein.equals("")) {
						result += "," + protein;
					}
				}
			}
		}
		return result;
	}
}
