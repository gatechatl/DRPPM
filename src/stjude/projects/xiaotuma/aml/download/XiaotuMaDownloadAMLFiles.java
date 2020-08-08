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
/**
 * For downloading Xiaotu's AML files
 * @author tshaw
 *
 */
public class XiaotuMaDownloadAMLFiles {

	public static String description() {
		return "Download AML files";
	}
	public static String type() {
		return "XIANGCHEN";
	}
	public static String parameter_info() {
		return "[inputFileList: first column contains the address and second column contains the filename] [outputFolderPath]";
	}
	public static void execute(String[] args) throws MalformedURLException, IOException {
		
		String inputFileList = args[0];
		String path = args[1];
		FileInputStream fstream = new FileInputStream(inputFileList);
		DataInputStream din = new DataInputStream(fstream);
		BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
		while (in.ready()) {
			String str = in.readLine();
			String[] split = str.split("\t");
			InputStream inputStream = new URL(split[0]).openStream();
			Files.copy(inputStream, Paths.get(path + "/" + split[1]), StandardCopyOption.REPLACE_EXISTING);
			
		}
		in.close();
		
	}
}
