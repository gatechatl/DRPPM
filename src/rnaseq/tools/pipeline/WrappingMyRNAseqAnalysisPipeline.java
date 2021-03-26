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
import misc.CommandLine;

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
	private static String SPLICING_DEFICIENCY_CONFIG = "NA";
	private static String CHR_NAME_LENGTH_FILE = "NA";
	private static String RSEQC_HOUSE_KEEPING_GENE_BED = "NA";
	private static String RSEQC_REFSEQ_BED = "NA";
	private static String RSEQC_RIBOSOME_BED = "NA";
	private static String PRIMARY_GTF_REF = "NA";
	private static String PRIMARY_GTF_EXON_REF = "NA";
	private static String PRIMARY_GTF_EXON_LENGTH = "NA";
	private static String JUNCSALVAGER_GENELIST = "NA";
	private static String JUNCSALVAGER_PARAM = "NA";	
	private static String RNAEDITING_VARIANTS = "NA";
	private static String PRIMARY_FASTA = "NA";
	private static String OPTITYPE_PROGRAM = "NA";
	
	private static String OUTPUT_RSEQC_FILELST = "NA";
	private static String OUTPUT_HTSEQGENE_FILELST = "NA";
	private static String OUTPUT_HTSEQEXON_FILELST = "NA";
	
	private static boolean RSEQC_NOWIG = false;
	
	
	private static boolean IS_PAIRED = true;
	private static boolean SKIP_BAM2FASTQ = false;
	private static boolean SKIP_STAR = false;
	private static boolean SKIP_FASTQC = false;
	private static boolean SKIP_RSEQC = false;
	private static boolean SKIP_PSI_PSO_CALC = false;
	private static boolean SKIP_SPLICING_DEFICIENCY = false;
	private static boolean SKIP_HTSEQ_EXON_QUANT = false;
	private static boolean SKIP_HTSEQ_GENE = false;
	private static boolean SKIP_JUNCSALVAGER = false;
	private static boolean SKIP_RNAEDIT = false;
	private static boolean SKIP_KNOWNVARIANTS = false;
	private static boolean SKIP_RNAINDEL = false;
	private static boolean SKIP_OPTITYPE = false;
	
	
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
							split[1] = split[1].split("#")[0].trim();
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
							if (split[0].equalsIgnoreCase("SPLICING_DEFICIENCY_CONFIG")) {
								SPLICING_DEFICIENCY_CONFIG = split[1];
							}
							if (split[0].equalsIgnoreCase("PRIMARY_GTF_REF")) {
								PRIMARY_GTF_REF = split[1];
							}
							if (split[0].equalsIgnoreCase("PRIMARY_GTF_EXON_REF")) {
								PRIMARY_GTF_EXON_REF = split[1];
							}
							if (split[0].equalsIgnoreCase("PRIMARY_FASTA")) {
								PRIMARY_FASTA = split[1];
							}
							if (split[0].equalsIgnoreCase("JUNCSALVAGER_GENELIST")) {
								JUNCSALVAGER_GENELIST = split[1];
							}
							if (split[0].equalsIgnoreCase("PRIMARY_GTF_EXON_LENGTH")) {
								PRIMARY_GTF_EXON_LENGTH = split[1];
							}
							if (split[0].equalsIgnoreCase("RNAEDITING_VARIANTS")) {
								RNAEDITING_VARIANTS = split[1];
							}
							if (split[0].equalsIgnoreCase("OPTITYPE_PROGRAM")) {
								OPTITYPE_PROGRAM = split[1];
							}
							if (split[0].equalsIgnoreCase("RSEQC_NOWIG")) {
								RSEQC_NOWIG = new Boolean(split[1]);
							}
							
							if (split[0].equalsIgnoreCase("OUTPUT_RSEQC_FILELST")) {
								OUTPUT_RSEQC_FILELST = split[1];
							}
							
							if (split[0].equalsIgnoreCase("OUTPUT_HTSEQGENE_FILELST")) {
								OUTPUT_HTSEQGENE_FILELST = split[1];
							}
							
							if (split[0].equalsIgnoreCase("OUTPUT_HTSEQEXON_FILELST")) {
								OUTPUT_HTSEQEXON_FILELST = split[1];
							}
							
							if (split[0].equalsIgnoreCase("SKIP_BAM2FASTQ")) {
								SKIP_BAM2FASTQ = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_STAR")) {
								SKIP_STAR = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_FASTQC")) {
								SKIP_FASTQC = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_RSEQC")) {
								SKIP_RSEQC = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_PSI_PSO_CALC")) {
								SKIP_PSI_PSO_CALC = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_SPLICING_DEFICIENCY")) {
								SKIP_SPLICING_DEFICIENCY = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_HTSEQ_EXON_QUANT")) {
								SKIP_HTSEQ_EXON_QUANT = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_HTSEQ_GENE")) {
								SKIP_HTSEQ_GENE = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_JUNCSALVAGER")) {
								SKIP_JUNCSALVAGER = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_OPTITYPE")) {
								SKIP_OPTITYPE = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_RNAEDIT")) {
								SKIP_RNAEDIT = new Boolean(split[1]);
							}
							
							
						}
					}
				}
			}
			in.close();

			
			FileWriter fwriter_OUTPUT_HTSEQEXON_FILELST = new FileWriter(OUTPUT_HTSEQEXON_FILELST);
			BufferedWriter out_OUTPUT_HTSEQEXON_FILELST = new BufferedWriter(fwriter_OUTPUT_HTSEQEXON_FILELST);						

			FileWriter fwriter_OUTPUT_RSEQC_FILELST = new FileWriter(OUTPUT_RSEQC_FILELST);
			BufferedWriter out_OUTPUT_RSEQC_FILELST = new BufferedWriter(fwriter_OUTPUT_RSEQC_FILELST);						

			FileWriter fwriter_OUTPUT_HTSEQGENE_FILELST = new FileWriter(OUTPUT_HTSEQGENE_FILELST);
			BufferedWriter out_OUTPUT_HTSEQGENE_FILELST = new BufferedWriter(fwriter_OUTPUT_HTSEQGENE_FILELST);											
			
			// need to add code to check for whether all the files are present
			
			
			// these are all the variables that we need to keep track from the input file
			LinkedList sampleName_linkedList = new LinkedList();
			HashMap fq1_path_map = new HashMap();
			HashMap fq2_path_map = new HashMap(); 
			HashMap bam_path_map = new HashMap();
			HashMap sj_path_map = new HashMap(); // assume that STAR was used for the mapping
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
								//bam_path_map.put(sampleName, bam_file_path);

								if ((new File(bam_file_path)).exists()) {
									bam_path_map.put(sampleName, bam_file_path);
								} else {
									System.out.println("*Warning* bam files are missing...");
									bam_path_map.put(sampleName, "NA");
								}
								
								if ((new File(bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"))).exists()) {
									sj_path_map.put(sampleName, bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"));
								} else {
									System.out.println("*Warning* STAR junctions are missing...");
									sj_path_map.put(sampleName, "NA");
								}
								
								
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
						
						if (!SKIP_BAM2FASTQ) {
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
						}
						fq1_path_map.put(sampleName, fastq_folder + "/" + sampleName + ".R1.fastq");
						fq2_path_map.put(sampleName, fastq_folder + "/" + sampleName + ".R2.fastq");
					}
				}
				
			} else {
				
				itr = sampleName_linkedList.iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					String fastq_folder = outputFolder + "/" + sampleName + "/fastq";
					if (!fq1_path_map.containsKey(sampleName)) {
						if (new File(fastq_folder + "/" + sampleName + ".R1.fastq").exists()) {
							fq1_path_map.put(sampleName, fastq_folder + "/" + sampleName + ".R1.fastq");
						}
					}
					if (!fq2_path_map.containsKey(sampleName)) {
						if (new File(fastq_folder + "/" + sampleName + ".R2.fastq").exists()) {
							fq2_path_map.put(sampleName, fastq_folder + "/" + sampleName + ".R2.fastq");
						}
					}
				}
			}
			
			// perform fastqc on the fq files
		
			if (type.equalsIgnoreCase("FASTQ") || remapping) {
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
						
						if (!SKIP_FASTQC) {
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
						
						
						if (!SKIP_STAR) {
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
						}
						String bam_file_path = outputFolder + "/" + sampleName + "/star/" + sampleName + ".STAR.Aligned.sortedByCoord.out.bam";
						if ((new File(bam_file_path)).exists() || remapping || type.equalsIgnoreCase("FASTQ")) {
							bam_path_map.put(sampleName, bam_file_path);
						} else {
							bam_path_map.put(sampleName, "NA");
						}
						if ((new File(bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"))).exists()  || remapping || type.equalsIgnoreCase("FASTQ")) {
							sj_path_map.put(sampleName, bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"));
						} else {						
							sj_path_map.put(sampleName, "NA");
						}
					}			
				}			
		
			} else { // generate a STAR like folder that includes the mapped bam files
				// we need to generate a folder and assumes the input is STAR input
				itr = sampleName_linkedList.iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					String bam_file = (String)bam_path_map.get(sampleName);
					
					String star_folder = outputFolder + "/" + sampleName + "/star";
					File star_folder_f = new File(star_folder);
					if (!star_folder_f.exists()) {
						star_folder_f.mkdir();
					}
					String bam_file_path = outputFolder + "/" + sampleName + "/star/" + sampleName + ".STAR.Aligned.sortedByCoord.out.bam";
					
					StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
					//string_buffer.append("## Soft linking the bam files to the output star folder ##\n");					
					//string_buffer.append("ln -s " + bam_file + " " + bam_file_path + "\n");
					if (!new File(bam_file_path).exists()) {					
						CommandLine.executeCommand("ln -s " + bam_file + " " + bam_file_path);
					}
					if (!new File(bam_file_path + ".bai").exists()) {
						if (new File(bam_file + ".bai").exists()) {
							CommandLine.executeCommand("ln -s " + bam_file + ".bai" + " " + bam_file_path + ".bai");
						} else {
							string_buffer.append("## generate samtools index of the bam file ##\n");
							string_buffer.append("samtools index " + bam_file_path + "\n");
							string_buffer.append("## END generating bam index ##\n\n");
						}
					}
					if ((new File(bam_file_path)).exists() || remapping || type.equalsIgnoreCase("FASTQ")) {
						bam_path_map.put(sampleName, bam_file_path);
					} else {
						bam_path_map.put(sampleName, "NA");
					}
					if ((new File(bam_file.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"))).exists()) {
						String orig_sj_tab = bam_file.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab");
						String new_sj_tab = bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab");						
						//string_buffer.append("ln -s " + orig_sj_tab + " " + new_sj_tab + "\n");
						//sj_path_map.put(sampleName, bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"));
						if (!new File(new_sj_tab).exists()) {					
							CommandLine.executeCommand("ln -s " + orig_sj_tab + " " + new_sj_tab);
						}
						
					} else {						
						sj_path_map.put(sampleName, "NA");
					}
					
					if ((new File(bam_file.replaceAll(".Aligned.sortedByCoord.out.bam", ".Log.final.out"))).exists()) {
						String orig_log_final_out = bam_file.replaceAll(".Aligned.sortedByCoord.out.bam", ".Log.final.out");
						String new_log_final_out = bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".Log.final.out");						
						//string_buffer.append("ln -s " + orig_sj_tab + " " + new_sj_tab + "\n");
						//sj_path_map.put(sampleName, bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"));
						if (!new File(new_log_final_out).exists()) {					
							CommandLine.executeCommand("ln -s " + orig_log_final_out + " " + new_log_final_out);
						}						
					}
					if ((new File(bam_file.replaceAll(".Aligned.sortedByCoord.out.bam", ".Chimeric.out.junction"))).exists()) {
						String orig_chimeric_tab = bam_file.replaceAll(".Aligned.sortedByCoord.out.bam", ".Chimeric.out.junction");
						String new_chimeric_tab = bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".Chimeric.out.junction");						
						//string_buffer.append("ln -s " + orig_sj_tab + " " + new_sj_tab + "\n");
						//sj_path_map.put(sampleName, bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"));
						if (!new File(new_chimeric_tab).exists()) {					
							CommandLine.executeCommand("ln -s " + orig_chimeric_tab + " " + new_chimeric_tab);
						}						
					}
					if ((new File(bam_file.replaceAll(".Aligned.sortedByCoord.out.bam", ".ReadsPerGene.out.tab"))).exists()) {
						String orig_readspergene_tab = bam_file.replaceAll(".Aligned.sortedByCoord.out.bam", ".ReadsPerGene.out.tab");
						String new_readspergene_tab = bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".ReadsPerGene.out.tab");						
						//string_buffer.append("ln -s " + orig_sj_tab + " " + new_sj_tab + "\n");
						//sj_path_map.put(sampleName, bam_file_path.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab"));
						if (!new File(new_readspergene_tab).exists()) {					
							CommandLine.executeCommand("ln -s " + orig_readspergene_tab + " " + new_readspergene_tab);
						}						
					}
				}
			}
			
			
			
			// check if we need to reindex the bam files ...
			
			
			// generate script for RSEQC
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (bam_path_map.containsKey(sampleName)) {
					String bam_file_path = (String)bam_path_map.get(sampleName);
					if ((new File(bam_file_path)).exists() || remapping || type.equalsIgnoreCase("FASTQ")) {
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
	
						if (!SKIP_RSEQC) {
							
							// need to add code to check for whether all the files are present
							
							StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
							//out.write("## RSEQC mapping ##\n");
							string_buffer.append("## RSEQC mapping ##\n");
							string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/rseqc/" + "\n");
							if (!RSEQC_NOWIG) {
								string_buffer.append("bam2wig.py -s " + CHR_NAME_LENGTH_FILE + " -i " + bam_file_path + " -o " + sampleName + "_bam2wig -u \n");
								string_buffer.append("wigToBigWig " + sampleName + "_bam2wig.wig " + CHR_NAME_LENGTH_FILE + " " + sampleName + "_bam2wig.bw -clip \n");
							}
							string_buffer.append("geneBody_coverage.py -r " + RSEQC_HOUSE_KEEPING_GENE_BED + " -i " + bam_file_path + " -o " + sampleName + "_geneBody_coverage > " + sampleName + "_geneBody_coverage.txt\n");
							string_buffer.append("bam_stat.py -i " + bam_file_path + " > rseqc_bam_stat_report.txt 2> rseqc_bam_stat_report_more.txt\n");
							string_buffer.append("junction_annotation.py -i " + bam_file_path + " -o " + sampleName + "_junction_annotation -r " + RSEQC_REFSEQ_BED + " > " + sampleName + "_junction_annotation_summary.txt 2> " + sampleName + "_junction_annotation_summary_more.txt\n");
							string_buffer.append("junction_saturation.py -i " + bam_file_path + " -r " + RSEQC_REFSEQ_BED + " -o " + sampleName + "_junction_saturation > " + sampleName + "_junction_saturation_summary.txt 2> " + sampleName + "_junction_saturation_summary_more.txt\n");
							string_buffer.append("tin.py -i " + bam_file_path + " -r " + RSEQC_RIBOSOME_BED + "\n");
							//string_buffer.append(rseqc_script_generation(sampleName, bam_file_path, CHR_NAME_LENGTH_FILE, RSEQC_HOUSE_KEEPING_GENE_BED, RSEQC_REFSEQ_BED, RSEQC_RIBOSOME_BED) + "\n");
							string_buffer.append("cd " + current_working_dir + "\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/rseqc/*" + " " + outputFolder + "/" + sampleName + "/rseqc/\n"); // need to specify the files to copy in the future...
							string_buffer.append("## END RSEQC mapping ##\n\n");
							
							string_buffer_map.put(sampleName, string_buffer);
						}
					} else {
						if (!SKIP_RSEQC) {
							System.out.println("BAM file for " + sampleName + " is missing... skipping RSEQC step...");
						}
					}
				}
			}

			// generate script for PSI_PSO calculation
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (sj_path_map.containsKey(sampleName)) {
					String sj_file_path = (String)sj_path_map.get(sampleName);
					
					if ((new File(sj_file_path)).exists() || remapping || type.equalsIgnoreCase("FASTQ") || !SKIP_STAR) {
						String psi_pso_folder = outputFolder + "/" + sampleName + "/psipso";
						File psi_pso_folder_f = new File(psi_pso_folder);
						if (!psi_pso_folder_f.exists()) {
							psi_pso_folder_f.mkdir();
						}
			
						String psi_pso_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/psipso";
						File psi_pso_intermediate_folder_f = new File(psi_pso_intermediate_folder);
						if (!psi_pso_intermediate_folder_f.exists()) {
							psi_pso_intermediate_folder_f.mkdir();
						}
						
						String sampleName_sj_lst = outputIntermediateFolder + "/" + sampleName + "/psipso/" + sampleName + ".SJ.file.lst";
						FileWriter fwriter_sampleName_sj_lst = new FileWriter(sampleName_sj_lst);
						BufferedWriter out_sampleName_sj_lst = new BufferedWriter(fwriter_sampleName_sj_lst);
						out_sampleName_sj_lst.write(sj_file_path + "\n");
						out_sampleName_sj_lst.close(); 
						
						if (!SKIP_PSI_PSO_CALC) {
							
							// need to add code to check for whether all the files are present
							
							StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
							string_buffer.append("## PSI PSO calculation ##\n");
							string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/psipso/" + "\n");					
							string_buffer.append("drppm -JuncSalvagerGeneratePSIScript " + sampleName_sj_lst + " " + PRIMARY_GTF_REF + " psi_pso_output step2_output_script.sh" + "\n");
							string_buffer.append("sh step2_output_script.sh" + "\n");
							string_buffer.append("cd " + current_working_dir + "\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/psipso/psi_pso_output/*" + " " + outputFolder + "/" + sampleName + "/psipso/\n"); // need to specify the files to copy in the future...
							string_buffer.append("## END PSI PSO calculation ##\n\n");
							string_buffer_map.put(sampleName, string_buffer);
						}
						

						
					} else {
						if (!SKIP_PSI_PSO_CALC) {
							System.out.println("SJ file for " + sampleName + " is missing... skipping the PSI PSO calculation step...");
						}
					}
					
				}
			}

			// generate script for Splicing Deficiency
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (bam_path_map.containsKey(sampleName)) {
					String bam_file_path = (String)bam_path_map.get(sampleName);
					
					if ((new File(bam_file_path)).exists() || remapping || type.equalsIgnoreCase("FASTQ") || !SKIP_STAR) {
						String splicingdeficiency_folder = outputFolder + "/" + sampleName + "/splicingdeficiency";
						File splicingdeficiency_folder_f = new File(splicingdeficiency_folder);
						if (!splicingdeficiency_folder_f.exists()) {
							splicingdeficiency_folder_f.mkdir();
						}
			
						String splicingdeficiency_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/splicingdeficiency";
						File splicingdeficiency_intermediate_folder_f = new File(splicingdeficiency_intermediate_folder);
						if (!splicingdeficiency_intermediate_folder_f.exists()) {
							splicingdeficiency_intermediate_folder_f.mkdir();
						}
						
					
	
						String sampleName_bam_lst = outputIntermediateFolder + "/" + sampleName + "/splicingdeficiency/" + sampleName + "_bam_file.lst";
						FileWriter fwriter_sampleName_bam_lst = new FileWriter(sampleName_bam_lst);
						BufferedWriter out_sampleName_bam_lst = new BufferedWriter(fwriter_sampleName_bam_lst);
						out_sampleName_bam_lst.write(sampleName + "\t" + bam_file_path + "\n");
						out_sampleName_bam_lst.close();
						
						if (!SKIP_SPLICING_DEFICIENCY) {
							
							// need to add code to check for whether all the files are present
							
							StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
							string_buffer.append("## Splicing Deficiency calculation ##\n");
							string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/splicingdeficiency/" + "\n");					
							string_buffer.append("drppm -IntronRetentionPipelineWrapper " + sampleName_bam_lst + " " + SPLICING_DEFICIENCY_CONFIG + " " + read_length_map.get(sampleName) + " " + sampleName + "\n");
							string_buffer.append("sh step1_" + sampleName + ".run_this_first.sh" + "\n");
							string_buffer.append("sh " + sampleName + ".sh" + "\n");
							string_buffer.append("drppm -CleanEnsemblGeneID2GeneName " + sampleName + ".STAR.Aligned.sortedByCoord.out.bam.bed_SD.txt " + PRIMARY_GTF_REF + " " + sampleName + ".STAR.Aligned.sortedByCoord.out.bam.bed_SD_geneName.txt" + "\n");
							string_buffer.append("cd " + current_working_dir + "\n");
							//string_buffer.append("rm -r " + outputIntermediateFolder + "/" + sampleName + "/splicingdeficiency/*bed\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/splicingdeficiency/*_intron_summary.txt" + " " + outputFolder + "/" + sampleName + "/splicingdeficiency/\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/splicingdeficiency/*_SD.txt" + " " + outputFolder + "/" + sampleName + "/splicingdeficiency/\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/splicingdeficiency/*_SD_geneName.txt" + " " + outputFolder + "/" + sampleName + "/splicingdeficiency/\n");
							string_buffer.append("## END Splicing Deficiency calculation ##\n\n");
							string_buffer_map.put(sampleName, string_buffer);
						}
					} else {
						if (!SKIP_SPLICING_DEFICIENCY) {
							System.out.println("BAM file for " + sampleName + " is missing... skipping the splicing deficiency calculation step...");
						}
					}
				}
			}
			
			
			// generate script for novel exon and novel alternative start site
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (sj_path_map.containsKey(sampleName)) {
					String sj_file_path = (String)sj_path_map.get(sampleName);
					String sj_bam_path = (String)bam_path_map.get(sampleName);
					if (((new File(sj_file_path)).exists() && (new File(sj_bam_path)).exists()) || remapping || type.equalsIgnoreCase("FASTQ") || !SKIP_STAR) {
						String juncsalvager_folder = outputFolder + "/" + sampleName + "/juncsalvager";
						File JuncSalvager_folder_f = new File(juncsalvager_folder);
						if (!JuncSalvager_folder_f.exists()) {
							JuncSalvager_folder_f.mkdir();
						}
			
						String juncsalvager_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/juncsalvager";
						File juncsalvager_intermediate_folder_f = new File(juncsalvager_intermediate_folder);
						if (!juncsalvager_intermediate_folder_f.exists()) {
							juncsalvager_intermediate_folder_f.mkdir();
						}
						
						String sampleName_juncsalvager_lst = outputIntermediateFolder + "/" + sampleName + "/juncsalvager/" + sampleName + ".juncsalvager.lst";
						FileWriter fwriter_sampleName_juncsalvager_lst = new FileWriter(sampleName_juncsalvager_lst);
						BufferedWriter out_sampleName_juncsalvager_lst = new BufferedWriter(fwriter_sampleName_juncsalvager_lst);
						out_sampleName_juncsalvager_lst.write(sampleName + "\t" + sj_bam_path + "\t" + sj_file_path + "\n");
						out_sampleName_juncsalvager_lst.close(); 
						
						if (!SKIP_JUNCSALVAGER) {
							
							// need to add code to check for whether all the files are present
							String juncsalvager_outputFolder = outputFolder + "/" + sampleName + "/juncsalvager/result";
							String juncsalvager_shellscript = outputIntermediateFolder + "/" + sampleName + "/juncsalvager/juncsalvager.sh";
							String juncsalvager_outputNovelFile = outputFolder + "/" + sampleName + "/juncsalvager/novel_exon.lst";
							String juncsalvager_AltSpliceFile = outputFolder + "/" + sampleName + "/juncsalvager/alternatie_start_site.lst";
							
							StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
							string_buffer.append("## Junc Salvager Pipeline ##\n");
							string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/juncsalvager/" + "\n");					
							string_buffer.append("drppm -JuncSalvagerPipeline " + sampleName_juncsalvager_lst + " " + JUNCSALVAGER_GENELIST + " " + PRIMARY_GTF_REF + " " + JUNCSALVAGER_PARAM + " " + juncsalvager_outputFolder + " " + juncsalvager_shellscript + " " + juncsalvager_outputNovelFile + " " + juncsalvager_AltSpliceFile + "\n");
							string_buffer.append("sh " + juncsalvager_shellscript + "\n");
							string_buffer.append("## END JuncSalvagerPipeline calculation ##\n\n");
							string_buffer_map.put(sampleName, string_buffer);
						}
					} else {
						if (!SKIP_JUNCSALVAGER) {
							System.out.println("junction SJ.tab file for " + sampleName + " is missing... skipping the juncsalvager step...");
						}
					}
				}
			}
	
			HashMap sampleName2htseqcount = new HashMap();
			
			// generate script for HTSEQ
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (bam_path_map.containsKey(sampleName)) {
					String bam_file_path = (String)bam_path_map.get(sampleName);
					
					if ((new File(bam_file_path)).exists() || remapping || type.equalsIgnoreCase("FASTQ")) {
						String htseq_gene_level_folder = outputFolder + "/" + sampleName + "/htseq_gene_level";
						File htseq_gene_level_folder_f = new File(htseq_gene_level_folder);
						if (!htseq_gene_level_folder_f.exists()) {
							htseq_gene_level_folder_f.mkdir();
						}
			
						String htseq_gene_level_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/htseq_gene_level";
						File htseq_gene_level_intermediate_folder_f = new File(htseq_gene_level_intermediate_folder);
						if (!htseq_gene_level_intermediate_folder_f.exists()) {
							htseq_gene_level_intermediate_folder_f.mkdir();
						}

						String sampleName_htseq_lst = outputIntermediateFolder + "/" + sampleName + "/htseq_gene_level/" + sampleName + ".htseq.lst";
						FileWriter fwriter_sampleName_htseq_lst = new FileWriter(sampleName_htseq_lst);
						BufferedWriter out_sampleName_htseq_lst = new BufferedWriter(fwriter_sampleName_htseq_lst);
						out_sampleName_htseq_lst.write(sampleName + ".htseq.rawcount");
						out_sampleName_htseq_lst.close(); 
						
						if (!SKIP_HTSEQ_GENE) {
							String strand_direction = (String)strand_direction_map.get(sampleName);
							String orientation = "no";
							if (strand_direction.equalsIgnoreCase("fr-firststrand")) {
								orientation = "yes";
							} else if (strand_direction.equalsIgnoreCase("fr-secondstrand")) {
								orientation = "reverse";
							}
							if (strand_direction.equalsIgnoreCase("yes")) {
								orientation = "yes";
							} else if (strand_direction.equalsIgnoreCase("reverse")) {
								orientation = "reverse";
							}
							
							StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
							string_buffer.append("## HTSEQ Gene Level ##\n");
							string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/htseq_gene_level/" + "\n");					
							string_buffer.append("htseq-count --quiet -f bam -r pos -a 0 -s " + orientation + " -m union -t exon " + bam_file_path + " " + PRIMARY_GTF_REF + " > counts." + sampleName + ".htseq.rawcount.txt\n");
							string_buffer.append("drppm -CombineHTSEQResultRaw " + sampleName + ".htseq.lst " + sampleName + ".htseq.count.txt\n");
							string_buffer.append("drppm -CombineHTSEQResultTotalFeatures " + sampleName + ".htseq.lst " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.cpm.txt\n");
							string_buffer.append("drppm -RPM2RPKMExon " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.cpm.txt " + sampleName + ".htseq.fpkm.txt\n");
							string_buffer.append("drppm -RPM2RPKMTranscript " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.count.txt " + sampleName + ".htseq.length.txt\n");
							string_buffer.append("drppm -RawCount2RPM " + sampleName + ".htseq.length.txt " + sampleName + ".htseq.tpm.txt\n");
							
							string_buffer.append("drppm -EnsemblGeneIDAppendAnnotation " + sampleName + ".htseq.count.txt " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.count.annotation.txt\n");
							string_buffer.append("drppm -EnsemblGeneIDAppendAnnotation " + sampleName + ".htseq.cpm.txt " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.cpm.annotation.txt\n");
							string_buffer.append("drppm -EnsemblGeneIDAppendAnnotation " + sampleName + ".htseq.tpm.txt " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.tpm.annotation.txt\n");
							string_buffer.append("drppm -EnsemblGeneIDAppendAnnotation " + sampleName + ".htseq.fpkm.txt " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.fpkm.annotation.txt\n");

							string_buffer.append("drppm -EnsemblGeneID2GeneName " + sampleName + ".htseq.count.txt " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.count.geneName.txt\n");
							string_buffer.append("drppm -EnsemblGeneID2GeneName " + sampleName + ".htseq.cpm.txt " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.cpm.geneName.txt\n");
							string_buffer.append("drppm -EnsemblGeneID2GeneName " + sampleName + ".htseq.tpm.txt " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.tpm.geneName.txt\n");
							string_buffer.append("drppm -EnsemblGeneID2GeneName " + sampleName + ".htseq.fpkm.txt " + PRIMARY_GTF_REF + " " + sampleName + ".htseq.fpkm.geneName.txt\n");
							string_buffer.append("drppm -MergeGeneNameMaxFast " + sampleName + ".htseq.fpkm.ganeName.txt " + sampleName + ".htseq.fpkm.geneName.max.txt\n");
							string_buffer.append("drppm -MergeGeneNameMaxFast " + sampleName + ".htseq.cpm.ganeName.txt " + sampleName + ".htseq.cpm.geneName.max.txt\n");
							string_buffer.append("drppm -MergeGeneNameMaxFast " + sampleName + ".htseq.tpm.ganeName.txt " + sampleName + ".htseq.tpm.geneName.max.txt\n");
							
							string_buffer.append("cd " + current_working_dir + "\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/htseq_gene_level/counts." + sampleName + ".htseq.rawcount.txt " + outputIntermediateFolder + "/" + sampleName + "/htseq_exon_level/\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/htseq_gene_level/" + sampleName + ".htseq.count.txt " + outputIntermediateFolder + "/" + sampleName + "/htseq_exon_level/\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/htseq_gene_level/*" + " " + outputFolder + "/" + sampleName + "/htseq_gene_level/\n");
							
							out_OUTPUT_HTSEQGENE_FILELST.write(sampleName + "\t" + outputFolder + "/" + sampleName + "/htseq_gene_level/" + sampleName + ".htseq.fpkm.txt" + "\t" + outputFolder + "/" + sampleName + "/htseq_gene_level/" + sampleName + ".htseq.rawcount.txt" + "\n");
							
							string_buffer.append("## END HTSEQ Gene Level  ##\n\n");
							string_buffer_map.put(sampleName, string_buffer);
						}
					} else {
						if (!SKIP_HTSEQ_GENE) {
							System.out.println("bam file for " + sampleName + " is missing... skipping the htseq gene pipeline...");
						}
					}
					
				}
			}
			

			
			// generate script for HTSEQ EXON
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (bam_path_map.containsKey(sampleName)) {
					String bam_file_path = (String)bam_path_map.get(sampleName);
					
					if ((new File(bam_file_path)).exists() || remapping || type.equalsIgnoreCase("FASTQ")) {
						String htseq_exon_level_folder = outputFolder + "/" + sampleName + "/htseq_exon_level";
						File htseq_exon_level_folder_f = new File(htseq_exon_level_folder);
						if (!htseq_exon_level_folder_f.exists()) {
							htseq_exon_level_folder_f.mkdir();
						}
			
						String htseq_exon_level_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/htseq_exon_level";
						File htseq_exon_level_intermediate_folder_f = new File(htseq_exon_level_intermediate_folder);
						if (!htseq_exon_level_intermediate_folder_f.exists()) {
							htseq_exon_level_intermediate_folder_f.mkdir();
						}

						String sampleName_htseq_lst = outputIntermediateFolder + "/" + sampleName + "/htseq_exon_level/" + sampleName + ".htseq.lst";
						FileWriter fwriter_sampleName_htseq_lst = new FileWriter(sampleName_htseq_lst);
						BufferedWriter out_sampleName_htseq_lst = new BufferedWriter(fwriter_sampleName_htseq_lst);
						out_sampleName_htseq_lst.write(sampleName + ".htseq.rawcount");
						out_sampleName_htseq_lst.close(); 
						
						String sampleName_exon_htseq_lst = outputIntermediateFolder + "/" + sampleName + "/htseq_exon_level/" + sampleName + ".exon.htseq.lst";
						FileWriter fwriter_exon_sampleName_htseq_lst = new FileWriter(sampleName_exon_htseq_lst);
						BufferedWriter out_exon_sampleName_htseq_lst = new BufferedWriter(fwriter_exon_sampleName_htseq_lst);
						out_exon_sampleName_htseq_lst.write(sampleName + ".exon.htseq.rawcount");
						out_exon_sampleName_htseq_lst.close(); 
						
						if (!SKIP_HTSEQ_EXON_QUANT) {
							String strand_direction = (String)strand_direction_map.get(sampleName);
							String orientation = "no";
							if (strand_direction.equalsIgnoreCase("fr-firststrand")) {
								orientation = "yes";
							} else if (strand_direction.equalsIgnoreCase("fr-secondstrand")) {
								orientation = "reverse";
							}
							if (strand_direction.equalsIgnoreCase("yes")) {
								orientation = "yes";
							} else if (strand_direction.equalsIgnoreCase("reverse")) {
								orientation = "reverse";
							}
							
							StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
							string_buffer.append("## HTSEQ Exon Quant ##\n");
							string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/htseq_exon_level/" + "\n");					
							string_buffer.append("htseq-count --quiet -f bam -r pos -a 0 -s " + orientation + " -m union -t exon --nonunique all " + bam_file_path + " " + PRIMARY_GTF_EXON_REF + " > counts." + sampleName + ".exon.htseq.rawcount.txt\n");
							
							File htseq_gene_file = new File(outputFolder + "/" + sampleName + "/htseq_gene_level/" + sampleName + ".htseq.count.txt");
							if (htseq_gene_file.exists()) {
								string_buffer.append("cp " + outputFolder + "/" + sampleName + "/htseq_gene_level/" + sampleName + ".htseq.count.txt " + sampleName + ".htseq.count.txt\n");
							} else {
								if (SKIP_HTSEQ_GENE) {
									string_buffer.append("htseq-count --quiet -f bam -r pos -a 0 -s " + orientation + " -m union -t exon " + bam_file_path + " " + PRIMARY_GTF_REF + " > counts." + sampleName + ".htseq.rawcount.txt\n");
									string_buffer.append("drppm -CombineHTSEQResultRaw " + sampleName + ".htseq.lst " + sampleName + ".htseq.count.txt\n");
								} else {								
									string_buffer.append("cp " + outputFolder + "/" + sampleName + "/htseq_gene_level/" + sampleName + ".htseq.count.txt " + sampleName + ".htseq.count.txt\n");
								}
							}
							
							string_buffer.append("drppm -CombineHTSEQResultRaw " + sampleName + ".exon.htseq.lst " + sampleName + ".exon.htseq.count.txt\n");
							string_buffer.append("drppm -RawExonCount2CPMProteinFeatures " + PRIMARY_GTF_REF + " " + sampleName + ".exon.htseq.count.txt " + sampleName + ".htseq.count.txt " + sampleName + ".exon.htseq.cpm.txt " + sampleName + ".htseq.total.count.txt \n");							
							
							string_buffer.append("drppm -RPM2RPKMWithLengthReference " + PRIMARY_GTF_EXON_LENGTH + " " + sampleName + ".exon.htseq.cpm.txt " + sampleName + ".exon.htseq.fpkm.txt 0 1\n");
							string_buffer.append("drppm -RPM2RPKMWithLengthReference " + PRIMARY_GTF_EXON_LENGTH + " " + sampleName + ".exon.htseq.count.txt " + sampleName + ".exon.htseq.length.txt 0 1\n");
							string_buffer.append("drppm -RawCount2RPM " + sampleName + ".exon.htseq.length.txt " + sampleName + ".exon.htseq.tpm.txt\n");							
							
							//string_buffer.append("drppm -CombineHTSEQResultTotalFeatures " + sampleName + ".exon.htseq.lst " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.cpm.txt\n");
							//string_buffer.append("drppm -RPM2RPKMExon " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.cpm.txt " + sampleName + ".exon.htseq.fpkm.txt\n");
							//string_buffer.append("drppm -RPM2RPKMTranscript " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.count.txt " + sampleName + ".exon.htseq.length.txt\n");
							//string_buffer.append("drppm -RawCount2RPM " + sampleName + ".exon.htseq.length.txt " + sampleName + ".exon.htseq.tpm.txt\n");
							
							//string_buffer.append("drppm -EnsemblGeneIDAppendAnnotation " + sampleName + ".exon.htseq.count.txt " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.count.annotation.txt\n");
							//string_buffer.append("drppm -EnsemblGeneIDAppendAnnotation " + sampleName + ".exon.htseq.cpm.txt " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.cpm.annotation.txt\n");
							//string_buffer.append("drppm -EnsemblGeneIDAppendAnnotation " + sampleName + ".exon.htseq.tpm.txt " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.tpm.annotation.txt\n");
							//string_buffer.append("drppm -EnsemblGeneIDAppendAnnotation " + sampleName + ".exon.htseq.fpkm.txt " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.fpkm.annotation.txt\n");

							//string_buffer.append("drppm -EnsemblGeneID2GeneName " + sampleName + ".exon.htseq.count.txt " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.count.geneName.txt\n");
							//string_buffer.append("drppm -EnsemblGeneID2GeneName " + sampleName + ".exon.htseq.cpm.txt " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.cpm.geneName.txt\n");
							//string_buffer.append("drppm -EnsemblGeneID2GeneName " + sampleName + ".exon.htseq.tpm.txt " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.tpm.geneName.txt\n");
							//string_buffer.append("drppm -EnsemblGeneID2GeneName " + sampleName + ".exon.htseq.fpkm.txt " + PRIMARY_GTF_EXON_REF + " " + sampleName + ".exon.htseq.fpkm.geneName.txt\n");
							//string_buffer.append("drppm -MergeGeneName " + sampleName + ".exon.htseq.fpkm.ganeName.txt MAX " + sampleName + ".exon.htseq.fpkm.geneName.max.txt\n");
							//string_buffer.append("drppm -MergeGeneName " + sampleName + ".exon.htseq.cpm.ganeName.txt MAX " + sampleName + ".exon.htseq.cpm.geneName.max.txt\n");
							//string_buffer.append("drppm -MergeGeneName " + sampleName + ".exon.htseq.tpm.ganeName.txt MAX " + sampleName + ".exon.htseq.tpm.geneName.max.txt\n");
							
							string_buffer.append("cd " + current_working_dir + "\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/htseq_exon_level/*" + " " + outputFolder + "/" + sampleName + "/htseq_exon_level/\n");
							
							out_OUTPUT_HTSEQEXON_FILELST.write(sampleName + "\t" + outputFolder + "/" + sampleName + "/htseq_exon_level/" + sampleName + ".exon.htseq.fpkm.txt" + "\t" + outputFolder + "/" + sampleName + "/htseq_exon_level/" + sampleName + ".htseq.total.count.txt" + "\n");
							
							string_buffer.append("## END HTSEQ Exon Quant ##\n\n");
							string_buffer_map.put(sampleName, string_buffer);
						}
					} else {
						if (!SKIP_HTSEQ_EXON_QUANT) {
							System.out.println("bam file for " + sampleName + " is missing... skipping the htseq exon pipeline...");
						}
					}
				}
			}

			
			
			// generate script for RNAEDITING variants
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (bam_path_map.containsKey(sampleName)) {
					String bam_file_path = (String)bam_path_map.get(sampleName);					
					if ((new File(bam_file_path)).exists() || remapping || type.equalsIgnoreCase("FASTQ")) {

						String rnaediting_folder = outputFolder + "/" + sampleName + "/rnaediting";
						File rnaediting_folder_f = new File(rnaediting_folder);
						if (!rnaediting_folder_f.exists()) {
							rnaediting_folder_f.mkdir();
						}
			
						String rnaediting_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/rnaediting";
						File rnaediting_intermediate_folder_f = new File(rnaediting_intermediate_folder);
						if (!rnaediting_intermediate_folder_f.exists()) {
							rnaediting_intermediate_folder_f.mkdir();
						}
						
						if (!SKIP_RNAEDIT) {
							String strand_direction = (String)strand_direction_map.get(sampleName);
							String orientation = "no";
							if (strand_direction.equalsIgnoreCase("fr-firststrand")) {
								orientation = "yes";
							} else if (strand_direction.equalsIgnoreCase("fr-secondstrand")) {
								orientation = "reverse";
							}
							if (strand_direction.equalsIgnoreCase("yes")) {
								orientation = "yes";
							} else if (strand_direction.equalsIgnoreCase("reverse")) {
								orientation = "reverse";
							}
							
							StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
							string_buffer.append("## RNAEditing Variants ##\n");
							string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/rnaediting/" + "\n");					
							string_buffer.append("bam-readcount -f " + PRIMARY_FASTA + " -l " + RNAEDITING_VARIANTS + " " + bam_file_path + " > " + sampleName + "_rnaediting_variants.txt\n");
							
							// need to add a step to summarize the variants
							
							// bam-readcount -f ~/References/genomic/hg38_release35_GRCh38.p13/FASTA/GRCh38.primary_assembly.genome.fa -l ~/References/genomic/rnaediting_references/pcgp_rnaediting_hg38_sites_updated.txt ../star/SJALCL014725_D1_PolyA/SJALCL014725_D1_PolyA.STAR.Aligned.sortedByCoord.out.bam
							
							string_buffer.append("cd " + current_working_dir + "\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/rnaediting/*" + " " + outputFolder + "/" + sampleName + "/rnaediting/\n");
							string_buffer.append("## END RNAEditing Variants ##\n\n");
							string_buffer_map.put(sampleName, string_buffer);
						}
					} else {
						if (!SKIP_RNAEDIT) {
							System.out.println("bam file for " + sampleName + " is missing... skipping the rnaedit pipeline...");
						}					
					}
				}
			}
			
			// Run Isotyping
			itr = sampleName_linkedList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (fq1_path_map.containsKey(sampleName)) {
					String fq1_file_path = (String)fq1_path_map.get(sampleName);
					if ((new File(fq1_file_path)).exists()) {
						String optitype_folder = outputFolder + "/" + sampleName + "/optitype";
						File optitype_folder_f = new File(optitype_folder);
						if (!optitype_folder_f.exists()) {
							optitype_folder_f.mkdir();
						}
			
						String optitype_intermediate_folder = outputIntermediateFolder + "/" + sampleName + "/optitype";
						File optitype_intermediate_folder_f = new File(optitype_intermediate_folder);
						if (!optitype_intermediate_folder_f.exists()) {
							optitype_intermediate_folder_f.mkdir();
						}
					

						if (!SKIP_OPTITYPE) {
							StringBuffer string_buffer = (StringBuffer)string_buffer_map.get(sampleName);
							string_buffer.append("## OPTITYPING ##\n");							
							string_buffer.append("cd " + outputIntermediateFolder + "/" + sampleName + "/optitype/" + "\n");
							String fq_files = (String)fq1_file_path;
							if (fq2_path_map.containsKey(sampleName)) {
								fq_files += " " + fq2_path_map.get(sampleName);
							}
							string_buffer.append("python " + OPTITYPE_PROGRAM + " -i " + fq_files + " --rna -v -o " + sampleName + ".optitype\n");
							string_buffer.append("cd " + current_working_dir + "\n");
							string_buffer.append("cp -r " + outputIntermediateFolder + "/" + sampleName + "/optitype/" + sampleName + ".optitype/*" + " " + outputFolder + "/" + sampleName + "/optitype/\n");							
							string_buffer.append("## END OPTITYPING ##\n\n");
							string_buffer_map.put(sampleName, string_buffer);
						}
					}
				}
			}
			
			// 

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
			
			out_OUTPUT_HTSEQEXON_FILELST.close();
			out_OUTPUT_HTSEQGENE_FILELST.close();
			out_OUTPUT_RSEQC_FILELST.close();
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}
	
	public static String rseqc_script_generation(String sampleName, String bam_file, String chrNameLengthFile, String houseKeepingGenebed, String refseq_bed, String ribosome_bed) {
		String script = "";
		script += "bam2wig.py -s " + chrNameLengthFile + " -i " + bam_file + " -o " + sampleName + "_bam2wig -u \n";
		script += "wigToBigWig " + sampleName + "_bam2wig.wig " + chrNameLengthFile + " " + sampleName + "_bam2wig.bw -clip \n";
		script += "geneBody_coverage.py -r " + houseKeepingGenebed + " -i " + bam_file + " -o " + sampleName + "_geneBody_coverage > " + sampleName + "_geneBody_coverage.txt\n";
		script += "bam_stat.py -i " + bam_file + " > rseqc_bam_stat_report.txt 2> rseqc_bam_stat_report_more.txt\n";
		script += "junction_annotation.py -i " + bam_file + " -o " + sampleName + "_junction_annotation -r " + refseq_bed + " > " + sampleName + "_junction_annotation_summary.txt 2> " + sampleName + "_junction_annotation_summary_more.txt\n";
		script += "junction_saturation.py -i " + bam_file + " -r " + refseq_bed + " -o " + sampleName + "_junction_saturation > " + sampleName + "_junction_saturation_summary.txt 2> " + sampleName + "_junction_saturation_summary_more.txt\n";
		script += "tin.py -i " + bam_file + " -r " + ribosome_bed + "\n";
		return script;
	}
			
}
