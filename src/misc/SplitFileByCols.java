package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class SplitFileByCols {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		String description = "Split the file with the specified number of cols\n";
		description += "[inputFile] a matrix\n";
		description += "[numRowPerFile] a numerical number/integer. number of cols per file\n";
		return description;
	}
	public static String parameter_info() {
		return "[InputFile] [name_cols: usually 0] [numColPerFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String[] name_cols = args[1].split(",");
			int intColPerFile = new Integer(args[2]);

			int total = 0;
			int name = 0;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split = header.split("\t");
			int number_of_splits = new Double(new Double(header_split.length) / intColPerFile).intValue() + 1;
			FileWriter[] fwriter = new FileWriter[number_of_splits];
			BufferedWriter[] out = new BufferedWriter[number_of_splits];		
			for (int index = 0; index < number_of_splits; index++) {
				boolean write_first = true;
				fwriter[index] = new FileWriter(inputFile + "_" + index);
				out[index] = new BufferedWriter(fwriter[index]);
				for (String name_col: name_cols) {
					if (write_first) {
						out[index].write(header_split[new Integer(name_col)]);
						write_first = false;
					} else {
						out[index].write("\t" + header_split[new Integer(name_col)]);
					}
				}				
			}
			System.out.println("Total number of columns: " + header_split.length);
			System.out.println("Num of splits: " + number_of_splits);
			
			for (int i = 0; i < header_split.length; i++) {
				boolean skip = false;
				for (String name_col: name_cols) {
					if (new Integer(name_col) == i) {
						skip = true;
					}
					int current_index = new Double(new Double(i) / intColPerFile).intValue();
					System.out.println("current_index: " + current_index + "\t" + i);
					if (!skip) {
						out[current_index].write("\t" + header_split[i]);
					}
				}
			}
			for (int index = 0; index < number_of_splits; index++) {
				out[index].write("\n");
			}
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int index = 0; index < number_of_splits; index++) {
					boolean write_first = true;
					for (String name_col: name_cols) {
						if (write_first) {
							out[index].write(split[new Integer(name_col)]);
							write_first = false;
						} else {
							out[index].write("\t" + split[new Integer(name_col)]);
						}
					}				
				}
				
				for (int i = 0; i < split.length; i++) {
					boolean skip = false;
					for (String name_col: name_cols) {
						if (new Integer(name_col) == i) {
							skip = true;
						}
						int current_index = new Double(new Double(i) / intColPerFile).intValue();
						if (!skip) {
							out[current_index].write("\t" + split[i]);
						}
					}
				}
				for (int index = 0; index < number_of_splits; index++) {
					out[index].write("\n");
				}
			}
			in.close();
			for (int index = 0; index < number_of_splits; index++) {
				out[index].close();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
