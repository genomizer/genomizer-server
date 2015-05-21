#!/usr/bin/perl

=head1 NAME

readsgff_to_allnucsgr – count the reads at different positions in each chromosome and create sgr

=head1 HEADER

=head1 SYNOPSIS

perl readsgff_to_allnucsgr_v1.pl, arguments will be asked

=head1 REQUIRES

OS used for development (Linux CentOS 5.3), Perl version (Perl5.8.8)

=head1 DESCRIPTION

count the reads at different positions in each chromosome and create sgr
create output in sgr folder

####### Modification Block #######
# Number    Author  Change  Date    Previous_Version

=head1 AUTHOR

Philge Philip, philge.philip@molbiol.umu.se
Per Stenberg, per.stenberg@molbiol.umu.se

You modify this program under the same terms as Perl itself

=cut

use strict;
use warnings;

my $VERSION = 1;

# This variable holds the current time #
my $now = time;

print "Enter the path of gff files\n";
chomp(my $dir = <STDIN>);
#my $dir = $ARGV[0];
my @files = <$dir\/*.gff>;
my $file ='';
my $parentdir = $dir;
mkdir "$parentdir"."allnucs_sgr", unless -d "$parentdir"."allnucs_sgr";  ## added by philge 13/04/18
foreach $file (@files){
        chomp $file;
	print "$file\n";
	my (@line, @position_counts) = ();
	my ($total_line_count, $line_count, $flag, $starting, $start, $end, $i, $j, $temp_value, $temp_value_prev, $flag1, $start_prev, $greatest_end) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0); ## greatest end, added by philge 13/04/18
	my ($chr, $prev_chr) = ('', '');

	$line_count = 0;
	
	open(IN, "<$file");
	$file=~s/\.gff$/\_v$VERSION\.sgr/;
	$file=~s/$dir/$parentdir\/allnucs_sgr/;
	open(OUT, ">$file");

	while(<IN>)
	{
	    $line_count++;
	    @line = split('\t', $_);
	    $chr = $line[0];
	    next if($chr =~ /^RNAME/);

	    #foreach chromosome
	    if(($chr ne $prev_chr)&&($flag == 1))
	    {
		$i = $starting;
		$j = 0;
		while($i <= $greatest_end)
		{
		    if(!$position_counts[$j])
		    {
		        $position_counts[$j] = 0;
		    }
		    $i++;
		    $j++;
		}
		$j = 0;
		$i = $starting;
		#generate sgr format data
		foreach $temp_value (@position_counts)
		{
			print OUT "$prev_chr\t$i\t$temp_value\n";
		     $i++;
		}
		($flag, $starting, $start, $end, $i, $j, $temp_value, $temp_value_prev, $flag1, $start_prev, $greatest_end) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		@position_counts = ();
	    }
	    #to store data start position
	    if($flag == 0)
	    {
		$starting = $line[3];
	    }
	    $flag = 1;
	    $start = $line[3];
	    $end = $line[4];
	    $greatest_end = $end if($greatest_end < $end);
	    $i = $start;
	    #define array index w.r.t data positions
	    $j = ($start - $starting);

	    while($i <= $end)
	    {
		if(!$position_counts[$j])
		{
		    $position_counts[$j] = 0;
		}
		$i++;
		$j++;
	    }

	    $j = ($start - $starting);
	    for($i=$start; $i<=$end; $i++)
	    {
		$position_counts[$j]++;
		$j++;
	    }
	    ($i, $j) = (0, 0);
	    $prev_chr = $chr;
	    @line = ();
	}
	#to print sgr format of last chromosome entry
	if($flag == 1)
	{
		$i = $starting;
		$j = 0;
		while($i <= $greatest_end)
		{
		    if(!$position_counts[$j])
		    {
		        $position_counts[$j] = 0;
		    }
		    $i++;
		    $j++;
		}
		$j = 0;
		$i = $starting;
		foreach $temp_value (@position_counts)
		{
		     print OUT "$prev_chr\t$i\t$temp_value\n";
		     $i++;
		}
	}
}

# Calculate total runtime (current time minus start time) and prints it
$now = time - $now;
printf("\n\nTotal running time: %02d:%02d:%02d\n\n", int($now / 3600), int(($now % 3600) / 60), int($now % 60));

exit;



