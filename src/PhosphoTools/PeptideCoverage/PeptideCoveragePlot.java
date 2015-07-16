package PhosphoTools.PeptideCoverage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PeptideCoveragePlot {

	public static void execute(String[] args) {
		try {

			HashMap map = new HashMap();
			String fastaFile = args[1];
			String name = "";
			int len = 0;
			boolean first = true;
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					if (!first) {
						map.put(name, len);
					}
					name = str.split(" ")[0].replaceAll(">", "");
					len = 0;
					first = false;
				} else {
					len += str.trim().length();
				}
			}
			in.close();
			map.put(name, len);
			
			//System.out.println(map.size());
			
			String inputFile = args[0];
			System.out.println(inputFile);
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String str = in.readLine();
			String[] split = str.split("\t");
			String protein_name = split[1];
			int length = (Integer)map.get(protein_name);
			in.close();
			
			double[] histogram = new double[length];
			for (int i = 0; i < length; i++) {
				histogram[i] = 0;
			}
			double[] shared_histogram = new double[length];
			for (int i = 0; i < length; i++) {
				shared_histogram[i] = 0;
			}
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				str = in.readLine();
				split = str.split("\t");
				//split[1];
				if (split[1].equals(protein_name)) {
					int NumScans = new Integer(split[2]);
					int start = new Integer(split[4]);
					int end = new Integer(split[5]);
					//System.out.println(split[0] + "\t" + start + "\t" + end);
					for (int i = start; i < end; i++) {
						histogram[i] += NumScans;
					}
				}
			}
			in.close();
			
			String sharedFile = args[2];
			fstream = new FileInputStream(sharedFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				str = in.readLine();
				split = str.split("\t");
				//split[1];
				if (split[1].equals(protein_name)) {
					int NumScans = new Integer(split[2]);
					int start = new Integer(split[4]);
					int end = new Integer(split[5]);
					//System.out.println(split[0] + "\t" + start + "\t" + end);
					for (int i = start; i < end; i++) {
						shared_histogram[i] += NumScans;
					}
				}
			}
			in.close();
			
			String outputFile = args[3];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Type\tPosition\tNum_Scans\n");
			for (int i = 0; i < histogram.length; i++) {
				out.write("Uniq\t" + (i + 1) + "\t" + histogram[i] + "\n");
			}
			for (int i = 0; i < shared_histogram.length; i++) {
				out.write("Shared\t" + (i + 1) + "\t" + shared_histogram[i] + "\n");
			}
			out.close();
			
			String pngFile = args[5];
			String RscriptFile = args[4];
			fwriter = new FileWriter(RscriptFile);
			out = new BufferedWriter(fwriter);
			out.write(generate_line_script(outputFile, pngFile));
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generate_line_script(String inputFile, String outputFile) {
		String script = "";
		script += "library(ggplot2);\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T);\n";
		script += "png(file = \"" + outputFile + "\", width=500,height=400)\n";
		//script += "ggplot(df,aes(x = Position,y = Num_scans,fill=Type)) +\n";
		//script += "geom_area( position = 'stack') +\n";
		//script += "geom_area( position = 'stack', colour=\"black\", show_guide=FALSE)\n";
		script += "ggplot(data, aes(x=Position, y=Num_Scans, fill=Type, group=Type)) + geom_line() + geom_area(alpha = 0.5, position = \"identity\") + labs(title=\"Gene Coverage Plot\");\n";
		script += "dev.off();\n";
		return script;
	}
}
