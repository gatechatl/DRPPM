package PhosphositePlusTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.UUID;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class DownloadAllPossibleSiteInfo {

	public static void execute(String[] args) {
		
		try {
			
			for (int i = 900; i < 100000; i++) {
				
				
				downloadFile("http://www.phosphosite.org/siteAction.do?id=" + i, "File" + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void downloadFile(String path, String fileName) {
		try {
			URL website = new URL(path);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
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
