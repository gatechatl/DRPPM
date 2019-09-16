package genomics.tools.gistic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CreateCNVkit2SEG {

	public static String type() {
		return "CICERO";
	}
	public static String description() {
		return "Extract fusion gene";
	}
	public static String parameter_info() {
		return "[inputFile] [geneFile] [outputFile]";
	}
	public static void main(String[] args) {
		
		try {
			
			String inputFolder = "Z:\\ResearchHome\\ProjectSpace\\mulligrp\\LeventakiALCLProject\\common\\CMPB\\BioinfoCore\\Exome\\cnvkit_summary\\cns\\";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\MethodDevelopment\\common\\ProteomicsAMLAnthonyHo\\tshaw\\ALCL\\GISTIC\\ALCL.seg";
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tChromosome\tStart_Position\tEnd_Position\tNum_Markers\tSeg.CN\n");
			File file = new File(inputFolder);
			for (File f: file.listFiles()) {
				if (!f.getName().contains("swp")) {
					String sampleName = f.getName().replaceAll(".cns", "");
					FileInputStream fstream = new FileInputStream(f.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					in.readLine();
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (split.length > 6) {
							out.write(sampleName + "\t" + "chr" + split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[6] + "\t" + (new Double(split[4]) - 1) + "\n");
						} else {
							System.out.println(str);
						}
					}
					in.close();
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
