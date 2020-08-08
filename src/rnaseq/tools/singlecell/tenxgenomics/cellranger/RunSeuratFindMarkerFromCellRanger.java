package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import java.io.File;

public class RunSeuratFindMarkerFromCellRanger {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Generate Seurat's analysis pipeline";
	}
	public static String parameter_info() {
		return "[folderPath] [prefix] [outputFolder] [resolution] [num_clusters]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folderPath = args[0];
			String prefix = args[1]; // 
			String outputFolder = args[2];
			File f = new File(outputFolder);
			if (!f.exists()) {				
				f.mkdir();
			}
			double resolution = new Double(args[3]);
			int num_clusters = new Integer(args[4]);
			System.out.println(findMarkerScript(folderPath, prefix, outputFolder, resolution, num_clusters));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		//
	public static String findMarkerScript(String folder, String prefix, String outputFolder, double resolution, int clusters) {
		String script = "library(Seurat)\n";
		script += "library(dplyr)\n";
		script += "library(Matrix)\n";
		script += "single.data0 <- Read10X(data.dir = \"" + folder + "\")\n";
		script += "at_least_one0 <- apply(single.data0, 2, function(x) sum(x>0))\n";
		script += "tmp <- apply(single.data0, 1, function(x) sum(x>0))\n";
		script += "keep <- tmp>=3\n";
		script += "tmp <- single.data0[keep,]\n";
		script += "at_least_one0 <- apply(tmp, 2, function(x) sum(x>0))\n";
		
		script += "single.data0 <- CreateSeuratObject(counts = single.data0, min.cells = 3, min.features = 200)\n";
		script += "single = single.data0\n";				
		script += "single <- NormalizeData(object = single, normalization.method = \"LogNormalize\", scale.factor = 1e4)\n";
		script += "single <- FindVariableFeatures(object = single, mean.function = \"FastExpMean\", dispersion.function = \"FastLogVMR\", mean.cutoff=c(0.0125, 3), dispersion.cutoff = c(0, 0.5))\n";
		script += "single <- ScaleData(object = single)\n";
		script += "single <- RunPCA(object = single, pc.genes = single@var.genes)\n";
		script += "single <- FindNeighbors(object = single)\n";
		script += "single <- FindClusters(object = single, reduction.type = \"pca\",dims.use = 1:10, resolution = " + resolution + ")\n";
		for (int i = 0; i < clusters; i++) {
			script += "cluster" + i + " = FindMarkers(single, ident.1 = " + i + ", min.pct = 0.25);\n";
	        script += "write.table(cluster" + i + " , file = \"" + outputFolder + "/" + prefix + "_Cluster" + i + "_markers.txt\", sep=\"\\t\")\n";
	        script += "\n";	        
		}
		
		return script;
	}
}
