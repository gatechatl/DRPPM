package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import misc.CommandLine;

public class McKinnonGCScanner {

	public static String description() {
		return "Generate GC content scanner";
	}
	public static String type() {
		return "MCKINNON";
	}
	public static String parameter_info() {
		return "[inputFastaFile] [indexBEDFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFastaFile = args[0];
			String inputBEDFile = args[1];

			
			HashMap bed = new HashMap();
			FileInputStream fstream = new FileInputStream(inputBEDFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[4];
				String chr = split[0];
				String start = split[1];
				String end = split[2];
				String dir = split[5];
				String tag = chr + ":" + start + "-" + end + "(" + dir + ")";
				bed.put(tag, geneName);
				
			}
			in.close();			
			
			fstream = new FileInputStream(inputFastaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String fasta_tag = in.readLine();
				String tag = fasta_tag.replaceAll(">", "");
				System.out.println(fasta_tag);
				String geneName = (String)bed.get(tag);
				String seq = in.readLine().toUpperCase();
				
				String outputFile = geneName + "_1000.fasta";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write(">" + geneName + " " + tag + "\n");
				out.write(seq);
				out.close();
				String script = "drppm -GCScanner " + outputFile + " 100 -1000 > " + outputFile + ".ScannerResult.txt";
				CommandLine.executeCommand(script);				
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
