package rnaseq.tools.singlecell.mapping.pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Combine two separate folder's fastq into a single file
 * @author tshaw
 *
 */
public class CombineFastqFiles {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Combine two separate folder's fastq into a single file";
	}
	public static String parameter_info() {
		return "[folder1] [folder2] [outputLstFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folder1 = args[0];
			String folder2 = args[1];
			String outputPath = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			File files = new File(folder1);
			for (File f: files.listFiles()) {
				if (!f.getName().contains("unpaired") && f.getName().contains(".trim.fastq")) {
					String name = f.getName();
					String tag = "";
					String[] split = name.split("_");
					for (int i = 0; i < split.length - 3; i++) {
						if (tag.equals("")) {
							tag = split[i];
						} else {
							tag += "_" + split[i];
						}
					}
					map.put(tag, tag);
				}
			}
			
			File files2 = new File(folder2);
			for (File f: files2.listFiles()) {
				if (!f.getName().contains("unpaired") && f.getName().contains(".trim.fastq")) {
					String name = f.getName();
					String tag = "";
					String[] split = name.split("_");
					for (int i = 0; i < split.length - 3; i++) {
						if (tag.equals("")) {
							tag = split[i];
						} else {
							tag += "_" + split[i];
						}
					}
					map.put(tag, tag);
				}
			}
			HashMap write_once = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				
				String outputFile_r1 = outputPath + "/" + tag + "_R1.trim.fastq";
				FileWriter fwriter_r1 = new FileWriter(outputFile_r1);
	            BufferedWriter out_r1 = new BufferedWriter(fwriter_r1);

	            String outputFile_r2 = outputPath + "/" + tag + "_R2.trim.fastq";
	            FileWriter fwriter_r2 = new FileWriter(outputFile_r2);
	            BufferedWriter out_r2 = new BufferedWriter(fwriter_r2);

	            if (!write_once.containsKey(outputFile_r1 + "\t" + outputFile_r2 + "\t" + tag + "\n")) {
					out.write(outputFile_r1 + "\t" + outputFile_r2 + "\t" + tag + "\n");
					out.flush();
					write_once.put(outputFile_r1 + "\t" + outputFile_r2 + "\t" + tag + "\n", "");
				}
	            
				for (File f: files.listFiles()) {
					String path1 = "";
					String path2 = "";
					String name = "";
					if (f.getName().contains(tag) && !f.getName().contains("unpaired") && f.getName().contains(".trim.fastq") && f.getName().contains("R1")) {
						path1 = f.getPath();
						
						FileInputStream fstream = new FileInputStream(f.getPath());
						DataInputStream din = new DataInputStream(fstream);
						BufferedReader in = new BufferedReader(new InputStreamReader(din));
						while (in.ready()) {
							String str = in.readLine();
							out_r1.write(str + "\n");
						}
						in.close();
						
					}
					if (f.getName().contains(tag) && !f.getName().contains("unpaired") && f.getName().contains(".trim.fastq") && f.getName().contains("R2")) {
						path2 = f.getPath();
						FileInputStream fstream = new FileInputStream(f.getPath());
						DataInputStream din = new DataInputStream(fstream);
						BufferedReader in = new BufferedReader(new InputStreamReader(din));
						while (in.ready()) {
							String str = in.readLine();
							out_r2.write(str + "\n");
						}
						in.close();
					}
					
				}
				out_r1.flush();
				out_r2.flush();
				for (File f: files2.listFiles()) {
					String path1 = "";
					String path2 = "";
					String name = "";
					if (f.getName().contains(tag) && !f.getName().contains("unpaired") && f.getName().contains(".trim.fastq") && f.getName().contains("R1")) {
						path1 = f.getPath();
						
						FileInputStream fstream = new FileInputStream(f.getPath());
						DataInputStream din = new DataInputStream(fstream);
						BufferedReader in = new BufferedReader(new InputStreamReader(din));
						while (in.ready()) {
							String str = in.readLine();
							out_r1.write(str + "\n");
						}
						in.close();
					}
					if (f.getName().contains(tag) && !f.getName().contains("unpaired") && f.getName().contains(".trim.fastq") && f.getName().contains("R2")) {
						path2 = f.getPath();
						FileInputStream fstream = new FileInputStream(f.getPath());
						DataInputStream din = new DataInputStream(fstream);
						BufferedReader in = new BufferedReader(new InputStreamReader(din));
						while (in.ready()) {
							String str = in.readLine();
							out_r2.write(str + "\n");
						}
						in.close();
					}
					//out.write(path1 + "\t" + path2 + "\t" + tag + "\n");
					
				}
				out_r1.close();
				out_r2.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
