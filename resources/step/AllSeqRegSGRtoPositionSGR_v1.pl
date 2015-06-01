#!/usr/bin/perl

=head1 NAME

AllSeqRegSGRtoPositionSGR_v1 – Use to convert seq-data into chip-type data

=head1 HEADER

=head1 SYNOPSIS

how to use?
perl AllSeqRegSGRtoPositionSGR_v1.pl arguments

=head1 REQUIRES

OS used for development (Linux CentOS 5.3), Perl version (Perl5.8.8)

=head1 DESCRIPTION

Use to convert seq-data into chip-type data

####### Modification Block #######
# Number    Author  Change  Date    Previous_Version

=head1 AUTHOR

Philge Philip, philge.philip@molbiol.umu.se
Per Stenberg, per.stenberg@molbiol.umu.se

You modify this program under the same terms as Perl itself

=cut

# modified from AllSeqRegSGRtoPositionSGR.pl, look for keywords "modified by philge 13/04/20" to see modifications
#print "This will make a step-sgr of all sgr files in current directory. Use only if sgr files contain non-overlapping regions.";
#print " Use to convert seq-data into chip-type data.\nContinue (y/n):";
$choice=$ARGV[0];
chomp($choice);
#print "Select step size (bp):";
$step=$ARGV[1];
$dir = $ARGV[2];
$parentdir = $dir;
# This variable holds the current time #
my $now = time;
#$parentdir =~ s/smoothed\///;
chomp($step);
if($choice eq 'y'){
    mkdir "$parentdir"."Step$step", unless -d "$parentdir"."Step$step";
    opendir(DIR, "$dir") or die "can't open directory!";
    while (defined($file = readdir(DIR))) {
      if(index($file, '.sgr')>-1 || index($file, '.SGR')>-1){
          open IN, "$dir$file" or die "can't open $file!";
          chop($file);
          chop($file);
          chop($file);
          chop($file);
          $outfile=$file."_step".$step.".sgr";
         
          open OUT, ">$parentdir\/Step$step/$outfile" or die "Can't open outfile!\n";
         
         $nr_row=0;
         @chrom_arr=();
         @pos_arr=();
         @val_arr=();
         while($row=<IN>){
            chomp($row);
            @tmp=split/\t/, $row;
            push @chrom_arr, $tmp[0];
            push @pos_arr, $tmp[1];
            push @val_arr, $tmp[2];
            $nr_row++;
         }
         close(IN);
         
         $pos=0;
         for($x=0; $x<$nr_row-1; $x++){
            if($chrom_arr[$x] ne $chrom_arr[$x+1]){
               $pos=0;
	       #print OUT "$chrom_arr[$x]\t",$pos_arr[$x],"\t0\n"; ## added by philge 13/04/20
               next;
            }
            if($pos==0){
		
                while($pos<=$pos_arr[$x]){
                    $pos+=$step;
                }
            }
            while($pos<$pos_arr[$x+1]){
               #if($val_arr[$x]!=0){ ## added by philge 13/04/20
                  print OUT "$chrom_arr[$x]\t$pos\t$val_arr[$x]\n";
               #}
               $pos+=$step;
            }
         }
	 #print OUT "$chrom_arr[$x-1]\t",$pos_arr[-1],"\t0\n"; ## added by philge 13/04/20
      }
	close(OUT);
   }
}

# Calculate total runtime (current time minus start time) and prints it
$now = time - $now;
printf("\n\nTotal running time: %02d:%02d:%02d\n\n", int($now / 3600), int(($now % 3600) / 60), int($now % 60));

