#!/usr/bin/perl

=head1 NAME

sam_to_readsgff – parse required fields from sam data files and create a gff with reads

=head1 HEADER

=head1 SYNOPSIS

how to use? perl sam_to_readsgff_v1.pl, arguments will be asked

=head1 REQUIRES

OS used for development (Linux CentOS 5.3), Perl version (Perl5.8.8) 

=head1 DESCRIPTION

parse required fields from sam data files and create a gff with reads
read length used to calculate read end pos
create readsgff files in reads_gff folder

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

print "Enter the path of sam files (mandatory file format: celltype_sample_replicate.* expected eg: WT_input_rep1.*), sam files should be sorted with chr and pos\n";
chomp(my $dir = <STDIN>);
#my $dir = $ARGV[0];
my @files = <$dir\/*.sam>;
my ($file, $input_file, $output_file) = ('', '', '');
my ($file_no, $readlength) = (0, 0);
my (@line) = ();
my $prev_chr = '';
my $parentdir = $dir;
#$parentdir =~ s/sam\///;
mkdir "$parentdir"."reads_gff", unless -d "$parentdir"."reads_gff";
foreach $file (@files)
{
    print "$file\n";
    $input_file = $file;
    $output_file = $file;
    $output_file =~ s/\.sam$/\_v$VERSION\.gff/;
    $output_file =~ s/$dir/$parentdir\/reads_gff/;
    if($file=~/_(\d+)\.csfasta/)
    {
        $file_no = $1;
    }
    open(IN, "<", "$input_file") or die $!;
    open(OUT, ">", "$output_file") or die $!;
    (@line) = ();
    $prev_chr = '';
    print OUT "RNAME\tsolid\tread\tPOS\tPOS+readlength\tMAPQ\t.\t.\tQNAME\n";
    while(<IN>)
    {
        chomp;
        next if(/^@/);
        @line = split('\t', $_);
        $readlength = length($line[9]);
        # to find chrm in data file
        #if($prev_chr ne $line[2])
        #{
            #print "$line[2]\n";
        #}
        #$prev_chr = $line[2];
        #next;
        if($line[2] =~ /^chr/) {
            print OUT "$line[2]\tsolid\tread\t$line[3]\t", ($line[3]+$readlength), "\t$line[4]\t.\t.\t$line[0]\n";  ## added by philge 13/04/18
        }elsif($line[2] ne '*') {
            print OUT "chr$line[2]\tsolid\tread\t$line[3]\t", ($line[3]+$readlength), "\t$line[4]\t.\t.\t$line[0]\n";  ## added by philge 13/04/18
        }
        (@line) = ();
        $readlength = 0;
    }
    close(IN);
    close(OUT);
    #$output_file = $input_file."\.tar\.gz";
    #`tar -cvzf $output_file $input_file`;
    ($file, $input_file, $output_file) = ('', '', '');
}

# Calculate total runtime (current time minus start time) and prints it
$now = time - $now;
printf("\n\nTotal running time: %02d:%02d:%02d\n\n", int($now / 3600), int(($now % 3600) / 60), int($now % 60));


exit;
