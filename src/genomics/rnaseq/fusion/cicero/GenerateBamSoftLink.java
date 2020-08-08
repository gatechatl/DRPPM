package genomics.rnaseq.fusion.cicero;

import java.io.File;

/**
 * For each bam file generate a soft link with a cleanup up name
 * 
 * @author tshaw
 *
 */
public class GenerateBamSoftLink {

	public static void execute(String[] args) {
		try {
			
			String fileFolder = args[0];
			File files = new File(fileFolder);
			for (File file : files.listFiles()) {
		        if (!file.isDirectory()) {
		        	if (file.getName().contains(".bai")) {
		            	String newName = file.getName().split("\\.")[0].split("-")[0];
		            	System.out.println("ln -s " + fileFolder + "/" + file.getName() + " " + fileFolder + "/" + newName + ".bam.bai");		        		
		        	} else if (file.getName().contains(".bam")) {
		            	String newName = file.getName().split("\\.")[0].split("-")[0];
		            	System.out.println("ln -s " + fileFolder + "/" + file.getName() + " " + fileFolder + "/" + newName + ".bam");
		            }		
		        
		        }
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
