package stjude.projects.jinghuizhang.dexseq.exon.annotation.gtex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Compile the list of GTEx file that were used
 * \\gsc.stjude.org\project_space\zhanggrp\MethodDevelopment\common\ExonLevelQuantificationPipeline\Reference\ExonLevelGTF\GTEx_Annotation
 * @author tshaw
 *
 */
public class JinghuiZhangGenerateSampleInformationForGTEx {


	public static String description() {
		return "Generate the meta information file for our dexseq result. The program also summarizes the major categories for these samples.";
	}
	public static String type() {
		return "GTEx";
	}
	public static String parameter_info() {
		return "[inputGTExSampleInfo] [dexseq_folder_name] [outputSummaryFile] [outputMetaFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputGTExSampleInfo = args[0]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\GTEx_Annotation\\GTEX_meta_information_20191111.txt";			
			String dexseq_folder_name = args[1]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\GTEx_dexseq_count\\";
			String outputFile = args[2]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\GTEx_Annotation\\GTEx_processed_data_summary_sample_count.txt";
			String outputMetaFile = args[3]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\GTEx_Annotation\\GTEx_processed_data_meta_information.txt";
			
			HashMap biospecimen_repository_sample_id_map = new HashMap();
			HashMap allele_information_map = new HashMap();
			HashMap assay_type_map = new HashMap();
			HashMap analyte_type_map = new HashMap();
			HashMap gtex_sample_information = new HashMap();
			HashMap gap_accession_map = new HashMap();
			HashMap body_site_map = new HashMap();
			HashMap library_layout_map = new HashMap();
			HashMap histology_map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputGTExSampleInfo);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			//String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				//System.out.println(sampleName);
				String analyte_type = split[1]; // RNA DNA 
				String assay_type = split[2];
				String biospecimen_repository_sample_id = split[8];
				String body_site = split[9];
				String library_layout = split[21];
				String gap_accession = split[16];
				String histology = split[17];
				String allele_information = split[36].replaceAll("\n", "").replaceAll("\r", "");
				//System.out.println(allele_information);
				//System.out.println(library_layout);
				
				//System.out.println(histology);
				body_site = body_site.replaceAll("-", "_").replaceAll(" ", "_").replaceAll("\\(", "_").replaceAll("\\)", "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");		
				
				
				
				biospecimen_repository_sample_id_map.put(biospecimen_repository_sample_id, 0);
				body_site_map.put(body_site, 0);
				assay_type_map.put(assay_type,  0);
				analyte_type_map.put(analyte_type, 0);
				library_layout_map.put(library_layout, 0);
				histology_map.put(histology, 0);
				allele_information_map.put(allele_information, 0);
				gap_accession_map.put(gap_accession, 0);
				
