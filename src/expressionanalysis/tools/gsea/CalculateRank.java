package expressionanalysis.tools.gsea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.UUID;


/**
 * Create an R script for calculating the rank of genes
 * @author tshaw
 *
 */
public class CalculateRank {
	
	public static void execute(String[] args) {
		
		try {
			String script = createRankScript(args[0]);
			String buffer = UUID.randomUUID().toString();
			writeFile(buffer, script);
			executeCommand("R --vanilla < " + buffer);
			executeCommand("rm -rf " + buffer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String createRankScript(String fileName) {
		
		String result = "data = read.csv(\"" + fileName + "\", skip=2, header=T, as.is=T, sep=\"\\t\")\n";
		result += "data[1,]\n";
		result += "data2 = data\n";
		result += "for (i in 3:dim(data2)[2]) {\n";
		result += "x = data[[i]]\n";
		result += "#y = -(rank(x, tie=\"random\")-dim(data)[1]-1)\n";
		result += "y = -(rank(x, tie=\"first\")-dim(data)[1]-1)\n";
		result += "data2[[i]] = y\n";
		result += "}\n";
		result += "write.table(data2, file=\"exp_rank.txt\",  row.names=F, quote=F, sep=\"\\t\")\n";
		result += "q()\n";
		return result;
		
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

	/*
	
			*/
}

