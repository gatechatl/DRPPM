package stjude.projects.jinghuizhang.pcgpaltsplice;

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
 * Summarize the PCGP exon FPKM matrix based on disease type
 * @author tshaw
 *
 */
public class JinghuiZhangCalculatePCGPExonDiseaseType {
	public static String description() {
		return "Generate disease FPKM table";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[input matrix file] [input sample meta file] [inputCustomGeneIDInfo] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap sample2disease = new HashMap();
			HashMap disease = new HashMap();
			HashMap disease2sampleName = new HashMap();
			
			String inputMatrixFile = args[0];
			String inputSampleFile = args[1];
			String inputCustomGeneIDInfo = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			
			FileInputStream fstream = new FileInputStream(inputSampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample2disease.put(split[0], split[1]);
				disease.put(split[1], split[1]);
				if (disease2sampleName.containsKey(split[1])) {
					LinkedList list = (LinkedList)disease2sampleName.get(split[1]);
					list.add(split[0]);
					disease2sampleName.put(split[1], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[0]);
					disease2sampleName.put(split[1], list);					
				}
			}
			in.close();
			
			HashMap geneID2metaInfo = new HashMap();
			HashMap geneID2length = new HashMap();
			LinkedList geneIDorder = new LinkedList();
			
			fstream = new FileInputStream(inputCustomGeneIDInfo);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
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
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));

			header = in.readLine();
			String[] split_header = header.split("\t");
			HashMap sample2index = new HashMap();
			for (int i = 0; i < split_header.length; i++) {
				sample2index.put(split_header[i], i);
			}
			out.write("ExonID");
			Iterator itr = disease.keySet().iterator();
			while (itr.hasNext()) {
				String tissue = (String)itr.next();
				out.write("\t" + tissue);
			}
			out.write("\tRNAseQC_ID\tgeneSymbol\tchr\tstart\tend\tdirection\texon_length\n");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (geneID2length.containsKey(split[0])) {
					out.write(split[0]);
					
					double exon_length = (Double)geneID2length.get(split[0]);
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