				if (assay_type.equals("RNA-Seq")) {					
					String line = analyte_type + "\t" + biospecimen_repository_sample_id + "\t" + body_site + "\t" + histology + "\t" + assay_type + "\t" + gap_accession + "\t" + library_layout + "\t" + allele_information + "\n"; 
					gtex_sample_information.put(split[0], line);
				}
			}
			in.close();
			
			
			FileWriter fwriter_meta = new FileWriter(outputMetaFile);
			BufferedWriter out_meta = new BufferedWriter(fwriter_meta);			
			out_meta.write("sampleName\tanalyte_type\tbiospecimen_repository_sample_id\tbody_site\thistology\tassay_type\tgap_accession\tlibrary_layout\tallele_information\n");
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			int count_processed_samples = 0;
			File folder = new File(dexseq_folder_name);
			for (File file: folder.listFiles()) {
				if (file.getName().contains("_dexseq_count.txt")) {
					String sample_name = file.getName().replaceAll("_dexseq_count.txt", "");
					if (gtex_sample_information.containsKey(sample_name)) {
						String line = (String)gtex_sample_information.get(sample_name);
						String[] split_line = line.split("\t");
						count_processed_samples++;
						out_meta.write(sample_name + "\t" + line + "\n");;
						
						String analyte_type = split_line[0];
						String biospecimen_repository_sample_id = split_line[1];
						String body_site = split_line[2];
						String histology = split_line[3];
						String assay_type = split_line[4];
						String gap_accession = split_line[5];
						String library_layout = split_line[6];
						String allele_information = split_line[7].replaceAll("\n", "").replaceAll("\r", "");						
						Iterator itr = analyte_type_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(analyte_type)) {
								int count = (Integer)analyte_type_map.get(key);
								count++;
								analyte_type_map.put(key, count);
							}
						}
						itr = biospecimen_repository_sample_id_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(biospecimen_repository_sample_id)) {
								int count = (Integer)biospecimen_repository_sample_id_map.get(key);
								count++;
								biospecimen_repository_sample_id_map.put(key, count);
							}
						}
						
						itr = body_site_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(body_site)) {
								int count = (Integer)body_site_map.get(key);
								count++;
								body_site_map.put(key, count);
							}
						}
						itr = histology_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(histology)) {
								int count = (Integer)histology_map.get(key);
								count++;
								histology_map.put(key, count);
							}
						}
						
						itr = assay_type_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(assay_type)) {
								int count = (Integer)assay_type_map.get(key);
								count++;
								assay_type_map.put(key, count);
							}
						}
						

						itr = gap_accession_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(gap_accession)) {
								int count = (Integer)gap_accession_map.get(key);
								count++;
								gap_accession_map.put(key, count);
							}
						}
						itr = library_layout_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(library_layout)) {
								int count = (Integer)library_layout_map.get(key);
								count++;
								library_layout_map.put(key, count);
							}
						}
						
						itr = allele_information_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(allele_information)) {
								int count = (Integer)allele_information_map.get(key);
								count++;
								allele_information_map.put(key, count);
							}
						}
					}
				}				
			}
			
			System.out.println("Global_Summary\tNum samples processed\t" + count_processed_samples);
			out.write("Global_Summary\tNum samples processed\t" + count_processed_samples + "\n");
			
			int total_biospecimen_repository_sample_id_map_count = 0;
			Iterator itr = biospecimen_repository_sample_id_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)biospecimen_repository_sample_id_map.get(key);
				total_biospecimen_repository_sample_id_map_count += count;
				//System.out.println(key + ": " + count);
				//out.write("biospecimen_repository_sample_id\t" + key + "\t" + count + "\n");
			}
			System.out.println("Global_Summary\ttotal_biospecimen_repository_sample\t" + total_biospecimen_repository_sample_id_map_count);
			out.write("Global_Summary\ttotal_biospecimen_repository_sample\t" + total_biospecimen_repository_sample_id_map_count + "\n");
			
			int total_gap_accession_count = 0;
			itr = gap_accession_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)gap_accession_map.get(key);
				total_gap_accession_count += count;
				//System.out.println("gap_accession\t" + key + "\t" + count);
				//out.write("gap_accession\t" + key + "\t" + count + "\n");
			}

			System.out.println("Global_Summary\ttotal_gap_accession\t" + total_gap_accession_count);
			out.write("Global_Summary\ttotal_gap_accession\t" + total_gap_accession_count + "\n");
			
			itr = analyte_type_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)analyte_type_map.get(key);
				System.out.println(key + ": " + count);
				out.write("analyte_type\t" + key + "\t" + count + "\n");
			}
			


			itr = body_site_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)body_site_map.get(key);
				System.out.println("body_site\t" + key + "\t" + count);
				out.write("body_site\t" + key + "\t" + count + "\n");
			}
			

			itr = histology_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)histology_map.get(key);
				System.out.println("histology\t" + key + "\t" + count);
				out.write("histology\t" + key + "\t" + count + "\n");
			}
			

			itr = assay_type_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)assay_type_map.get(key);
				System.out.println("assay_type_map\t" + key + "\t" + count);
				out.write("assay_type_map\t" + key + "\t" + count + "\n");
			}


			itr = library_layout_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)library_layout_map.get(key);
				System.out.println("library_layout\t" + key + "\t" + count);
				out.write("library_layout\t" + key + "\t" + count + "\n");
			}
			
			itr = allele_information_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)allele_information_map.get(key);
				System.out.println("allele_information\t" + key + "\t" + count);
				out.write("allele_information\t" + key + "\t" + count + "\n");
			}
			
			
			out_meta.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	public static void main(String[] args) {
		
		try {
			HashMap biospecimen_repository_sample_id_map = new HashMap();
			HashMap allele_information_map = new HashMap();
			HashMap assay_type_map = new HashMap();
			HashMap analyte_type_map = new HashMap();
			HashMap gtex_sample_information = new HashMap();
			HashMap gap_accession_map = new HashMap();
			HashMap body_site_map = new HashMap();
			HashMap library_layout_map = new HashMap();
			HashMap histology_map = new HashMap();
			String inputGTExSampleInfo = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\GTEx_Annotation\\GTEX_meta_information_20191111.txt";
			FileInputStream fstream = new FileInputStream(inputGTExSampleInfo);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			//String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				//System.out.println(sampleName);
				String analyte_type = split[1]; // RNA DNA 
				String assay_type = split[2];
				String biospecimen_repository_sample_id = split[8];
				String body_site = split[9];
				String library_layout = split[21];
				String gap_accession = split[16];
				String histology = split[17];
				String allele_information = split[36].replaceAll("\n", "").replaceAll("\r", "");
				//System.out.println(allele_information);
				//System.out.println(library_layout);
				
				//System.out.println(histology);
				body_site = body_site.replaceAll("-", "_").replaceAll(" ", "_").replaceAll("\\(", "_").replaceAll("\\)", "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");		
				
				
				
				biospecimen_repository_sample_id_map.put(biospecimen_repository_sample_id, 0);
				body_site_map.put(body_site, 0);
				assay_type_map.put(assay_type,  0);
				analyte_type_map.put(analyte_type, 0);
				library_layout_map.put(library_layout, 0);
				histology_map.put(histology, 0);
				allele_information_map.put(allele_information, 0);
				gap_accession_map.put(gap_accession, 0);
				
				if (assay_type.equals("RNA-Seq")) {					
					String line = analyte_type + "\t" + biospecimen_repository_sample_id + "\t" + body_site + "\t" + histology + "\t" + assay_type + "\t" + gap_accession + "\t" + library_layout + "\t" + allele_information + "\n"; 
					gtex_sample_information.put(split[0], line);
				}
			}
			in.close();
			
			String outputMetaFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\GTEx_Annotation\\GTEx_processed_data_meta_information.txt";
			FileWriter fwriter_meta = new FileWriter(outputMetaFile);
			BufferedWriter out_meta = new BufferedWriter(fwriter_meta);			
			out_meta.write("sampleName\tanalyte_type\tbiospecimen_repository_sample_id\tbody_site\thistology\tassay_type\tgap_accession\tlibrary_layout\tallele_information\n");
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\GTEx_Annotation\\GTEx_processed_data_summary_sample_count.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			int count_processed_samples = 0;
			File folder = new File("\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\GTEx_dexseq_count\\");
			for (File file: folder.listFiles()) {
				if (file.getName().contains("_dexseq_count.txt")) {
					String sample_name = file.getName().replaceAll("_dexseq_count.txt", "");
					if (gtex_sample_information.containsKey(sample_name)) {
						String line = (String)gtex_sample_information.get(sample_name);
						String[] split_line = line.split("\t");
						count_processed_samples++;
						out_meta.write(sample_name + "\t" + line + "\n");;
						
						String analyte_type = split_line[0];
						String biospecimen_repository_sample_id = split_line[1];
						String body_site = split_line[2];
						String histology = split_line[3];
						String assay_type = split_line[4];
						String gap_accession = split_line[5];
						String library_layout = split_line[6];
						String allele_information = split_line[7].replaceAll("\n", "").replaceAll("\r", "");						
						Iterator itr = analyte_type_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(analyte_type)) {
								int count = (Integer)analyte_type_map.get(key);
								count++;
								analyte_type_map.put(key, count);
							}
						}
						itr = biospecimen_repository_sample_id_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(biospecimen_repository_sample_id)) {
								int count = (Integer)biospecimen_repository_sample_id_map.get(key);
								count++;
								biospecimen_repository_sample_id_map.put(key, count);
							}
						}
						
						itr = body_site_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(body_site)) {
								int count = (Integer)body_site_map.get(key);
								count++;
								body_site_map.put(key, count);
							}
						}
						itr = histology_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(histology)) {
								int count = (Integer)histology_map.get(key);
								count++;
								histology_map.put(key, count);
							}
						}
						
						itr = assay_type_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(assay_type)) {
								int count = (Integer)assay_type_map.get(key);
								count++;
								assay_type_map.put(key, count);
							}
						}
						

						itr = gap_accession_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(gap_accession)) {
								int count = (Integer)gap_accession_map.get(key);
								count++;
								gap_accession_map.put(key, count);
							}
						}
						itr = library_layout_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(library_layout)) {
								int count = (Integer)library_layout_map.get(key);
								count++;
								library_layout_map.put(key, count);
							}
						}
						
						itr = allele_information_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(allele_information)) {
								int count = (Integer)allele_information_map.get(key);
								count++;
								allele_information_map.put(key, count);
							}
						}
					}
				}
				
			}
			
			System.out.println("Global_Summary\tNum samples processed\t" + count_processed_samples);
			out.write("Global_Summary\tNum samples processed\t" + count_processed_samples + "\n");
			Iterator itr = analyte_type_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)analyte_type_map.get(key);
				System.out.println(key + ": " + count);
				out.write("analyte_type\t" + key + "\t" + count + "\n");
			}
			

			int total_biospecimen_repository_sample_id_map_count = 0;
			itr = biospecimen_repository_sample_id_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)biospecimen_repository_sample_id_map.get(key);
				total_biospecimen_repository_sample_id_map_count += count;
				//System.out.println(key + ": " + count);
				//out.write("biospecimen_repository_sample_id\t" + key + "\t" + count + "\n");
			}
			System.out.println("Global_Summary\ttotal_biospecimen_repository_sample\t" + total_biospecimen_repository_sample_id_map_count);
			out.write("Global_Summary\ttotal_biospecimen_repository_sample\t" + total_biospecimen_repository_sample_id_map_count + "\n");
			
			itr = body_site_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)body_site_map.get(key);
				System.out.println("body_site\t" + key + "\t" + count);
				out.write("body_site" + key + "\t" + count + "\n");
			}
			

			itr = histology_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)histology_map.get(key);
				System.out.println("histology\t" + key + "\t" + count);
				out.write("histology\t" + key + "\t" + count + "\n");
			}
			

			itr = assay_type_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)assay_type_map.get(key);
				System.out.println("assay_type_map\t" + key + "\t" + count);
				out.write("assay_type_map\t" + key + "\t" + count + "\n");
			}


			int total_gap_accession_count = 0;
			itr = gap_accession_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)gap_accession_map.get(key);
				total_gap_accession_count += count;
				//System.out.println("gap_accession\t" + key + "\t" + count);
				//out.write("gap_accession\t" + key + "\t" + count + "\n");
			}

			System.out.println("Global_Summary\ttotal_gap_accession\t" + total_gap_accession_count);
			out.write("Global_Summary\ttotal_gap_accession\t" + total_gap_accession_count + "\n");
			
			itr = library_layout_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)library_layout_map.get(key);
				System.out.println("library_layout\t" + key + "\t" + count);
				out.write("library_layout\t" + key + "\t" + count + "\n");
			}
			
			itr = allele_information_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)allele_information_map.get(key);
				System.out.println("allele_information\t" + key + "\t" + count);
				out.write("allele_information\t" + key + "\t" + count + "\n");
			}
			
			
			out_meta.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
