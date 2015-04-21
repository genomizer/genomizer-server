#!/usr/bin/perl

=head1 NAME

MakeSGRfromGFF - create SGR format from GFF

=head1 HEADER

=head1 SYNOPSIS

how to use?
perl MakeSGRfromGFF_v1.pl arguments

=head1 REQUIRES

OS used for development (Linux CentOS 5.3), Perl version (Perl5.8.8)

=head1 DESCRIPTION

create SGR format from GFF

####### Modification Block #######
# Number    Author  Change  Date    Previous_Version

=head1 AUTHOR

Philge Philip, philge.philip@molbiol.umu.se

You modify this program under the same terms as Perl itself

=cut

$VERSION = '1';

use strict;
use warnings;

my $gff_format_file = $ARGV[1];
open(IN, "<", $gff_format_file);
my $sgr_format_file = $ARGV[2];
open(OUT, ">", $sgr_format_file);

my ($line, $centre) = ('', 0);
my (@columns) = ();

while($line = <IN>)
{
    @columns = split('\t', $line);
    $centre = int(($columns[4] - $columns[3])/2);
    print OUT "$columns[0]\t",($columns[3]+$centre),"\t$columns[5]\n";
}

close(IN);
close(OUT);

exit;




