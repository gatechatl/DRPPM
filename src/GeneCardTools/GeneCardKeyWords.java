package GeneCardTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import MISC.CommandLine;

/**
 * Gene Card Keywords
 * @author tshaw
 *
 */
public class GeneCardKeyWords {

	public static String parameter_info() {
		return "[inputFile] [GeneIndex] [keyword (split by ,)]";
	}
	public static String[] getRemaining(String[] args) {
		String[] args_remain = new String[args.length - 2];
		boolean startQuotation = false;
		LinkedList list = new LinkedList();
		String longStr = "";

		for (int i = 2; i < args.length; i++) {
			// System.out.println("args: " + args[i]);
			if (args[i].contains("\"") && !startQuotation) {
				startQuotation = true;
				longStr = args[i];
			} else if (args[i].contains("\"") && startQuotation) {
				longStr += " " + args[i];
				longStr.replaceAll("\"", "");
				list.add(longStr);
				startQuotation = false;
			} else {
				list.add(args[i]);
			}
		}

		int i = 0;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			args_remain[i] = str;
			// System.out.println(args_remain[i]);
			i++;
		}
		return args_remain;
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];			
			int geneIndex = new Integer(args[1]);
			String[] keywords = getRemaining(args); //<em>RNA binding<
			/*for (int i = 0; i < keywords.length; i++) {
				System.out.println(keywords[i]);
			}*/
			boolean[] found = new boolean[keywords.length];
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String gene = split[geneIndex].replaceAll("\"", "").toUpperCase();
				String script = "wget http://www.genecards.org/cgi-bin/carddisp.pl?gene=" + gene + " -O temp_output";
				CommandLine.executeCommand(script);

				
				for (int i = 0; i < found.length; i++) {
					found[i] = false;
				}
				File f = new File("temp_output");
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream("temp_output");
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine().trim().replaceAll(" ", "_");
						int i = 0;
						for (String keyword: keywords) {
							if (str2.contains(keyword)) {
								found[i] = true;
							}
							i++;
						}
					}
					in2.close();
					int i = 0;
					String result = "";
					for (String keyword: keywords) {
						if (result.equals("")) {
							result = found[i] + "";
						} else {
							result += "\t" + found[i];
						}
						i++;
					}
					System.out.println(str + "\t" + result);
					f.delete();
				}
				//File f = new File("temp_output");
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
