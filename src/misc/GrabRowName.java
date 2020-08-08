package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class GrabRowName {

	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		String fileName = args[0];
		String outputFile = args[1];
		GetRowNames(fileName, outputFile);
	}
	public static void GetRowNames(String fileName, String outputFile) {
		try {
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(">header\n");
			
			while(in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0] + "\n");
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
