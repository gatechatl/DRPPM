package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import statistics.general.MathTools;

public class SummarizeMIXCRresult {
	public static String description() {
		return "Summarize MIXCR result";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[three column file (sampleName) (R1_fq) (R2_fq)] [outputSummaryFile]";
	}
	public static void execute(String[] args) {
		
		
		try {
			
			
			
			String sampleNameFile = args[0];
			String outputFile = args[1];
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			out.write("SampleName\tTCR_type\tEntropy\tHighest\tTags\n");
			/*FileInputStream fstream = new FileInputStream(sampleNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write("\t" + split[0]);				
				
			}
			in.close();
			out.write("\n");*/

			FileInputStream fstream = new FileInputStream(sampleNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");

				double entropy_sum = 0.0;
				double entropy_n = 0.0;
				double highest = 0;
				String dominant_clone_name = "NA";
				//out.write("TRA_ENTROPY");
				boolean write_something_flag = false;
				FileInputStream fstream2 = new FileInputStream(split[0] + ".TRA.clones.txt");
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				String header = in2.readLine();
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					entropy_sum += (new Double(split2[2]) * MathTools.log2(new Double(split2[2])));
					entropy_n++;
					if (highest < new Double(split2[1])) {
						dominant_clone_name = split2[5] + ":" + split2[6] + ":" + split2[7] + ":" + split2[8];
						highest = new Double(split2[1]);
					}
					
					write_something_flag = true;
				}	
				
				if (write_something_flag) {
					out.write(split[0] + "\tTRA\t" + -entropy_sum / MathTools.log2(entropy_n) + "\t" + highest + "\t" + dominant_clone_name + "\n");
				} else {
					out.write(split[0] + "\tTRA\tNA\tNA\tNA\n");
				}

			
				entropy_sum = 0.0;
				entropy_n = 0.0;
				highest = 0;
				write_something_flag = false;
				dominant_clone_name = "NA";
				fstream2 = new FileInputStream(split[0] + ".TRB.clones.txt");
				din2 = new DataInputStream(fstream2);
				in2 = new BufferedReader(new InputStreamReader(din2));
				header = in2.readLine();
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					entropy_sum += (new Double(split2[2]) * MathTools.log2(new Double(split2[2])));
					entropy_n++;
					if (highest < new Double(split2[1])) {
						dominant_clone_name = split2[5] + ":" + split2[6] + ":" + split2[7] + ":" + split2[8];
						highest = new Double(split2[1]);
					}
					
					write_something_flag = true;
				}
				if (write_something_flag) {
					out.write(split[0] + "\tTRB\t" + -entropy_sum / Math.sqrt(entropy_n) + "\t" + highest + "\t" + dominant_clone_name + "\n");
				} else {
					out.write(split[0] + "\tTRB\tNA\tNA\tNA\n");
				}
				
				entropy_sum = 0.0;
				entropy_n = 0.0;
				highest = 0;
				write_something_flag = false;
				dominant_clone_name = "NA";
				fstream2 = new FileInputStream(split[0] + ".TRD.clones.txt");
				din2 = new DataInputStream(fstream2);
				in2 = new BufferedReader(new InputStreamReader(din2));
				header = in2.readLine();
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					entropy_sum += (new Double(split2[2]) * MathTools.log2(new Double(split2[2])));
					entropy_n++;
					if (highest < new Double(split2[1])) {
						dominant_clone_name = split2[5] + ":" + split2[6] + ":" + split2[7] + ":" + split2[8];
						highest = new Double(split2[1]);
					}
					
					write_something_flag = true;
				}
				if (write_something_flag) {
					out.write(split[0] + "\tTRD\t" + -entropy_sum / Math.sqrt(entropy_n) + "\t" + highest + "\t" + dominant_clone_name + "\n");
				} else {
					out.write(split[0] + "\tTRD\tNA\tNA\tNA\n");
				}
				
				entropy_sum = 0.0;
				entropy_n = 0.0;
				highest = 0;
				write_something_flag = false;
				dominant_clone_name = "NA";
				fstream2 = new FileInputStream(split[0] + ".TRG.clones.txt");
				din2 = new DataInputStream(fstream2);
				in2 = new BufferedReader(new InputStreamReader(din2));
				header = in2.readLine();
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					entropy_sum += (new Double(split2[2]) * MathTools.log2(new Double(split2[2])));
					entropy_n++;
					if (highest < new Double(split2[1])) {
						dominant_clone_name = split2[5] + ":" + split2[6] + ":" + split2[7] + ":" + split2[8];
						highest = new Double(split2[1]);
					}
					
					write_something_flag = true;
				}
				if (write_something_flag) {
					out.write(split[0] + "\tTRG\t" + -entropy_sum / Math.sqrt(entropy_n) + "\t" + highest + "\t" + dominant_clone_name + "\n");
				} else {
					out.write(split[0] + "\tTRG\tNA\tNA\tNA\n");
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
