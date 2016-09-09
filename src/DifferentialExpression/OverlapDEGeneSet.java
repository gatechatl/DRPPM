package DifferentialExpression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class OverlapDEGeneSet {

	public static String type() {
		return "DE";
	}
	public static String description() {
		return "Overlap geneset between two LIMMA result";
	}
	public static String parameter_info() {
		return "[inputFileDE1] [inputFileDE2] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileDE1 = args[0];
			String inputFileDE2 = args[1];
			String outputFile = args[2];
			
			HashMap overlap = new HashMap();
			HashMap map1 = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFileDE1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header1 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map1.put(split[0], str);
			}
			in.close();
			
			HashMap map2 = new HashMap();
			fstream = new FileInputStream(inputFileDE2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String header2 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map2.put(split[0], str);
				if (map1.containsKey(split[0])) {
					overlap.put(split[0], split[0]);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Gene1\t" + header1 + "\tGene2\t" + header2 + "\n");
			Iterator itr = overlap.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String map1_line = (String)map1.get(geneName);
				String map2_line = (String)map2.get(geneName);
				out.write(map1_line + "\t" + map2_line + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
