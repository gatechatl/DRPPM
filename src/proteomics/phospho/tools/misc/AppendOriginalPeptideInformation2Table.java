package proteomics.phospho.tools.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import proteomics.phospho.tools.motifs.MotifTools;

/**
 * Use the original peptide information to append the original peptide information
 * for each result
 * @author tshaw
 *
 */
public class AppendOriginalPeptideInformation2Table {

	public static void execute(String[] args) {
		try {
			String inputMotifFile = args[0];
			String orig_ascoreFile = args[1]; //
			String idsum_file = args[2];
			String outputFile = args[3]; // new motif file;
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			HashMap peptide2inference = peptide2inference(orig_ascoreFile);
			HashMap tag2score = idsum2JscoredJnQValue(idsum_file);
			HashMap tag2geneName = idsum2GeneName(idsum_file);
						
			FileInputStream fstream = new FileInputStream(inputMotifFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("Name")) {
					String kinase_sub_peptide = split[0];
					
					String peptide = kinase_sub_peptide.split("_")[kinase_sub_peptide.split("_").length - 1];
					String clean_peptide = MotifTools.cleanAll(peptide);
					String names_str = (String)peptide2inference.get(clean_peptide);
					String[] names = names_str.split(",");
					String geneName = "";
					String scores = "";
					
					for (String name: names) {
						
						if (tag2score.containsKey(name)) {
							geneName = (String)tag2geneName.get(name);
							if (scores.equals("")) {
								scores = (String)tag2score.get(name);
							} else {
								String new_score = "";
								String addmore = (String)tag2score.get(name);
								String[] split_scores_orig = scores.split("\t");
								String[] split_scoare_new = addmore.split("\t");
								for (int i = 0; i < split_scores_orig.length; i++) {
									if (i > 0) {
										new_score += "\t";
									}
									new_score += split_scores_orig[i] + "," + split_scoare_new[i];								
								}
								scores = new_score;
							}
						}
					}
					out.write(str + "\t" + geneName + "\t" + names_str + "\t" + scores + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap idsum2GeneName(String inputIDSumFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputIDSumFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split  = str.split("\t");
				if (split.length > 9) {
					String uniprot = split[2];
					String runNum = split[6];
					String scanNum = split[7];
					
					String gene_name = split[4];
					
					String uniq_name = uniprot + "." + runNum + "." + scanNum;
					map.put(uniq_name, gene_name);
				}
				
			}
			in.close();						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;		
	}
	public static HashMap idsum2JscoredJnQValue(String inputIDSumFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputIDSumFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split  = str.split("\t");
				if (split.length > 9) {
					String uniprot = split[2];
					String runNum = split[6];
					String scanNum = split[7];
					
					String gene_name = split[4];
					
					String mz = split[8];
					String z = split[9];
					String ppm = split[10];
					String jscore = split[11];
					String dJN = split[12];
					String qvalue = split[13];
					
					String uniq_name = uniprot + "." + runNum + "." + scanNum;
					//System.out.println(uniq_name);
					String stuff = mz + "\t" + z + "\t" + ppm + "\t" + jscore + "\t" + dJN + "\t" + qvalue;
					map.put(uniq_name, stuff);
				}
			}
			in.close();						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;		
	}
	public static HashMap peptide2inference(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split  = str.split("\t");
				String seq = MotifTools.cleanAll(split[3]);
				if (map.containsKey(seq)) {
					String name = (String)map.get(seq);
					name += "," + split[2];
					map.put(seq, name);
				} else {
					map.put(seq, split[2]);
				}
			}
			in.close();						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
