package nextgenerationsequencing.fastq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * In certain files, they included both forward and reverse strand to a single sequence. 
 * We will need to separate the samples into two files.
 * @author tshaw
 *
 */
public class SplitFastqForwardReverse {

	public static String type() {
		return "FASTQ";
	}
	public static String description() {
		return "In certain files, they included both forward and reverse strand to a single sequence. We will separate the fastq file into two files.";
	}
	public static String parameter_info() {
		return "[orig_fastq] [length of halfway point: 125] [fastq1] [fastq2]";
	}
	public static void execute(String [] args) {
		
		try {
			
			String fileName = args[0];
			int len = new Integer(args[1]);
			String outputFile1 = args[2];
			String outputFile2 = args[3];
			FileWriter fwriter1 = new FileWriter(outputFile1);
			BufferedWriter out1 = new BufferedWriter(fwriter1);
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);

			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			//in.readLine();
			while (in.ready()) {
				String tag = in.readLine();
				
				String[] split_tag = tag.split(" ");
				String tag1 = split_tag[0] + " " + split_tag[1] + " 1 " + split_tag[2];
				String tag2 = split_tag[0] + " " + split_tag[1] + " 2 " + split_tag[2];

				String seq = in.readLine();
				String seq1 = seq.substring(0, len);
				String seq2 = seq.substring(len, seq.length());
				String sep = "+"; in.readLine();
				String qual = in.readLine();
				String qual1 = qual.substring(0, len);
				String qual2 = qual.substring(len, qual.length());
				out1.write(tag1 + "\n");
				out2.write(tag2 + "\n");
				out1.write(seq1 + "\n");
				out2.write(seq2 + "\n");
				out1.write(sep + "\n");
				out2.write(sep + "\n");
				out1.write(qual1 + "\n");
				out2.write(qual2 + "\n");
			}
			in.close();
			
			out1.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
