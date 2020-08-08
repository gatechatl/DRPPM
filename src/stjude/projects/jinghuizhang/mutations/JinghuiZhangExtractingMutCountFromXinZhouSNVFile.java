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
public class JinghuiZhangExtractingMutCountFromXinZhouSNVFile {

	public static String description() {
		return "Extract mutations from Xin Zhou's SNV file";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[vcf file] [outputFile_mutcount]";
	}
	public static void execute(String[] args) {
		
		try {
			boolean start_reading = false;
			String[] sample_info = {};
						
			String outputFile_mutcount = args[1];
			FileWriter fwriter_mutcount = new FileWriter(outputFile_mutcount);
			BufferedWriter out_mutcount = new BufferedWriter(fwriter_mutcount);
			out_mutcount.write("Sample\tPlatform\tCount");
			
			
			
			String outputFile_protein_altering_mutcount = args[2];
			FileWriter fwriter_protein_alt_mutcount = new FileWriter(outputFile_protein_altering_mutcount);
			BufferedWriter out_protein_alt_mutcount = new BufferedWriter(fwriter_protein_alt_mutcount);
			out_protein_alt_mutcount.write("Sample\tPlatform\tCount");
			
			
			HashMap mutcount = new HashMap();
			HashMap mutcount_protein_alt = new HashMap();
			
			String input_vcf_file = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\pediatric.hg19.vcf";
			FileInputStream fstream = new FileInputStream(input_vcf_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (start_reading) {
					//System.out.println(split[7]);
					
					String[] split_information = split[7].split("\\|");
					
					if (split_information.length > 3) {
						String gene = split_information[3];
					
						if (split_information.length > 11) {
							
					//		if (cancer_driver.containsKey(gene)) {
								//outGeneRaw.write(str + "\n");
								String type= split_information[1];
								String mut_genetic = split_information[10];
								String mut_protein = split_information[11];
								System.out.println(gene + "\t" + type + "\t" + mut_protein + "\t" + mut_genetic);								
								for (int i = 9; i < sample_info.length; i++) {
									if (split[i].contains("somatic")) {
										System.out.println(sample_info[i] + "\t" + split[i]);
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
										
										if (type.contains("missense_variant") || type.contains("frameshift_variant") || type.contains("protein_altering_variant") || type.contains("stop_gained") || type.contains("inframe_deletion") || type.contains("splice")) {
											if (mutcount_protein_alt.containsKey(sample_info[i])) {
												HashMap platforms = (HashMap)mutcount_protein_alt.get(sample_info[i]);
												if (platforms.containsKey(platform)) {
													int count = (Integer)platforms.get(platform);
													count++;
													platforms.put(platform, count);
													mutcount_protein_alt.put(sample_info[i], platforms);
												} else {												
													platforms.put(platform, 1);
													mutcount_protein_alt.put(sample_info[i], platforms);	
												}
											} else {
												HashMap platforms = new HashMap();
												platforms.put(platform, 1);
												mutcount_protein_alt.put(sample_info[i], platforms);
											}
										}
									}
						//		}
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
					//if (count > max) {
					//	max = count;
					//	max_platform = platform;
					out_mutcount.write(sampleName + "\t" + platform + "\t" + count + "\n");
					//}
				}											
			}
			out_mutcount.close();
			
			
			itr = mutcount_protein_alt.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				HashMap platforms = (HashMap)mutcount_protein_alt.get(sampleName);
				
				int max = 0;
				String max_platform = "";
				Iterator itr2 = platforms.keySet().iterator();
				while (itr2.hasNext()) {
					String platform = (String)itr2.next();
					int count = (Integer)platforms.get(platform);
					//if (count > max) {
					//	max = count;
					//	max_platform = platform;
					out_protein_alt_mutcount.write(sampleName + "\t" + platform + "\t" + count + "\n");
					//}
				}											
			}
			out_protein_alt_mutcount.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
