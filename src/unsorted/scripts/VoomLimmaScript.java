package unsorted.scripts;

public class VoomLimmaScript {

	/**
#!/bin env Rscript
rm(list=ls(all=TRUE))
options(stringsAsFactors = FALSE);
library(edgeR)
library(limma)


# provide two input arguments:
# 1. data file
# 2. meta file


# read variables
args   <- commandArgs(TRUE)
fn     <- "Rh18_HBX_SUD_RNAseq_Exon_Junction.txt"
fn_meta<- "sample_meta.txt"
setwd("C:/Users/gwu/Desktop/Projects/Sudemycin/Rh18_junction")

# read meta data
targets <- readTargets(fn_meta)
targets <- targets[1:6,]
rownames(targets)=targets$ID

GD <- factor(targets$Treatment)
design <- model.matrix(~0+GD)

# read data
counts <- read.table(fn, sep ="\t", header=TRUE, row.names = 1)
junc.annot=counts[,1:6]
counts=counts[,c(colnames(counts)[1:2],rownames(targets))]

colnames(counts)[3:ncol(counts)] <- targets[colnames(counts)[3:ncol(counts)],"Sample_Name"]

counts2 <-as.matrix(counts[,3:ncol(counts)])

#TMM normalization and voom transformation
dge <- DGEList(counts=counts2)
keep <- rowSums(cpm(dge)>1) >= 2
dge <- dge[keep, , keep.lib.sizes=FALSE]

dge <- calcNormFactors(dge)
v <- voom(dge,design,plot=TRUE)
pdf("MA.plots_cpm1Filtered.pdf",width=8,height=11)
par(mfrow=c(3,2))
for (i in 2:ncol(counts2)){
 M = v$E[,i] - v$E[,1]
 A = (v$E[,i] + v$E[,1]) / 2
 plot(M~A,xlab="AverageExpression",ylab="logFC",pch=".",cex=2,
 main=paste(colnames(v$E)[i],colnames(v$E)[1],sep=" vs "))
 abline(h=0)
}
dev.off()

fit <- lmFit(v,design)

cont.matrix <- makeContrasts(Diff=GDSUD-GDDMSO,levels=design)
fitcon <-contrasts.fit(fit,cont.matrix)
fitcon <-eBayes(fitcon)
tt2=topTable(fitcon,n=nrow(counts2))
plot(logFC ~ AveExpr, data=tt2,pch=".",main="Sudemycin vs DMSO junctions")
abline(h=0)



### try to use mean value for core junction as proxy for gene expression level
j = as.data.frame(v$E)
j$Gene = junc.annot[rownames(j),"Gene"]
j$set = junc.annot[rownames(j),"Annotation_Set"]
j <- j[j$set=="core",]
g = aggregate( j[,1:6],list(Gene=j$Gene),mean)
g=g[g$Gene != "",]
rownames(g)=g$Gene
g = g[,2:ncol(g)]

SUD.mean = apply(g[,4:6],1,mean)
DMSO.mean = apply(g[,1:3],1,mean)
SUD.DMSO.log2FC=SUD.mean - DMSO.mean
comb <- as.data.frame(cbind(tt2,junc.annot[rownames(tt2),]))
comb$Gene.log2FC =  SUD.DMSO.log2FC[as.character(comb$Gene)]
write.table(comb, file ="SUD_vs_DMSO_diff_junctions_mean_gene.txt", sep ="\t", col.names = TRUE, row.names = TRUE)
comb1 <- comb[!is.na(comb$Gene.log2FC),]
comb1.core <- comb1[comb1$Annotation_Set=="core",]
gene.diff <- read.table("Rh18_SUD_diff.txt",row.names=1,header=T)
comb1$g.log2FC <- gene.diff[as.character(comb1$Gene),"logFC"]
comb2 <- comb1[!is.na(comb1$g.log2FC),]

jfc <- comb1.core$logFC
gfc <- comb1.core$Gene.log2FC
n.fit <- lm(jfc ~ gfc)

pdf("Rh18_Sudemycin_Junction_level_diff_expr.pdf",width=8,height=3)
par(mfrow=c(1,3))
plot(logFC ~ Gene.log2FC, data=comb1[comb1$Annotation_Set=="core",],pch=".",cex=2,col=rgb(0,128/255,0),xlim=range(comb1$Gene.log2FC),ylim=range(comb1$logFC),
     main="Core Junctions",ylab="Junction Log2FC",xlab="Gene Log2FC")
abline(v=0,h=0,col="grey")
abline(n.fit,lty=2)
plot(logFC ~ Gene.log2FC, data=comb1[comb1$Annotation_Set=="extended",],pch=".",cex=2,col=rgb(0,102/255,204/255),xlim=range(comb1$Gene.log2FC),ylim=range(comb1$logFC),
	main="Extended Junctions",ylab="Junction Log2FC",xlab="Gene Log2FC")
abline(v=0,h=0,col="grey")
abline(n.fit,lty=2)
plot(logFC ~ Gene.log2FC, data=comb1[comb1$Annotation_Set=="novel",],pch=".",cex=2,col=rgb(204/255,0,0),xlim=range(comb1$Gene.log2FC),ylim=range(comb1$logFC),
	main="Novel Junctions",ylab="Junction Log2FC",xlab="Gene Log2FC")
abline(v=0,h=0,col="grey")
abline(n.fit,lty=2)
dev.off()




sig.up <- comb1[comb1$logFC>2 & comb1$adj.P.Val<0.05,]
sig.down <- comb1[comb1$logFC<(-2) & comb1$adj.P.Val<0.05,]
table(comb1$Annotation_Set)
#    core extended    novel 
#   96947     4413     2972 
res <- as.data.frame(cbind(table(comb1$Annotation_Set),table(sig.up$Annotation_Set),table(sig.down$Annotation_Set)))
write.table(sig.up, file ="SUD_vs_DMSO_diff_junctions_up.txt", sep ="\t", col.names = TRUE, row.names = TRUE)
write.table(sig.down, file ="SUD_vs_DMSO_diff_junctions_down.txt", sep ="\t", col.names = TRUE, row.names = TRUE)


============== not used below ============
n.interval=predict(n.fit, newdata=data.frame(x=comb1$g.log2FC), interval="predict")
tt2$low.bound = n.interval[,2]
tt2$high.bound = n.interval[,3]
tt2$diff.log2FC = tt2$logFC - tt2$Gene.log2FC
p<-data.frame(x=gfc,y1=n.interval[,2],y2=n.interval[,3])
p=p[!is.na(p$x),]
p=p[order(p$x),]
polygon(c(p[1,1],p[nrow(p),1],p[nrow(p),1],p[1,1]),
        c(p[1,2],p[nrow(p),2],p[nrow(p),3],p[1,3]),col=rgb(0,0,0.8,0.2))
#lines(gfc,n.interval[,2],lty=2,col="blue")
#lines(gfc,n.interval[,3],lty=2,col="blue")
up.up<-tt2[which(gfc>2 & jfc > unlist(n.interval[,3]) & tt2$adj.P.Val<0.05),]
up.down<-tt2[which(gfc>2 & jfc < unlist(n.interval[,2])& tt2$adj.P.Val<0.05),]
down.up<-tt2[which(gfc < (-2) & jfc > unlist(n.interval[,3]& tt2$adj.P.Val<0.05)),]
down.down<-tt2[which(gfc < (-2) & jfc < unlist(n.interval[,2])& tt2$adj.P.Val<0.05),]
sig=rbind(up.up,up.down,down.up,down.down)
points(logFC ~ Gene.log2FC, data=sig,pch=19,col="red")

junc.sig<-tt2[which(abs(gfc) < 2 & abs(tt2$diff.log2FC) >3 & tt2$adj.P.Val<0.05),]
points(logFC ~ Gene.log2FC, data=junc.sig,pch=19,col="magenta")
sig=rbind(sig,junc.sig)

write.table(cbind(sig,counts[rownames(sig),]),"NSC_voom_trimmed_SUD_vs_DMSO_sig_diff_junctions_mean_gene.txt",sep ="\t", col.names = TRUE, row.names = TRUE,quote=F)

=====================
Testing G34R vs WT flag
=====================
cont.matrix <- makeContrasts(Diff=GDGF-GDDMSO,levels=design)
fitcon <-contrasts.fit(fit,cont.matrix)
fitcon <-eBayes(fitcon)
tt2=topTable(fitcon,n=nrow(counts2))
plot(logFC ~ AveExpr, data=tt2,pch=".",main="GF vs DMSO  NSC junctions")
abline(h=0)

### try to use mean value for core junction as proxy for gene expression level
j = as.data.frame(v$E)
j$Gene = junc.annot[rownames(j),"Gene"]
j$set = junc.annot[rownames(j),"Annotation_Set"]
g = aggregate( j[,1:12],list(Gene=j$Gene),mean)
g=g[g$Gene != "",]
rownames(g)=g$Gene
g = g[,2:ncol(g)]

GF.mean = apply(g[,4:6],1,mean)
DMSO.mean = apply(g[,10:12],1,mean)
GF.DMSO.log2FC=GF.mean - DMSO.mean
tt2$Gene = junc.annot[rownames(tt2),"Gene"]
tt2$Gene.log2FC =  GF.DMSO.log2FC[as.character(tt2$Gene)]
write.table(tt2, file ="NSC_voom_trimmed_GF_vs_DMSO_diff_junctions_mean_gene.txt", sep ="\t", col.names = TRUE, row.names = TRUE)

jfc <- tt2$logFC
gfc <- tt2$Gene.log2FC
n.fit <- lm(jfc ~ gfc)
 #Lab.palette <- colorRampPalette(rev(rainbow(10, end = 4/6)))
 #smoothScatter(jfc ~ gfc, pch=".",cex=2,main="GF vs DMSO  NSC junctions/gene",ylab="Junction.log2FC")

plot(jfc ~ gfc, pch=".",cex=2,col="grey",main="GF vs DMSO  NSC junctions|gene",ylab="Junction.log2FC",xlab="Gene.log2FC (mean of core junctions)")
abline(v=0,h=0)
abline(n.fit)
n.interval=predict(n.fit, newdata=data.frame(x=gfc), interval="predict")
tt2$low.bound = n.interval[,2]
tt2$high.bound = n.interval[,3]
tt2$diff.log2FC = tt2$logFC - tt2$Gene.log2FC
p<-data.frame(x=gfc,y1=n.interval[,2],y2=n.interval[,3])
p=p[!is.na(p$x),]
p=p[order(p$x),]
polygon(c(p[1,1],p[nrow(p),1],p[nrow(p),1],p[1,1]),
        c(p[1,2],p[nrow(p),2],p[nrow(p),3],p[1,3]),col=rgb(0,0,0.8,0.2))
#lines(gfc,n.interval[,2],lty=2,col="blue")
#lines(gfc,n.interval[,3],lty=2,col="blue")
up.up<-tt2[which(gfc>2 & jfc > unlist(n.interval[,3]) & tt2$adj.P.Val<0.05),]
up.down<-tt2[which(gfc>2 & jfc < unlist(n.interval[,2])& tt2$adj.P.Val<0.05),]
down.up<-tt2[which(gfc < (-2) & jfc > unlist(n.interval[,3]& tt2$adj.P.Val<0.05)),]
down.down<-tt2[which(gfc < (-2) & jfc < unlist(n.interval[,2])& tt2$adj.P.Val<0.05),]
sig=rbind(up.up,up.down,down.up,down.down)
points(logFC ~ Gene.log2FC, data=sig,pch=19,col="red")

junc.sig<-tt2[which(abs(gfc) < 2 & abs(tt2$diff.log2FC) >3 & tt2$adj.P.Val<0.05),]
points(logFC ~ Gene.log2FC, data=junc.sig,pch=19,col="magenta")
sig=rbind(sig,junc.sig)


write.table(cbind(sig,counts[rownames(sig),]),"NSC_voom_trimmed_GF_vs_DMSO_sig_diff_junctions_mean_gene.txt",sep ="\t", col.names = TRUE, row.names = TRUE,quote=F)

=====================
Testing WT vs CTRL
=====================
cont.matrix <- makeContrasts(Diff=GDDMSO-GDCtl,levels=design)
fitcon <-contrasts.fit(fit,cont.matrix)
fitcon <-eBayes(fitcon)
tt2=topTable(fitcon,n=nrow(counts2))
plot(logFC ~ AveExpr, data=tt2,pch=".",main="DMSO vs CONTROL NSC junctions")
abline(h=0)

### try to use mean value for core junction as proxy for gene expression level
j = as.data.frame(v$E)
j$Gene = junc.annot[rownames(j),"Gene"]
j$set = junc.annot[rownames(j),"Annotation_Set"]
g = aggregate( j[,1:12],list(Gene=j$Gene),mean)
g=g[g$Gene != "",]
rownames(g)=g$Gene
g = g[,2:ncol(g)]

Ctl.mean = apply(g[,1:3],1,mean)
DMSO.mean = apply(g[,10:12],1,mean)
DMSO.Ctl.log2FC=DMSO.mean - Ctl.mean
tt2$Gene = junc.annot[rownames(tt2),"Gene"]
tt2$Gene.log2FC =  DMSO.Ctl.log2FC[as.character(tt2$Gene)]
write.table(tt2, file ="NSC_voom_trimmed_DMSO_vs_Ctl_diff_junctions_mean_gene.txt", sep ="\t", col.names = TRUE, row.names = TRUE)

jfc <- tt2$logFC
gfc <- tt2$Gene.log2FC
n.fit <- lm(jfc ~ gfc)
 #Lab.palette <- colorRampPalette(rev(rainbow(10, end = 4/6)))
 #smoothScatter(jfc ~ gfc, pch=".",cex=2,main="DMSO vs Ctl  NSC junctions/gene",ylab="Junction.log2FC")

plot(jfc ~ gfc, pch=".",cex=2,col="grey",main="DMSO vs Ctl  NSC junctions|gene",ylab="Junction.log2FC",xlab="Gene.log2FC (mean of core junctions)")
abline(v=0,h=0)
abline(n.fit)
n.interval=predict(n.fit, newdata=data.frame(x=gfc), interval="predict")
tt2$low.bound = n.interval[,2]
tt2$high.bound = n.interval[,3]
tt2$diff.log2FC = tt2$logFC - tt2$Gene.log2FC
p<-data.frame(x=gfc,y1=n.interval[,2],y2=n.interval[,3])
p=p[!is.na(p$x),]
p=p[order(p$x),]
polygon(c(p[1,1],p[nrow(p),1],p[nrow(p),1],p[1,1]),
        c(p[1,2],p[nrow(p),2],p[nrow(p),3],p[1,3]),col=rgb(0,0,0.8,0.2))
#lines(gfc,n.interval[,2],lty=2,col="blue")
#lines(gfc,n.interval[,3],lty=2,col="blue")
up.up<-tt2[which(gfc>2 & jfc > unlist(n.interval[,3]) & tt2$adj.P.Val<0.05),]
up.down<-tt2[which(gfc>2 & jfc < unlist(n.interval[,2])& tt2$adj.P.Val<0.05),]
down.up<-tt2[which(gfc < (-2) & jfc > unlist(n.interval[,3]& tt2$adj.P.Val<0.05)),]
down.down<-tt2[which(gfc < (-2) & jfc < unlist(n.interval[,2])& tt2$adj.P.Val<0.05),]
sig=rbind(up.up,up.down,down.up,down.down)
points(logFC ~ Gene.log2FC, data=sig,pch=19,col="red")

junc.sig<-tt2[which(abs(gfc) < 2 & abs(tt2$diff.log2FC) >3 & tt2$adj.P.Val<0.05),]
points(logFC ~ Gene.log2FC, data=junc.sig,pch=19,col="magenta")
sig=rbind(sig,junc.sig)

write.table(cbind(sig,counts[rownames(sig),]),"NSC_voom_trimmed_DMSO_vs_Ctl_sig_diff_junctions_mean_gene.txt",sep ="\t", col.names = TRUE, row.names = TRUE,quote=F)

######## DESEQ TMM normalization
countData <- data.frame(counts2)
colData   <- data.frame(condition=as.factor(rep(1:4,each=3)))
rownames(colData) <- colnames(counts2)
ddsHTSeq  <- DESeqDataSetFromMatrix(countData, colData, formula(~ condition))

# calculate the normalization
dds       <- estimateSizeFactors( ddsHTSeq )
rawcount  <- counts(dds,normalized=FALSE)
normcount <- counts(dds,normalized=TRUE)
normcount <- round(normcount, 2)
#colnames(normcount) <- paste(colnames(normcount),'norm', sep='.')
normcount <- log2(normcount+0.5)
pdf("MA.plots.DESEQ.TMM.pdf",width=8,height=11)
par(mfrow=c(3,2))
for (i in 2:ncol(counts2)){
 M = normcount[,i] - normcount[,1]
 A = (normcount[,i] + normcount[,1]) / 2
 plot(M~A,xlab="AverageExpression",ylab="logFC",pch=".",cex=2,
 main=paste(colnames(normcount)[i],colnames(normcount)[1],sep=" vs "))
 abline(h=c(-1,0,1))
 md <- normcount[M>1 & A>8,]
 write.table(cbind(md,counts[rownames(md),]),paste(colnames(normcount)[i],colnames(normcount)[1],"txt",sep="."),row.names=T,col.names=T,sep="\t",quote=F)

}
dev.off()

========================================
### try to use sum value for core junction as proxy for gene expression level
j = as.data.frame(v$E)
j$Gene = junc.annot[rownames(j),"Gene"]
j$set = junc.annot[rownames(j),"Annotation_Set"]
g = aggregate( j[,1:12],list(Gene=j$Gene),sum)
g=g[g$Gene != "",]
rownames(g)=g$Gene
g = g[,2:ncol(g)]

GF.mean = apply(g[,7:9],1,mean)
DMSO.mean = apply(g[,10:12],1,mean)
GF.DMSO.log2FC=GF.mean - DMSO.mean
tt2$Gene = junc.annot[rownames(tt2),"Gene"]
tt2$Gene.log2FC =  GF.DMSO.log2FC[as.character(tt2$Gene)]
write.table(tt2, file ="NSC_voom_trimmed_GF_vs_DMSO_diff_junctions_sum_gene.txt", sep ="\t", col.names = TRUE, row.names = TRUE)

jfc <- tt2$logFC
gfc <- tt2$Gene.log2FC
n.fit <- lm(jfc ~ gfc)
 #Lab.palette <- colorRampPalette(rev(rainbow(10, end = 4/6)))
 #smoothScatter(jfc ~ gfc, pch=".",cex=2,main="GF vs DMSO  NSC junctions/gene",ylab="Junction.log2FC")

plot(jfc ~ gfc, pch=".",cex=2,col="grey",main="GF vs DMSO  NSC junctions|gene",ylab="Junction.log2FC",xlab="Gene.log2FC (sum of core junctions)")
abline(v=0,h=0)
abline(n.fit)
n.interval=predict(n.fit, newdata=data.frame(x=gfc), interval="predict")
p<-data.frame(x=gfc,y1=n.interval[,2],y2=n.interval[,3])
p=p[!is.na(p$x),]
p=p[order(p$x),]
polygon(c(p[1,1],p[nrow(p),1],p[nrow(p),1],p[1,1]),
        c(p[1,2],p[nrow(p),2],p[nrow(p),3],p[1,3]),col=rgb(0,0,0.8,0.2))
#lines(gfc,n.interval[,2],lty=2,col="blue")
#lines(gfc,n.interval[,3],lty=2,col="blue")
up.up<-tt2[which(gfc>2 & jfc > unlist(n.interval[,3])),]
up.down<-tt2[which(gfc>2 & jfc < unlist(n.interval[,2])),]
down.up<-tt2[which(gfc < (-2) & jfc > unlist(n.interval[,3])),]
down.down<-tt2[which(gfc < (-2) & jfc < unlist(n.interval[,2])),]
sig=rbind(up.up,up.down,down.up,down.down)

points(logFC ~ Gene.log2FC, data=sig,pch=19,col="red")
write.table(cbind(sig,counts[rownames(sig),]),"NSC_voom_trimmed_GF_vs_DMSO_sig_diff_junctions_sum_gene.txt",sep ="\t", col.names = TRUE, row.names = TRUE,quote=F)

### try to use max value for core junction as proxy for gene expression level
j = as.data.frame(v$E)
j$Gene = junc.annot[rownames(j),"Gene"]
j$set = junc.annot[rownames(j),"Annotation_Set"]
g = aggregate( j[,1:12],list(Gene=j$Gene),max)
g=g[g$Gene != "",]
rownames(g)=g$Gene
g = g[,2:ncol(g)]

GF.mean = apply(g[,7:9],1,mean)
DMSO.mean = apply(g[,10:12],1,mean)
GF.DMSO.log2FC=GF.mean - DMSO.mean
tt2$Gene = junc.annot[rownames(tt2),"Gene"]
tt2$Gene.log2FC =  GF.DMSO.log2FC[as.character(tt2$Gene)]
write.table(tt2, file ="NSC_voom_trimmed_GF_vs_DMSO_diff_junctions_max_gene.txt", sep ="\t", col.names = TRUE, row.names = TRUE)

jfc <- tt2$logFC
gfc <- tt2$Gene.log2FC
n.fit <- lm(jfc ~ gfc)
 #Lab.palette <- colorRampPalette(rev(rainbow(10, end = 4/6)))
 #smoothScatter(jfc ~ gfc, pch=".",cex=2,main="GF vs DMSO  NSC junctions/gene",ylab="Junction.log2FC")

plot(jfc ~ gfc, pch=".",cex=2,col="grey",main="GF vs DMSO  NSC junctions|gene",ylab="Junction.log2FC",xlab="Gene.log2FC (max junction)")
abline(v=0,h=0)
abline(n.fit)
n.interval=predict(n.fit, newdata=data.frame(x=gfc), interval="predict")
p<-data.frame(x=gfc,y1=n.interval[,2],y2=n.interval[,3])
p=p[!is.na(p$x),]
p=p[order(p$x),]
polygon(c(p[1,1],p[nrow(p),1],p[nrow(p),1],p[1,1]),
        c(p[1,2],p[nrow(p),2],p[nrow(p),3],p[1,3]),col=rgb(0,0,0.8,0.2))
#lines(gfc,n.interval[,2],lty=2,col="blue")
#lines(gfc,n.interval[,3],lty=2,col="blue")
up.up<-tt2[which(gfc>2 & jfc > unlist(n.interval[,3]) & tt2$adj.P.Val<0.05),]
up.down<-tt2[which(gfc>2 & jfc < unlist(n.interval[,2])& tt2$adj.P.Val<0.05),]
down.up<-tt2[which(gfc < (-2) & jfc > unlist(n.interval[,3]& tt2$adj.P.Val<0.05)),]
down.down<-tt2[which(gfc < (-2) & jfc < unlist(n.interval[,2])& tt2$adj.P.Val<0.05),]
sig=rbind(up.up,up.down,down.up,down.down)

points(logFC ~ Gene.log2FC, data=sig,pch=19,col="red")
write.table(cbind(sig,counts[rownames(sig),]),"NSC_voom_trimmed_GF_vs_DMSO_sig_diff_junctions_max_gene.txt",sep ="\t", col.names = TRUE, row.names = TRUE,quote=F)


 write.table(tt2[which(abs(gfc) < 2 & abs(tt2$diff.log2FC) >3),],"DMSO_vs_Ctl_nonsig_diff_junc.txt",sep ="\t", col.names = TRUE, row.names = TRUE,quote=F)

	 */
}
