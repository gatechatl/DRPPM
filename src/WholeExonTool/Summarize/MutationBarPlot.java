package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MutationBarPlot {

	public static String type() {
		return "EXONCAP";
	}
	public static String description() {
		return "Generate break point output file from cicero result";
	}
	public static String parameter_info() {
		return "[inputFile] [organismChromosome] [qualityFilter] [organism] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String chromosomeLengthFile = args[1];
			String qualityFilter = args[2];
			String organism = args[3];
			String outputFile = args[4];						
			
			HashMap map = new HashMap();
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
            out.write("Chromosome\tPosition\tType\n");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = split[1];
				
				String chr1 = split[4];
				
				int loc1 = new Integer(split[5]);
				String chr2 = split[9];
				
				/*if (organism.equals("MOUSE")) {
					if (chr1.equals("X")) {
						chr1 = "20";
					}
					if (chr2.equals("X")) {
						chr2 = "20";
					}
					if (chr1.equals("Y")) {
						chr1 = "21";
					}
					if (chr2.equals("Y")) {
						chr2 = "21";
					}
				}*/
				int loc2 = new Integer(split[10]);
				String rearrangement = split[12];
				boolean flag = false;
				if (qualityFilter.contains(split[1])) {
					if (chr1.equals(chr2)) {
						if (Math.abs(loc2 - loc1) > 1000000) {
							flag = true;
						}
					} else {
						flag = true;
					}
				}
				if (flag) {
					if (!chr1.equals("chrX") && !chr1.equals("chrY") && !chr1.equals("chrM")) {
						if (!map.containsKey(sample + chr1 + loc1)) {
							out.write(chr1.replace("chr", "") + "\t" + loc1 + "\t" + rearrangement + "\n");
						}
					}
					if (!chr2.equals("chrX") && !chr2.equals("chrY") && !chr2.equals("chrM")) {
						if (!map.containsKey(sample + chr2 + loc2)) {
							out.write(chr2.replace("chr", "") + "\t" + loc2 + "\t" + rearrangement + "\n");
						}
					}
					map.put(sample + chr1 + loc1, "");
					map.put(sample + chr2 + loc2, "");
				}
				
			}
			in.close();
			out.close();
			
			System.out.println(generate_script(chromosomeLengthFile, outputFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Based on the script from http://stackoverflow.com/questions/33727432/how-to-plot-positions-along-a-chromosome-graphic
	 * @param chromFile
	 * @param markFile
	 * @return
	 */
	public static String generate_script(String chromFile, String markFile) {
		String script = "";
		script += "png(file = \"" + markFile + ".png\", width=700,height=500)\n";				
		script += "data = read.csv(\"" + chromFile + "\", sep = \"\\t\")\n";
		script += "marks = read.csv(\"" + markFile + "\", sep = \"\\t\")\n";
		script += "bp <- barplot(data$size, border=NA, col=\"grey80\", names.arg=data$Chromosome)\n";
		script += "with(marks,\n";
		script += "segments(\n";
		script += "bp[Chromosome,]-0.5,\n";
		script += "Position,\n";
		script += "bp[Chromosome,]+0.5,\n";
		script += "Position,\n";
		script += "col=rgb(0, 0, 0, 0.3),\n";
		script += "lwd=2,\n"; 
		script += "lend=1\n";
		
		script += ")\n";
		script += ")\n";
		script += "dev.off();\n";
		
		return script;
	}
}
