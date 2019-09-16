package stjude.projects.jinghuizhang.fusion2expression;

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
 * /rgs01/project_space/zhanggrp/PanTARGET/common/result/CNV
 * 
 * @author tshaw
 *
 */
public class JinghuiZhangDefineSCNA {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String chr = "1";
			String arm = "p";
			int start = 1;
			int end = 0;
			
			String comprehensive_summary_outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ComprehensiveCopyNumberChange\\TARGET\\Comprehensive_Summary_20190821.txt";
			FileWriter comp_fwriter = new FileWriter(comprehensive_summary_outputFile);
			BufferedWriter comp_out = new BufferedWriter(comp_fwriter);
			comp_out.write("Patient_Name\tcancer_type\tLogRatio>2.0\tLogRatio<2.0\n");
			String[] types = {"AML", "NBL", "OS", "WT", "BALL"};
			for (String type: types) {
				//String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ComprehensiveCopyNumberChange\\TARGET\\AML_SCNA_Summary_20190821.txt";
				String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ComprehensiveCopyNumberChange\\TARGET\\" + type + "_SCNA_Summary_20190821.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				
				String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\references\\genome_info\\hg19\\chromosome\\cytoBand.txt";
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					
					
					if (!split[0].equals(chr)) {
						String band_info = chr + "\t" + start + "\t" + end + "\tq_arm";		
						System.out.println(band_info);
						map.put(band_info, 0.0);
						start = 1;					
					}
					
					if (arm.equals("p") && split[3].substring(0,1).equals("q")) {
						String band_info = chr + "\t" + start + "\t" + end + "\tp_arm";
						System.out.println(band_info);		
						map.put(band_info, 0.0);
						
						start = end + 1;
					}
					end = new Integer(split[2]);
					arm = split[3].substring(0, 1);
					chr = split[0];
				}
				in.close();
				String band_info = chr + "\t" + start + "\t" + end + "\tp_arm";
				System.out.println(band_info);		
	
				//String folder = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\PanTARGET\\common\\result\\CNV\\AML";
				String folder = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\PanTARGET\\common\\result\\CNV\\" + type;
				File folder_file = new File(folder);
				for (File target_file: folder_file.listFiles()) {
					HashMap map_copy = (HashMap)map.clone();
					if (target_file.isDirectory()) {
						String conserting_input = target_file.getPath() + "\\" + target_file.getName() + "_CONSERTING.txt";
						System.out.println(conserting_input);
						out.write(conserting_input + "\n");
						File conserting_file = new File(conserting_input);
						if (conserting_file.exists()) {
							fstream = new FileInputStream(conserting_input);
							din = new DataInputStream(fstream);
							in = new BufferedReader(new InputStreamReader(din)); 
							String header = in.readLine();				
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								String chr_consert = split[0];
								double start_consert = new Double(split[1]);
								if (!split[2].contains("e")) {
									split[2] = split[2].replaceAll("e\\+07", "00000").replaceAll("\\.", "");
									split[2] = split[2].replaceAll("e\\+08", "00000").replaceAll("\\.", "");
									/*if (split[2].equals("3.9e+07")) {
										split[2] = "39000201";
									}
									if (split[2].equals("1.1e+08")) {
										split[2] = "110000401";
									}
									if (split[2].equals("1.44e+08")) {
										split[2] = "144000901";
									}*/
									double end_consert = new Double(split[2]);
									double logRatioScore = new Double(split[8]) * (end_consert - start_consert + 1);
									Iterator itr = map_copy.keySet().iterator();
									while (itr.hasNext()) {
										String band_info_str = (String)itr.next();
										String[] split_band_info_str = band_info_str.split("\t");
										String chr_band = split_band_info_str[0];
										
										int start_band = new Integer(split_band_info_str[1]);
										int end_band = new Integer(split_band_info_str[2]);
										if (start_band <= start_consert && start_consert <= end_band && start_band <= end_consert && end_consert <= end_band) {
											double val = (Double)map_copy.get(band_info_str) + logRatioScore;
											map_copy.put(band_info_str, val);							
										}						
									}
								}
							}
							in.close();
							int positive = 0;
							int negative = 0;
							Iterator itr = map_copy.keySet().iterator();
							while (itr.hasNext()) {
								String band_info_str = (String)itr.next();
								String[] split_band_info_str = band_info_str.split("\t");
								String chr_band = split_band_info_str[0];					
								int start_band = new Integer(split_band_info_str[1]);
								int end_band = new Integer(split_band_info_str[2]);
								double val = (Double)map_copy.get(band_info_str);
								System.out.println(band_info_str + "\t" + (val / (end_band - start_band)));
								out.write(band_info_str + "\t" + (val / (end_band - start_band)) + "\n");
								if ((val / (end_band - start_band)) > 1.0) {
									positive++;									
								}
								if ((val / (end_band - start_band)) < -1.0) {
									negative++;									
								}
							}
							comp_out.write(target_file.getName() + "\t" + type + "\t" + positive + "\t" + negative + "\n");
						}
					}
				}
				/*
				String inputFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_methylation_table_WilcoxResult_0.01_cutoff_20170926.txt";
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
				String header = in.readLine();
				out.write(header + "\tcolor_channel\tchr\tloc\tstrand\trefseq_geneName\trefseq_accession\tbodyinformation\tcpgisland\tseq\n");
				while (in.ready()) {
					String str = in.readLine().replaceAll("\"", "");
					String[] split = str.split("\t");
					//System.out.println(split[0]);
					map.put(split[0], str);
				}
				*/
				out.close();
				
			}
			comp_out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
