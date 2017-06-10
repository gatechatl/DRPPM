package stjude.projects.singlecellsequencing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateMappingInputFile {

	public static String type() {
		return "STJUDE";
	}
	public static String parameter_info() {
		return "[inputPath] [outputFile]";
	}
	public static String description() {
		return "Generate a mapping input file for single cell sequencing pipeline";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputPath = args[0];
			String outputFile = args[1];
						
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			HashMap map = new HashMap();
			File files = new File(inputPath);
			for (File f: files.listFiles()) {
				String fileName = f.getName();
				String uniqName = "";
				String[] split = fileName.split("_");
				for (int i = 0; i < split.length - 2; i++) {
					if (i == 0) {
						uniqName += split[i];
					} else {
						uniqName += "_" + split[i];
					}
				}
				map.put(uniqName, "");
			}

			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String uniqName = (String)itr.next();
				String R1 = "";
				String R2 = "";
				for (File f: files.listFiles()) {
					String fileName = f.getName();
					if (fileName.contains(uniqName) && fileName.contains("R1")) {
						R1 = fileName;
					}
					if (fileName.contains(uniqName) && fileName.contains("R2")) {
						R2 = fileName;
					}
				}
				if (!uniqName.equals("")) {
					out.write(R1 + "\t" + R2 + "\t" + uniqName + "\n");
				}
			}
				
				
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
