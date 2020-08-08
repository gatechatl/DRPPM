package stjude.projects.xiangchen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CombineBMIQNormalizedFiles {

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
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream[] fstreams = new FileInputStream[sampleName.size()];
			DataInputStream[] dins = new DataInputStream[sampleName.size()];
			BufferedReader[] ins = new BufferedReader[sampleName.size()];
			HashMap map = new HashMap();
			LinkedList probes = new LinkedList();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[probeNameIndex]);
			int j = 0;
			Iterator itr = sampleName.iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out.write("\t" + key);
				fstreams[j] = new FileInputStream(key + "_normalized_withNAs.txt");
				dins[j] = new DataInputStream(fstreams[j]);
				ins[j] = new BufferedReader(new InputStreamReader(dins[j]));
				ins[j].readLine(); // header
				j++;
			}
			for (int i = lastMetaIndex; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\n");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				probes.add(split[probeNameIndex]);
				String meta = split[lastMetaIndex];
				for (int i = lastMetaIndex + 1; i < split.length; i++) {
					meta += "\t" + split[i];
				}
				map.put(split[probeNameIndex], meta);
			}
			in.close();
			// iterate through individual probes
			Iterator itr_probes = probes.iterator();
			while (itr_probes.hasNext()) {
				String probe = (String)itr_probes.next();
				out.write(probe);
				j = 0;
				itr = sampleName.iterator();
				// reading individual files
				while (itr.hasNext()) {
					String key = (String)itr.next();
	
					String str = ins[j].readLine(); // read a line from each file
					String[] split = str.split("\t");					
					out.write("\t" + split[1]);
					j++;
				} // itr
				String meta = (String)map.get(probe);
				out.write("\t" + meta + "\n");
			} // itr_probes
			//System.out.println(create_bind_script(inputFile, sampleName, probeNameIndex + 1, lastMetaIndex + 1, outputFile));
			out.close();
			
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
