package stjude.projects.jinghuizhang.alexgout.cloudproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AlexGoutAppendMutations {


	public static String description() {
		return "Append mutation information to the sample list.";
	}
	public static String type() {
		return "Cloud";
	}
	public static String parameter_info() {
		return "[inputFile] [mutationFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String mutationFile = args[1];
			String outputFile = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(mutationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0].split("-")[0].split("\\.")[0], str);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0].split("-")[0].split("\\.")[0])) {
					String line = (String)map.get(split[0].split("-")[0].split("\\.")[0]);
					out.write(line + "\n");
				} else {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
}
