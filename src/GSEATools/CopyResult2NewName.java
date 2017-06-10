package GSEATools;

import java.io.File;

public class CopyResult2NewName {

	
	public static void execute(String[] args) {
		
		String folder = args[0];
		File file = new File(folder);
		File[] files = file.listFiles();
		for (File folders: files) {
			if (folders.isDirectory()) {
				
			}
		}
	}
}
