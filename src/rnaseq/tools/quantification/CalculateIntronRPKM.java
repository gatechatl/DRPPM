package rnaseq.tools.quantification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import misc.CommandLine;

/**
 * Input a list of bam file and a list of intron coordinates. Grab the number of reads for each genomic region for each bam file, and normalize by the total number of reads. 
 * @author tshaw
 *
 */
public class CalculateIntronRPKM {

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Input a list of bam file and a list of intron coordinates. Grab the number of reads for each genomic region for each bam file, and normalize by the total number of reads.";
	}
	public static String parameter_info() {
		return "[inputBamFile] [inputCoordinate (in tab): (IntronName) (chr) (start) (end) (direction)] [outputCount] [outputRPKM]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBamFile = args[0];
			String inputCoordinates = args[1];
			String outputFile_count = args[2];
			String outputFile_rpkm = args[3];

			FileWriter fwriter_count = new FileWriter(outputFile_count);
			BufferedWriter out_count = new BufferedWriter(fwriter_count);

			FileWriter fwriter_rpkm = new FileWriter(outputFile_rpkm);
			BufferedWriter out_rpkm = new BufferedWriter(fwriter_rpkm);

			HashMap introns = new HashMap();
			LinkedList intron_order = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputCoordinates);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				introns.put(split[0], str);
				intron_order.add(split[0]);
				//String chr = split[0];
				//String start = split[1];
				//String end = split[2];				
			}
			in.close();

			out_count.write("IntronName\tChr\tStart\tEnd\tDirection");
			out_rpkm.write("IntronName\tChr\tStart\tEnd\tDirection");
			fstream = new FileInputStream(inputBamFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				out_count.write("\t" + str.trim());
				out_rpkm.write("\t" + str.trim());
			}
			in.close();
			out_count.write("\n");
			out_rpkm.write("\n");
			
			Iterator itr = intron_order.iterator();
			while (itr.hasNext()) {
				String intron_name = (String)itr.next();				
				String line = (String)introns.get(intron_name);				
				String[] split = line.split("\t");
				String chr = split[1];
				int start = new Integer(split[2]);
				int end = new Integer(split[3]);
				int len = end - start + 1;
				String dir = split[4];
				
				out_count.write(line);
				out_rpkm.write(line);
				fstream = new FileInputStream(inputBamFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					System.out.println(str);
					if (!str.trim().equals("")) {
						
						File f = new File(str + ".flagstat.txt");
						if (!f.exists()) {
							String generate_flagstat = "samtools flagstat " + str.trim() + " > " + str.trim() + ".flagstat.txt";
							CommandLine.executeCommand(generate_flagstat);
							
						}
						
						FileInputStream fstream_flagstat = new FileInputStream(str.trim() + ".flagstat.txt");
						DataInputStream din_flagstat = new DataInputStream(fstream_flagstat);
						BufferedReader in_flagstat = new BufferedReader(new InputStreamReader(din_flagstat));
						in_flagstat.readLine();
						in_flagstat.readLine();
						String str_flagstat = in_flagstat.readLine();
						double total_mapped_reads = new Double(str_flagstat.split(" ")[0]);
						System.out.println("Total mapped reads: " + total_mapped_reads);
						in_flagstat.close();
						
	
						String script1 = "samtools view " + str + " " + chr + ":" + start + "-" + end + " | awk -F '\\t' '($6 ~ /101M/)' | wc -l > " + intron_name;
						System.out.println(script1);
						CommandLine.executeCommand(script1);
						
						
						FileInputStream fstream_intron = new FileInputStream(intron_name);
						DataInputStream din_intron = new DataInputStream(fstream_intron);
						BufferedReader in_intron = new BufferedReader(new InputStreamReader(din_intron));
						double intron_count = new Double(in_intron.readLine().trim());
						System.out.println(intron_count);
						in_intron.close();
						File f_intron = new File(intron_name);
						f_intron.delete();
						double rpkm = intron_count * 1000000000 / (total_mapped_reads * len);
						out_rpkm.write("\t" + rpkm);
						out_count.write("\t" + intron_count);
						
					}
					
				}
				in.close();
				out_count.write("\n");
				out_rpkm.write("\n");
			}
			
			out_count.close();
			out_rpkm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
