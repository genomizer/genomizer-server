#!/bin/bash

scriptdir=`dirname \"$0\" | sed -e 's/\"//g'`
ratio_calc=$scriptdir/ratio_calculator_v3.pl

workingdir=$1;
input=$2;
IP=$3;
outfileName=$4;
mean=$5;
readsCutOff=$6;
chromosomes=$7;

infiledir=`echo $input | perl -pe 's/\/[^\/]+$//'`/

echo infiledir: $infiledir

# workingdir=workingdir-ratio-`echo $input | perl -pe 's/[\_\/]//g'`/
resultdir=$workingdir/ratios/

echo workingdir: $workingdir

mkdir -p $workingdir

cp $input $workingdir/x_input_step.sgr
cp $IP $workingdir/x_IP_step.sgr

perl $ratio_calc $workingdir $mean $cutoff $chromosomes

# mv $resultdir/* $infiledir/
# rm -rf $resultdir
