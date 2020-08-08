package stjude.projects.jinghuizhang.immunesignature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangAppendTCGAImmuneClusterInformation {

	public static String type() {
		return "TCGA";
	}
	public static String description() {
		return "Append the sample immune cluster information.";
	}
	public static String parameter_info() {
		return "[inputSSGSEAFile] [inputMutationalSignature] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputSSGSEAFile= args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\ImmuneScore\\ACC.updated.ssGSEA.fpkm.geneName.max.txt";
			String inputMutationalSignature = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\ImmuneScore\\ImmunityPaper\\PatientID_ImmuneSubtype_20191015.txt";
			String outputFile = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\ImmuneScore\\ACC.updated.ssGSEA.fpkm.geneName.max_updated.txt";
			
			FileInputStream fstream = new FileInputStream(inputMutationalSignature);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[2].equals("NA")) {
					map.put("TCGA." + split[1] + "_" + split[0].replaceAll("-", "\\."), split[2]);
					
				}
			}
			in.close();		
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			 		
			fstream = new FileInputStream(inputSSGSEAFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header.replaceAll(" ", "_") + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					out.write(split[0] + "_" + ((String)map.get(split[0])).replaceAll("C", "ImmClust"));
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				} else {
					//out.write(str + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
