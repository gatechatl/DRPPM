package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class TransposeMatrixSplit {

	public static String description() {
		return "Transpose the matrix (flip the matrix).";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [num col_row] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int num_gene = new Integer(args[1]);
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			int index = 1;
			LinkedList lines = new LinkedList();
			
			String new_sample_header = "GeneSets";
			out.write("SampleName");
			HashMap map_gene = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			
			for (String name: split_header) {
				if (map_gene.containsKey(index)) {
					LinkedList list = (LinkedList)map_gene.get(index);
					list.add(name);
					map_gene.put(index, list);
					if (list.size() >= num_gene) {
						index++;
					}
				} else {
					LinkedList list = new LinkedList();
					list.add(name);
					map_gene.put(index, list);
				}
			}			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write("\t" + split[0]);
			}
			in.close();
			out.write("\n");			
			
			Iterator itr = map_gene.keySet().iterator();
			while (itr.hasNext()) {
			
				HashMap map = new HashMap();
				int idx = (Integer)itr.next();
				LinkedList names = (LinkedList)map_gene.get(idx);
				HashMap row_names = new HashMap();
				
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();
				split_header = header.split("\t");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					row_names.put(split[0], split[0]);
					for (int i = 0; i < split_header.length; i++) {
						if (names.contains(split_header[i])) {
							map.put(split_header[i] + "\t" + split[0], split[i]);
						}
					}
				}				
				
				Iterator itr_names = names.iterator();
				while (itr_names.hasNext()) {
					String col_name = (String)itr_names.next();
					out.write(col_name);
					Iterator itr3 = row_names.keySet().iterator();
					while (itr3.hasNext()) {
						String row_name = (String)itr3.next();
						String value = (String)map.get(col_name + "\t" + row_name);
						out.write("\t" + value);
					}
					out.write("\n");
				}
				
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
