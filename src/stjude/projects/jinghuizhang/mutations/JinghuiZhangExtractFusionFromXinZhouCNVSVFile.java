package stjude.projects.jinghuizhang.mutations;
import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Previously Xin Zhou gave me the SNV file 
 * Z:\ResearchHome\ProjectSpace\zhanggrp\AltSpliceAtlas\common\analysis\PCGP_References\pediatric.hg19.vcf
 * @author tshaw
 *
 */
public class JinghuiZhangExtractFusionFromXinZhouCNVSVFile {

	public static String description() {
		return "Extract mutations from Xin Zhou's SNV file";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[cancer_driver gene list file] [Xin's cnv sv file] [GTFfile] [outputRaw] [outputResult]";
	}
	public static void execute(String[] args) {
		
		try {
			boolean start_reading = false;
			String[] sample_info = {};
			HashMap cancer_driver = new HashMap();
			String cancer_driver_file = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer142Genes_WithTimAppendedGenes.txt";
			FileInputStream fstream = new FileInputStream(cancer_driver_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				cancer_driver.put(split[0], split[0]);
			}
			in.close();
			
			HashMap gene_coord_map = new HashMap();
			String gtf_file = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer142Genes_WithTimAppendedGenes.txt";
			fstream = new FileInputStream(gtf_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 2).equals("##")) {									
					String[] split = str.split("\t");
					String chr = split[0];
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					String direction = split[6];
					String geneName = GTFFile.grabMeta(split[8], "gene_name");
					if (cancer_driver.containsKey(geneName)) {
						if (gene_coord_map.containsKey(geneName)) {
							String coord = (String)gene_coord_map.get(geneName);
							String prev_chr = coord.split("\t")[0];
							int prev_start = new Integer(coord.split("\t")[1]);
							int prev_end = new Integer(coord.split("\t")[2]);
							
							int new_start = prev_start;
							if (start < prev_start) {
								new_start = start;
							}
							int new_end = prev_end;
							if (end > prev_end) {
								new_end = end;
							}
							gene_coord_map.put(geneName, chr + "\t" + new_start + "\t" + new_end + "\t" + direction);
						} else {
							gene_coord_map.put(geneName, chr + "\t" + start + "\t" + end + "\t" + direction);
						}
					}
				}
			}
			in.close();
			
			String outputFile = args[4]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String outputFileGeneRaw = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample_Raw.txt";
			FileWriter fwriterGeneRaw = new FileWriter(outputFileGeneRaw);
			BufferedWriter outGeneRaw = new BufferedWriter(fwriterGeneRaw);
			
			String input_cnvsv_file = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\pediatric.hg19.vcf";
			fstream = new FileInputStream(input_cnvsv_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine(); // header
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String text = split[3].replaceAll(" ", "").replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "");
				String sampleName = "NA";
				String type = "NA";
				String geneA = "NA";
				String geneB = "NA";
				String internal_dup_gene = "NA";
				String vorigin = "NA";
				String pmid = "NA";
				String dna_assay = "NA";
				String project = "NA";
				String svtype = "NA";
				double value = Double.NaN;
				double segmean = Double.NaN;
				//String chrB = split[0];
				String chr_in_file = split[0];
				int loc1_in_file = new Integer(split[1]);
				int loc2_in_file = new Integer(split[2]);
				
				int other_loc1 = -1; 
				int other_loc2 = -1;				
				String other_chr = "NA";
				
				for (String split_text: text.split(",")) {					
					if (split_text.contains("sample:")) {
						sampleName = split_text.replaceAll("sample:", "").trim();
					}
					if (split_text.contains("dt:")) {
						type = split_text.replaceAll("dt:", "").trim();						
					}
					if (split_text.contains("geneA:")) {
						geneA = split_text.replaceAll("geneA:", "").trim();						
					}
					if (split_text.contains("geneB:")) {
						geneB = split_text.replaceAll("geneB:", "").trim();						
					}
					if (split_text.contains("vorigin:")) {
						vorigin = split_text.replaceAll("vorigin:", "").trim();						
					}
					if (split_text.contains("dna_assay:")) {
						dna_assay = split_text.replaceAll("dna_assay:", "").trim();						
					}
					if (split_text.contains("project:")) {
						project = split_text.replaceAll("project:", "").trim();						
					}
					if (split_text.contains("svtype:")) {
						svtype = split_text.replaceAll("svtype:", "").trim();						
					}
					if (split_text.contains("chrA:")) {
						other_chr = split_text.replaceAll("chrA:", "").trim();						
					}
					if (split_text.contains("chrB:")) {
						other_chr = split_text.replaceAll("chrB:", "").trim();						
					}
					if (split_text.contains("posA:")) {
						other_loc1 = new Integer(split_text.replaceAll("posA:", "").trim());
						other_loc2 = new Integer(split_text.replaceAll("posA:", "").trim());
					}
					if (split_text.contains("posB:")) {
						other_loc1 = new Integer(split_text.replaceAll("posB:", "").trim());
						other_loc2 = new Integer(split_text.replaceAll("posB:", "").trim());
					}
					if (split_text.contains("gene:")) {
						internal_dup_gene = split_text.replaceAll("gene:", "").trim();
					}
					if (split_text.contains("value:")) {
						value = new Double(split_text.replaceAll("value:", "").trim());
					}
					if (split_text.contains("segmean:")) {
						segmean = new Double(split_text.replaceAll("segmean:", "").trim());
					}
					
				}
				if (type.equals("2")) {
					outGeneRaw.write(str + "\n");		
					//if (cancer_driver.containsKey(geneA) || cancer_driver.containsKey(geneB)) {
						out.write(sampleName + "\t" + geneA + "\t" + geneB + "\t" + dna_assay + "\t" + vorigin + "\t" + svtype + "\t" + chr_in_file + "\t" + loc1_in_file + "\t" + loc2_in_file + "\t" + other_chr + "\t" + other_loc1 + "\t" + "RNA_Fusion\t" + type + "\t" + value + "\t" + project + "\n");
					//}
				}
				if (type.equals("5")) {
					outGeneRaw.write(str + "\n");
					
					/*if ((split[0] + "\t" + split[1] + "\t" + split[2]).equals("chr9\t133729522\t133729522")) {
						System.out.println(locB_1 + "\t" + locB_2);
						System.out.println(locA + "\t" + locA);
						System.out.println(sampleName + "\t" + geneA + "\t" + geneB + "\t" + dna_assay + "\t" + vorigin + "\t" + svtype + "\t" + chrB + "\t" + locB_1 + "\t" + locB_2 + "\t" + chrA + "\t" + locA + "\t" + "DNA_Fusion\t" + type + "\t" + value + "\t" + project + "\n");
					}*/
					
					Iterator itr = gene_coord_map.keySet().iterator();
					while (itr.hasNext()) {
						String geneName = (String)itr.next();
						
						String coord = (String)gene_coord_map.get(geneName);
						String chr = coord.split("\t")[0];
						int start = new Integer(coord.split("\t")[1]);
						int end = new Integer(coord.split("\t")[2]);
						if (chr.equals(chr_in_file)) {
							if (geneA.equals("NA")) {
								if (check_overlap(start, end, loc1_in_file, loc2_in_file)) {
									geneA = geneName;
								}
							}
							/*if ((split[0] + "\t" + split[1] + "\t" + split[2]).equals("chr9\t133729522\t133729522")) {
								if (geneName.equals("ABL1")) {
									System.out.println(geneB + "\t" + start + "\t" + end + "\t" + locB_1 + "\t" + locB_2);
									System.out.println(check_overlap(start, end, locB_1, locB_2));
								}
							}*/
						}
						if (chr.equals(other_chr)) {
							if (geneB.equals("NA")) {
								if (check_overlap(start, end, other_loc1, other_loc1)) {
									geneB = geneName;
								}
							}
							/*if ((split[0] + "\t" + split[1] + "\t" + split[2]).equals("chr9\t133729522\t133729522")) {
								if (geneName.equals("ABL1")) {
									System.out.println(geneA + "\t" + start + "\t" + end + "\t" + locA + "\t" + locA);
									System.out.println(check_overlap(start, end, locA, locA));
								}
							}*/
						}
					}
					/*if ((split[0] + "\t" + split[1] + "\t" + split[2]).equals("chr9\t133729522\t133729522")) {
						System.out.println(sampleName + "\t" + geneA + "\t" + geneB + "\t" + dna_assay + "\t" + vorigin + "\t" + svtype + "\t" + chrB + "\t" + locB_1 + "\t" + locB_2 + "\t" + chrA + "\t" + locA + "\t" + "DNA_Fusion\t" + type + "\t" + value + "\t" + project + "\n");
					}*/
					if (cancer_driver.containsKey(geneA) || cancer_driver.containsKey(geneB)) {
						out.write(sampleName + "\t" + geneA + "\t" + geneB + "\t" + dna_assay + "\t" + vorigin + "\t" + svtype + "\t" + chr_in_file + "\t" + loc1_in_file + "\t" + loc2_in_file + "\t" + other_chr + "\t" + other_loc1 + "\t" + "DNA_Fusion\t" + type + "\t" + value + "\t" + project + "\n");
					}
				}
				if (type.equals("6")) {
					if (cancer_driver.containsKey(internal_dup_gene)) {
						out.write(sampleName + "\t" + internal_dup_gene + "\t" + internal_dup_gene + "\t" + dna_assay + "\t" + vorigin + "\t" + svtype + "\t" + chr_in_file + "\t" + loc1_in_file + "\t" + loc2_in_file + "\t" + other_chr + "\t" + other_loc1 + "\t" + "InternalDups\t" + type + "\t" + value + "\t" + project + "\n");
					}
				}
				if (type.equals("4")) {
					if (value > 0.3 || value < -0.3) {
						outGeneRaw.write(str + "\n");
						LinkedList list = new LinkedList();
						Iterator itr = gene_coord_map.keySet().iterator();
						while (itr.hasNext()) {
							String geneName = (String)itr.next();
							
							String coord = (String)gene_coord_map.get(geneName);
							String chr = coord.split("\t")[0];
							if (chr_in_file.equals(chr)) {
								int start = new Integer(coord.split("\t")[1]);
								int end = new Integer(coord.split("\t")[2]);
								if (check_overlap(start, end, loc1_in_file, loc2_in_file)) {
									geneA += geneName + ",";
									geneB += geneName + ",";
									if (!list.contains(geneName)) {
										list.add(geneName);
									}
								}						
							}							
						}
						boolean found = false;
						String final_list = "";
						Iterator itr2 = list.iterator();
						while (itr2.hasNext()) {
							String geneName = (String)itr2.next();
							if (cancer_driver.containsKey(geneName)) {
								found = true;
								final_list += geneName + ",";
							}
						}
						if (found) {
							out.write(sampleName + "\t" + final_list + "\t" + final_list + "\t" + dna_assay + "\t" + vorigin + "\t" + svtype + "\t" + chr_in_file + "\t" + loc1_in_file + "\t" + loc2_in_file + "\t" + other_chr + "\t" + other_loc1 + "\t" + "CNV\t" + type + "\t" + value + "\t" + project + "\n");
						}
					}
				}
				if (type.equals("10")) {
					if (segmean > 0.16 || segmean < -0.16) {
						outGeneRaw.write(str + "\n");
						LinkedList list = new LinkedList();
						Iterator itr = gene_coord_map.keySet().iterator();
						while (itr.hasNext()) {
							String geneName = (String)itr.next();
							
							geneA = "";
							geneB = "";
							String coord = (String)gene_coord_map.get(geneName);
							String chr = coord.split("\t")[0];
							int start = new Integer(coord.split("\t")[1]);
							int end = new Integer(coord.split("\t")[2]);
							if (chr.equals(chr_in_file)) {
								if (check_overlap(start, end, loc1_in_file, loc2_in_file)) {
									geneA += geneName + ",";
									geneB += geneName + ",";
									if (!list.contains(geneName)) {
										list.add(geneName);
									}
								}
							}							
						}
						boolean found = false;
						String final_list = "";
						Iterator itr2 = list.iterator();
						while (itr2.hasNext()) {
							String geneName = (String)itr2.next();
							if (cancer_driver.containsKey(geneName)) {
								found = true;
								final_list += geneName + ",";
							}
						}
						if (found) {
							out.write(sampleName + "\t" + final_list + "\t" + final_list + "\t" + dna_assay + "\t" + vorigin + "\t" + svtype + "\t" + chr_in_file + "\t" + loc1_in_file + "\t" + loc2_in_file + "\t" + other_chr + "\t" + other_loc1 + "\t" + "LOH\t" + type + "\t" + segmean + "\t" + project + "\n");
						}
					}
				}
			}
			in.close();
			out.close();
			outGeneRaw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean check_overlap(int a1, int a2, int b1, int b2) {
		if (a1 <= b1 && b1 <= a2) {
			return true;
		}
		if (a1 <= b2 && b2 <= a2) {
			return true;
		}
		if (b1 <= a1 && a1 <= b2) {
			return true;
		}
		if (b1 <= a2 && a2 <= b2) {
			return true;
		}
		return false;
	}
}
