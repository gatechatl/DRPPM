package stjude.projects.jinghuizhang.immunepopulation.logistictraining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangPreprocessCibersortTrainingFile {

	
	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputClassesFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\MethodDevelopment\\common\\immune_signature\\CIBERSORT\\LM22-classes.txt";
			FileInputStream fstream = new FileInputStream(inputClassesFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll(" ", "_");
				System.out.println(split[0]);
				for (int i = 0; i < split.length; i++) {
					if (split[i].equals("1")) {
						map.put(i, split[0]);
					}
				}								
			}
			in.close();
			
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\MethodDevelopment\\common\\immune_signature\\CIBERSORT\\LM22-ref-sample_output.txt";
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\LM22-ref-sample_filtered_output.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName");
			
			String inputMatrixFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\MethodDevelopment\\common\\immune_signature\\CIBERSORT\\LM22-ref-sample.txt";
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				if (map.containsKey(i)) {
					split_header[i] = map.get(i) + "." + split_header[i];
					out.write("\t" + split_header[i]);
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split_header.length; i++) {
					if (map.containsKey(i)) {
						out.write("\t" + split[i]);
					}
				}
				out.write("\n");
			}
			
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
