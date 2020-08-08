package stjude.projects.xiaotuma.aml.tsne;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaAppendFusionAnnotation {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap map_flt3 = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\CICERO_combine.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String major_fusion = split[64];
				String minor_fusion = split[65];
				if (minor_fusion.contains("FLT3")) {
					map_flt3.put(split[0], "FLT3");
				}
				
				String subgroup = "NA";
				if (map.containsKey(split[0])) {
					subgroup = (String)map.get(split[0]);
				}
				if (major_fusion.contains("RUNX1")) {
					if (subgroup.equals("NA")) {
						subgroup = "RUNX1";
					} else {
						if (!subgroup.contains("RUNX1")) {
							subgroup += ",RUNX1";
						}
					}
				} 
				if (major_fusion.contains("KMT2A")) {
					if (subgroup.equals("NA")) {
						subgroup = "KMT2A";
					} else {
						if (!subgroup.contains("KMT2A")) {
							subgroup += ",KMT2A";
						}
					}
				} 
				if (major_fusion.contains("CBFB")) {
					if (subgroup.equals("NA")) {
						subgroup = "CBFB";
					} else {
						if (!subgroup.contains("CBFB")) {
							subgroup += ",CBFB";
						}
					}
				} 
				if (major_fusion.contains("NUP98")) {
					if (subgroup.equals("NA")) {
						subgroup = "NUP98";
					} else {
						if (!subgroup.contains("NUP98")) {
							subgroup += ",NUP98";
						}
					}
				} 
				if (major_fusion.contains("NUP214")) {
					if (subgroup.equals("NA")) {
						subgroup = "NUP214";
					} else {
						if (!subgroup.contains("NUP214")) {
							subgroup += ",NUP214";
						}
					}
				}
				if (major_fusion.contains("MLLT10")) {
					if (subgroup.equals("NA")) {
						subgroup = "MLLT10";
					} else {
						if (!subgroup.contains("MLLT10")) {
							subgroup += ",MLLT10";
						}
					}
				}
				if (major_fusion.contains("CBFA2T3")) {
					if (subgroup.equals("NA")) {
						subgroup = "CBFA2T3";
					} else {
						if (!subgroup.contains("CBFA2T3")) {
							subgroup += ",CBFA2T3";
						}
					}
				} 
				if (major_fusion.contains("ALK")) {
					if (subgroup.equals("NA")) {
						subgroup = "ALK";
					} else {
						if (!subgroup.contains("ALK")) {
							subgroup += ",ALK";
						}
					}
				} 
				if (major_fusion.contains("ABL1")) {
					if (subgroup.equals("NA")) {
						subgroup = "ABL1";
					} else {
						if (!subgroup.contains("ABL1")) {
							subgroup += ",ABL1";
						}
					}
				} 
				if (major_fusion.contains("ERG")) {
					if (subgroup.equals("NA")) {
						subgroup = "ERG";
					} else {
						if (!subgroup.contains("ERG")) {
							subgroup += ",ERG";
						}
					}
				}
				if (major_fusion.contains("MYB")) {
					if (subgroup.equals("NA")) {
						subgroup = "MYB";
					} else {
						if (!subgroup.contains("MYB")) {
							subgroup += ",MYB";
						}
					}
				}
				if (major_fusion.contains("MLF1")) {
					if (subgroup.equals("NA")) {
						subgroup = "MLF1";
					} else {
						if (!subgroup.contains("MLF1")) {
							subgroup += ",MLF1";
						}
					}
				}
				if (major_fusion.contains("CREBBP")) {
					if (subgroup.equals("NA")) {
						subgroup = "CREBBP";
					} else {
						if (!subgroup.contains("CREBBP")) {
							subgroup += ",CREBBP";
						}
					}
				}
				if (!subgroup.equals("NA")) {
					map.put(split[0], subgroup);
				}
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_analysis\\tsne\\FredHutch_AML_TSNE_output_per50_annotated.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tTSNE_1\tTSNE_2\tMajorFusion\tFLT3\n");
			// 
			inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_analysis\\tsne\\FredHutch_AML_TSNE_output_per50.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				String subtype = "NA";
				if (map.containsKey(split[1])) {
					subtype = (String)map.get(split[1]);
				}
				String flt3 = "NA";
				if (map_flt3.containsKey(split[1])) {
					flt3 = (String)map_flt3.get(split[1]);
				}
				out.write(split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + subtype + "\t" + flt3 + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
