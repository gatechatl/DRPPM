package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import java.io.File;

/**
 * Run the Seurat analysis from cell ranger
 * https://davetang.org/muse/2017/08/01/getting-started-seurat/
 * 
 * Worked with the following dependencies
 * module load R/3.5.1-rh7
 * module load hdf5_18/1.8.18
 * install.packages("devtools")
 * source("http://bioconductor.org/biocLite.R")
 * biocLite()
 * biocLite(c("DDRTree", "pheatmap"))
 * install.packages('Seurat') 
 * library(Seurat)
 * which version was Seurat? Seurat_3.0.1
 * @author tshaw
 * 
 */
public class RunSeuratAnalysisFromCellRanger {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Generate Seurat's analysis pipeline";
	}
	public static String parameter_info() {
		return "[folderPath] [prefix] [outputFolder] [organism: human or mouse] [cutoff_filter: true or false] [min gene per cell] [max gene per cell]";
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
			String organism = args[3]; // human or mouse
			boolean cutoff_filter_bool = new Boolean(args[4]); // filter based on expression cutoff
			double min = new Double(args[5]);
			double max = new Double(args[6]);
			System.out.println(generateSeuratAnalysisScript(folderPath, prefix, outputFolder, organism, cutoff_filter_bool, min, max));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateSeuratAnalysisScript(String folderPath, String prefix, String outputFolder, String organism, boolean filter, double lower_gene_num_threshold, double max_gene_num_threshold) {
		
		String script = "";
		script += "sink(\"" + outputFolder + "/logofcode.txt\")\n";
		script += "library(Seurat)\n";
		script += "library(dplyr)\n";
		script += "library(Matrix)\n";
		 
		String[] split_folder_path = folderPath.split(",");
		for (int i = 0; i < split_folder_path.length; i++) {
			script += "single.data" + i + " <- Read10X(data.dir = \"" + split_folder_path[i] + "\")\n";							
			
			script += "at_least_one" + i + " <- apply(single.data" + i + ", 2, function(x) sum(x>0))\n";
			
			script += "pdf(\"" + outputFolder + "/1_" + prefix + "_single.data" + i + "_Histogram_At_Least_One.pdf\")\n";
			script += "hist(at_least_one" + i + ", breaks = 100,\n";
				script += "main = \"Distribution of detected genes\",\n";
				script += "xlab = \"Genes with at least one tag\")\n";
	 
			script += "dev.off()\n";
			
			script += "\n";
			script += "pdf(\"" + outputFolder + "/2_" + prefix + "_Single.Data" + i + "_Histogram_Expr_Sum_per_Cell.pdf\")\n";
			script += "hist(colSums(single.data" + i + "),\n";
				script += "breaks = 100, main = \"Expression sum per cell\",\n";
				script += "xlab = \"Sum expression\")\n";
			
			script += "dev.off()\n";
			
			script += "\n";
			script += "tmp <- apply(single.data" + i + ", 1, function(x) sum(x>0))\n";
			script += "table(tmp>=3)\n";
			
			script += "\n";
			script += "keep <- tmp>=3\n";
			script += "tmp <- single.data" + i + "[keep,]\n";
			script += "at_least_one" + i + " <- apply(tmp, 2, function(x) sum(x>0))\n";
			script += "summary(at_least_one" + i + ")\n"; 
			script += "\n";
			
			script += "dim(tmp)\n";
			script += "\n";
						
			script += "single.data" + i + " <- CreateSeuratObject(counts = single.data" + i + ", min.cells = 3, min.features = 200)\n";

	        script += "pdf(\"" + outputFolder + "/4_" + prefix + "_data" + i + "Total_Expr_Before_Norm.pdf\")\n";
	        script += "hist(colSums(single.data" + i + "$RNA@data),\n";
	        script += "breaks = 100,\n";
	        script += "main = \"Total expression before normalisation\",\n";
	        script += "xlab = \"Sum of expression\")\n";
	        script += "dev.off();\n";

			script += "single.data" + i + " <- NormalizeData(object = single.data" + i + ", normalization.method = \"LogNormalize\", scale.factor = 1e4)\n";

	        script += "pdf(\"" + outputFolder + "/5_" + prefix + "_data" + i + "_Total_Expr_After_Norm.pdf\")\n";
	        script += "hist(colSums(single.data" + i + "$RNA@data),\n";
	        script += "breaks = 100,\n";
	        script += "main = \"Total expression after normalisation\",\n";
	        script += "xlab = \"Sum of expression\")\n";
	        script += "dev.off();\n";
	        script += "single.data" + i + " <- FindVariableFeatures(object = single.data" + i + ", selection.method = \"vst\")\n";
			/*if (i == 1) {
				script += "single <- MergeSeurat(object1 = single0, object2 = single1, add.cell.id1 = \"" + prefix + "\", add.cell.id2 = \"" + prefix + i + "\", project = \"" + prefix + "\")\n";
			} else if (i > 1) {
				script += "single <- MergeSeurat(object1 = single, object2 = single" + i + ", add.cell.id1 = \"" + prefix + "\", add.cell.id2 = \"" + prefix + i + "\", project = \"" + prefix + "\")\n";
			}*/
		}
		if (split_folder_path.length > 1) {
			String list_str = "";
			for (int i = 1; i < split_folder_path.length; i++) {
				if (i == 1) {
					list_str = "single.data" + i;
				} else {
					list_str += ",single.data" + i;
				}
			}
			

			script += "single <- merge(x = single.data0, y = list(" + list_str + "))\n";
		} else {
			script += "single = single.data0\n";
			
	        script += "pdf(\"" + outputFolder + "/4_" + prefix + "_Total_Expr_Before_Norm.pdf\")\n";
	        script += "hist(colSums(single$RNA@data),\n";
	        script += "breaks = 100,\n";
	        script += "main = \"Total expression before normalisation\",\n";
	        script += "xlab = \"Sum of expression\")\n";
	        script += "dev.off();\n";

			script += "single <- NormalizeData(object = single, normalization.method = \"LogNormalize\", scale.factor = 1e4)\n";

	        script += "pdf(\"" + outputFolder + "/5_" + prefix + "_Total_Expr_After_Norm.pdf\")\n";
	        script += "hist(colSums(single$RNA@data),\n";
	        script += "breaks = 100,\n";
	        script += "main = \"Total expression after normalisation\",\n";
	        script += "xlab = \"Sum of expression\")\n";
	        script += "dev.off();\n";
	        //script += "single <- FindVariableFeatures(object = single, mean.function = \"FastExpMean\", dispersion.function = \"FastLogVMR\", mean.cutoff=c(0.0125, 3), dispersion.cutoff = c(0, 0.5))\n";
		}

		script += "single <- FindVariableFeatures(object = single, mean.function = \"FastExpMean\", dispersion.function = \"FastLogVMR\", mean.cutoff=c(0.0125, 3), dispersion.cutoff = c(0, 0.5))\n";		
		script += "single <- ScaleData(object = single)\n";
		script += "single <- RunPCA(object = single, pc.genes = single@var.genes)\n";
		script += "single <- FindNeighbors(object = single)\n";
		
		script += "single <- FindClusters(object = single, reduction.type = \"pca\",dims.use = 1:10, resolution = 0.6)\n";
		
		script += "single <- RunTSNE(object = single, check_duplicates = FALSE, dims.use = 1:10)\n";
		

        script += "write.table(single@active.ident, file = \"" + outputFolder + "/" + prefix + "_CellsIdentity_Res.txt\",sep=\"\\t\")\n";
        
        script += "write.table(single$tsne@cell.embeddings, file = \"" + outputFolder + "/" + prefix + "_TSNE_Res.txt\",sep=\"\\t\")\n";
        script += "\n";
		script += "pdf(\"" + outputFolder + "/6_" + prefix + "_PCAPlot.pdf\")\n";
        script += "DimPlot(object = single, reduction = \"pca\")\n";
        script += "dev.off();\n";
        script += "\n";

        script += "pdf(\"" + outputFolder + "/8_" + prefix + "_TSNEPlot.pdf\")\n";
        script += "DimPlot(object = single, reduction = \"tsne\")\n";
        script += "dev.off();\n";
        
		int[] perplexities = {30, 50, 100, 10, 20, 40};
		double[] resolutions = {0.6, 0.4, 0.9, 0.1, 0.2, 0.3, 0.5, 0.7, 0.8, 1.0};
		for (double resolution: resolutions) {
			for (int perplexity: perplexities) {
				script += "single <- FindClusters(object = single, dims.use = 1:10, resolution = " + resolution + ")\n";
				
				script += "single <- RunTSNE(object = single, dims.use = 1:10, check_duplicates = FALSE, perplexity = " + perplexity + ")\n";
				
		
		        script += "write.table(single@active.ident, file = \"" + outputFolder + "/" + prefix + "_CellsIdentity_Res" + resolution + "_Per" + perplexity + ".txt\",sep=\"\\t\")\n";
		        
		        script += "write.table(single$tsne@cell.embeddings, file = \"" + outputFolder + "/" + prefix + "_TSNE_Res" + resolution + "_Per" + perplexity + ".txt\",sep=\"\\t\")\n";
		        script += "\n";
				script += "pdf(\"" + outputFolder + "/6_" + prefix + "_PCAPlot.pdf\")\n";
		        script += "DimPlot(object = single, reduction = \"pca\")\n";
		        script += "dev.off();\n";
		        script += "\n";
		
		        script += "pdf(\"" + outputFolder + "/8_" + prefix + "_TSNEPlot_Res" + resolution + "_Per" + perplexity + ".pdf\")\n";
		        script += "DimPlot(object = single, reduction = \"tsne\")\n";
		        script += "dev.off();\n";
			}
		}

		/*
		script += "single <- CreateSeuratObject(raw.data = single.data,\n";
        script += "min.cells = 3,\n";
        script += "min.genes = 200,\n";
        script += "project = \"10X_Project\")\n";
        script += "\n";
        
		if (organism.toUpperCase().equals("MOUSE")) {
			script += "mito.genes <- grep(pattern = \"^mt-\", x = rownames(x = single@data), value = TRUE)\n";
		} else {
			script += "mito.genes <- grep(pattern = \"^MT-\", x = rownames(x = single@data), value = TRUE)\n";
		}
		script += "percent.mito <- Matrix::colSums(single@raw.data[mito.genes, ]) / Matrix::colSums(single@raw.data)\n";
		
		script += "single <- AddMetaData(object = single,\n";
        script += "metadata = percent.mito,\n";
        script += "col.name = \"percent.mito\")\n";
                
        script += "pdf(\"" + outputFolder + "/3_" + prefix + "_ViolinPlot_Gene_UMI_Mito.pdf\")\n";
        script += "VlnPlot(object = single,\n";
        script += "features.plot = c(\"nGene\", \"nUMI\", \"percent.mito\"),\n";
        script += "nCol = 3)\n";
        script += "dev.off()\n";
        script += "\n";
        
        script += "pdf(\"" + outputFolder + "/3_" + prefix + "_UMI_Percent_Mito_nGene.pdf\")\n";
        script += "par(mfrow = c(1, 2))\n";
        script += "GenePlot(object = single, gene1 = \"nUMI\", gene2 = \"percent.mito\", pch.use = '.')\n";
        script += "GenePlot(object = single, gene1 = \"nUMI\", gene2 = \"nGene\", pch.use = '.')\n";
        script += "dev.off();\n";
        script += "\n";
        
        // filter cells? might skip this step        
        script += "#single <- FilterCells(object = single,\n";
        script += "#subset.names = c(\"nGene\", \"percent.mito\"),\n";
        script += "#low.thresholds = c(200, -Inf), # need to set lower threshold\n";
        script += "#high.thresholds = c(2500, 0.05)) # need to set upper threshold\n";
        script += "\n";
        
        script += "pdf(\"" + outputFolder + "/4_" + prefix + "_Total_Expr_Before_Norm.pdf\")\n";
        script += "hist(colSums(single@data),\n";
        script += "breaks = 100,\n";
        script += "main = \"Total expression before normalisation\",\n";
        script += "xlab = \"Sum of expression\")\n";
        script += "dev.off();\n";
        
        script += "\n";
        script += "single <- NormalizeData(object = single,\n";
        script += "normalization.method = \"LogNormalize\",\n";
        script += "scale.factor = 1e4)\n";
        
        script += "pdf(\"" + outputFolder + "/5_" + prefix + "_Total_Expr_After_Norm.pdf\")\n";
        script += "hist(colSums(single@data),\n";
        script += "breaks = 100,\n";
        script += "main = \"Total expression after normalisation\",\n";
        script += "xlab = \"Sum of expression\")\n";
        script += "dev.off();\n";
        
        script += "\n";
        script += "single <- FindVariableGenes(object = single,\n";
        script += "mean.function = ExpMean,\n";
        script += "dispersion.function = LogVMR,\n";
        script += "x.low.cutoff = 0.0125,\n";
        script += "x.high.cutoff = 3,\n";
        script += "y.cutoff = 0.5)\n";
        
        script += "single <- ScaleData(object = single,\n";
        script += "vars.to.regress = c(\"nUMI\", \"percent.mito\"))\n";
        
        script += "single <- RunPCA(object = single,\n";
        script += "pc.genes = single@var.genes,\n";
        script += "do.print = TRUE,\n";
        script += "pcs.print = 1:5,\n";
        script += "genes.print = 5)\n";
        
        script += "VizPCA(object = single, pcs.use = 1:2)\n";
        
        script += "pdf(\"" + outputFolder + "/6_" + prefix + "_PCAPlot.pdf\")\n";
        script += "PCAPlot(object = single, dim.1 = 1, dim.2 = 2)\n";
        script += "dev.off();\n";
        script += "\n";
        
        script += "single <- ProjectPCA(object = single, do.print = FALSE)\n";
        
        script += "pdf(\"" + outputFolder + "/7_" + prefix + "_PCAHeatmapPlot.pdf\")\n";
        script += "PCHeatmap(object = single,\n";
        script += "pc.use = 1,\n";
        script += "cells.use = 500,\n";
        script += "do.balanced = TRUE,\n";
        script += "label.columns = FALSE)\n";
        script += "dev.off();\n";
        script += "\n";
        
        // time consuming provide option to comment out
        script += "#single <- JackStraw(object = single,\n";
        script += "#num.replicate = 100,\n";
        script += "#do.print = FALSE)\n";
        
        // determine the number of principle component to use
        script += "#pdf(\"" + outputFolder + "/" + prefix + "_JackStrawPlot.pdf\")\n";
        script += "#JackStrawPlot(object = single, PCs = 1:12)\n";
        script += "#dev.off();\n";
        script += "\n";
        
        script += "#pdf(\"" + outputFolder + "/" + prefix + "_BowlPlot.pdf\")\n";
        script += "#PCElbowPlot(object = single)\n";
        script += "#dev.off();\n";
        script += "\n";
        
        script += "single <- FindClusters(object = single,\n";
        script += "reduction.type = \"pca\",\n";
        script += "dims.use = 1:10,\n";
        script += "resolution = 0.6,\n";
        script += "print.output = 0,\n";
        script += "save.SNN = TRUE)\n";
        
        script += "PrintFindClustersParams(object = single)\n";
        
        script += "single <- RunTSNE(object = single,\n";
        script += "dims.use = 1:10,\n";
        script += "do.fast = TRUE)\n";
        
        script += "pdf(\"" + outputFolder + "/8_" + prefix + "_TSNEPlot.pdf\")\n";
        script += "TSNEPlot(object = single, do.label = TRUE)\n";
        script += "dev.off();\n";
        script += "\n";
        
        script += "#cluster1.markers <- FindMarkers(object = single,\n";
        script += "#ident.1 = 1,\n";
        script += "#min.pct = 0.25)\n";
        
        script += "#cluster5.markers <- FindMarkers(object = single,\n";
        script += "#ident.1 = 5,\n";
        script += "#ident.2 = c(0,3),\n";
        script += "#min.pct = 0.25)\n";
        
        script += "single.markers <- FindAllMarkers(object = single,\n";
        script += "only.pos = TRUE,\n";
        script += "min.pct = 0.25,\n";
        script += "thresh.use = 0.25)\n";
        
        script += "write.table(single.markers, file = \"" + outputFolder + "/" + prefix + "_Markers.txt\", sep=\"\\t\")\n";
        script += "\n";
        
        script += "write.table(single@ident, file = \"" + outputFolder + "/" + prefix + "_CellsIdentity.txt\",sep=\"\\t\")\n";
        
        script += "write.table(single@dr$tsne@cell.embeddings, file = \"" + outputFolder + "/" + prefix + "_TSNE.txt\",sep=\"\\t\")\n";

        script += "\n";        
        script += "single.markers %>%\n";
        script += "group_by(cluster) %>%\n";
        script += "top_n(10, avg_logFC) -> top10\n";
        
        script += "pdf(\"" + outputFolder + "/10_" + prefix + "_TopDiffHeatmap.pdf\")\n";        
        script += "DoHeatmap(object = single,\n";
        script += "genes.use = top10$gene,\n";
        //script += "order.by.ident = TRUE,\n";
        script += "cex.row = 8,\n";
        script += "slim.col.label = TRUE,\n";
        script += "remove.key = TRUE)\n";
        script += "dev.off();\n";
        script += "\n";
        
        script += "#my_bimod <- FindMarkers(object = single,\n";
        script += "#ident.1 = 1,\n";
        script += "#thresh.use = 0.25,\n";
        script += "#test.use = \"bimod\",\n";
        script += "#only.pos = TRUE)\n";
        script += "\n";
        
        script += "#my_roc <- FindMarkers(object = single,\n";
        script += "#ident.1 = 1,\n";
        script += "#thresh.use = 0.25,\n";
        script += "#test.use = \"roc\",\n";
        script += "#only.pos = TRUE)\n";
        script += "\n";
        
        script += "#my_t <- FindMarkers(object = single,\n";
        script += "#ident.1 = 1,\n";
        script += "#thresh.use = 0.25,\n";
        script += "#test.use = \"t\",\n";
        script += "#only.pos = TRUE)\n";
        script += "\n";
        
        script += "#my_tobit <- FindMarkers(object = single,\n";
        script += "#ident.1 = 1,\n";
        script += "#thresh.use = 0.25,\n";
        script += "#test.use = \"tobit\",\n";
        script += "#only.pos = TRUE)\n";
        
        script += "\n";
        script += "#pdf(\"" + outputFolder + "/" + prefix + "_ViolinPlotFeatures.pdf\")\n";
        script += "#VlnPlot(object = single, features.plot = c(\"MS4A1\", \"CD79A\"))\n";
        script += "#dev.off();\n";
        script += "\n";
        
        script += "#pdf(\"" + outputFolder + "/" + prefix + "_ViolinPlotFeatures.pdf\")\n";
        script += "#VlnPlot(object = single,\n";
        script += "#features.plot = c(\"NKG7\", \"PF4\"),\n";
        script += "#use.raw = TRUE,\n";
        script += "#y.log = TRUE)\n";
        script += "#dev.off();\n";        
        script += "\n";
        
        script += "#pdf(\"" + outputFolder + "/" + prefix + "_FeaturePlot.pdf\")\n";
        script += "#FeaturePlot(object = single,\n";
        script += "#features.plot = c(\"MS4A1\", \"GNLY\", \"CD3E\", \"CD14\", \"FCER1A\", \"FCGR3A\", \"LYZ\", \"PPBP\", \"CD8A\"),\n";
        script += "#cols.use = c(\"grey\", \"blue\"),\n";
        script += "#reduction.use = \"tsne\")\n";
        script += "#dev.off();\n";
        script += "\n";
        */
        
		return script;
	}
}
