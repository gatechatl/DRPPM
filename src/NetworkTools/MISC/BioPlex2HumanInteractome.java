package NetworkTools.MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Simple tool for converting bioplex to a human interactome database
 * @author tshaw
 *
 */
public class BioPlex2HumanInteractome {
	
	public static void execute(String[] args) {
		
		try {
			
			
			String fileName = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Entrez Gene IDA\tSymbol A\tEntrez Gene IDB\tSymbol B\n");
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0] + "\t" + split[4] + "\t" + split[1] + "\t" + split[5] + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
