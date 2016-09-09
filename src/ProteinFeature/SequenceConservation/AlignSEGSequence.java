package ProteinFeature.SequenceConservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * @author tshaw
 *
 */
public class AlignSEGSequence {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Align SEG sequence";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String name = "";
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str.split("\\(")[0];
					
				} else {
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						map.put(name, seq + str.trim().toUpperCase());
					} else {
						map.put(name, str.trim().toUpperCase());
					}
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				name = (String)itr.next();
				String seq = (String)map.get(name);
				out.write(name + "\n" + seq + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
