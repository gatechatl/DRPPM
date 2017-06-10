package mathtools.expressionanalysis.differentialexpression;

public class CalculateEdgeR {

}
/*
library(edgeR);
library(limma);
raw.data <- read.delim("C:/Users/tshaw/Desktop/RNASEQ/Mckinnon/Differential_Exon_Junction/RNAseq_Exon_Junction_Normalized.txt");

names = names(raw.data);
#names[c(4, 5, 6)]; # Hour 0
#names[c(7, 8, 9)]; # Hour 2
#names[c(10)]; # Hour 4
#names[c(11, 12, 13)]; # Hour 6
#names[c(14, 15, 16)]; # Hour 12
#names[c(17, 18, 19)]; # Hour 16
#names[c(20, 21)]; # Hour 24
#names[c(22, 23)]; # Hour 48

pot = c(26:28,34:40);
atmtdp1 = c(41:46);
lig4 = c(29:33);
names[pot]
names[atmtdp1]
names[lig4]

other = c(atmtdp1, lig4);
d <- raw.data[c(pot, other)];
rownames(d)=raw.data[,1];
group <- c(rep("Pot", length(pot)), rep("Other", length(other)));

d <- DGEList(counts = d, group = group);
dim(d);
cpm.d <- cpm(d);
d <- d[ rowSums(cpm.d > 1) >=3, ];
#d <- calcNormFactors(d);
d <- estimateCommonDisp(d, verbose=TRUE) # does it make a difference?
#d <- estimateTagwiseDisp(d) # does it make a difference?
et <- exactTest(d);
write.table(topTags(et, 500000), "C:/Users/tshaw/Desktop/RNASEQ/Mckinnon/Differential_Exon_Junction/Diff_Exon_Result_POT_vs_Other.txt");

png("C:/Users/tshaw/Desktop/RNASEQ/Mckinnon/Differential_Exon_Junction/SmearPlot_POT_vs_Other.png" , width=800, height=600);
de <- decideTestsDGE(et, p.value=0.05)
detags <- rownames(d)[as.logical(de)]
plotSmear(et, de.tags=detags)
abline(h = c(-2, 2), col = "blue")
dev.off();


other = c(pot, lig4);
d <- raw.data[c(atmtdp1, other)];
rownames(d)=raw.data[,1];
group <- c(rep("atmtdp1", length(atmtdp1)), rep("Other", length(other)));

d <- DGEList(counts = d, group = group);
dim(d);
cpm.d <- cpm(d);
d <- d[ rowSums(cpm.d > 1) >=3, ];
#d <- calcNormFactors(d);
d <- estimateCommonDisp(d, verbose=TRUE) # does it make a difference?
#d <- estimateTagwiseDisp(d) # does it make a difference?
et <- exactTest(d);
write.table(topTags(et, 500000), "C:/Users/tshaw/Desktop/RNASEQ/Mckinnon/Differential_Exon_Junction/Diff_Exon_Result_Atmtdp1_vs_Other.txt");


png("C:/Users/tshaw/Desktop/RNASEQ/Mckinnon/Differential_Exon_Junction/SmearPlot_Atmtdp1_vs_Other.png" , width=800, height=600);
de <- decideTestsDGE(et, p.value=0.05)
detags <- rownames(d)[as.logical(de)]
plotSmear(et, de.tags=detags)
abline(h = c(-2, 2), col = "blue")
dev.off();


other = c(pot, atmtdp1);
d <- raw.data[c(lig4, other)];
rownames(d)=raw.data[,1];
group <- c(rep("lig4", length(lig4)), rep("Other", length(other)));

d <- DGEList(counts = d, group = group);
dim(d);
cpm.d <- cpm(d);
d <- d[ rowSums(cpm.d > 1) >=3, ];
#d <- calcNormFactors(d);
d <- estimateCommonDisp(d, verbose=TRUE) # does it make a difference?
#d <- estimateTagwiseDisp(d) # does it make a difference?
et <- exactTest(d);
write.table(topTags(et, 500000), "C:/Users/tshaw/Desktop/RNASEQ/Mckinnon/Differential_Exon_Junction/Diff_Exon_Result_Lig4_vs_Other.txt");

png("C:/Users/tshaw/Desktop/RNASEQ/Mckinnon/Differential_Exon_Junction/SmearPlot_Lig4_vs_Other.png" , width=800, height=600);
de <- decideTestsDGE(et, p.value=0.05)
detags <- rownames(d)[as.logical(de)]
plotSmear(et, de.tags=detags)
abline(h = c(-2, 2), col = "blue")
dev.off();


d <- raw.data[c(pot, lig4, atmtdp1)];

group <- c(names[c(pot, lig4, atmtdp1)]); #rep("Pot", length(pot)), rep("Lig4", length(lig4)), rep("Atmtdp1", length(atmtdp1)));

d <- DGEList(counts = d, group = group);
dim(d);
cpm.d <- cpm(d);
d <- d[ rowSums(cpm.d > 1) >=3, ];
#d <- calcNormFactors(d);

png("C:/Users/tshaw/Desktop/RNASEQ/Mckinnon/Differential_Exon_Junction/MDSplot.png" , width=600, height=800);

plotMDS(d, xlim=c(-5,3));

dev.off();

######################################################


*/