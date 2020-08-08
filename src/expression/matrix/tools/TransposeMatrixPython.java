package expression.matrix.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;

import misc.CommandLine;

public class TransposeMatrixPython {


	public static String description() {
		return "Transpose the matrix (flip the matrix).";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile] [outputPythonScript]";
	}
	
	public static void execute(String[] args) {
		try {
			String inputFile = args[0];
			String outputFile = args[1];
			String outputpythonScript = args[2];
			
			FileWriter fwriter = new FileWriter(outputpythonScript);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(generate_convert_script(inputFile));
			out.close();
			CommandLine.executeCommand("python " + outputpythonScript + " > " + outputFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generate_convert_script(String inputFileName) {
		String script = "with open('" + inputFileName + "') as file:\n";
		script += "    lis = [x.replace('\\n', '').split('\\t') for x in file]\n";
		script += "\n";
		script += "# normal text file\n";
		script += "for x in zip(*lis):\n";
		script += "    for y in x:\n";
		script += "        print(y+'\\t', end='')\n";
		script += "    print('')\n";
		return script;
	}
}
