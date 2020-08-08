package stjude.projects.jinghuizhang.pcgpaltsplice.gtex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Calculate a median level quantification for each tissue sample group
 * @author tshaw
 *
 */
public class JinghuiZhangGTExExonMedianQuan {

	public static String description() {
		return "Calculate a median level quantification for each tissue sample group";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputGTExSampleRefFile] [inputCustomGeneIDInfo] [inputExonCountFile] [inputSampleTotalReads] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap sample2tissue = new HashMap();
			HashMap tissue2sampleName = new HashMap();
			HashMap tissue2exon_median = new HashMap();
			
			String inputGTExSampleRefFile = args[0]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\GTEx_metainfo.txt";
			String inputCustomGeneIDInfo = args[1]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\RNAseQC_forGTEx_gencode.v19.genes.v7.patched_contigs.exon.txt";
			String inputExonCountFile= args[2]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\GTEx_Analysis_2016-01-15_v7_RNASeQCv1.1.8_exon_reads.txt";
			String inputSampleTotalReads = args[3];
			String outputFile = args[4]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\Tissue_expr_median.txt";
			
			/*
			String inputGTExSampleRefFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\GTEx_metainfo.txt";
			String inputCustomGeneIDInfo = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\RNAseQC_forGTEx_gencode.v19.genes.v7.patched_contigs.exon.txt";
			String inputExonCountFile= "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\GTEx_Analysis_2016-01-15_v7_RNASeQCv1.1.8_exon_reads.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\Tissue_expr_median.txt";
			*/
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);		
			
			
			FileInputStream fstream = new FileInputStream(inputGTExSampleRefFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample2tissue.put(split[0], split[1]);										
				if (tissue2sampleName.containsKey(split[1])) {
					LinkedList list = (LinkedList)tissue2sampleName.get(split[1]);
					if (!list.contains(split[0])) {
						list.add(split[0]);
					}
					tissue2sampleName.put(split[1], list);
				} else {					
					LinkedList list = new LinkedList();
					list.add(split[0]);
					tissue2sampleName.put(split[1], list);										
				}
			}
			in.close();			

			// this was calculated from /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/references/gtf/hg19/fromGTEx_20190226_download/RNAseQC_forGTEx_gencode.v19.genes.v7.patched_contigs.exon.txt
			HashMap geneID2metaInfo = new HashMap();
			HashMap geneID2length = new HashMap();
			fstream = new FileInputStream(inputCustomGeneIDInfo);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double length = new Double(split[6]);
				geneID2metaInfo.put(split[0], str);	
				geneID2length.put(split[0], length);
			}
			in.close();			
			
			/////////////////////////////////////////////////////////////////////////////////
			HashMap sampleName2totalReads = new HashMap();
			fstream = new FileInputStream(inputSampleTotalReads);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sampleName2totalReads.put(split[0], new Double(split[1]));
			}
			in.close();
			
			//////////////////////////////////////////////////////////////////////////////////
			out.write("ExonID");
			Iterator itr = tissue2sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String tissue = (String)itr.next();
				out.write("\t" + tissue);
			}
			out.write("\tRNAseQC_ID\tgeneSymbol\tchr\tstart\tend\tdirection\texon_length\n");
			
			///////////////////////////////////////////////////////////////////////////////////
			fstream = new FileInputStream(inputExonCountFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
						
			String header = in.readLine();
			String[] split_header = header.split("\t");
			HashMap sample2index = new HashMap();
			for (int i = 0; i < split_header.length; i++) {
				sample2index.put(split_header[i], i);
			}
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (geneID2length.containsKey(split[0])) {
					out.write(split[0]);
					
					double exon_length = (Double)geneID2length.get(split[0]);
					itr = tissue2sampleName.keySet().iterator();
					while (itr.hasNext()) {
						String tissue = (String)itr.next();
						
						LinkedList sample_name_list = (LinkedList)tissue2sampleName.get(tissue);
						//double[] tissue_values = new double[sample_name_list.size()];
						LinkedList tissue_values_list = new LinkedList();
						int values_id = 0;
						Iterator itr2 = sample_name_list.iterator();
						while (itr2.hasNext()) {
							String sampleName = (String)itr2.next();
							if (sample2index.containsKey(sampleName)) {
								int index = (Integer)sample2index.get(sampleName);
								double count = new Double(split[index]);
								double total = (Double)sampleName2totalReads.get(sampleName);
								double fpkm_value = (((count + 0.0001) * 1000 * 1000000) / (exon_length * total));
								tissue_values_list.add(fpkm_value + "");
								//tissue_values[values_id] = fpkm_value;
								values_id++;
							}
						}
						double[] tissue_values = MathTools.convertListStr2Double(tissue_values_list);
						if (tissue_values.length > 0) {
							double tissue_median = MathTools.median(tissue_values);
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
