package RNAseqTools.SingleCell.MappingPipeline;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Validation of STAR Mapping
 * @author tshaw
 *
 */
public class ValidateSTARMapping {


	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Validation of STAR Mapping by looking for *Log.final.out";
	}
	public static String parameter_info() {
		return "[STARMappingShellScript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String STARMappingShellScript = args[0];
			FileInputStream fstream = new FileInputStream(STARMappingShellScript);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				String tag = "";
				for (int i = 0; i < split.length; i++) {
					if (split[i].equals("--outFileNamePrefix")) {
						tag = split[i + 1];
					}
				}
				File f = new File(tag + "Log.final.out");
				if (!f.exists()) {
					System.out.println(str);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
