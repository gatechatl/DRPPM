package protein.features.ensembl.membranebound;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FastaFilterSequenceLength {

	public static String type() {
		return "FASTA";
	}
	public static String description() {
		return "Extract fasta line with less than 8000 aa";
	}
	public static String parameter_info() {
		return "[inputFasta] [ouutputFasta]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFasta = args[0];
			String ouutputFasta = args[1];

			FileWriter fwriter = new FileWriter(ouutputFasta);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String name = "NA";
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str;
				} else {
					if (str.length() < 8000) {
						out.write(name + "\n" + str + "\n");
					}
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
