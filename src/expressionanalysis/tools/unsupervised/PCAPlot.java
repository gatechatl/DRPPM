package expressionanalysis.tools.unsupervised;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class PCAPlot {

	public static void main(String[] args) {
		
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public static void executePCA(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		System.out.println(calculatePCA(inputFile, outputFile));
	}
	public static void executePlotPCA(String[] args) {
		String inputFile = args[0];
		String colorFile = args[1];
		
		System.out.println(generatePCAPlot(inputFile, colorFile));
	}
	/**
	 * Generate PCA plots
	 */
	//public static String generatePCAPlot(String inputFile, String outputFile, String listA, String title, int topCount) {
	public static String calculatePCA(String inputFile, String outputFile) {
		String script = "";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, row.names=1);\n";
		script += "gene=rownames(data)\n";
		script += "allDat = data;\n";
		script += "selection = allDat;\n";
		script += "genenames = gene;\n";
		script += "col_labels = colnames(allDat[1,]);\n";
		script += "sampleNames = col_labels;\n";
		script += "colnames(selection) = col_labels;\n";
		script += "rownames(selection) = genenames;\n";
		script += "mat = selection;\n";
		script += "rownames(mat)=genenames\n";
		script += "isexpr <- rowSums(mat>1) >= 1\n";
		script += "mat <- mat[isexpr,]\n";
		script += "mat <- log2(mat + 0.00001);\n";
		script += "zmat = apply(mat, 1, scale);\n";
		script += "zmat = apply(zmat, 1, rev)\n";
		script += "colnames(zmat) = names(mat)\n";
		script += "mat = as.matrix(zmat);\n";
		script += "colnames = colnames(mat);\n";
		script += "rownames = rownames(mat);\n";
		script += "revmat = apply(mat, 1, rev)\n";
		script += "pca.object <- prcomp(revmat)\n";
		script += "scores = pca.object$x\n";
		script += "var = cumsum((pca.object$sdev)^2) / sum(pca.object$sdev^2)\n";
		script += "varexp = c(var[1], var[2]-var[1], var[3]-var[2])\n";
		script += "write.csv(scores, file=\"" + outputFile + "\");\n";
		script += "write.csv(varexp, file=\"" + outputFile + ".variance\");\n";
		
		return script;
	}
	public static HashMap parseColorFile(String colorFile) {
		HashMap map = new HashMap();
		try {			
			FileInputStream fstream = new FileInputStream(colorFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {					
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * Generate PCA plots
	 */
	//public static String generatePCAPlot(String inputFile, String outputFile, String listA, String title, int topCount) {
	public static String generatePCAPlot(String inputFile, String colorsFile) {
		String script = "";
		script += "import numpy as np\n";
		script += "from mpl_toolkits.mplot3d import Axes3D\n";
		script += "import matplotlib.pyplot as plt\n";
		script += "import csv;\n";
		script += "ifile  = open('" + inputFile + "', \"rb\");\n";
		script += "reader = csv.reader(ifile);\n";
		script += "rownum = 0;\n";
		script += "for row in reader:\n";
		script += "    rownum += 1;\n";
		script += "ifile.close()\n";
		script += "matrix = [[0 for x in range(rownum)] for x in range(3)] ;\n";
		script += "fig = plt.figure()\n";
		script += "ax = fig.add_subplot(111, projection='3d')\n";
		script += "ifile  = open('" + inputFile + "', \"rb\")\n";
		script += "reader = csv.reader(ifile)\n";
		script += "rownum = 0\n";
		script += "for row in reader:\n";
		script += "    if rownum == 0:\n";
		script += "        header = row;\n";
		script += "    else:\n";
		script += "        colnum = 0\n";
		//script += "        matrix[j - 1][rownum - 1] = row[j];\n";
		script += "        row[0];\n";
		script += "        c = 'y';\n";
		script += "        m = 'o';\n";
		
		HashMap map = parseColorFile(colorsFile);
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			script += "        if row[0].find(\"" + key + "\") != -1:\n";
			script += "            c = '" + (String)map.get(key) + "';\n";
		}
		script += "        ax.scatter(float(row[1]), float(row[2]), float(row[3]), c=c, marker=m, s=100)\n";
		script += "    rownum += 1\n";
		script += "ifile.close()\n";
		script += "ax.set_xlabel('PC 1')\n";
		script += "ax.set_ylabel('PC 2')\n";
		script += "ax.set_zlabel('PC 3')\n";
		script += "plt.show();\n";
		return script;
	}
}
