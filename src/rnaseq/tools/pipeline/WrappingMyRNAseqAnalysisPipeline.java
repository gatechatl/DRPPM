package rnaseq.tools.pipeline;

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

import mappingtools.Bam2Fastq;

/**
 * Generates the entire script for the RNAseq analysis pipeline.
 * 1. input either FASTQ or BAM files
 * 2. Bam files will be remapped with STAR
 * 3. 
 * @author gatechatl
 *
 */
public class WrappingMyRNAseqAnalysisPipeline {

	
	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Generates the entire script for the RNAseq analysis pipeline.";
	}
	public static String parameter_info() {
		return "[inputFileLst] [type: FASTQ, BAM] [remapping flag: true or false] [run time config file] [prefix for output folder] [outputShellscript]";
	}
	
	//chrNameLengthFile, String houseKeepingGenebed, String refseq_bed, String ribosome_bed
	private static String STAR_INDEX_DIR = "NA"; 
	private static String CHR_NAME_LENGTH_FILE = "NA";
	private static String RSEQC_HOUSE_KEEPING_GENE_BED = "NA";
	private static String RSEQC_REFSEQ_BED = "NA";
	private static String RSEQC_RIBOSOME_BED = "NA";
	public static void execute(String[] args) {
		
		try {
			String inputFileLst = args[0];
			String type = args[1]; // FASTQ, BAM, 
			boolean remapping = new Boolean(args[2]); // boolean to indicate whether to remap
			String runtime_config_file = args[3];			
			String outputFolder_prefix = args[4];
			String outputShellScript = args[5];
			String outputIntermediateFolder_prefix = "Intermediate";
			
			String current_working_dir = System.getProperty("user.dir");
			
			String outputFolder = current_working_dir + "/" + outputFolder_prefix;
			String outputIntermediateFolder = current_working_dir + "/" + outputIntermediateFolder_prefix;
			File outputFolder_f = new File(outputFolder);
			if (!outputFolder_f.exists()) {
				outputFolder_f.mkdir();
			}
			File outputIntermediateFolder_f = new File(outputIntermediateFolder);
			if (!outputIntermediateFolder_f.exists()) {
				outputIntermediateFolder_f.mkdir();
			}
			
			FileWriter fwriter = new FileWriter(outputShellScript);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			
			
			// parsing the runtime config file
			FileInputStream fstream = new FileInputStream(runtime_config_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.length() > 0) {
					if (!str.substring(0, 1).equals("#")) {
						String[] split = str.split("=");
												
						if (str.contains("=")) {
							
							split[0] = split[0].trim();
							split[1] = split[1].trim();
							if (split[0].equalsIgnoreCase("STAR_INDEX_DIR")) {
								STAR_INDEX_DIR = split[1];
							}
							if (split[0].equalsIgnoreCase("CHR_NAME_LENGTH_FILE")) {
								CHR_NAME_LENGTH_FILE = split[1];
							}
							if (split[0].equalsIgnoreCase("RSEQC_HOUSE_KEEPING_GENE_BED")) {
								RSEQC_HOUSE_KEEPING_GENE_BED = split[1];
							}
							if (split[0].equalsIgnoreCase("RSEQC_REFSEQ_BED")) {
								RSEQC_REFSEQ_BED = split[1];
							}
							if (split[0].equalsIgnoreCase("RSEQC_RIBOSOME_BED")) {
								RSEQC_RIBOSOME_BED = split[1];
							}
							
						}
					}
				}
			}
			in.close();
			
			// these are all the variables that we need to keep track from the input file
			LinkedList sampleName_linkedList = new LinkedList();
			HashMap fq1_path_map = new HashMap();
			HashMap fq2_path_map = new HashMap(); 
			HashMap bam_path_map = new HashMap();
			HashMap read_length_map = new HashMap();
			HashMap strand_direction_map = new HashMap();
			HashMap other_parameters_map = new HashMap();
			
			// check if it is a fastq input file
			if (type.equalsIgnoreCase("FASTQ")) {
				
				fstream = new FileInputStream(inputFileLst);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine().trim();
					if (str.length() > 0) {
						if (!str.substring(0, 1).equals("#")) {
							String[] split = str.split("\t");
							String sampleName = split[0];										
							String fq1_path = split[1];					
							String fq2_path = split[2];
							
							// make sure that the line is not empty
							if (!sampleName.equals("")) {
								int read_length = 101; // default set to 101
								if (split.length > 3) {
									read_length = new Integer(split[3]);
									
								}
								
								String strand_direction = "NA";
								if (split.length > 4) {
									strand_direction = split[4]; // forward reverse unstranded
								}
								if (split.length > 5) {
									String other_parameters = split[5];
								}
			
								sampleName_linkedList.add(sampleName);
								fq1_path_map.put(sampleName,  fq1_path);
								fq2_path_map.put(sampleName,  fq2_path);
								read_length_map.put(sampleName, read_length);
								strand_direction_map.put(sampleName, strand_direction);
								
								// generate output sample folder
								String sample_output_folder = outputFolder + "/" + sampleName;
								File sample_output_folder_f = new File(sample_output_folder);
								if (!sample_output_folder_f.exists()) {
									sample_output_folder_f.mkdir();
								}
								
								// generate output sample folder
								String sample_outputIntermediateFolder = outputIntermediateFolder + "/" + sampleName;
								File sample_outputIntermediateFolder_f = new File(sample_outputIntermediateFolder);
								if (!sample_outputIntermediateFolder_f.exists()) {
									sample_outputIntermediateFolder_f.mkdir();
								}
							}
						}
					}
				}
				LinkedList sampleNames_linkedList = new LinkedList();
				
				in.close();
				
			} else {
				// parse through the input fileList
				fstream = new FileInputStream(inputFileLst);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine().trim();
					if (str.length() > 0) {
						String[] split = str.split("\t");						
						if (!str.substring(0, 1).equals("#")) {
							String sampleName = split[0];
							
							if (!sampleName.equals("")) {
						
								String bam_file_path = split[1];
								
								int read_length = 101; // guessing 101
								if (split.length > 2) {
									read_length = new Integer(split[2]);
								}
								String strand_direction = "unstranded";
								if (split.length > 3) {
									strand_direction = split[3]; // forward reverse unstranded
								}
								String other_parameters = "NA";
								if (split.length > 4) {
									other_parameters = split[4];
								}
								
								sampleName_linkedList.add(sampleName);
								bam_path_map.put(sampleName, bam_file_path);
								read_length_map.put(sampleName, read_length);
								strand_direction_map.put(sampleName, strand_direction);
								// generate output sample folder
								String sample_output_folder = outputFolder + "/" + sampleName;
								File sample_output_folder_f = new File(sample_output_folder);
								if (!sample_output_folder_f.exists()) {
									sample_output_folder_f.mkdir();
								}
								
								// generate output sample folder
								String sample_outputIntermediateFolder = outputIntermediateFolder + "/" + sampleName;
								File sample_outputIntermediateFolder_f = new File(sample_outputIntermediateFolder);
								if (!sample_outputIntermediateFolder_f.exists()) {
									sample_outputIntermediateFolder_f.mkdir();
								}
							}
						}
					}
				}
				in.close();

			}

			// generate the bufferedstring
			HashMap string_buffer_map = new HashMap();
			Iterator itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				string_buffer_map.put(sampleName, new StringBuffer());
				
			}
			
			
			// perform the remapping with STAR
			if (remapping) {
				// generate bam2fastq
				
				itr = sampleName_linkedList.iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					if (bam_path_map.containsKey(sampleName)) {
						String bam_file = (String)bam_path_map.get(sampleName);
						
						String fastq_folder = outputFolder + "/" + sampleName + "/fastq";
						File fastq_folder_f = new File(fastq_folder);
						if (!fastq_folder_f.exists()) {
							fastq_folder_f.mkdir();
						}
						
						String fastq_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/fastq";
						File fastq_intermediate_folder_f = new File(fastq_intermediate_folder);
						if (!fastq_intermediate_folder_f.exists()) {
							fastq_intermediate_folder_f.mkdir();
						}
						
						
						String sampleName_bam_2fq_lst = outputIntermediateFolder + "/" + sampleName + "/fastq/" + sampleName + "_bam_file.lst";
						FileWriter fwriter_sampleName_bam_2fq_lst = new FileWriter(sampleName_bam_2fq_lst);
						BufferedWriter out_sampleName_bam_2fq_lst = new BufferedWriter(fwriter_sampleName_bam_2fq_lst);
						out_sampleName_bam_2fq_lst.write(sampleName + "\t" + bam_file + "\n");
						out_sampleName_bam_2fq_lst.close();
						
						//String bam2fastq_shell = outputIntermediateFolder + "/" + sampleName + "/fastq/bam2fastq.sh";
						//String[] bam2fastq_command = {sampleName_bam_2fq_lst, bam2fastq_shell};
						//Bam2Fastq.execute(bam2fastq_command);
						
						StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
						
						//out.write("## bam2fastq conversion ##\n");
						string_buffer.append("## bam2fastq conversion ##\n");
						//out.write("cd " + outputIntermediateFolder + "/" + sampleName + "/fastq/" + "\n");
						string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/fastq/" + "\n");
						//out.write("drppm -Bam2Fastq " + sampleName + "_bam_file.lst bam2fastq.sh" + "\n");
						string_buffer.append("drppm -Bam2Fastq " + sampleName + "_bam_file.lst bam2fastq.sh" + "\n");
						//out.write("sh bam2fastq.sh" + "\n");
						string_buffer.append("sh bam2fastq.sh" + "\n");
						//out.write("cd " + current_working_dir + "\n"); // change back to current directory
						string_buffer.append("cd " + current_working_dir + "\n");
						
						// move the fastq files from the intermediat folder to the final output fastq folder
	
						//out.write("mv " + outputIntermediateFolder + "/" + sampleName + "/fastq/" + sampleName + ".R1.fastq " + " " + fastq_folder + "\n");
						string_buffer.append("mv " + outputIntermediateFolder + "/" + sampleName + "/fastq/" + sampleName + ".R1.fastq " + " " + fastq_folder + "/\n");
						//out.write("mv " + outputIntermediateFolder + "/" + sampleName + "/fastq/" + sampleName + ".R2.fastq " + " " + fastq_folder + "\n");
						string_buffer.append("mv " + outputIntermediateFolder + "/" + sampleName + "/fastq/" + sampleName + ".R2.fastq " + " " + fastq_folder + "/\n");
						//out.write("## end bam2fastq conversion ##\n\n");
						string_buffer.append("## end bam2fastq conversion ##\n\n");
								
						string_buffer_map.put(sampleName, string_buffer);
						fq1_path_map.put(sampleName, fastq_folder + "/" + sampleName + ".R1.fastq");
						fq2_path_map.put(sampleName, fastq_folder + "/" + sampleName + ".R2.fastq");
					}
				}
				
			} 
			
			// perform fastqc on the fq files
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {			
				String sampleName = (String)itr.next();
				
				if (fq1_path_map.containsKey(sampleName)) {
					String qc_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/qc";
					File qc_intermediate_folder_f = new File(qc_intermediate_folder);
					if (!qc_intermediate_folder_f.exists()) {
						qc_intermediate_folder_f.mkdir();
					}
					String qc_folder = outputFolder + "/" + sampleName + "/qc";
					File qc_folder_f = new File(qc_folder);
					if (!qc_folder_f.exists()) {
						qc_folder_f.mkdir();
					}
					String fastqc_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/qc/fastqc";
					File fastqc_intermediate_folder_f = new File(fastqc_intermediate_folder);
					if (!fastqc_intermediate_folder_f.exists()) {
						fastqc_intermediate_folder_f.mkdir();
					}
					String fastqc_folder = outputFolder + "/" + sampleName + "/qc/fastqc";
					File fastqc_folder_f = new File(fastqc_folder);
					if (!fastqc_folder_f.exists()) {
						fastqc_folder_f.mkdir();
					}
					
					StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);					
					//out.write("## fastqc ##\n");
					string_buffer.append("## fastqc ##\n");
					if (fq1_path_map.containsKey(sampleName)) {
						String fq1_path = (String)fq1_path_map.get(sampleName);
						//out.write("fastqc " + fq1_path + " -o " + fastqc_intermediate_folder + "\n");
						string_buffer.append("fastqc " + fq1_path + " -o " + fastqc_folder + "\n");
					}
					if (fq2_path_map.containsKey(sampleName)) {
						String fq2_path = (String)fq2_path_map.get(sampleName);
						//out.write("fastqc " + fq2_path + " -o " + fastqc_intermediate_folder + "\n");
						string_buffer.append("fastqc " + fq2_path + " -o " + fastqc_folder + "\n");
					}
					//out.write("cp -r " + fastqc_intermediate_folder + " " + fastqc_folder + "\n");
					//string_buffer.append("cp -r " + fastqc_intermediate_folder + " " + fastqc_folder + "\n");
					//out.write("## end fastqc ##\n\n");
					string_buffer.append("## end fastqc ##\n\n");
					string_buffer_map.put(sampleName, string_buffer);
				}
			}
			// generate STAR script
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (fq1_path_map.containsKey(sampleName)) {
					String star_folder = outputFolder + "/" + sampleName + "/star";
					File star_folder_f = new File(star_folder);
					if (!star_folder_f.exists()) {
						star_folder_f.mkdir();
					}
		
					String star_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/star/";
					File star_intermediate_folder_f = new File(star_intermediate_folder);
					if (!star_intermediate_folder_f.exists()) {
						star_intermediate_folder_f.mkdir();
					}
		
					// generate sample_fastq_lst
					String fq1 = (String)fq1_path_map.get(sampleName);
					String fq2 = (String)fq2_path_map.get(sampleName);
		
					// generate fq mapping file.
					String sampleName_fq_star_lst = outputIntermediateFolder + "/" + sampleName + "/star/" + sampleName + "_star_file.lst";
					FileWriter fwriter_sampleName_fq_star_lst = new FileWriter(sampleName_fq_star_lst);
					BufferedWriter out_sampleName_fq_star_lst = new BufferedWriter(fwriter_sampleName_fq_star_lst);
					out_sampleName_fq_star_lst.write(fq1 + "\t" + fq2 + "\t" + sampleName + "\n");
					out_sampleName_fq_star_lst.close();
					
					StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
					//out.write("## STAR mapping ##\n");
					string_buffer.append("## STAR mapping ##\n");
					//out.write("cd " + current_working_dir + "\n"); // change to current directory
					//out.write("cd " + outputIntermediateFolder + "/" + sampleName + "/star/" + "\n");
					string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/star/" + "\n");
					//out.write("drppm -JinghuiZhangSTARMappingFromYaweiUpdated " + sampleName_fq_star_lst + " " + STAR_INDEX_DIR + " 4 step2_execute.sh false" + "\n");
					string_buffer.append("drppm -JinghuiZhangSTARMappingFromYaweiUpdated " + sampleName_fq_star_lst + " " + STAR_INDEX_DIR + " 4 step2_execute.sh false" + "\n");
					//out.write("sh step2_execute.sh\n");
					string_buffer.append("sh step2_execute.sh\n");
					//out.write("cd " + current_working_dir + "\n"); // change back to current directory
					string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/star/" + sampleName + "\n");
					string_buffer.append("samtools index " + sampleName + ".STAR.Aligned.sortedByCoord.out.bam" + "\n");
					//string_buffer.append("samtools index " + sampleName + ".STAR.Aligned.toTranscriptome.out.bam" + "\n");					
					string_buffer.append("cd " + current_working_dir + "\n");
					//out.write("cp -r " + outputIntermediateFolder + "/star/" + " " + outputFolder + "/star/\n");
					string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/star/" + sampleName + "/*"+ " " + outputFolder + "/" + sampleName + "/star/\n");
					
					//out.write("## End STAR Mapping ##\n\n");
					string_buffer.append("## End STAR Mapping ##\n\n");
					string_buffer_map.put(sampleName, string_buffer);	
								
					
					bam_path_map.put(sampleName, outputFolder + "/" + sampleName + "/star/" + sampleName + ".STAR.Aligned.sortedByCoord.out.bam");
				}			
			}			

			// generate script for RSEQC
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (bam_path_map.containsKey(sampleName)) {
					String bam_file_path = (String)bam_path_map.get(sampleName);
					String rseqc_folder = outputFolder + "/" + sampleName + "/rseqc";
					File rseqc_folder_f = new File(rseqc_folder);
					if (!rseqc_folder_f.exists()) {
						rseqc_folder_f.mkdir();
					}
		
					String rseqc_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/rseqc";
					File rseqc_intermediate_folder_f = new File(rseqc_intermediate_folder);
					if (!rseqc_intermediate_folder_f.exists()) {
						rseqc_intermediate_folder_f.mkdir();
					}
					
					StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
					//out.write("## RSEQC mapping ##\n");
					string_buffer.append("## RSEQC mapping ##\n");
					string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/rseqc/" + "\n");					
					string_buffer.append(rseqc_script_generation(sampleName, bam_file_path, CHR_NAME_LENGTH_FILE, RSEQC_HOUSE_KEEPING_GENE_BED, RSEQC_REFSEQ_BED, RSEQC_RIBOSOME_BED) + "\n");
					string_buffer.append("cd " + current_working_dir + "\n");
					string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/rseqc/" + " " + outputFolder + "/" + sampleName + "/rseqc/\n");
					string_buffer.append("## END RSEQC mapping ##\n\n");
					
				}
			}

			// finally write out the shell script
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				out.write("sh " + outputIntermediateFolder_prefix + "/" + sampleName + ".sh\n");
				StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
			
				FileWriter fwriter_sample_shell = new FileWriter(outputIntermediateFolder_prefix + "/" + sampleName + ".sh");
				BufferedWriter out_sample_shell = new BufferedWriter(fwriter_sample_shell);						
				out_sample_shell.write(string_buffer.toString());
				out_sample_shell.close();
			}
			out.close();
			
			
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}
	
	public static String rseqc_script_generation(String sampleName, String bam_file, String chrNameLengthFile, String houseKeepingGenebed, String refseq_bed, String ribosome_bed) {
		String script = "";
		script += "bam2wig.py -s " + chrNameLengthFile + " -i " + bam_file + " -o " + sampleName + "_bam2wig -u \n";
		script += "wigToBigWig " + sampleName + "_bam2wig.wig " + chrNameLengthFile + " " + sampleName + "_bam2wig.bw -clip \n";
		script += "geneBody_coverage.py -r " + houseKeepingGenebed + " -i " + bam_file + " -o " + sampleName + "_geneBody_coverage > " + sampleName + "_geneBody_coverage.txt\n";
		script += "bam_stat.py -i " + bam_file + " > rseqc_bam_stat_report.txt\n";
		script += "junction_annotation.py -i " + bam_file + " -o " + sampleName + "_junction_annotation -r " + refseq_bed + " > " + sampleName + "_junction_annotation_summary.txt\n";
		script += "junction_saturation.py -i " + bam_file + " -r " + refseq_bed + " -o " + sampleName + "_junction_saturation > " + sampleName + "_junction_saturation_summary.txt\n";
		script += "tin.py -i " + bam_file + " -r " + ribosome_bed + "\n";
		return script;
	}
			
}
