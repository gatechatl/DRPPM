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

public class JinghuiZhangGTExGenerateCategoryBarplot {

	public static String description() {
		return "Generate barplot";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputCutoffMatrix] [metaInfoFile] [keypercentileFile] [geneID] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap sample2tissue = new HashMap();
			HashMap tissue2sampleName = new HashMap();
			HashMap tissue2category = new HashMap();
			HashMap all_category = new HashMap();
			//String inputMatrix = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\	";
			String inputCutoffMatrix = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\ENSG00000115414.14_23_EB-D_exon.txt";
			String metaInfoFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\GTEx_metainfo.txt";
			//String 
			String keypercentileFile = args[2]; 
			String geneID = args[3];
			String outputFile = args[4]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\GTEx_FN1_ED-B_fpkm_boxplot.txt";			
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);		
			
			FileInputStream fstream = new FileInputStream(metaInfoFile);
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
			
			HashMap first_quartile_map = new HashMap();
			HashMap second_quartile_map = new HashMap();
			HashMap third_quartile_map = new HashMap();
			fstream = new FileInputStream(keypercentileFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				first_quartile_map.put(split[0], new Double(split[1]));
				second_quartile_map.put(split[0], new Double(split[2]));
				third_quartile_map.put(split[0], new Double(split[3]));
					
			}

			fstream = new FileInputStream(inputCutoffMatrix);
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

				if (split[0].equals(geneID)) {
					Iterator itr = tissue2sampleName.keySet().iterator();
					while (itr.hasNext()) {
						String tissue = (String)itr.next();
						
						if (first_quartile_map.containsKey(tissue)) {
							double first_quartile = (Double)first_quartile_map.get(tissue);
							double second_quartile = (Double)second_quartile_map.get(tissue);
							double third_quartile = (Double)third_quartile_map.get(tissue);
							
							LinkedList sample_name_list = (LinkedList)tissue2sampleName.get(tissue);
							//double[] tissue_values = new double[sample_name_list.size()];
							LinkedList tissue_values_list = new LinkedList();
							int values_id = 0;
							Iterator itr2 = sample_name_list.iterator();
							while (itr2.hasNext()) {
								String sampleName = (String)itr2.next();
								if (sample2index.containsKey(sampleName)) {
									int index = (Integer)sample2index.get(sampleName);
									
									String category_type = "NA";
									if (new Double(split[index]) < first_quartile) {
										category_type = "0";
									} else if (first_quartile <= new Double(split[index]) && new Double(split[index]) < second_quartile) {
										category_type = "1";
											
									} else if (second_quartile <= new Double(split[index]) && new Double(split[index]) < third_quartile) {
										category_type = "2";
									} else if (third_quartile <= new Double(split[index])) {
										category_type = "3";
									}
									all_category.put(category_type, category_type);
									if (tissue2category.containsKey(tissue)) {
										HashMap category = (HashMap)tissue2category.get(tissue);
										if (category.containsKey(category_type)) {
											double count = (Double)category.get(category_type);
											count++;
											category.put(category_type, count);
										} else {
											category.put(category_type, 1.0);
										}
										tissue2category.put(tissue, category);
									} else {
										HashMap category = new HashMap();
										category.put(category_type, 1.0);
										tissue2category.put(tissue, category);
									}
								}
							}
						}
					}
				}
			}
			in.close();
			
			out.write("Type");
			Iterator itr3 = all_category.keySet().iterator();
			while (itr3.hasNext()) {
				String category_type = (String)itr3.next();
				out.write("\t" + category_type);
			}
			out.write("\n");
			
			Iterator itr = tissue2category.keySet().iterator();
			while (itr.hasNext()) {
				String tissue = (String)itr.next();
				out.write(tissue);
				HashMap category = (HashMap)tissue2category.get(tissue);
				double total = 0.0;
				Iterator itr2 = all_category.keySet().iterator();
				while (itr2.hasNext()) {
					String category_type = (String)itr2.next();
					if (category.containsKey(category_type)) {
						double count = (Double)category.get(category_type);
						out.write("\t" + count);
					} else {
						out.write("\t" + 0.0);
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
