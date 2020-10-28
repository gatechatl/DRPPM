package expressionanalysis.tools.partial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Given a matrix, partial out the user defined covariate. Return a new matrix that represents the intercept + resid. 
 * @author gatechatl
 *
 */
public class GenerateScriptForPartiallingOut {


	public static String description() {
		return "Generate a matrix after partialling out the covariate.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [covariate index] [outputScriptFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int index = new Integer(args[1]); 
			String outputScriptFile = args[2];
			String outputFile = args[3];
			
			HashMap group2color = new HashMap();
        	
			FileWriter fwriter = new FileWriter(outputScriptFile);
            BufferedWriter out = new BufferedWriter(fwriter);           
            
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split = header.split("\t");
			out.write("data = read.table(\"" + inputFile + "\", sep=\"\\t\", header = T, row.names=1);\n");
			for (int i = 1; i < split.length; i++) {
				if (i != index) {
					out.write("r" + i + " = resid(m" + i + " <- lm(" + split[i] + " ~ " + split[index] + ", data = data))\n");
					out.write(split[i] + " = coef(m" + i + ")[\"(Intercept)\"] + r" + i + "\n");
				}
			}
			boolean first = true;
			out.write("out = cbind(");
			for (int i = 1; i < split.length; i++) {
				if (i != index) {
					if (first) {
						out.write(split[i]);
					} else{
						out.write("," + split[i]);
					}
					first = false;
				}
			}
			out.write(")\n");
			out.write("write.table(out, file = \"" + outputFile + "\", sep=\"\\t\", col.names=NA)\n");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
