package genomics.tools.gistic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GenerateGISTICMarkers {

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
			
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\mulligrp\\LeventakiALCLProject\\common\\CMPB\\BioinfoCore\\Exome\\cnvkit_091\\SJALCL014723_G1.cnr";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\MethodDevelopment\\common\\ProteomicsAMLAnthonyHo\\tshaw\\ALCL\\GISTIC\\ALCL.marker.txt";
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tChromosome\tPosition\n");

			String id = "ID";
			int i = 1;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int pos1 = new Integer(split[1]);
				int pos2 = new Integer(split[2]);
				out.write(id + i + "\t" + "chr" + split[0] + "\t" + (pos1) + "\n");
				out.write(id + i + "\t" + "chr" + split[0] + "\t" + (pos2) + "\n");
				i++;
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
