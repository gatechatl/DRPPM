package genomics.rnaseq.expression.transcriptionfactornetwork;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Generate GMT file based on the Aracne output file.
 * @author tshaw
 *
 */
public class ConvertAracneOutput2GMT {

	public static String type() {
		return "ARACNE";
	}
	public static String description() {
		return "Generate GMT file based on the output file.";
	}
	public static String parameter_info() {
		return "[outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String outputFile = args[0];
			FileInputStream fstream = new FileInputStream(outputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.contains(">")) {
					
					String[] split = str.split("\t");
					String line = split[0] + "\tAracneOutput";
					for (int i = 1; i < split.length; i = i + 2) {
						line += "\t" + split[i];
					}
					System.out.println(line);
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
