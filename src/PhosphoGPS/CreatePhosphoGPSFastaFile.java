package PhosphoGPS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CreatePhosphoGPSFastaFile {

	public static void execute(String[] args) {
		
		try {
			
			String fileName = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String name = str.replaceAll(">", "").split("\t")[0];
					String seq = in.readLine().split("\t")[0];
					
					String newseq = seq.replaceAll("T\\*", "pT").replaceAll("S\\*", "pS").replaceAll("Y\\*", "pY");
					out.write(">" + name + "\n");
					out.write(newseq + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
