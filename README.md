# DRPPM
## DNA RNA Proteome Post-translational Modifications 

DRPPM is a library collection of scripts for analyzing DNA/RNA/Proteome/Post-translational Modifications. The program is capable of performing data wrangling of genomics, transcriptomics, and proteomics data. While the program collection includes some personalized pipelines, it also contains standardized RNAseq analysis pipelines like WRAP for splicing analysis. In addition to basic data processing, the DRPPM also contains the ability to generate publication-ready figures and interactive plots of gene/protein networks. See https://github.com/gatechatl/DRPPM_Example_Input_Output for the examples.

## Details are documented in the Wiki page 
* Access additional details about the program [wiki](https://github.com/gatechatl/DRPPM/wiki/)

## Git Checkout
```git clone git@github.com:gatechatl/DRPPM.git  # Clone the repo```

## Prerequisites
* [Java version J2SE-1.5](https://www.oracle.com/technetwork/java/javase/)

Required R script libraries (possibly an incomplete list)
| Packages  | version |
| ------------- | ------------- |
| limma  |   |
| edgeR  |   |
| DESeq2 |   |   
| dplyr |   |
| magrittr |   |
| readr |   |
| Rtsne |   |
| ggpubr |   |
| sva |   |
| ggplot2 |   |
| pheatmap |   |
| pvclust |   |
| Peptides |   |
| lavaan |    |
| gridExtra |    |
| BioNet |    |
| VennDiagram |    |
| RUVSeq |    |
| RColorBrewer |    |
| reshape2 |    |
| Seurat |    |
| Matrix |    |
| monocle |    |
| rsnps |    |
| RPMM |    |



## Current downloadable release
* [jar package] (https://github.com/gatechatl/DRPPM/blob/master/export/DRPPM_20210211_newmachine.jar)

## Usage
```$ drppm 

DRPPM is a toolbox for processing integrated datasets.

optional arguments:
  -h, --help   show this help message and exit

### Search for a program
type drppm -Find [keyword]

### Inputs and Outputs
The inputs for DRPPM are defined. To see the list of inputs, type drppm -[program]

### Outputs
The outputs for DRPPM are also defined in the parameter. To see the list of outputs in drppm -[program]

```
## Reference
Timothy I. Shaw. DRPPM: an integrated software for processing omics data. 2020.

## Example Publications
KH Lee, P Zhang, HJ Kim et al. C9orf72 Dipeptide Repeats Impair the Assembly, Dynamics, and Function of Membrane-Less Organelles. Cell 167 (3), 774-788. e17

KM Joo JH, Wang B et al. The Noncanonical Role of ULK/ATG1 in ER-to-Golgi Trafficking Is Essential for Cellular Homeostasis. Mol Cell 62 (4), 491-506.

Tan H, Yang K, Li Y, Shaw T et al. Integrative Proteomics and Phosphoproteomics Profiling Reveals Dynamic Signaling Networks and Bioenergetics Pathways Underlying T Cell Activation. Immunity. 2017 Mar 21;46(3):488-503. doi: 10.1016/j.immuni.2017.02.010.


