package stjude.projects.jinghuizhang.mutations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Previously Xin Zhou gave me the SNV file 
 * Z:\ResearchHome\ProjectSpace\zhanggrp\AltSpliceAtlas\common\analysis\PCGP_References\pediatric.hg19.vcf
 * @author tshaw
 *
 */
public class JinghuiZhangExtractingMutationsFromXinZhouSNVFile {

	public static String description() {
		return "Extract mutations from Xin Zhou's SNV file";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[cancer_driver gene list file] [vcf file] [outputRaw] [outputResult] [outputFile_mutcount]";
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
			
			String outputFile = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String outputFileGeneRaw = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample_Raw.txt";
			FileWriter fwriterGeneRaw = new FileWriter(outputFileGeneRaw);
			BufferedWriter outGeneRaw = new BufferedWriter(fwriterGeneRaw);
			
			String outputFile_mutcount = args[4];
			FileWriter fwriter_mutcount = new FileWriter(outputFile_mutcount);
			BufferedWriter out_mutcount = new BufferedWriter(fwriter_mutcount);
			out_mutcount.write("Sample\tPlatform\tCount");
			
			HashMap mutcount = new HashMap();
			
			String input_vcf_file = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\pediatric.hg19.vcf";
			fstream = new FileInputStream(input_vcf_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (start_reading) {
					//System.out.println(split[7]);
					
					String[] split_information = split[7].split("\\|");
					
					if (split_information.length > 3) {
						String gene = split_information[3];
						if (cancer_driver.containsKey(gene)) {
							outGeneRaw.write(str + "\n");
						}
					
						if (split_information.length > 11) {
							
							if (cancer_driver.containsKey(gene)) {
								//outGeneRaw.write(str + "\n");
								String type= split_information[1];
								String mut_genetic = split_information[10];
								String mut_protein = split_information[11];
								System.out.println(gene + "\t" + type + "\t" + mut_protein + "\t" + mut_genetic);
								out.write(">" + gene + "\t" + type + "\t" + split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4] + "\t" + split[5] + "\t" + split[6] + "\t" + mut_protein + "\t" + mut_genetic + "\n");
								for (int i = 9; i < sample_info.length; i++) {
									if (split[i].contains("somatic")) {
										System.out.println(sample_info[i] + "\t" + split[i]);
										out.write(sample_info[i] + "\t" + split[i] + "\n");
										String platform = "NA";
										if (split[i].contains(":wgs:")) {
											platform = "WGS";
										} else if (split[i].contains(":cgi:")) {
											platform = "CGI";
										} else if (split[i].contains(":wes:")) {
											platform = "WES";
										} else if (split[i].contains(":cc:")) {
											platform = "CC";
										} else {
											platform = "OTHER";
										}
										if (mutcount.containsKey(sample_info[i])) {
											HashMap platforms = (HashMap)mutcount.get(sample_info[i]);
											if (platforms.containsKey(platform)) {
												int count = (Integer)platforms.get(platform);
												count++;
												platforms.put(platform, count);
												mutcount.put(sample_info[i], platforms);
											} else {												
												platforms.put(platform, 1);
												mutcount.put(sample_info[i], platforms);	
											}
										} else {
											HashMap platforms = new HashMap();
											platforms.put(platform, 1);
											mutcount.put(sample_info[i], platforms);
										}
									}
								}
							}
						}
					}
				}
				
				if (split[0].equals("#CHROM")) {
					start_reading = true;
					sample_info = str.split("\t");
				}
				
				
			}
			in.close();
			out.close();
			outGeneRaw.close();
			
			Iterator itr = mutcount.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				HashMap platforms = (HashMap)mutcount.get(sampleName);
				
				int max = 0;
				String max_platform = "";
				Iterator itr2 = platforms.keySet().iterator();
				while (itr2.hasNext()) {
					String platform = (String)itr2.next();
					int count = (Integer)platforms.get(platform);
					if (count > max) {
						max = count;
						max_platform = platform;
					}
				}
				out_mutcount.write(sampleName + "\t" + max_platform + "\t" + max + "\n");								
			}
			out_mutcount.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
