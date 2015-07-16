package PhosphoTools.MotifTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;


public class GeneratePredictedHistogramDistribution {

	public static void execute(String[] args) {
		
		String motif_all_file = args[0];
		String original_outputFolder = args[1];
		String outputPNGFolder = args[2];
		HashMap kinase_map = MotifTools.grabKinase2Motif(motif_all_file);
		Iterator itr_kinase = kinase_map.keySet().iterator();
		while (itr_kinase.hasNext()) {
			String kinase_name = (String)itr_kinase.next();
			String inputFile = original_outputFolder + "/Predicted_" + kinase_name + "_relative_quantification.txt";
			String script = generateHistogramScript(inputFile, outputPNGFolder + "/" + kinase_name + ".png", kinase_name);
			executeRScript(script, outputPNGFolder);			
		}
		
	}
	public static void executeRScript(String script, String folder) {
		String buffer = UUID.randomUUID().toString();
		writeFile(folder + "/" + buffer, script);
		executeCommand("R --vanilla < " + folder + "/" + buffer);
    	File f = new File(folder + "/" + buffer);
    	f.delete();		
	}
	public static void executeCommand(String executeThis) {
    	try {
    		
    		String buffer = UUID.randomUUID().toString();
        	writeFile(buffer + "tempexecuteCommand.sh", executeThis);
        	String[] command = {"sh", buffer + "tempexecuteCommand.sh"};
        	Process p1 = Runtime.getRuntime().exec(command);
        	BufferedReader inputn = new BufferedReader(new InputStreamReader(p1.getInputStream()));
        	String line=null;
        	while((line=inputn.readLine()) != null) {}
        	inputn.close();
        	p1.destroy();
        	File f = new File(buffer + "tempexecuteCommand.sh");
        	f.delete();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	public static void writeFile(String fileName, String command) {
    	try {
        	FileWriter fwriter2 = new FileWriter(fileName);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            out2.write(command + "\n");
            out2.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	public static String generateHistogramScript(String inputFile, String outputPNG, String geneName) {
		String script = "";
		script += "library(ggplot2);\n";
		//script += "library(gridExtra);\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=F);\n";
		script += "png(file = \"" + outputPNG + "\", width=600,height=500);\n";
		script += "qplot(as.numeric(data.matrix(data[,11])),geom=\"blank\", main=\"Predicted " + geneName + " Pearson Cor Histogram\") + geom_line(aes(y=..density..), stat = 'density') + geom_histogram(aes(y=..density..), alpha = 0.4, colour = \"black\", fill=\"lightblue\") + xlab(\"Spearman Correlation\");\n";
		script += "dev.off();\n";
		return script;
	}
}
