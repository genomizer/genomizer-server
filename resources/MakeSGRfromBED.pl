#!/usr/bin/perl

=head1 NAME

MakeSGRfromBED - create SGR format from BED

=head1 HEADER

=head1 SYNOPSIS

how to use?
perl MakeSGRfromBED_v1.pl arguments

=head1 REQUIRES

OS used for development (Linux CentOS 5.3), Perl version (Perl5.8.8)

=head1 DESCRIPTION

create SGR format from BED

####### Modification Block #######
# Number    Author  Change  Date    Previous_Version

=head1 AUTHOR

Philge Philip, philge.philip@molbiol.umu.se

You modify this program under the same terms as Perl itself

=cut

$VERSION = '1';

use strict;
use warnings;

my $bed_format_file = $ARGV[0];
open(IN, "<", $bed_format_file);
my $sgr_format_file = $bed_format_file;
$sgr_format_file = $ARGV[1];
open(OUT, ">", $sgr_format_file);

my ($line, $centre) = ('', 0);
my (@columns) = ();

while($line = <IN>)
{
   chomp($line);
   @columns = split('\t', $line);
   $centre = int(($columns[2] - $columns[1])/2);
   print OUT "$columns[0]\t",($columns[1]+$centre),"\t$columns[4]\n";
}

close(IN);
close(OUT);

exit;




