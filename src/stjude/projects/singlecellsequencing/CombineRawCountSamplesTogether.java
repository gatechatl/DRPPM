package stjude.projects.singlecellsequencing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CombineRawCountSamplesTogether {

	public static String type() {
		return "STJUDE";
	}
	public static String parameter_info() {
		return "[inputRawCountFile] [outputFile]";
	}
	public static String description() {
		return "Combine similar sample names together";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			String inputFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			String[] sampleNames = header.split("\t");
			for (int i = 1; i < sampleNames.length; i++) {
				String combinedName = sampleNames[i].split("L00")[0];
				map.put(combinedName, combinedName);
			}
			
			out.write("GeneName");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				out.write("\t" + sampleName);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					double count = 0;
					for (int i = 1; i < split.length; i++) {
						if (sampleNames[i].contains(sampleName)) {
							count+= new Double(split[i]);
						}
						
					}
					out.write("\t" + count);
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
