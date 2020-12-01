package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Only keep reads that satisfy the length condition
 * @author tshaw
 *
 */
public class FilterBEDReads {
	public static void execute(String[] args) {
		try {
			
			String fileName = args[0];
			int length = new Integer(args[1]);
			int buffer = new Integer(args[2]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				double start = new Double(split[1]);
				double end = new Double(split[2]);
				if ((end - start) >= (length - buffer) && (length + buffer) >= (end - start)) {
					out.write(str + "\n");
				}			
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
