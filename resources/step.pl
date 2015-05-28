#!/usr/bin/perl

# AllSeqRegSGRtoPositionSGR_v1 – Use to convert seq-data into chip-type data
# 
# Philge Philip, philge.philip@molbiol.umu.se
# Per Stenberg, per.stenberg@molbiol.umu.se
# 
# Modified by Niklas Fries, niklasf@cs.umu.se
# 
# Use: ./step.pl step_size infile outfile

$step=$ARGV[0];
$infile=$ARGV[1];
$outfile=$ARGV[2];

open INFILE, "<", $infile or die "Can't open infile!\n";
open OUTFILE, ">", $outfile or die "Can't open outfile!\n";

$nr_row=0;
@chrom_arr=();
@pos_arr=();
@val_arr=();

while(<INFILE>){
    $row = $_;
    chomp($row);
    @tmp=split/\t/, $row;
    push @chrom_arr, $tmp[0];
    push @pos_arr, $tmp[1];
    push @val_arr, $tmp[2];
    $nr_row++;
}

$pos=0;
for($x=0; $x<$nr_row-1; $x++){
    if($chrom_arr[$x] ne $chrom_arr[$x+1]){
        $pos=0;
        next;
    }

    if($pos==0){
        while($pos<=$pos_arr[$x]){
            $pos+=$step;
        }
    }
    while($pos<$pos_arr[$x+1]){
        print OUTFILE "$chrom_arr[$x]\t$pos\t$val_arr[$x]\n";
        $pos+=$step;
    }
}
