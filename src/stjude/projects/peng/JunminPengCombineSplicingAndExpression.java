package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Merge two matrix geneName file together
 * @author tshaw
 *
 */
public class JunminPengCombineSplicingAndExpression {
	

	public static String description() {
		return "Combinwo Splicing change with expression change";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile1] [inputFile2] [newOutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inuputFile1 = args[0];						
			HashMap comprehensive = new HashMap();
			
			/*HashMap map = new HashMap();
			int sample1_len = 0;
			
			FileInputStream fstream = new FileInputStream(inuputFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header1 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample1_len = split.length - 1;
				map.put(split[0], str);
				comprehensive.put(split[0], split[0]);
			}
			in.close();
			*/
			
			int sample2_len = 0;
			String inuputFile2 = args[1];
			HashMap map2 = new HashMap();
			FileInputStream fstream2 = new FileInputStream(inuputFile2);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			String header2 = in2.readLine();
			while (in2.ready()) {
				String str = in2.readLine();
				String[] split = str.split("\t");
				sample2_len = split.length - 1;
				map2.put(split[0], str);
				//comprehensive.put(split[0], split[0]);
			}
			in2.close();
			
			String outputFile = args[2];

			FileInputStream fstream = new FileInputStream(inuputFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header1 = in.readLine();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(header1);
			String[] split = header2.split("\t");
			for (int i = 1; i < split.length; i++) {
				out.write("\t" + split[i]);
			}
			out.write("\n");
			
			while (in.ready()) {
				String str = in.readLine();
				split = str.split("\t");
				String gene = split[0];
				String file1_line = str;
			
				String file2_line = "";
				for (int j = 0; j < sample2_len; j++) {
					file2_line += "\tNA";
				}
				
				out.write(file1_line);
				if (map2.containsKey(gene)) {
					file2_line = (String)map2.get(gene);
					String[] split2 = file2_line.split("\t");
					for (int i = 1; i < split2.length; i++) {
						out.write("\t" + split2[i]);
					}					
				} else {
					out.write(file2_line);
				}
				out.write("\n");
			
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
