package RNAseqTools.Summary;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Plot histogram
 * @author tshaw
 *
 */
public class PlotBinningTable {

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Plot a particular sample from the binning table.";
	}
	public static String parameter_info() {
		return "[inputFile] [columnName] [outputPng]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String columnName = args[1];
			String outputPng = args[2];
			
			LinkedList bin = new LinkedList();
			LinkedList value = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				bin.add(split[0]);
				for (int i = 0; i < header_split.length; i++) {
					if (header_split[i].equals(columnName)) {
						value.add(split[i]);
					}
				}
			}
			in.close();
			
			String script = plot_hist(bin, value, outputPng);
			System.out.println(script);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String plot_hist(LinkedList list, LinkedList value, String outputPngFile) {
		String script = "ageRanges <- list(";
		Iterator itr = list.iterator();
		int index = 0;
		while (itr.hasNext()) {
			index++;
			String str = (String)itr.next();
			if (index == list.size()) {
				script += str + "";
			} else {
				script += str + ",";
			}			
		}
		script += ")\n";
		script += "pcPop <- c(";
		Iterator itr2 = value.iterator();
		index = 0;
		while (itr2.hasNext()) {
			index++;
			String str = (String)itr2.next();
			if (index == list.size()) {
				script += str + "";
			} else {
				script += str + ",";
			}			
		}
		script += ")\n";
		script += "ages <- lapply(1:length(ageRanges), function(i) {\n";
		script += "ageRange <- ageRanges[[i]]\n";
		script += "round(runif(pcPop[i] * 100, min=ageRange[1], max=ageRange[length(ageRange)-1]), 0)\n";
		script += "})\n";
		script += "ages <- unlist(ages)\n";
		
		script += "breaks <- append(0, sapply(ageRanges, function(x) x[length(x)]))\n";
		script += "png(\"" + outputPngFile + "\", width = 500, height = 300);\n";
		script += "hist(ages, breaks=breaks)\n";
		script += "dev.off()\n";
		return script;
	}
}
