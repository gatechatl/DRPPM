package RNAseqTools.SingleCell.CellOfOrigin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.MathTools;

/**
 * For debugging of methodology purpose. 
 * Generate a matrix separated by the two sample groups.
 * @author tshaw
 *
 */
public class GenerateMatrixForTwoGroups {

	public static String type() {
		return "MATRIX";
	}
	public static String description() {
		return "Generate a matrix table with the two groups side by side";
	}
	public static String parameter_info() {
		return "[TableMatrix] [group1File] [group2File] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String matrixFile = args[0];
			String group1File = args[1];
			String group2File = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			LinkedList group1_list = new LinkedList();
			LinkedList group2_list = new LinkedList();
			FileInputStream fstream = new FileInputStream(group1File);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				group1_list.add(str.trim().replaceAll("-", "."));
				//System.out.println(str.trim().replaceAll("-", "."));
			}
			in.close();
			
			fstream = new FileInputStream(group2File);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				group2_list.add(str.trim().replaceAll("-", "."));
				//System.out.println(str.trim().replaceAll("-", "."));
			}
			in.close();
			
			out.write("Variant");
			Iterator itr = group1_list.iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				out.write("\t" + sample);
			}
			itr = group2_list.iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				out.write("\t" + sample);
			}			
			out.write("\n");
			
			itr = group2_list.iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
			}
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine().replaceAll("-", ".");
			//System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] header_split = header.split("\t");
				String line = split[0];
				itr = group1_list.iterator();
				while (itr.hasNext()) {
					String sample = (String)itr.next();
					for (int i = 1; i < split.length; i++) {
						if (header_split[i].equals(sample)) {
							line += "\t" + split[i];
							break;
						}
					}
				}
				itr = group2_list.iterator();
				while (itr.hasNext()) {
					String sample = (String)itr.next();
					for (int i = 1; i < split.length; i++) {
						if (header_split[i].equals(sample)) {
							line += "\t" + split[i];
							break;
						}
					}
				}

				out.write(line + "\n");
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
