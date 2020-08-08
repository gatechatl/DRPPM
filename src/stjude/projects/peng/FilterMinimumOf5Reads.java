package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Customized script for filtering reads based on 5 read cutoff for intron and exon
 * @author tshaw
 *
 */
public class FilterMinimumOf5Reads {
	public static String description() {
		return "Customized script for filtering reads based on 5 read cutoff for intron and exon";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputMatrixFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				boolean skip = false;
				if (new Integer(split[3]) < 5 || new Integer(split[5]) < 5 || new Integer(split[8]) < 5 || new Integer(split[10]) < 5 
						|| new Integer(split[13]) < 5 || new Integer(split[15]) < 5 || new Integer(split[18]) < 5 || new Integer(split[20]) < 5
						|| new Integer(split[23]) < 5 || new Integer(split[25]) < 5 || new Integer(split[28]) < 5 || new Integer(split[30]) < 5) {
					skip = true;
				}
				if (!skip) {
					System.out.println(str);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
