package stjude.projects.jinghuizhang.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * For each folder downloaded from GDC. We will iterate through the files and generate a matrix for each cancer type.
 * @author tshaw
 *
 */
public class JinghuiZhangGenerateTCGAMatrixSampleID {

	public static String description() {
		return "For each folder downloaded from GDC. We will iterate through the files and generate a matrix for each cancer type.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[folderPath] [referenceFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folderPath = args[0];
			String referenceFile = args[1];
			String outputFile = args[2];
			
			
			
			
			HashMap sampleName = new HashMap();
			FileInputStream fstream = new FileInputStream(referenceFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sampleName.put(split[0], split[4] + "_" + split[6]);
				
			}
			in.close();
			
			HashMap newGene = new HashMap();
			boolean first = true;
			LinkedList sampleName_list = new LinkedList();
			HashMap matrix = new HashMap();
			File folder = new File(folderPath);
			for (File otherFolders: folder.listFiles()) {
				if (otherFolders.isDirectory()) {
					if (sampleName.containsKey(otherFolders.getName())) {
						String new_sample_name = (String)sampleName.get(otherFolders.getName());
						for (File f: otherFolders.listFiles()) {
							if (f.getName().contains("FPKM.txt")) {
								sampleName_list.add(new_sample_name);
								fstream = new FileInputStream(f.getPath());
								din = new DataInputStream(fstream);
								in = new BufferedReader(new InputStreamReader(din));
								while (in.ready()) {
									String str = in.readLine();
									String[] split = str.split("\t");
									if (matrix.containsKey(split[0])) {
										String line = (String)matrix.get(split[0]);
										line += "\t" + split[1];
										matrix.put(split[0], line);
									} else {
										matrix.put(split[0], split[1]);
									}								
									if (first) {
										newGene.put(split[0], split[0]);
									} else {
										if (!newGene.containsKey(split[0])) {
											System.out.println("Unknown gene id. Need to remove certain gene names.");
											System.exit(0);
										}
									}
								}
								in.close();
								first = false;
							}
						}
					}
				}
			}
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName");
			Iterator itr = sampleName_list.iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				out.write("\t" + name);
			}
			out.write("\n");
			
			itr = matrix.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write(geneName);
				String line = (String)matrix.get(geneName);
				out.write("\t" + line + "\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
