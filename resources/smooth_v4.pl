#!/usr/bin/perl

=head1 NAME

smooth – parse sgr file data, calculate median or trimed mean for a window size with step pos limit

=head1 HEADER

=head1 SYNOPSIS

how to use? perl smooth_v4.pl, arguments will be asked

=head1 REQUIRES

OS used for development (Linux CentOS 5.3), Perl version (Perl5.8.8) 

=head1 DESCRIPTION

parse sgr file data, calculate median or trimed mean for a window size with step pos limit
create smoothed files in smooth folder

####### Modification Block #######
# Number    Author  Change  Date    Previous_Version

=head1 AUTHOR

Philge Philip, philge.philip@molbiol.umu.se
Per Stenberg, per.stenberg@molbiol.umu.se

You modify this program under the same terms as Perl itself

=cut

use strict;
use warnings;

my $VERSION = 4;
# This variable holds the current time #
my $now = time;
#print "Data in file processed while reading, please make sure you dont have empty lines at end of the file\n";
print "Enter the path of sgr files\n";
chomp(my $dir = <STDIN>);
#my $dir = $ARGV[0];
my @files = <$dir\/*.sgr>;
my $file = '';
print "Enter window size\n";
chomp(my $window_size = <STDIN>);
#my $window_size = $ARGV[1];
print "Enter 1 or 0, to smooth with median (1) or trimmed mean (0)\n";
chomp(my $median_or_mean = <STDIN>);
#my $median_or_mean = $ARGV[2];
print "Enter minimum step pos to smooth\n";
chomp(my $minimum_probes = <STDIN>);
#my $minimum_probes = $ARGV[3];
print "Do you want to calculate total mean (1) or no (0) and print on terminal\n";
chomp(my $total_mean = <STDIN>);
#my $total_mean = $ARGV[4];
print "Do you want to print pos with no reads in smoothed file, (1) or no (0)\n";
chomp(my $printzeros = <STDIN>);
#my $printzeros = $ARGV[5];
my (@signals_sub, @signals, @probes, @probes_temp, @probes_new, @signals_temp, @signals_for_total_mean) = ();
my ($prev_chr, $signals, $probes, $chr) = ('', '', '', '');
my ($flag, $probe, $stop, $mean, $signal_value, $median, $start, $last_key, $i, $window_size_half) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
my $parentdir = $dir;  ## added by philge 13/04/18
#$parentdir =~ s/sgr\/$//;  ## added by philge 13/04/18
mkdir "$parentdir"."smoothed", unless -d "$parentdir"."smoothed";  ## added by philge 13/04/18
foreach $file (@files){
    #print "$file\n$window_size\n$median_or_mean\n$minimum_probes\n";
    (@signals_sub, @signals, @probes, @probes_temp, @probes_new, @signals_temp, @signals_for_total_mean) = ();
    ($prev_chr, $signals, $probes, $chr) = ('', '', '', '');
    ($flag, $probe, $stop, $mean, $signal_value, $median, $start, $last_key, $i, $window_size_half) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    #to handle last chromosome data in the file, since i am processing the data directly reading from file chromosome-wise
    #my ($total_line_count, $line_count) = (0, 0);
    #$total_line_count = `wc -l $file`;
    #if($total_line_count =~/(\d+)/)
    #{
    #    $total_line_count = $1;
    #}
    my $file1 = $file;
    $file1 =~ s/$dir/$parentdir\/smoothed/;
    if($median_or_mean == 1) {
        $file1=~s/\.sgr/_median_smooth/;
        $file1 .= '_winSiz-'.$window_size.'_minProbe-'.$minimum_probes.'.sgr';
    }
    if($median_or_mean == 0) {
        $file1=~s/\.sgr/_trimmed_mean_smooth/;
        $file1 .= '_winSiz-'.$window_size.'_minProbe-'.$minimum_probes.'.sgr';
    }
    print "\n$file1\n";
    open(OUT, ">$file1") || die ("can't open the file: $!");
    open(IN, "<$file") || die ("can't open the file: $!");
    while(<IN>)
    {
        ($chr, $probe, $signal_value) = split('\t', $_);
        #$line_count++;
        chomp($signal_value);
        if(($chr ne $prev_chr)&&($flag == 1))
        {
            if($median_or_mean == 1)
            {
                &calculate_median($prev_chr, \@signals, \@probes, $window_size);
            }
            if($median_or_mean == 0)
            {
                &calculate_trimmed_mean($prev_chr, \@signals, \@probes, $window_size);
            }
            (@signals, @probes) = ();
        }
        $flag = 1;
        $prev_chr = $chr;
        push(@signals, $signal_value);
        push(@signals_for_total_mean, $signal_value) if($total_mean);
        push(@probes, $probe);
    }
    close(IN);
    if($flag == 1)
    {
        if($median_or_mean == 1)
        {
            &calculate_median($prev_chr, \@signals, \@probes, $window_size);
        }
        if($median_or_mean == 0)
        {
            &calculate_trimmed_mean($prev_chr, \@signals, \@probes, $window_size);
        }
    }
    if($total_mean) {
        $mean = &average(\@signals_for_total_mean);
        print "\nMean of all signals from file: ", $mean, "\n";
    }
    close(OUT);
}

# Print runtime #
printf("\n\nTotal running time: %02d:%02d:%02d\n\n", int($now / 3600), int(($now % 3600) / 60), int($now % 60));
exit;

sub calculate_median
{
    ($prev_chr, $signals, $probes, $window_size) = @_;
    $window_size_half = ($window_size/2);
    $window_size_half =int($window_size_half);
    @signals = @$signals;
    @probes = @$probes;
    $last_key = $probes[-1];
    @probes_temp = @probes;
    @signals_temp = @signals;
    #@signals = ();
    my ($z, $j) = (0, 0);
    foreach my $key_master (@probes)
    {
        if($signals[$j] == 0) {
            print OUT "$prev_chr\t$key_master\t0\n" if($printzeros);
            $j++;
            next;
        }
        $j++;
        #window start and stop
        $start = ($key_master - $window_size_half);
        $stop = ($key_master + $window_size_half);
        $i = 0;
        @signals_sub = ();
        # remove probes which are used up to reduce iterations
        if($z)
        {
            splice(@probes_temp, 0, $z);
            splice(@signals_temp, 0, $z);
            $z = 0;
        }
        #to slide for a window
        foreach my $key (@probes_temp)
        {
            #all probes before start
            if($key < $start)
            {
                $i++;
                $z = $i;
                next;
            }
            #if window stop is reached or no more probes
            if(($key >= $stop)||($key == $last_key))
            {
                if(($key == $stop)||($key == $last_key))
                {
                    push(@signals_sub, $signals_temp[$i]);
                }
                if(@signals_sub >= $minimum_probes)
                {
                    #$median = median(@signals_sub);
                    $median = &median(\@signals_sub);
                    print OUT "$prev_chr\t$key_master\t$median\n";
                }
                ($start, $stop, $median) = (0, 0);
                (@signals_sub) = ();
                last;
            }
            push(@signals_sub, $signals_temp[$i]);
            $i++;
        }
    }
}

sub calculate_trimmed_mean
{
    ($prev_chr, $signals, $probes, $window_size) = @_;
    $window_size_half = ($window_size/2);
    $window_size_half =int($window_size_half);
    @signals = @$signals;
    @probes = @$probes;
    $last_key = $probes[-1];
    @probes_temp = @probes;
    @signals_temp = @signals;
    my ($z, $j) = (0, 0);
    #@signals = ();
    foreach my $key_master (@probes)
    {
        if($signals[$j] == 0) {
            print OUT "$prev_chr\t$key_master\t0\n" if($printzeros);
            $j++;
            next;
        }
        $j++;
        #window start and stop
        $start = $key_master - $window_size_half;
        $stop = $key_master + $window_size_half;
        $i = 0;
        @signals_sub = ();
        # remove probes which are used up to reduce iterations
        if($z)
        {
            splice(@probes_temp, 0, $z);
            splice(@signals_temp, 0, $z);
            $z = 0;
        }
        #to slide for a window
        foreach my $key (@probes_temp)
        {
            #all probes before start
            if($key < $start)
            {
                $i++;
                $z = $i;
                next;
            }
            #if window stop is reached or no more probes
            if(($key >= $stop)||($key == $last_key))
            {
                if(($key == $stop)||($key == $last_key))
                {
                    push(@signals_sub, $signals_temp[$i]);
                }
                #trimming
                @signals_sub = sort @signals_sub;
                shift @signals_sub;
                pop @signals_sub;
                if(@signals_sub >= $minimum_probes)
                {
                    #$mean = mean(@signals_sub);
                    $mean = &average(\@signals_sub);
                    print OUT "$prev_chr\t$key_master\t$mean\n";
                }
                ($start, $stop, $mean) = (0, 0);
                (@signals_sub) = ();
                last;
            }
            push(@signals_sub, $signals_temp[$i]);
            $i++;
        }
    }
}

sub average { 
my ($array_ref) = @_; 
my $sum; 
my $count = scalar @$array_ref; 
foreach (@$array_ref) { $sum += $_; } 
return $sum / $count; 
} 

sub median { 
my ($array_ref) = @_; 
my $count = scalar @$array_ref; 
my @array = sort { $a <=> $b } @$array_ref; 
if ($count % 2) { 
return $array[int($count/2)]; 
} else { 
return ($array[$count/2] + $array[$count/2 - 1]) / 2; 
}
}
