package rnaseq.splicing.alternativejuncpipeline.juncsalvager;

import misc.CommandLine;

public class JuncSalvagerSortSumColumn {

	public static String description() {
		return "Sort the column for the barplot.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputQuartileCount] [outputQuartileCount]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[2];
			String script = generate_sort_python_script(inputFile, outputFile);
			CommandLine.writeFile(outputFile + "_python.py", script);
			CommandLine.executeCommand("python " + outputFile + "_python.py");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_sort_python_script(String inputFile, String outputFile) {
		
		String script = "";
		script += "import numpy as np\n";
		script += "import pandas as pd\n";
		script += "data = pd.read_csv(\"" + inputFile + "\", sep=\"\\t\")\n";
		script += "data[\"sum_column\"] = data.sum(axis=1)\n";
		script += "data = data.sort_values(\"sum_column\", ascending=False)\n";
		script += "data = data.drop(\"sum_column\", axis = 1)\n";
		script += "data.to_csv(\"" + outputFile + "\", sep=\"\\t\", index=False)\n";
		return script;
	}
}
