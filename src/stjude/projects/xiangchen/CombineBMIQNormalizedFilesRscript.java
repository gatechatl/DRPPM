package stjude.projects.xiangchen;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class CombineBMIQNormalizedFilesRscript {

	public static String description() {
		return "Combine the BMIQ normalized samples";
	}
	public static String type() {
		return "XIANGCHEN";
	}
	public static String parameter_info() {
		return "[inputFile] [sampleNameFile] [probeNameIndex] [lastMetaIndex] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String sampleNameFile = args[1];
			int probeNameIndex = new Integer(args[2]);
			int lastMetaIndex = new Integer(args[3]);
			String outputFile = args[4];
			LinkedList sampleName = new LinkedList();
			FileInputStream fstream = new FileInputStream(sampleNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				sampleName.add(str);
			}
			
			System.out.println(create_bind_script(inputFile, sampleName, probeNameIndex + 1, lastMetaIndex + 1, outputFile));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String create_bind_script(String inputFile, LinkedList sampleName, int probeIndex, int lastSampleIndex, String outputFile) {
		String script = "";
		script += "origData = read.csv(\"" + inputFile + "\", header=T, sep=\"\\t\");\n";
		script += "m = paste(origData[," + probeIndex + "])\n";
		Iterator itr = sampleName.iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			script += "data = read.csv(\"" + key + "_normalized_withNAs.txt\", header=T, sep=\"\\t\");\n";
			script += "m = cbind(m, paste(data[,2]))\n";
		}
		script += "m = cbind(m, paste(origData[," + lastSampleIndex + ":length(origData[1,])]))\n";
		script += "write.table(m, file=\"" + outputFile + "\", sep = \"\\t\")\n";
		return script;
	}
}
