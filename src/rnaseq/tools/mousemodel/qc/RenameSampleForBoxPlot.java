package rnaseq.tools.mousemodel.qc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.UUID;

public class RenameSampleForBoxPlot {

	public static void execute(String[] args) {		
		try {
			
			String fileName = args[0];
			String path = args[1];
			String outputPath = args[2];
			
			executeCommand("mkdir " + outputPath);
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String title = in.readLine();
			String[] split_title = title.split("\t");
			String[] genes = new String[split_title.length - 4];
			for (int i = 4; i < split_title.length; i++) {
				genes[i - 4] = split_title[i];
				System.out.println("Genes present: " + genes[i - 4]);
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				boolean[] boo = new boolean[genes.length];
				for (int i = 4; i < split.length; i++) {
					if (split[i].equals("yes")) {
						boo[i - 4] = true;
					} else {
						boo[i - 4] = false;
					}
				}
				String total_filename = split[0].replaceAll("_", ".").replaceAll("-", ".");
				File[] files = new File(path).listFiles();
				
				for (File file: files) {
					//System.out.println(file.getPath());
					if (file.getPath().contains("_total.txt")) {
						String single_file_name = file.getName().replaceAll("_", ".").replaceAll("-", ".");
						if (single_file_name.contains(total_filename)) {
							String newName = total_filename;
							boolean noTagFound = true;
							for (int i = 0; i < boo.length; i++) {
								if (boo[i]) {
									newName += "." + genes[i];
									noTagFound = false;
								}
							}
							if (noTagFound) {
								newName += ".CNTRL_total.txt";
							}
							newName += "_total.txt";
							executeCommand("cp " + file.getPath() + " " + outputPath + "/" + newName);
						}
						
					} else {
						String single_file_name = file.getName().replaceAll("_", ".").replaceAll("-", ".");
						if (single_file_name.contains(total_filename)) {
							String newName = total_filename;
							for (int i = 0; i < boo.length; i++) {
								if (boo[i]) {
									newName += "." + genes[i];
								}
							}
							newName += "_Exon.txt";
							executeCommand("cp " + file.getPath() + " " + outputPath + "/" + newName);
						}
					}
					//}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
