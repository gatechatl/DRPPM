package expressionanalysis.tools.boxplot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateExpressionBoxPlot {

	public static String description() {
		return "Generate boxplot based on expression and annotation file.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputAnnotationFile] [type_index] [matrixFile] [geneName] [outputFile] [outputRscript]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputAnnotationFile = args[0];
			int type_index = new Integer(args[1]);			
			String matrixFile = args[2];
			String geneName = args[3];
			String unit = args[4];
			String outputFile = args[5];
			String outputRscript = args[6];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\t" + unit + "\tType\n");
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputAnnotationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[type_index])) {
					LinkedList list = (LinkedList)map.get(split[type_index]);
					list.add(split[0]);
					map.put(split[type_index], list);

				} else {
					LinkedList list = new LinkedList();
					list.add(split[0]);
					map.put(split[type_index], list);
				}				
			}
			in.close();			
			
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			String[] samples = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals(geneName)) {
					for (int i = 1; i < samples.length; i++) {
						String sampleName = samples[i];
						Iterator itr = map.keySet().iterator();
						String defined_type = "";
						while (itr.hasNext()) {
							String type = (String)itr.next();
							LinkedList list = (LinkedList)map.get(type);
							if (list.contains(sampleName)) {
								defined_type = type;
							}
						}
						out.write(sampleName + "\t" + split[i] + "\t" + defined_type + "\n");
					}
				}
			}
			in.close();
			out.close();
			
			FileWriter fwriter_R = new FileWriter(outputRscript);
			BufferedWriter out_R = new BufferedWriter(fwriter_R);
			out_R.write("library(ggplot2);\n");
			out_R.write("options(bitmapType='cairo')\n");
			out_R.write("data = read.table(\"" + outputFile + "\", sep=\"\\t\",header=T);\n");			
			out_R.write("p1 = ggplot(data, aes(factor(Type), " + unit + ")) + geom_boxplot() + theme(plot.title=element_text(size=20), axis.text.x = element_text(angle = 70, size=20), axis.text.y = element_text(size=20), axis.title = element_text(size=20), legend.text = element_text(size=20), legend.title = element_text(size=20)) + geom_dotplot(binaxis='y', stackdir='center', stackratio=1.0, dotsize=0.2) + theme_bw() + ggtitle(\"" + geneName + "\");\n");
			out_R.write("png(file = \"" + outputFile + ".boxplot.png\", width=800,height=750)\n");
			out_R.write("p1\n");
			out_R.write("dev.off();\n");
			out_R.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
