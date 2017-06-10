package stjude.StJudeSoftLinks;

import java.io.File;

import MISC.CommandLine;

public class FlagStatSoftLink {
	public static String parameter_info() {
		return "[rgs Folder] [destination Folder] [type EXOM/TRANSCRIPTOME]";
	}
	public static void execute(String[] args) {
		
		try {
			String queryPath = args[0];
			String destination = args[1];
			String type = args[2]; //EXOME or TRANSCRIPTOME
			
			File f = new File(queryPath);
			File[] fs = f.listFiles();
			for (File folder: fs) {
			
				if (folder.getName().split("_").length < 3) {
					if (folder.isDirectory()) {												
						for (File insideFolder: folder.listFiles()) {							
							if (insideFolder.getName().equals(type)) {
								//System.out.println(insideFolder.getPath());								
								File ds = new File(insideFolder.getPath() + "/bam");
								if (ds.exists()) {
									for (File txtFile: ds.listFiles()) {
										if (!txtFile.isDirectory()) {
											if (txtFile.getName().contains("flagstat")) {
												String script = "ln -s " + txtFile.getPath() + " " + destination + "/" + txtFile.getName();
												File outputFile = new File(destination + "/" + txtFile.getName());
												if (outputFile.exists()) {
													CommandLine.executeCommand("rm " + destination + "/" + txtFile.getName());
													CommandLine.executeCommand(script);
												} else {
													
													CommandLine.executeCommand(script);
												}
											}
											//System.out.println(script);
										}
									}
								}
							}
						}
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
