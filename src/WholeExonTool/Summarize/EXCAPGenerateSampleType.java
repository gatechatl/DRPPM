package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class EXCAPGenerateSampleType {

	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			String inputFile = args[0];
			String outputFile = args[1];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap sampleType = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sampleType.put(split[2], split[2]);
			}
			in.close();
			
			Iterator itr = sampleType.keySet().iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				out.write(sample + "\t" + "Group1\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
