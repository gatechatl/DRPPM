package rnaseq.splicing.alternativejuncpipeline.juncsalvager;

import misc.CommandLine;

public class JuncSalvagerSortSumColumn {

	public static String description() {
		return "Sort the column for the quartile barplot.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputQuartileCount] [ascend or descent: true or false] [outputQuartileCount]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			boolean flag = new Boolean(args[1]);
			String outputFile = args[2];
			String script = generate_sort_python_script(inputFile, outputFile, flag);
			CommandLine.writeFile(outputFile + "_python.py", script);
			CommandLine.executeCommand("python " + outputFile + "_python.py");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_sort_python_script(String inputFile, String outputFile, boolean flag) {
		
		String script = "";
		script += "import numpy as np\n";
		script += "import pandas as pd\n";
		script += "data = pd.read_csv(\"" + inputFile + "\", sep=\"\\t\")\n";
		script += "data[\"sum_column\"] = data.sum(axis=1)\n";
		if (flag) {
			script += "data = data.sort_values(\"sum_column\", ascending=True)\n";
		} else {
			script += "data = data.sort_values(\"sum_column\", ascending=False)\n";
		}
		script += "data = data.drop(\"sum_column\", axis = 1)\n";
		script += "data.to_csv(\"" + outputFile + "\", sep=\"\\t\", index=False)\n";
		return script;
	}
}
