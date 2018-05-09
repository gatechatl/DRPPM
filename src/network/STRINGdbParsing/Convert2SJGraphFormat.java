package network.STRINGdbParsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This is a specialized format by the proteomics group
 * List: GeneA, GeneB, Direction, Action, Function
 * @author tshaw
 *
 */
public class Convert2SJGraphFormat {
	
	public static String type() {
		return "Network";
	}
	public static String description() {
		return "A program to convert any graph format to the proteomics SJGraph Format";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile] [indexGeneA] [indexGeneB] [version#] [author name] [indexDirection] [indexScore] [indexAction] [indexFunction] [header flag true/false]\n * If value is not available put -1";
		
	}
	public static void execute(String[] args) {
		
		try {	
			String inputFile = args[0];
			String outputFile = args[1];
			int indexGeneA = new Integer(args[2]);
			int indexGeneB = new Integer(args[3]);
			String version = args[4];
			String author = args[5];
			boolean header = true;
			File f = new File(inputFile);
			String source = f.getName();
			int indexDirection = -1;
			int indexScore = -1;
			int indexAction = -1;
			int indexFunction = -1;
			if (args.length > 6) {
				indexDirection = new Integer(args[6]);
			}
			if (args.length > 7) {
				indexScore = new Integer(args[7]);
			}
			if (args.length > 8) {
				indexAction = new Integer(args[8]);
			}
			if (args.length > 9) {
				indexFunction = new Integer(args[9]);
			}
			if (args.length > 10) {
				header = new Boolean(args[10]);
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("# version: " + version + "\n");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();			
			out.write("# date: " + dateFormat.format(date) + "\n");
			out.write("# author: " + author + "\n");
			out.write("# source: " + source + "\n");			
			out.write("GeneA\tGeneB\tDirection\tScore\tAction\tFunction\n");
			//out.write("ProteinA\tConnection\tProteinB\n");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			if (header) {
				in.readLine();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneA = split[indexGeneA];
				String geneB = split[indexGeneB];
				String direction = "NA";
				String score = "NA";
				String action = "NA";
				String function = "NA";
				if (indexDirection >= 0) {
					direction = split[indexDirection];
				}
				if (indexScore >= 0) {
					score = split[indexScore];
				}
				if (indexAction >= 0) {
					action = split[indexAction];
				}
				if (indexFunction >= 0) {
					function = split[indexFunction];
				}
				out.write(geneA + "\t" + geneB + "\t" + direction + "\t" + score + "\t" + action + "\t" + function + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
