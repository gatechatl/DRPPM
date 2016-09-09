package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AddSiftPrediction {

	public static String parameter_info() {
		return "[inputFIle] [siftOutput] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			String inputFile = args[0]; // recurrent summary file
			String siftOutput = args[1]; // sift output
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(siftOutput);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0] + "\t" + split[1], split[3]);
			}
			in.close();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\tSIFTAnnotation\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String siftTag = "NA";
				if (map.containsKey(split[7] + "\t" + split[6])) {
					siftTag = (String)map.get(split[7] + "\t" + split[6]);
				}
				out.write(str + "\t" + siftTag + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
