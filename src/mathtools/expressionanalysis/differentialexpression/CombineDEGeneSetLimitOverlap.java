package mathtools.expressionanalysis.differentialexpression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CombineDEGeneSetLimitOverlap {
	public static String type() {
		return "DE";
	}
	public static String description() {
		return "Combine geneset between two LIMMA result, but only print out overlapping DE set";
	}
	public static String parameter_info() {
		return "[inputFileUp1] [inputFileUp2] [inputFileDn1] [inputFileDn2] [inputFileDEAll1] [inputFileDEAll2] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileUp1 = args[0];
			String inputFileUp2 = args[1];
			String inputFileDn1 = args[2];
			String inputFileDn2 = args[3];
			String inputFileAll1 = args[4];
			String inputFileAll2 = args[5];
			String outputFile = args[6];
			
			HashMap allUPDE1 = new HashMap();
			HashMap allDNDE1 = new HashMap();
			
			HashMap overlap = new HashMap();
			HashMap map1 = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFileUp1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header1 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				allUPDE1.put(split[0], split[0]);
			}
			in.close();
			
			HashMap map2 = new HashMap();
			fstream = new FileInputStream(inputFileUp2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String header2 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (allUPDE1.containsKey(split[0])) {
					overlap.put(split[0], split[0]);
				}
				//allDE.put(split[0], split[0]);
				
			}
			in.close();
			
			// all the down samples
			fstream = new FileInputStream(inputFileDn1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header1 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				allDNDE1.put(split[0], split[0]);
			}
			in.close();
			
			
			fstream = new FileInputStream(inputFileDn2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			header2 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (allDNDE1.containsKey(split[0])) {
					overlap.put(split[0], split[0]);
				}
				//allDE.put(split[0], split[0]);
				
			}
			in.close();
			
			
			fstream = new FileInputStream(inputFileAll1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String header3 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (overlap.containsKey(split[0])) {
					map1.put(split[0], str);
				}								
			}
			in.close();
			
			fstream = new FileInputStream(inputFileAll2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String header4 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (overlap.containsKey(split[0])) {
					map2.put(split[0], str);
				}								
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Gene1\t" + header1 + "\tGene2\t" + header2 + "\n");
			Iterator itr = overlap.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String map1_line = (String)map1.get(geneName);
				String map2_line = (String)map2.get(geneName);
				out.write(map1_line + "\t" + map2_line + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
