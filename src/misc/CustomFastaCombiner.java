package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CustomFastaCombiner {

	public static String type() {
		return "FASTA";
	}
	public static String description() {
		return "Custom Fasta Combiner";
	}
	public static String parameter_info() {
		return "[humanFasta] [mouseFasta] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String humanFasta = args[0];
			String mouseFasta = args[1];
			String outputFile = args[2];
			
			File f = new File(outputFile);
			if (f.exists()) {
				System.out.println("File exists: " + outputFile);
				System.exit(0);
			}
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(humanFasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				out.write(str + "\n");
			}
			in.close();
			
			fstream = new FileInputStream(mouseFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					str = str.replace(">", ">mchr");
					out.write(str + "\n");
				} else {
					out.write(str + "\n");
				}
			}
			in.close();
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
