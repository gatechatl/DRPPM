package stjude.projects.jinghuizhang.pcgpaltsplice;

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
 * Generate a count file for PCGP
 * @author tshaw
 *
 */
public class JinghuiZhangCalculatePCGPExonCount {
	public static String description() {
		return "Generate FPKM table";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[input sampleFile list] [path of comprehensive folder] [inputCustomGeneIDInfo] [outputFile] [outputTotalFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String sampleFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\PCGP_sample.txt";
			String path = args[1]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\Comprehensive\\";
			String inputCustomGeneIDInfo = args[2]; //"ENSG00000115414.14_22";
			String outputFile = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\FN1_ENSG00000115414.14_22_FPKM.txt";
			String outputTotalFile = args[4];
			HashMap pass_cutoff = new HashMap();
			HashMap type_count = new HashMap();
			
			HashMap geneID2metaInfo = new HashMap();
			HashMap geneID2length = new HashMap();
			LinkedList geneIDorder = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputCustomGeneIDInfo);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double length = new Double(split[6]);
				geneID2metaInfo.put(split[0], str);	
				geneID2length.put(split[0], length);
				geneIDorder.add(split[0]);
			}
			in.close();			
			
			FileWriter fwriter_total = new FileWriter(outputTotalFile);
			BufferedWriter out_total = new BufferedWriter(fwriter_total);	
			out_total.write("SampleName\tTotalCount\n");

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write("ExonID");

			// read the sampleFile information
			
			LinkedList available_sample_list = new LinkedList();
			HashMap sample2exon = new HashMap();
			HashMap exon_info = new HashMap();
			fstream = new FileInputStream(sampleFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0].trim();
				String type = sampleName.split("0")[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
				
				String exonFile = path + "/" + sampleName + "/" + sampleName + "/" + sampleName + ".metrics.tmp.txt.intronReport.txt_exonOnly.txt";
				String rpkmFile = path + "/" + sampleName + "/" + "genes.rpkm.gct";
				//System.out.println(exonFile);
				double total = 0.0;
				double fn1_splice_count = 0.0;
				File f = new File(exonFile);
				if (f.exists()) {
					if (!available_sample_list.contains(sampleName)) {
						available_sample_list.add(sampleName);
						out.write("\t" + sampleName);
					}
					HashMap values = new HashMap();
					FileInputStream fstream2 = new FileInputStream(exonFile);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					header = in2.readLine();
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						double count = new Double(split2[3]);
						total += count;
						values.put(split2[0], count);
					}
					in2.close();
					
					
					Iterator itr2 = geneIDorder.iterator();
					while (itr2.hasNext()) {
						String exon_id = (String)itr2.next();
						double count = new Double(0.0);
						if (values.containsKey(exon_id)) {
							count = (Double)values.get(exon_id);
						}
						if (exon_info.containsKey(exon_id)) {
							StringBuffer buffer = (StringBuffer)exon_info.get(exon_id);
							buffer.append("\t" + count);
							exon_info.put(exon_id, buffer);
						} else {
							StringBuffer buffer = new StringBuffer();
							buffer.append(count);
							exon_info.put(exon_id, buffer);							
						}
					}
					
						/*if (sample2exon.containsKey(sampleName)) {
							//HashMap exon_map = (HashMap)sample2exon.get(sampleName);
							//exon_map.put(split2[0], count);
							sample2exon.put(sampleName, exon_map);							
						} else {
							//HashMap exon_map = new HashMap();
							//exon_map.put(split2[0], count);
							StringBuffer buffer = new StringBuffer();
							buffer.append(count);
							sample2exon.put(sampleName, );
						}*/

					
					
					out_total.write(sampleName + "\t" + total + "\n");
					out_total.flush();
				}
			}
			in.close();
			out_total.close();
			
			out.write("\tRNAseQC_ID\tgeneSymbol\tchr\tstart\tend\tdirection\texon_length\n");
			
			
			Iterator itr2 = geneIDorder.iterator();
			while (itr2.hasNext()) {
				String exon_id = (String)itr2.next();
				StringBuffer buffer = (StringBuffer)exon_info.get(exon_id);
				out.write(exon_id + "\t" + buffer.toString());
				String meta = (String)geneID2metaInfo.get(exon_id);
				out.write("\t" + meta + "\n");
				
				/*Iterator itr = available_sample_list.iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					out.write(exon_id);
					HashMap exon_map = (HashMap)sample2exon.get(sampleName);
					if (exon_map.containsKey(exon_id)) {
						out.write("\t" + exon_map.get(exon_id));
					} else {
						out.write("\t0.0");
					}
				
				}
				*/
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
