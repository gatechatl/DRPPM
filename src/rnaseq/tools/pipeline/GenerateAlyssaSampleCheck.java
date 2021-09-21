package rnaseq.tools.pipeline;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class GenerateAlyssaSampleCheck {


	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Generate python script to perform sample QC check";
	}
	public static String parameter_info() {
		return "[ no parameters... ]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFolder = args[0];
			String outputFilePrefix = args[1];

			FileWriter fwriter = new FileWriter("samplecheck.py");
			BufferedWriter out = new BufferedWriter(fwriter);						
			StringBuffer string_buffer = new StringBuffer();		
			string_buffer.append("#! /bin/python2.7.9\n");
			string_buffer.append("\n");
			string_buffer.append("import sys\n");
			string_buffer.append("import csv\n");
			string_buffer.append("import argparse\n");
			string_buffer.append("\n");
			string_buffer.append("parser=argparse.ArgumentParser(description='Compare starting sample list with global summary table output to ensure number of samples and sample names match.')\n");
			string_buffer.append("\n");
			string_buffer.append("parser.add_argument('-s','--start', type=argparse.FileType('r'), required=True, metavar='', help='path to starting .lst file')\n");
			string_buffer.append("parser.add_argument('-e','--end', type=argparse.FileType('r'), required=True, metavar='', help='path to global_summary.tsv file')\n");
			string_buffer.append("\n");
			string_buffer.append("args=parser.parse_args()\n");
			string_buffer.append("\n");
			string_buffer.append("#open files\n");
			string_buffer.append("starlst=args.start\n");
			string_buffer.append("gsummfil=args.end\n");
			string_buffer.append("\n");
			string_buffer.append("#read as tab delim\n");
			string_buffer.append("starl=csv.reader(starlst, delimiter='\t')\n");
			string_buffer.append("gsumm=csv.reader(gsummfil, delimiter='\t')\n");
			string_buffer.append("\n");
			string_buffer.append("#make list of sample names from each file\n");
			string_buffer.append("slist=[i[0] for i in starl]\n");
			string_buffer.append("glist=[i[0] for i in gsumm]\n");
			string_buffer.append("\n");
			string_buffer.append("#remove 'Sample' header from global summary table\n");
			string_buffer.append("glist.remove('Sample')\n");
			string_buffer.append("\n");
			string_buffer.append("#compare list lengths and missing values\n");
			string_buffer.append("if len(slist) != len(glist):\n");
			string_buffer.append("	print \"Length of of starting sample list and samples in global summary table does not match.\"\n");
			string_buffer.append("	sset=set(slist)\n");
			string_buffer.append("	gset=set(glist)\n");
			string_buffer.append("	missing=list(sorted(sset-gset))\n");
			string_buffer.append("	added=list(sorted(gset-sset))\n");
			string_buffer.append("	if len(missing) > 0:\n");
			string_buffer.append("		print \"Samples missing from global summary table: \", missing\n");
			string_buffer.append("	if len(added) > 0:\n");
			string_buffer.append("		print \"Samples missing from starting sample list: \", added\n");
			string_buffer.append("elif len(slist) == len(glist):\n");
			string_buffer.append("	sset=set(slist)\n");
			string_buffer.append("	gset=set(glist)\n");
			string_buffer.append("	missing=list(sorted(sset-gset))\n");
			string_buffer.append("	added=list(sorted(gset-sset))\n");
			string_buffer.append("	if len(missing) > 0:\n");
			string_buffer.append("		print \"Length of of starting sample list and samples in global summary table match, but some sample names do not match.\"\n");
			string_buffer.append("		print \"Samples missing from global summary table: \", missing\n");
			string_buffer.append("	if len(added) > 0:\n");
			string_buffer.append("		print \"Length of of starting sample list and samples in global summary table match, but some sample names do not match.\"\n");
			string_buffer.append("		print \"Samples missing from starting sample list: \", added\n");
			string_buffer.append("	else:\n");
			string_buffer.append("		print \"Sample names and length of starting sample list and samples in global summary table match.\"\n");

			out.write(string_buffer.toString());
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
