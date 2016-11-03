package RNAseqTools.SingleCell.CellRanger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import MISC.CommandLine;

/**
 * Takes in the PCA.r output and generate python's scatter plot
 * @author tshaw
 *
 */
public class GeneratePCAScatterPlotPython {

	public static String type() {
		return "CELLRANGER";
	}
	public static String description() {
		return "Takes in the PCA.r output and generate python's scatter plot";
	}
	public static String parameter_info() {
		return "[inputFile] [sampleMetaInfo] [outputMatrix] [outputPythonScript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String sampleMetaInfo = args[1];
			String outputMatrix = args[2];			
			String outputPythonScript = args[3];
			
			FileWriter fwriter = new FileWriter(outputMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap sampleName2groupName = new HashMap();
			HashMap group2color = new HashMap();
			FileInputStream fstream = new FileInputStream(sampleMetaInfo);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String groupName = split[1];
				String colorName = split[2];
				sampleName2groupName.put(sampleName, groupName);
				group2color.put(groupName, colorName);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = (String)in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				String sampleName = split[0].replaceAll("\"", "");
				String value1 = split[1];
				String value2 = split[2];
				String groupName = (String)sampleName2groupName.get(sampleName);
				out.write(sampleName + "\t" + groupName + "\t" + value1 + "\t" + value2 + "\n");
			}
			in.close();									
			out.close();
			
			String pythonScript = generateScatterPlotPythonScript(outputMatrix, outputMatrix + ".pdf", group2color);
			CommandLine.writeFile(outputPythonScript, pythonScript);
			CommandLine.executeCommand("python " + outputPythonScript);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateScatterPlotPythonScript(String inputFile, String outputPDFFile, HashMap groups) {
		String script = "import csv\n";
		script += "import matplotlib\n";
		script += "matplotlib.use('Agg')\n";
		script += "import matplotlib.pyplot as plt\n";
		script += "from matplotlib.backends.backend_pdf import PdfPages\n";
		script += "pp = PdfPages('" + outputPDFFile + "')\n";
		script += "name = []\n";
		script += "group = []\n";
		script += "PC1 = []\n";
		script += "PC2 = []\n";
		script += "\n";
		script += "with open('" + inputFile + "','r') as f:\n";
		script += "\treader = csv.reader(f,delimiter='\t')\n";
		script += "\tfor row in reader:\n";
		script += "\t\tname.append(row[0])\n";
		script += "\t\tgroup.append(row[1])\n";
		script += "\t\tPC1.append(row[2])\n";
		script += "\t\tPC2.append(row[3])\n";
		script += "\n";
		script += "fig, ax = plt.subplots()\n";
		script += "ax.scatter(PC1, PC2, color='gray', alpha=0.5)\n";
		script += "for i, txt in enumerate(group):\n";
		Iterator itr = groups.keySet().iterator();
		while (itr.hasNext()) {
			String group = (String)itr.next();
			String color = (String)groups.get(group);
			script += "\tif group[i] == '" + group + "':\n";
			script += "\t\tax.scatter(PC1[i],PC2[i], color='" + color + "', alpha=0.5)\n";
		}
		
		script += "\n";
		script += "plt.xlabel('PC1')\n";
		script += "plt.ylabel('PC2')\n";
		script += "plt.title('PCA Plot')\n";
		script += "plt.savefig(pp, format='pdf')\n";
		script += "pp.savefig()\n";
		script += "pp.close()\n";
		
		return script;
	}
}
