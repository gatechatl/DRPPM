package NetworkTools.STRINGdbParsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Convert2SIFFile {

	public static String type() {
		return "NETWORK";
	}
	public static String description() {
		return "convert a node/edge table into a sif formated table";
	}
	
	public static String parameter_info() {
		return "[inputFile] [index1] [index2] [outputFile] [has header? (true/false)]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			int index1 = new Integer(args[1]);
			int index2 = new Integer(args[2]);
			String outputFile = args[3];
			boolean header = false;
			if (args.length > 4) {
				header = new Boolean(args[4]);
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			//out.write("ProteinA\tConnection\tProteinB\n");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			if (header) {
				in.readLine();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				out.write(split[index1] + "\tconnection\t" + split[index2] + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
