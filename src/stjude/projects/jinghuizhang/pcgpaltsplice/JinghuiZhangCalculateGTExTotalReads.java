package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JinghuiZhangCalculateGTExTotalReads {

	public static String description() {
		return "Generate total read count of matrix";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[matrix file of read count]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap total_reads = new HashMap();
			LinkedList sampleNames = new LinkedList();
			String inputReadCountFile = args[0];
			FileInputStream fstream = new FileInputStream(inputReadCountFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				total_reads.put(split_header[i], 0.0);
				sampleNames.add(split_header[i]);
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split_header.length; i++) {
					double prev_count = (Double)total_reads.get(split_header[i]);
					prev_count += new Double(split[i]);
					total_reads.put(split_header[i], prev_count);
				}
			}
			in.close();
			Iterator itr = sampleNames.iterator();
			System.out.print("Count");
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				double count = (Double)total_reads.get(sampleName);
				System.out.print("\t" + count);
				//System.out.println(sampleName + "\t" + count);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
