package stude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * After running variants2matrix annotate the samples based on 
 * @author tshaw
 *
 */
public class GenerateRNAHGGSampleK27MStatus {
	public static String description() {
		return "Generate the HGG single cell status based on K27M annotation";
	}
	public static String type() {
		return "SNV";
	}
	public static String parameter_info() {
		return "[matrix_combined_matrix_simple.tab] [outputColorStatus]";
	}
	public static void execute(String[] args) {
		
		try {

			String inputFile = args[0];			
			String outputFile = args[1];
			HashMap map_ref = new HashMap();
			HashMap map_mut = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] name_split = split[0].split("_");
				String name = "";
				for (int i = 0; i < name_split.length - 1; i++) {
					if (i == 0) {
						name = name_split[i];
					} else {
						name += "_" + name_split[i];
					}
				}
				double ref = new Integer(split[2]);
				double mut = new Integer(split[3]);
				if (map_ref.containsKey(name)) {
					double old_ref = (Double)map_ref.get(name);
					old_ref += ref;
					map_ref.put(name, old_ref);
				} else {
					map_ref.put(name, ref);
				}
				if (map_mut.containsKey(name)) {
					double old_mut = (Double)map_mut.get(name);
					old_mut += mut;
					map_mut.put(name, old_mut);
				} else {
					map_mut.put(name, mut);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			Iterator itr = map_mut.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				double ref = (Double)map_ref.get(name);
				double mut = (Double)map_mut.get(name);
				String status = "NA";
				String color = "black";
				if (ref <= 3 && mut <= 3) {
					status = "BAD";
					color = "yellow";
				}
				if (ref > 3 && mut > 3) {
					status = "K27M_HET";
					color = "blue";
				}
				if (ref <= 3 && mut > 3) {
					status = "K27M_ONLY";
					color = "red";
				}
				if (ref > 3 && mut <= 3) {
					status = "WT";
					color = "green";
				}
				out.write(name + "_\t" + status + "\t" + color + "\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
