package network.protein.complex.annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class AppendProteinComplexInfo {

	public static String parameter_info() {
		return "[refFile] [inputFile] [geneIndex] [outputFile_append] [outputFile_complex]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			HashMap complex_map = new HashMap();
			String refFile = args[0];
			String inputFile = args[1]; 			
			int geneIndex = new Integer(args[2]);
			String outputFileAppend = args[3];
			String outputFIleComplex = args[4];
			FileInputStream fstream = new FileInputStream(refFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				complex_map.put(split[1], "");
				if (map.containsKey(split[3])) {
					String terms = (String)map.get(split[3]);
					map.put(split[3], terms + "\t" + split[1]);
				} else {
					map.put(split[3], split[1]);
				}
				
			}
			in.close();

			FileWriter fwriter = new FileWriter(outputFileAppend);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_complex = new FileWriter(outputFIleComplex);
			BufferedWriter out_complex = new BufferedWriter(fwriter_complex);
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[geneIndex];
				String annotation = "NA";
				if (map.containsKey(geneName)) {
					annotation = (String)map.get(geneName);
					if (complex_map.containsKey(annotation)) {
						String geneList = (String)complex_map.get(annotation);
						if (geneList.equals("")) {
							geneList = geneName;
						} else {
							geneList += "," + geneName;
						}
						complex_map.put(annotation, geneList);
					}
					
				}
				out.write(str + "\t" + annotation + "\n");
				//System.out.println(str + "\t" + annotation);
			}
			in.close();
			out.close();
			Iterator itr = complex_map.keySet().iterator();
			while (itr.hasNext()) {
				String annot = (String)itr.next();
				String geneList = (String)complex_map.get(annot);
				if (!geneList.equals("")) {
					out_complex.write(annot + "\t" + geneList + "\n");
				}
			}
			out_complex.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
