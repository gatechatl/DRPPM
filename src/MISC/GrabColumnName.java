package MISC;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class GrabColumnName {

	public static void main(String[] args) {
		try {
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void execute(String[] args) {
		String fileName = args[0];
		String outputFile = args[1];
		GetNames(fileName, outputFile);
	}
	public static void GetNames(String fileName, String outputFile) {
		try {
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			in.close();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String[] split = header.split("\t");
			for (int i = 1; i < split.length; i++) {
				out.write(split[i] + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}