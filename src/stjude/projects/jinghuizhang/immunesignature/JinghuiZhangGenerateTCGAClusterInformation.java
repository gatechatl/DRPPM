package stjude.projects.jinghuizhang.immunesignature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangGenerateTCGAClusterInformation {

	public static String type() {
		return "TCGA";
	}
	public static String description() {
		return "Append the sample immune cluster information.";
	}
	public static String parameter_info() {
		return "[inputSSGSEAFile] [inputMutationalSignature] [outputFile]";
	}
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			String inputMutationalSignature = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\ImmuneScore\\ImmunityPaper\\PatientID_ImmuneSubtype_20191015.txt";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\ImmuneScore\\ImmunityPaper\\PatientID_ImmuneSubtype_Cleaned_20191015.txt";

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			FileInputStream fstream = new FileInputStream(inputMutationalSignature);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("Sample\tType\tCluster\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = "TCGA." + split[1] + "_" + split[0].replaceAll("-", "\\.") + "_" + (split[2]).replaceAll("C", "ImmClust");
				
				String type = split[1];
				String cluster = split[2];
				if (cluster.equals("NA")) {
					cluster = "NotAvail";
					name = "TCGA." + split[1] + "_" + split[0].replaceAll("-", "\\.");
				}
				out.write(name + "\t" + type + "\t" + cluster + "\n");
				map.put(name, name);
			}
			in.close();
			
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\ImmuneScore\\all";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("Sample")) {
					if (!map.containsKey(split[0])) {
						out.write(split[0] + "\t" + split[0].split("_")[0].replaceAll("TCGA\\.",  "") + "\t" + "NotAvail\n");
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
