package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;


/**
 * 
 * @author tshaw
 *
 */
public class Disorder2BEDFile {

	public static String type() {
		return "BED";
	}
	public static String description() {
		return "Convert ensembl to BED file.";
	}
	public static String parameter_info() {
		return "[inputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];			
			System.out.println("track name=DisorderRegion description=\"Clone Paired Reads\" useScore=1");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[3];
				String starts = split[4];
				String ends = split[5];
				String[] start = starts.split(",");
				String[] end = ends.split(",");
				for (int i = 0; i < start.length; i++) {
					System.out.println(chr + "\t" + start[i] + "\t" + end[i]);
				}
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
