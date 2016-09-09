package TextMiningSoftwareAnnotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import MISC.CommandLine;

/**
 * Customized website keyword search (text mining)
 * @author tshaw
 *
 */
public class WebTextMining {

	public static String type() {
		return "TEXTMING";
	}
	public static String description() {
		return "Customized website keyword search (text mining)";
	}
	public static String parameter_info() {
		return "[websitePath] [tagListFile] [keyword] [outputFIle]";
	}
	public static void execute(String[] args) {
		
		try {
			String path = args[0];
			String tagListFile = args[1];
			String keyword = args[2];
			String outputFile = args[3];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(tagListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String script = "wget " + path + str.trim() + " -O wgetoutput";
				CommandLine.executeCommand(script);
				
				boolean found = false;
				FileInputStream fstream2 = new FileInputStream("wgetoutput");
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine().toUpperCase();
					if (str2.contains(keyword)) {
						found = true;
					}
				}
				File f = new File("wgetoutput");
				f.delete();
				
				if (found) {
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
