package RNAseqTools.circos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * For every 5 million generate a binned height
 * @author tshaw
 *
 */
public class GenerateCircosCoverageBed {

	public static String type() {
		return "CIRCOS";
	}
	public static String description() {
		return "Generate Circos Coverage Plot";
	}
	public static String parameter_info() {
		return "[inputBedGraphFile] [offset buffer: 1000000] [organism mm/hs]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int offset = new Integer(args[1]);
			String organism = args[2];
			int buffer = offset;
			int score = 0;
			int length = 0;
			int max = 0;
			int min = 0;
			String prev_chr = "";
			LinkedList list = new LinkedList();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0].replaceAll("chr", organism);
				
				int start = new Integer(split[1]);
				int end = new Integer(split[2]);
				int coverage = new Integer(split[3]);
				if (!chr.equals(prev_chr)) {
					//list.add(prev_chr + "\t" + max + "\t" + buffer + "\t" + (score / length));
					if (length > 0) {
						System.out.println(prev_chr + "\t" + min + "\t" + max + "\t" + (score / length));
					}
					score = 0;
					length = 0;
					buffer = offset;
					prev_chr = chr;
					max = 0;
					min = 0;
				}
				// for regions with at least 10 reads covered calculate a binnin
				if (coverage > 0) {
					int range = end - start;
					length += end - start;
					score += range * coverage;
					max = end;
					
					if (end > buffer) {
						//list.add(chr + "\t" + (buffer - 5000000) + "\t" + buffer + "\t" + (score / length));
						System.out.println(chr + "\t" + min + "\t" + buffer + "\t" + (score / length));
						min = buffer;					
						buffer += offset;						
						score = 0;
						length = 0;
						
					}
				}
			}
			in.close();									
			System.out.println(prev_chr + "\t" + min + "\t" + max + "\t" + (score / length));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
