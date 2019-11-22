package stjude.projects.xiaotuma.aml.download;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class XiaotuMaDownloadFile {

	
	public static void main(String[] args) throws MalformedURLException, IOException {
		
		String inputFileList = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\tim_test\\1201-1300.txt";
		FileInputStream fstream = new FileInputStream(inputFileList);
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
		while (in.ready()) {
			String str = in.readLine();
			String[] split = str.split("\t");
			InputStream inputStream = new URL(split[0]).openStream();
			Files.copy(inputStream, Paths.get("\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\tim_test\\" + split[1]), StandardCopyOption.REPLACE_EXISTING);
			
		}
		in.close();
		
	}
}
