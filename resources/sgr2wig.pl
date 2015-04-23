#chdir "/home/main/sub/newfiles";
my $dir = $ARGV[1];
my $files = <$ARGV[0]>;

open(OUTPUT, ">$dir");
open(IN, "<$files") or die $!;

my $chr="chr"; 
my $start=-1; 
my $sig=0; 

while(<IN>) 
{ 
        my @line=split/\s+/, $_; 
        my $temp_chr=$line[0]; 
        my $temp_start=$line[1]-1; 
        my $temp_sig=$line[2]; 
        if ($chr ne $temp_chr){ 
                $chr=$temp_chr; 
                $start=$temp_start+1; 
                $sig=$temp_sig; 
        } 
        else{ 
                print OUTPUT "$chr\t$start\t$temp_start\t$sig\n"; 
                $start=$temp_start+1; 
                $sig=$temp_sig; 
        } 

} 
close(OUTPUT); 
close(DATA); 


