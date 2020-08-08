package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * A temporary class for 
 * @author tshaw
 *
 */
public class MISCConvertPeptideID {

	public static String description() {
		return "Convert Centric Peptide to Full Peptide";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputFile] [oneHitWonderFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String oneFile = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String peptide = str.split(";")[0];
				String center = peptide.split("\\.")[1];
				map.put(center, peptide);
			}
			in.close();

			HashMap uniq = new HashMap();
			int num = 1;
			fstream = new FileInputStream(oneFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = (String)map.get(split[1]);
				if (!uniq.containsKey(peptide)) {
					String line = "";
					for (int i = 2; i < split.length; i++) {
						if (line.equals("")) {
							line = split[i];
						} else {
							line += "\t" + split[i];
						}
					}
					System.out.println("peptide_" + num + " = " + peptide);
					num++;
					out.write(split[0] + "\t" + peptide + "\t" + line + "\n");
					//System.out.println(split[0] + "\t" + peptide + "\t" + line);
					uniq.put(peptide, peptide);
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
