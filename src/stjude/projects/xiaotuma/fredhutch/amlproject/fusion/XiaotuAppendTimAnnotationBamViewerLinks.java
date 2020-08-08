package stjude.projects.xiaotuma.fredhutch.amlproject.fusion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class XiaotuAppendTimAnnotationBamViewerLinks {

	public static void main(String[] args) {
		try {
			
			String outputUpdatedCiceroFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\20200104\\fus_tim_updated_20200106.txt";			
			FileWriter fwriter = new FileWriter(outputUpdatedCiceroFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			HashMap known_aml_oncogene_map = new HashMap();
			HashMap known_aml_fusion_map = new HashMap();
			HashMap known_aml_major_fusion_map = new HashMap();
			HashMap putative_novel_fusion_map = new HashMap(); // these are putative candidates
			
			HashMap sample_major_fusion = new HashMap();
			HashMap sample_secondary_fusion = new HashMap();
			HashMap sample_novel_fusion = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Recurrent_Fusion_List_20200105.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String fusion = split[0];
				String geneA = fusion.split(";")[0];
				String geneB = fusion.split(";")[1];
				String type = fusion.split(";")[2];
				//String fusion_name = fusion.split(";")[3];
				int frequency = new Integer(split[1]);
				boolean known_aml_oncogene = new Boolean(split[2]); // this represent a hit on any of the genes that was present in Soheil's publication
				boolean known_aml_fusion = new Boolean(split[3]); // this represent a hit on genes that was present in Soheil's fusion list
				boolean known_aml_major_fusion = new Boolean(split[4]); // this represent a hit on a more refined list of major fusion genes putative subtypes
				boolean putative_novel_fusion = new Boolean(split[5]); // this present a list of putative novel drivers
				if (known_aml_oncogene) {
					known_aml_oncogene_map.put(fusion, fusion);
					if (!known_aml_major_fusion) {
						
						String[] samples = split[6].split(",");
						for (String sample: samples) {
							if (sample_secondary_fusion.containsKey(sample)) {						
								LinkedList list = (LinkedList)sample_secondary_fusion.get(sample);
								if (!list.contains(fusion)) {
									list.add(fusion);
								}
								sample_secondary_fusion.put(sample, list);
							} else {
								LinkedList list = new LinkedList();
								list.add(fusion);
								sample_secondary_fusion.put(sample, list);
							}
						}
					}
				}
				if (known_aml_fusion) {
					known_aml_fusion_map.put(fusion, fusion);
					
					if (!known_aml_major_fusion) {
						
						String[] samples = split[6].split(",");
						for (String sample: samples) {
							if (sample_secondary_fusion.containsKey(sample)) {						
								LinkedList list = (LinkedList)sample_secondary_fusion.get(sample);
								if (!list.contains(fusion)) {
									list.add(fusion);
								}
								sample_secondary_fusion.put(sample, list);
							} else {
								LinkedList list = new LinkedList();
								list.add(fusion);
								sample_secondary_fusion.put(sample, list);
							}
						}
					}
				}
				if (known_aml_major_fusion) {
					known_aml_major_fusion_map.put(fusion, fusion);
					
					// put samples
					String[] samples = split[6].split(",");
					for (String sample: samples) {
						if (sample_major_fusion.containsKey(sample)) {						
							LinkedList list = (LinkedList)sample_major_fusion.get(sample);
							if (!list.contains(fusion)) {
								list.add(fusion);
							}
							sample_major_fusion.put(sample, list);
						} else {
							LinkedList list = new LinkedList();
							list.add(fusion);
							sample_major_fusion.put(sample, list);
						}
					}
					
				}
				if (putative_novel_fusion) {
					putative_novel_fusion_map.put(fusion, fusion);
					
					String[] samples = split[6].split(",");
					for (String sample: samples) {
						if (sample_novel_fusion.containsKey(sample)) {						
							LinkedList list = (LinkedList)sample_novel_fusion.get(sample);
							if (!list.contains(fusion)) {
								list.add(fusion);
							}
							sample_novel_fusion.put(sample, list);
						} else {
							LinkedList list = new LinkedList();
							list.add(fusion);
							sample_novel_fusion.put(sample, list);
						}
					}
				}
				
			}
			in.close();
			
			//String inputFileCiceroOutput = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\20200104\\fus.txt";
			String inputFileCiceroOutput= "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\xiaotu_fus_review_01042020.txt";
			fstream = new FileInputStream(inputFileCiceroOutput);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			header = in.readLine();
			String[] split_header = header.split("\t");
			System.out.println(split_header.length);
			out.write(header + "\t" + "TimKnownAMLOncogene\tTimKnownFusionGene\tTimKnownMajorDriverFusion\tTimPutativeNovelFusion\tSampleMajorFusionSimplified\tSampleMajorFusion\tSampleSecondaryFusion\tSampleNovelFusion\tBamViewer_A\tBamViewer_B\n");
			System.out.println("Header: " + header);
			while (in.ready()) {
				String str = in.readLine();
				
				
				
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					out.write("\t" + split[i]);
				}
				
				
				for (int i = split.length; i < split_header.length; i++) {
					out.write("\tNA");
				}
			
				String sampleName = split[0];
				String chr_A = split[1];
				String pos_A = split[2];
				String chr_B = split[5];
				String pos_B = split[6];
				String rating = split[37];
				String geneA = split[18];
				String geneB = split[20];
				String type = split[9];
				String fusion = geneA + ";" + geneB + ";" + type + ";" + split[12];
				String simplified_fusion_name = split[12];
				if (known_aml_oncogene_map.containsKey(fusion)) {
					out.write("\t" + fusion);
				} else {
					out.write("\t" + "NA");
				}
				
				if (known_aml_fusion_map.containsKey(fusion)) {
					out.write("\t" + fusion);
				} else {
					out.write("\t" + "NA");
				}
				
				if (known_aml_major_fusion_map.containsKey(fusion)) {
					out.write("\t" + fusion);
				} else {
					out.write("\t" + "NA");
				}
				

				if (putative_novel_fusion_map.containsKey(fusion)) {
					out.write("\t" + fusion);
				} else {
					out.write("\t" + "NA");
				}
			
				String sample_major_fusion_simplified = "";
				String sample_major_fusion_str = "";
				if (sample_major_fusion.containsKey(sampleName)) {
					LinkedList list = (LinkedList)sample_major_fusion.get(sampleName);
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String major_fusion = (String)itr.next();
						String[] split_major_fusion = major_fusion.split(";");
						if (split_major_fusion.length > 3) {
							if (!sample_major_fusion_simplified.contains(split_major_fusion[3]) && !split_major_fusion[3].contains(",") && split_major_fusion[3].contains("_")) {
								sample_major_fusion_simplified += split_major_fusion[3] + ",";
							}
						}
						sample_major_fusion_str += major_fusion + ",";
					}
				}
				
				String sample_secondary_fusion_str = "";
				if (sample_secondary_fusion.containsKey(sampleName)) {
					LinkedList list = (LinkedList)sample_secondary_fusion.get(sampleName);
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String secondary_fusion = (String)itr.next();
						sample_secondary_fusion_str += secondary_fusion + ",";
					}
				}
				
				String sample_novel_fusion_str = "";
				if (sample_novel_fusion.containsKey(sampleName)) {
					LinkedList list = (LinkedList)sample_novel_fusion.get(sampleName);
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String secondary_fusion = (String)itr.next();
						sample_novel_fusion_str += secondary_fusion + ",";
					}
				}
				
				out.write("\t" + sample_major_fusion_simplified + "\t" + sample_major_fusion_str + "\t" + sample_secondary_fusion_str + "\t" + sample_novel_fusion_str);
				String bamviewer_link_A = "http://bamviewer-rt:8080/BAMViewer/aceview/splash?ref=hg19&region=" + chr_A + "&center=" + pos_A + "&fullPath=TRUE&tumorname=/rgs01/resgen/prod/tartan/index/data/XiaotuMa/AMLFredHutch/" + sampleName + "/TRANSCRIPTOME/bam/" + sampleName + ".bam";
				String bamviewer_link_B = "http://bamviewer-rt:8080/BAMViewer/aceview/splash?ref=hg19&region=" + chr_B + "&center=" + pos_B + "&fullPath=TRUE&tumorname=/rgs01/resgen/prod/tartan/index/data/XiaotuMa/AMLFredHutch/" + sampleName + "/TRANSCRIPTOME/bam/" + sampleName + ".bam";
				
				out.write("\t" + bamviewer_link_A);
				out.write("\t" + bamviewer_link_B);
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
