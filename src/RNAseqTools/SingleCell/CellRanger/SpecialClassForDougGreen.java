package RNAseqTools.SingleCell.CellRanger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class SpecialClassForDougGreen {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Generate color the meta data Doug Green's 10X dropseq data";
	}
	public static String parameter_info() {
		return "[inputFile1] [inputFile2]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile1 = args[0];
			String inputFile2 = args[1];
			FileInputStream fstream = new FileInputStream(inputFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine().replaceAll("\"", "");
			String[] header_split = header.split("\t");
			for (int i = 0; i < header_split.length; i++) {
				System.out.println(header_split[i].replaceAll("-", ".") + "\t" + "group1" + "\t" + "color1");
			}
			in.close();			
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine().replaceAll("\"", "");
			header_split = header.split("\t");
			for (int i = 0; i < header_split.length; i++) {
				System.out.println(header_split[i].replaceAll("-", ".") + "\t" + "group2" + "\t" + "color2");
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
