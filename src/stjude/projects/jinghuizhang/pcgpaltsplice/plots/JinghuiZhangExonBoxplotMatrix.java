package stjude.projects.jinghuizhang.pcgpaltsplice.plots;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangExonBoxplotMatrix {

	public static String description() {
		return "Generate boxplot matrix.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[input exon Matrix] [metaInfoFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap sample2type = new HashMap();
			//String inputMatrix = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\	";
			String inputMatrix = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\ENSG00000115414.14_23_EB-D_exon.txt";
			String metaInfoFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\GTEx_metainfo.txt";
			//String 
			String outputFile = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\GTEx_FN1_ED-B_fpkm_boxplot.txt";			
			
			FileInputStream fstream = new FileInputStream(metaInfoFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample2type.put(split[0], split[1]);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);		
			out.write("Name\tFPKM\tType\n");
			fstream = new FileInputStream(inputMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			String[] split_header = header.split("\t");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					
					if (sample2type.containsKey(split_header[i])) {
						String sample_type = (String)sample2type.get(split_header[i]);
						//if (sample_type.contains("Adipose") || sample_type.contains("Artery")) {
							out.write(split_header[i] + "\t" + split[i].replaceAll(",", "") + "\t" + sample_type + "\n");
						//}
					}
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
}
