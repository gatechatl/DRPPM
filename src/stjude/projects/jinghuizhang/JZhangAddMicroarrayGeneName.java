package stjude.projects.jinghuizhang;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JZhangAddMicroarrayGeneName {

	
	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\USP7\\common\\ProteomicsSummary\\ExternalData\\Mouse430_2.na36.annot.csv";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\",\"");
				if (split.length > 13) {
					//System.out.println(split[0].replaceAll("\"", "") + "\t" + split[14].replaceAll("\"", ""));
					map.put(split[0].replaceAll("\"", ""), split[14].replaceAll("\"", ""));
				}
			}
			in.close();
			

			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\USP7\\common\\ProteomicsSummary\\ExternalData\\GSE72988_series_matrix_addGeneName.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			String microarrayFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\USP7\\common\\ProteomicsSummary\\ExternalData\\GSE72988_series_matrix.txt"; 
			fstream = new FileInputStream(microarrayFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("GeneName\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0].replaceAll("\"", ""))) {
					out.write(map.get(split[0].replaceAll("\"", "")) + "\t" + str + "\n");
				} else {
					out.write("NA\t" + str + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
