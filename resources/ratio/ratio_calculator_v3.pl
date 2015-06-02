#!/usr/bin/perl

=head1 NAME

ratio_calculator – to calculate ratios between IP and Input read counts

=head1 HEADER

=head1 SYNOPSIS

how to use?
perl ratio_calculator_v2.pl arguments

=head1 REQUIRES

OS used for development (Linux CentOS 5.3), Perl version (Perl5.8.8)

=head1 DESCRIPTION

to calculate ratios between IP and Input read counts
perl ratio_calculator_v2.pl IP, input files path single(to mention, we need single or double mean cutoff from IP and Input) 4(to mention, input reads cutoff) 0 (to mention, chrs)
output will be created in ratios folder

use input reads cutoff and finally median of ratios to adjust depth

####### Modification Block #######
# Number    Author  Change  Date    Previous_Version

=head1 AUTHOR

Philge Philip, philge.philip@molbiol.umu.se
Per Stenberg, per.stenberg@molbiol.umu.se

You modify this program under the same terms as Perl itself

=cut

use strict;
use warnings;

my $dir = $ARGV[0];
my $double_mean = $ARGV[1];
my $input_read_cutoff = $ARGV[2];
my $chro = $ARGV[3];

my $VERSION = 2;
# This variable holds the current time #
my $now = time;
# parameters
# print "Enter the path of IP and Input SGR files\n";
# chomp(my $dir = <STDIN>);
#my $dir = $ARGV[0];
my $change = 0;
my ($log2_scale, $random, $files, $ratio, $bin) = (1, 1, 1, 1, 1);
# print "Do you want double mean (double) or single mean (single) cutoff from Input\n";
# chomp(my $double_mean = <STDIN>);
# #my $double_mean = $ARGV[2];
# print "Give Input reads count cutoff\n";
# chomp(my $input_read_cutoff = <STDIN>);
# #my $input_read_cutoff = $ARGV[3];
# print "Do you want to specify chr's (type like chr1,chr2) or no (0)\n";
# chomp(my $chro = <STDIN>);
#my $chro = $ARGV[4];
my $parentdir = $dir;
mkdir "$parentdir"."ratios", unless -d "$parentdir"."ratios";
print "Default parameters\n";
print "Input read count cutoff: $input_read_cutoff\n";
print "log2 scale: yes\n";
print "randomize counts: yes\n";
print "create IP and Input sgr's: yes\n";
print "calculate ratio: yes\n";
print "calculate bins: yes (no. of bins: 200, ratio min: -10, ratio max: 10\n";
if($change==1)
{
	print "Enter double mean (double) or not (single)\n";
	$double_mean = <STDIN>;
	print "Enter log2 scale (1) or not (0)\n";
	$log2_scale = <STDIN>;
	print "Enter randomize counts (1) or not (0)\n";
	$random = <STDIN>;
	print "Enter create IP and Input sgr's (1) or not (0)\n";
	$files = <STDIN>;
	print "Enter calculate ratio (1) or not (0)\n";
	$ratio = <STDIN>;
	print "Enter calculate bins (1) or not (0)\n";
	$bin = <STDIN>;
	print "Enter input read count cutoff\n";
	$input_read_cutoff = <STDIN>;
}
# ratios binning parameters
my ($no_bins, $min_val, $max_val) = (200, -10, 10);
if($bin == 1)
{
	#print "Do you want change bin parameters (1) or not(0)\n";
	#$change = <STDIN>;
	$change = 0;
	if($change==1)
	{
		print "Enter number of bins\n";
		$no_bins = <STDIN>;
		print "Enter ratio minimum value\n";
		$min_val = <STDIN>;
		print "Enter ratio maximum value\n";
		$max_val = <STDIN>;
	}
}
my @chrs = ();
# storing chrs if user input
if($chro)
{
	print "Chr: $chro\n";
	@chrs = split('\,', $chro);
}

my (%chrs) = ();
if(@chrs)
{
	foreach(@chrs)
	{
		$chrs{$_} = 1 ;
	}
}

