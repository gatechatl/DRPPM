package metagenomics.qiime.postprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class NormalizePerHundredKilo {

	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");
			for (int i = 1; i < header.length; i++) {
				map.put(header[i], 0.0);
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					double addNum = (Double)map.get(header[i]);
					addNum += new Double(split[i]);
					map.put(header[i], addNum);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);							
						
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					double total = (Double)map.get(header[i]);
					double count = new Double(split[i]);
					double norm = new Double(new Double(count * 100000000 / total).intValue()) / 1000;
					out.write("\t" + norm);
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
