package network.layout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class GenerateLayoutForEachHub {

	public static void main(String[] args) {
		System.out.println(generateRandomColor());
	}
	
	public static String description() {
		return "Generate the layout for displaying the hub.";
	}
	public static String type() {
		return "NETWORK";
	}
	public static String parameter_info() {
		return "[inputFile] [numberHorizontally (input numeric)] [outputFile]";
	}
	
	public static void execute(String[] args) {
		String[] colors = {"#60b644", "#0f497b", "#dc8126", "#af74b1", "#d3d3d3", "#0007ef", "#00cd00", "#b26666", "#800080", "#8c1919", "#0000cd"};
		
		try {
			
			String inputFile = args[0];
			int numberHorizontally = new Integer(args[1]); 
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
				
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				list.add(split[0]);
			}
			in.close();
			int x = 0;
			int y = 3000;
			int index = 0;
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				x += 3000;
				if (x == 3000 * (numberHorizontally + 1)) {
					x = 3000;
					y += 3000;
				}
				if (index >= colors.length) {
					out.write(name + "\t" + x + "\t" + y + "\t" + generateRandomColor() + "\n");
				} else {
					out.write(name + "\t" + x + "\t" + y + "\t" + colors[index] + "\n");
				}
				index++;
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateRandomColor() {
		Random rand = new Random();
		String r = Integer.toHexString(rand.nextInt(255));
		if (r.length() == 1) {
			r = "0" + r;
		}
		String g = Integer.toHexString(rand.nextInt(255));
		if (g.length() == 1) {
			g = "0" + g;
		}
		String b = Integer.toHexString(rand.nextInt(255));
		if (b.length() == 1) {
			b = "0" + b;
		}
		return "#" + r + g + b;
	}
}
