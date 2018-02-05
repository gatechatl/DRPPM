package expressionanalysis.tools.batchcorrection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Perform mean centering to correct the expression.
 * @author tshaw
 *
 */
public class TwoGroupMeanCenteringFlex {

	public static String description() {
		return "Perform mean centering on batch2 relative to batch1.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [sample_batch_1_file] [sample rep for batch1] [sample_batch_2_file] [sample rep for batch2] [outputCorrectedMatrixFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String sample_batch1_file = args[1];
			String sample_batch1_representative_file = args[2];
			String sample_batch2_file = args[3];
			String sample_batch2_representative_file = args[4];
			String outputCorrectedMatrixFile = args[5];
			
			HashMap batch1 = new HashMap();
			FileInputStream fstream = new FileInputStream(sample_batch1_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				batch1.put(str.trim(), str);
			}
			in.close();
			
			HashMap batch2 = new HashMap();
			fstream = new FileInputStream(sample_batch2_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				batch2.put(str.trim(), str);
			}
			in.close();
			
			HashMap batch1_rep = new HashMap();
			fstream = new FileInputStream(sample_batch1_representative_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				batch1_rep.put(str.trim(), str);
			}
			in.close();
			
			HashMap batch2_rep = new HashMap();
			fstream = new FileInputStream(sample_batch2_representative_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				batch2_rep.put(str.trim(), str);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputCorrectedMatrixFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");			
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				LinkedList batch1_values_list = new LinkedList();
				LinkedList batch2_values_list = new LinkedList();
				for (int i = 1; i < split.length; i++) {
					if (batch1_rep.containsKey(split_header[i])) {
						batch1_values_list.add(MathTools.log2(new Double(split[i]) + 1) + "");
					} else if (batch2_rep.containsKey(split_header[i])) {
						batch2_values_list.add(MathTools.log2(new Double(split[i]) + 1) + "");
					} else {
						//System.out.println("Missing sampleName: " + split_header[i]);
					}
				}
				double[] batch1_values = MathTools.convertListStr2Double(batch1_values_list);
				double[] batch2_values = MathTools.convertListStr2Double(batch2_values_list);
				
				double batch1_avg = MathTools.mean(batch1_values);
				double batch2_avg = MathTools.mean(batch2_values);
				double ratio = batch1_avg / batch2_avg;
				if (batch1_avg == 0) {
					ratio = 1.0;
				}
				if (batch2_avg == 0) {
					ratio = 1.0;
				}
				//System.out.println(split[0] + "\t" + batch1_avg + "\t" + batch2_avg + "\t" + ratio);
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					if (batch1.containsKey(split_header[i])) {
						
						out.write("\t" + split[i]);
					} else if (batch2.containsKey(split_header[i])) {
						double newvalue = Math.pow(2, MathTools.log2(new Double(split[i]) + 1) * ratio) - 1;
						out.write("\t" + newvalue);
					} else {
						out.write("\t" + split[i]);
						//System.out.println("Missing sampleName: " + split_header[i]);
					}
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
