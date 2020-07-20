package stjude.projects.jinghuizhang.dexseq.exon.annotation.pcgptarget;

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

import statistics.general.MathTools;

/**
 * Calculate the exon expression median for each sample type.
 * Example: /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/Comprehensive_CAR-T_Analysis/hg38_analysis/AfterLiqingExonCounting
 * @author tshaw
 *
 */
public class JinghuiZhangCalculateSampleTypeExonExpressionMedianNewAnnot {
	public static String description() {
		return "Calculate the exon expression median for each sample type.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [inputSampleFile] [inputGeneIDInfo] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputMatrixFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\PCGP_sample.txt";
			String inputSampleFile = args[1]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\Comprehensive\\";
			String inputGeneIDInfo = args[2]; //"ENSG00000115414.14_22";
			String outputFile = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\FN1_ENSG00000115414.14_22_FPKM.txt";			
			HashMap pass_cutoff = new HashMap();
			HashMap type_count = new HashMap();
			
			HashMap geneID2metaInfo = new HashMap();

			FileInputStream fstream = new FileInputStream(inputGeneIDInfo);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				// ExonID  ExonName        Type    Chr     Start   End     Strand
				String ExonID = split[0];
				String ExonName = split[1];
				String Type = split[2];
				String Chr = split[3];
				String Start = split[4];
				String End = split[5];
				String Strand = split[6];
				//double length = new Double(split[6]);
				geneID2metaInfo.put(split[1], ExonID + "\t" + ExonName + "\t" + Type + "\t" + Chr + "\t" + Start + "\t" + End + "\t" + Strand);					
				
			}
			in.close();						
						
			HashMap sample2disease = new HashMap();
			HashMap disease = new HashMap();
			HashMap disease2sampleName = new HashMap();			
			
			fstream = new FileInputStream(inputSampleFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample2disease.put(split[0].split("-")[0], split[1]);
				disease.put(split[1], split[1]);
				if (disease2sampleName.containsKey(split[1])) {
					LinkedList list = (LinkedList)disease2sampleName.get(split[1]);
					list.add(split[0].split("-")[0]);
					disease2sampleName.put(split[1], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[0].split("-")[0]);
					disease2sampleName.put(split[1], list);					
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write("ExonID");

			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));

			header = in.readLine();
			String[] split_header = header.split("\t");
			HashMap sample2index = new HashMap();
			for (int i = 0; i < split_header.length; i++) {
				sample2index.put(split_header[i].split("-")[0], i);
			}
			out.write("ExonID");
			Iterator itr = disease.keySet().iterator();
			while (itr.hasNext()) {
				String tissue = (String)itr.next();
				out.write("\t" + tissue);
			}
			// ExonID  ExonName        Type    Chr     Start   End     Strand
			out.write("\tExonID\tExonName\tType\tChr\tStart\tEnd\tStrand\n");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (geneID2metaInfo.containsKey(split[0])) {
					out.write(split[0]);
					
					
					itr = disease.keySet().iterator();
					while (itr.hasNext()) {
						String tissue = (String)itr.next();
						
						LinkedList sample_name_list = (LinkedList)disease2sampleName.get(tissue);
						//double[] tissue_values = new double[sample_name_list.size()];
						LinkedList disease_values_list = new LinkedList();
						int values_id = 0;
						Iterator itr2 = sample_name_list.iterator();
						while (itr2.hasNext()) {
							String sampleName = (String)itr2.next();
							if (sample2index.containsKey(sampleName)) {
								int index = (Integer)sample2index.get(sampleName);
								double fpkm_value = new Double(split[index]);
								disease_values_list.add(fpkm_value + "");
								//tissue_values[values_id] = fpkm_value;
								values_id++;
							}
						}
						double[] disease_values = MathTools.convertListStr2Double(disease_values_list);
						if (disease_values.length > 0) {
							double tissue_median = MathTools.median(disease_values);
							out.write("\t" + tissue_median);
						} else {
							out.write("\t" + "NA");
						}
					}
					if (geneID2metaInfo.containsKey(split[0])) {
						out.write("\t" + geneID2metaInfo.get(split[0]));
					}
					out.write("\n");
				} else {
					System.out.println("The GTF length is missing geneID exon from the count file: " + split[0]);
				}
			}
			in.close();			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