my @files = <$dir\/*.sgr>;
print "@files\n";
my (%ip_files, %input_files) = ();
my $file = '';
foreach $file (@files){
	next if($file !~ /\_step/);
	if($file =~ /\_input\_/) {
		$input_files{$file} = 1;
	}else {
		$ip_files{$file} = 1;
	}
}
my (@keys, @keys1) = ();
my ($IP, $Input, $IP_temp, $Input_temp, $flag) = ('', '', '', '', 0);
foreach $IP(keys %ip_files) {
	$IP_temp = $IP;
	$IP_temp =~ s/$dir//;
	@keys = split('\_', $IP_temp);
	$keys[1] = 'input';
	$flag = 0;
	foreach $Input(keys %input_files) {
		$Input_temp = $Input;
		$Input_temp =~ s/$dir//;
		@keys1 = split('\_', $Input_temp);
		if(($keys[0]  eq $keys1[0])&&($keys[1]  eq $keys1[1])&&($keys[2]  eq $keys1[2])){
			my $IP_log2_sgr = $IP;
			my $IP_bin = $IP;
			my $Input_temp = '';
			if($Input =~ m/.*\/(.*\.sgr)$/)
			{
				$Input_temp = $1;
			}
			
			my $IP_log2_sgr_to_smooth = $IP_log2_sgr;
			$IP_log2_sgr =~ s/$dir/$parentdir\/ratios/;
			$IP_log2_sgr_to_smooth =~ s/$dir/$parentdir\/ratios/;
			$IP_bin =~ s/$dir/$parentdir\/ratios/;
			
			# change output filename if randomization
			if($random)
			{
				if($log2_scale)
				{
					$IP_log2_sgr =~ s/.sgr$/_rand_ratio_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					$IP_log2_sgr_to_smooth =~ s/.sgr$/_rand_ratio_to_smooth_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					$IP_bin =~ s/.sgr$/_rand_ratio_bin_$double_mean\_mean_cutoff\_v$VERSION.txt/;
				}else
				{
					$IP_log2_sgr =~ s/.sgr$/_rand_diff_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					$IP_log2_sgr_to_smooth =~ s/.sgr$/_rand_to_smooth_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					$IP_bin =~ s/.sgr$/_rand_diff_bin_$double_mean\_mean_cutoff\_v$VERSION.txt/;
				}
			}else
			{
				if($log2_scale)
				{
					$IP_log2_sgr =~ s/.sgr$/_ratio_$double_mean\_mean_cutoff\_v$VERSION.sgr/ ;
					$IP_log2_sgr_to_smooth =~ s/.sgr$/_ratio_to_smooth_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					$IP_bin =~ s/.sgr$/_ratio_bin_$double_mean\_mean_cutoff\_v$VERSION.txt/;
				}else
				{
					$IP_log2_sgr =~ s/.sgr$/_diff_$double_mean\_mean_cutoff\_v$VERSION.sgr/ ;
					$IP_log2_sgr_to_smooth =~ s/.sgr$/_to_smooth_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					$IP_bin =~ s/.sgr$/_diff_bin_$double_mean\_mean_cutoff\_v$VERSION.txt/;
				}
			}
			if($ratio)
			{
                print "IP_log2_sgr: $IP_log2_sgr\n";
				open(OUT, ">", "$IP_log2_sgr") or die $!;
				open(OUT4, ">", "$IP_log2_sgr_to_smooth") or die $!;
				if($bin)
				{
					open(OUT3, ">", "$IP_bin") or die $!;
				}
			}
			my $IP_sgr = $IP;
			my $Input_sgr = $Input;
			$IP_sgr =~ s/$dir/$parentdir\/ratios/;
			$Input_sgr =~ s/$dir/$parentdir\/ratios/;
			
			if($files)
			{
				# create individual files
				if($random)
				{
					if($log2_scale)
					{
						$IP_sgr =~ s/.sgr$/_random_log2_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					open(OUT1, '>', "$IP_sgr") or die $!;
					$Input_sgr =~ s/.sgr$/_random_log2_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					open(OUT2, '>', "$Input_sgr") or die $!;
					}else
					{
						$IP_sgr =~ s/.sgr$/_random_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					open(OUT1, '>', "$IP_sgr") or die $!;
					$Input_sgr =~ s/.sgr$/_random_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					open(OUT2, '>', "$Input_sgr") or die $!;
					}
				}else
				{
					if($log2_scale)
					{
						$IP_sgr =~ s/.sgr$/_log2_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					open(OUT1, '>', "$IP_sgr") or die $!;
					$Input_sgr =~ s/.sgr$/_log2_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					open(OUT2, '>', "$Input_sgr") or die $!;
					}else
					{
						$IP_sgr =~ s/.sgr$/_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					open(OUT1, '>', "$IP_sgr") or die $!;
					$Input_sgr =~ s/.sgr$/_$double_mean\_mean_cutoff\_v$VERSION.sgr/;
					open(OUT2, '>', "$Input_sgr") or die $!;
					}
				}
			}
			# unwanted variables were not cross checked and removed after the revision of the script
			my ($line, $chrs, $prev_chr, $diff_prev1, $chr) = ('', '', '', 0, '');
			my ($value, $pos, $ratiovalue, $ratio_median, $ratio_adjusted) = ('', '', '', '', '');
			my (@line, @position_counts, @ip_position_counts, @ratios, @ratios_for_median) = ();
			my ($starting, $start, $end, $i, $j, $end_prev, $read_count, $Rand, $start_prev, $ip_count_prev) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			my ($ip_count, $input_count, $diff, $diff_prev, $ip_start_prev, $flag1, $input_count_prev, $l, $start_flag, $index) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			my (@bins) = ();
			my ($read_count_sum, $coordinate_length_sum, $average, $coordinate_length, $count) = (0, 0, 0, 0, 0);
			# read IP data to find maximum read count
			open(IN, '<', "$IP") or die $!;
			while ($line = <IN>)
			{
				chomp($line);
				@line = split('\t', $line);
				$chr = $line[0];
				# get chroms from IP file
				if(@chrs)
				{
					#if(not exists $chrs{$chr})
					#{
						#next;
					#}
				}else
				{
					$chrs{$chr} = 1;
				}
				#if($chr =~ /^chr/)
				#{
				#	# taking only pos with atleast 1 read
				#	if($line[2] > 0) {
				#			$read_count_sum += $line[2];
				#			$count += 1;
				#	}
				#}
			}
			close(IN);
			# read Input data to find maximum read count
			open(IN, '<', "$Input") or die $!;
			while ($line = <IN>)
			{
				chomp($line);
				@line = split('\t', $line);
				$chr = $line[0];
				if(@chrs)
				{
					#if(not exists $chrs{$chr})
					#{
						#next;
					#}
				}else
				{
					$chrs{$chr} = 1;
				}
				if($chr =~ /^chr/)
				{
					# taking only pos with atleast 1 read
					if($line[2] > 0) {
						$read_count_sum += $line[2];
						$count += 1;
					}
				}
			}
			# calculate Input cutoff
			print "Input counts: $read_count_sum, $count\n";
			$average = ($read_count_sum/$count);
			print "Input average: $average\n";
			my $input_count_cutoff = $average;
			# compare with user input
			if($input_count_cutoff < $input_read_cutoff) {
				$input_count_cutoff = $input_read_cutoff;
			}
			print "Input average adjusted: $input_count_cutoff\n";
			($read_count_sum, $coordinate_length_sum, $count) = (0, 0, 0);
			if($double_mean eq 'double')
			{
				print "Double Mean\n";
				$input_count_cutoff *= 2;
			}else
			{
				print "Single Mean\n";
			}
			close(IN);
			
			# for each chrom
			foreach $chrs(keys %chrs)
			{
				($line, $chr, $prev_chr) = ('', '', '');
				(@line, @position_counts, @ip_position_counts) = ();
				($starting, $start, $end, $i, $j, $end_prev, $read_count, $Rand, $start_prev, $ip_count_prev) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
				($ip_count, $input_count, $diff, $diff_prev, $ip_start_prev, $flag1, $input_count_prev, $l, $start_flag, $index) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
				# read input data
				open(IN, '<', "$Input") or die $!;
				while ($line = <IN>)
				{
					chomp($line);
					@line = split('\t', $line);
					$chr = $line[0];
					# end reading if chrom changed
					if($prev_chr)
					{
					    last if($chr ne $prev_chr);
					}
					# if chr data exist in the file
					if($chr eq $chrs)
					{
						$start = $line[1];
						$read_count = $line[2];
						# comparing read count with cutoff
						if($read_count < $input_count_cutoff) {
							$read_count = $input_count_cutoff;
						}
						# if randomize
						if($random)
						{
						    $Rand = (rand(1)) - 0.5;
						    $read_count += $Rand;
						}
						if($log2_scale)
						{
							$read_count =  log($read_count)/log(2);
						}
						# to store first region start position
						if(!$end_prev)
						{
							$starting = $start;
						}
						# to store current start
						$end_prev = $start;
						#set array index w.r.t read positions and count
						$position_counts[$start] = $read_count;
						# store current chrom to compare later
						$prev_chr = $chr;
					}
				}
				close(IN);
				$j = $starting;
				# printing input log2 to individual sgrs
				while($j <= $end_prev)
				{
				    # print input data
				    if(($files)&&(defined $position_counts[$j]))
				    {
					$input_count = $position_counts[$j];
					if(($input_count_prev != $input_count)&&($flag1==1))
					{
					    print OUT2 "$chrs\t$start_prev\t$input_count_prev\n";
					    $start_prev = $j;
					}
					$input_count_prev = $input_count;
					if($flag1 == 0)
					{
					    $start_prev = $j;
					    $flag1 = 1;
					}
				    }
				    $j++;
				}
				# print last input data
				if($files)
				{
					print OUT2 "$chrs\t$start_prev\t$input_count_prev\n";
				}
				# if not ratio calculation empty input reads
				if(!$ratio)
				{
					undef(@position_counts);
				}
				($line, $chr, $prev_chr, $diff_prev1) = ('', '', '', 0);
				(@line) = ();
				($starting, $start, $end, $i, $j, $end_prev, $read_count, $Rand, $start_prev, $ip_count_prev) = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
				($ip_count, $input_count, $diff, $diff_prev, $ip_start_prev, $flag1, $input_count_prev, $l, $start_flag) = (0, 0, 0, 0, 0, 0, 0, 0, 0);
				# read IP data
				open(IN, '<', "$IP") or die $!;
				while ($line = <IN>)
				{
					chomp($line);
					@line = split('\t', $line);
					$chr = $line[0];
					if($prev_chr)
					{
					    last if($chr ne $prev_chr);
					}
					# if chr data exist in the file
					if($chr eq $chrs)
					{
						# to store each read start and end position, read count
						$start = $line[1];
						$read_count = $line[2];
						
						if($read_count == 0) {
							if($ratio)
							{
								push(@ratios, "$chrs\t$start\t$read_count\t$read_count") ;
							}
							if($files)
							{
								$ip_position_counts[$start] = 0;
							}
							next;
						}
						if($random)
						{
						    $Rand = (rand(1)) - 0.5;
						    $read_count += $Rand;
						}
						if($log2_scale)
						{
							$read_count =  log($read_count)/log(2);
						}
						# to store region start position
						if(!$end_prev)
						{
						    $starting = $start;
						}
						$end_prev = $start;
						$i = $start;
						$Rand = 0;
						if($ratio)
						{
							# if input pos not found setting it
							if(defined $position_counts[$i])
							{
								$ip_count = $read_count;
								$input_count = $position_counts[$i];
							}else
							{
								$ip_count = $read_count;
								$input_count = $input_count_cutoff;
								# if randomize
								if($random)
								{
									$Rand = (rand(1)) - 0.5;
									$input_count += $Rand;
								}
								if($log2_scale)
								{
									$input_count =  log($input_count)/log(2);
								} ##
							}
							undef $position_counts[$i];
							# calculate ratio
							$diff = $ip_count - $input_count;
							push(@ratios, "$chrs\t$start\t$diff\t1");
							push(@ratios_for_median, $diff);
						}
						# store ip data to print separately
						if($files)
						{
							$ip_position_counts[$i] = $ip_count;
						}
						$prev_chr = $chr;
					}
				}
				close(IN);
				undef(@position_counts);
			
				# print ip data separately
				if($files)
				{
					$j = $starting;
					$flag1 = 0;
					while($j <= $end_prev)
					{
					    if(defined $ip_position_counts[$j]){
						$ip_count = $ip_position_counts[$j];
						# to print only changing pos
						if(($ip_count_prev != $ip_count)&&($flag1==1))
						{
						    print OUT1 "$chrs\t$start_prev\t$ip_count_prev\n";
						    $start_prev = $j;
						}
						$ip_count_prev = $ip_count;
						if($flag1 == 0)
						{
						    $start_prev = $j;
						    $flag1 = 1;
						}
					    }
					    $j++;
					}
					print OUT1 "$chrs\t$start_prev\t$ip_count_prev\n";
				}
				undef(@ip_position_counts);
			}
			# ratio adjustment with median
			($value, $pos, $ratiovalue, $ratio_median, $ratio_adjusted) = ('', '', '', '', '');
			my $check = '';
			$diff_prev = 0;
			# print ratios after adjustment
			if($ratio)
			{
				$ratio_median = &median(\@ratios_for_median);
				print "Ratio median: $ratio_median\n";
				foreach $value (@ratios) {
					($chr, $pos, $ratiovalue, $check) = split('\t', $value);
					if(($ratiovalue == 0)&&($check == 0)) {
						$ratio_adjusted = $ratiovalue;
						if($diff_prev == 0) {
							print OUT "$chr\t$pos\t$ratio_adjusted\n";
							$diff_prev = 1;
						}
						print OUT4 "$chr\t$pos\t$ratio_adjusted\n";
						next;
					}else {
						$ratio_adjusted = $ratiovalue - $ratio_median; # when ratio median is negative it will be added and vice versa
						$diff_prev = 0;
					}
					print OUT "$chr\t$pos\t$ratio_adjusted\n";
					print OUT4 "$chr\t$pos\t$ratio_adjusted\n";
					# binning the ratios
					if($ratio_adjusted <= $min_val)
					{
						$index = 0;
					}elsif($ratio_adjusted >= $max_val)
					{
						$index = ($no_bins-1);
					}else
					{
						$index = int(($ratio_adjusted-$min_val)/(($max_val-$min_val)/$no_bins))
					}
			
					if(not defined $bins[$index])
					{
						$bins[$index] = 1;
					}else
					{
						$bins[$index]++;
					}
				}
			}
			$i = 0;
			if($ratio)
			{
				foreach (@bins)
				{
					if($_)
					{
						print OUT3 "$i\t$_\n"; 
					}else
					{
						print OUT3 "$i\t0\n";	
					}
					$i++;
				}
				while($i<$no_bins)
				{
					print OUT3 "$i\t0\n";
					$i++;
				}
			}
			close(OUT);
			close(OUT1);
			close(OUT2);
			close(OUT3);
			close(OUT4);
			$flag = 1;
			last;
		}
	}
	if($flag == 0) {
		print "INPUT OF $IP NOT FOUND\n";
		exit;
	}
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

# Calculate total runtime (current time minus start time) #
$now = time - $now;
# Print runtime #
printf("\n\nTotal running time: %02d:%02d:%02d\n\n", int($now / 3600), int(($now % 3600) / 60), int($now % 60));

