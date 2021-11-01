package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Purpose of this class is to add gene KO information to the sample
 * @author tshaw
 *
 */
public class AddGeneKO2SampleName {

	public static String description() {
		return "Append the Genotype information to the sample name.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [metaFile] [outputFile] [starting index of the annotation]";
	}
	public static void execute(String[] args) {		
		try {
			String fpkm_file = args[0];
			String metaFile = args[1];			
			String outputFile = args[2];
			int num = new Integer(args[3]); // this is the start index for the annotation
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			FileInputStream fstream2 = new FileInputStream(fpkm_file);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			String header = in2.readLine();
			String[] headers = header.split("\t");
			
			int count_matches = 0;
			
			out.write(headers[0]);
			for (int j = 1; j < headers.length; j++) {
				// for each gene name
			
				FileInputStream fstream = new FileInputStream(metaFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String title = in.readLine();
				String[] split_title = title.split("\t");
				
				String[] genes = new String[split_title.length - num];
				HashMap[] map_annotations = new HashMap[split_title.length - num];
				for (int i = num; i < split_title.length; i++) {
					genes[i - num] = split_title[i];
					System.out.println("Annotation added: " + genes[i - num]);
					map_annotations[i] = new HashMap();
				}
				
				
				
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					boolean[] boo = new boolean[genes.length];
					for (int i = num; i < split.length; i++) {
						if (split[i].equals("yes")) {
							boo[i - num] = true;
							String new_filename = split[0].replaceAll("_", ".").replaceAll("-", ".");
							map_annotations[i - num].put(new_filename, new_filename);
						} else {
							boo[i - num] = false;
						}
					}
					
					String total_filename = split[0].replaceAll("_", ".").replaceAll("-", ".");
					
						String single_file_name = headers[j].replaceAll("_", ".").replaceAll("-", ".");
						
						if (single_file_name.equals(total_filename)) {
							String newName = total_filename;
							boolean noTagFound = true;
							for (int i = 0; i < boo.length; i++) {
								if (boo[i]) {
									newName += "." + genes[i];
									System.out.println(genes[i]);
									noTagFound = false;
								}
							}
							
							out.write("\t" + newName);
							
						}
					
				
					
				}
				in.close();
			
			} // for loop
			out.write("\n");
			while (in2.ready()) {
				String str = in2.readLine();
				out.write(str + "\n");
			}
			in2.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
