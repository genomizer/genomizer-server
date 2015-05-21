import unittest
from pyicoteolib.core import Cluster, DifferentChromosome, InvalidLine, Region
from pyicoteolib.defaults import *

class TestCoreObjects(unittest.TestCase):
    def setUp(self):
        self.tag_list_short = [Cluster('chr1', 1, 10, read=PK), Cluster('chr1', 5, 14, read=PK)]
        self.cluster_short = Cluster(rounding=True, read=PK)
        self.cluster_short.read_line('chr1 1 14 4:1|6:2|4:1')

        self.pk_cluster = Cluster(rounding=True, read=PK, write=PK)
        self.pk_cluster.read_line('chr1 1 15 4:1|1:2|2:1|3:4|2:5|2:2|1:1')
        self.bed_tag = Cluster(read=BED, write=BED, rounding=True)
        self.bed_tag.read_line('chr1 1 100 hola 666 +')
        self.cluster1 = Cluster(rounding=True, read=PK)
        self.cluster2 = Cluster(rounding=True, read=PK)

    def test_cached_cluster(self):
        cached = Cluster(read=BED, cached=True, rounding=True)
        for i in xrange(0, 200):
            cached.read_line('chr1 %s %s hola 666 +'%(i, i+99))

        self.assertEqual(cached.write_line(), 'chr1\t0\t298\t1:1|1:2|1:3|1:4|1:5|1:6|1:7|1:8|1:9|1:10|1:11|1:12|1:13|1:14|1:15|1:16|1:17|1:18|1:19|1:20|1:21|1:22|1:23|1:24|1:25|1:26|1:27|1:28|1:29|1:30|1:31|1:32|1:33|1:34|1:35|1:36|1:37|1:38|1:39|1:40|1:41|1:42|1:43|1:44|1:45|1:46|1:47|1:48|1:49|1:50|1:51|1:52|1:53|1:54|1:55|1:56|1:57|1:58|1:59|1:60|1:61|1:62|1:63|1:64|1:65|1:66|1:67|1:68|1:69|1:70|1:71|1:72|1:73|1:74|1:75|1:76|1:77|1:78|1:79|1:80|1:81|1:82|1:83|1:84|1:85|1:86|1:87|1:88|1:89|1:90|1:91|1:92|1:93|1:94|1:95|1:96|1:97|1:98|1:99|101:100|1:99|1:98|1:97|1:96|1:95|1:94|1:93|1:92|1:91|1:90|1:89|1:88|1:87|1:86|1:85|1:84|1:83|1:82|1:81|1:80|1:79|1:78|1:77|1:76|1:75|1:74|1:73|1:72|1:71|1:70|1:69|1:68|1:67|1:66|1:65|1:64|1:63|1:62|1:61|1:60|1:59|1:58|1:57|1:56|1:55|1:54|1:53|1:52|1:51|1:50|1:49|1:48|1:47|1:46|1:45|1:44|1:43|1:42|1:41|1:40|1:39|1:38|1:37|1:36|1:35|1:34|1:33|1:32|1:31|1:30|1:29|1:28|1:27|1:26|1:25|1:24|1:23|1:22|1:21|1:20|1:19|1:18|1:17|1:16|1:15|1:14|1:13|1:12|1:11|1:10|1:9|1:8|1:7|1:6|1:5|1:4|1:3|1:2|1:1\t100.0\t+\t149\t20000.0\n')

    #####################   REGION TESTS           ############################
    def test_FDR(self):
        r = Region('', 1, 1999)
        tags = []
        for i in range(0, 50):
            c = Cluster()
            c.read_line('chr4 %s %s 20:1'%(i, i+50))
            tags.append(c)
        r.add_tags(tags, True)
        c = Cluster()
        c.read_line('chr4 55555 55558 7:1')
        r.add_tags(c)
        result = r.get_FDR_clusters()
        self.assertEqual(len(result), 1)


    def test_get_profile(self):
        r = Region(start=1, end=1999)
        c = Cluster(read=BED)
        c.read_line('chr4 1 40')
        r.add_tags(c, True)
        c = Cluster(read=BED, read_half_open=True)
        c.read_line('chr4 400 500')
        r.add_tags(c, True)

        meta = r.get_metacluster()
        self.assertEqual(meta._levels, [[40, 1.0], [360, 0.0], [100, 1.0]])

        
    def test_region_swap_rpkm(self):
        total_reads = 400000000
        total_reads_b = 500000000
        r = Region("chr1", 1, 1000, name2="bla")
        c = Cluster(read=BED)
        c.read_line('chr1 1 40')
        r.add_tags(c)
        c = Cluster(read=BED)
        c.read_line('chr1 2 40')
        r.add_tags(c)
        c = Cluster(read=BED)
        c.read_line('chr1 3 40')
        r.add_tags(c)
        r2 = Region("chr1", 1, 1000, name2="bla")
        c = Cluster(read=BED)
        c.read_line('chr1 100 140')
        r2.add_tags(c)
        c = Cluster(read=BED)
        c.read_line('chr1 101 140')
        r2.add_tags(c)
        c = Cluster(read=BED)
        c.read_line('chr1 102 140')
        r2.add_tags(c)

        swap1, swap2 = r.swap(r2)
        len(r.tags), len(r2.tags), len(swap1.tags), len(swap2.tags), r.rpkm(total_reads), r2.rpkm(total_reads_b), swap1.rpkm((total_reads+total_reads_b)/2), swap2.rpkm((total_reads+total_reads_b)/2) #TODO dont know how to test this

    #####################   CONVERSION TESTS     ############################################
    
    def test_simple_ucsc_representation(self):
        """Confirmed visually at the UCSC browser

        track name=simple_read visibility=full
        chr3 101 200 noname 555 +
        track type=wiggle_0 name=the_test visibility=full
        chr3 101 200 1
        """
        cluster = Cluster(read=BED, write=WIG, read_half_open=True, write_half_open=True, rounding = True, cached=True)
        
        cluster.read_line('chr3 101 200 noname 555 +')
        self.assertEqual(cluster.write_line(), 'chr3\t101\t200\t1\n')
        



    def test_bed_to_half_open_wig(self):
        """Confirmed visually at the UCSC browser

        track name=simple_cluster visibility=full
        chr1 1 100 hola 666  +
        chr1 10 130 hola 666 +
        track type=wiggle_0 name=the_test  visibility=full
        chr1    1       10      1
        chr1    10      100     2
        chr1    100     130     1
        """
        cluster = Cluster(read=BED, write=WIG, read_half_open=True, write_half_open=True, rounding = True, cached=True)
        cluster.read_line('chr1 1 100 hola 666  +')
        cluster.read_line('chr1 10 130 hola 666 +')
        self.assertEqual(cluster.write_line(), 'chr1\t1\t10\t1\nchr1\t10\t100\t2\nchr1\t100\t130\t1\n')


    def read_and_extend(self, cluster, line, extension):
        cluster_aux = Cluster(read=BED, write=BED, read_half_open=True, write_half_open=True, rounding = True)
        if cluster.is_empty():
            cluster.read_line(line)
            cluster.extend(extension)
            #print cluster.write_line(), cluster._profile
        else:
            cluster_aux.read_line(line)
            cluster_aux.extend(extension)
            #print cluster_aux.write_line(), cluster_aux._profile
            cluster += cluster_aux

        #print cluster.write_line()
        return cluster


    def test_pk_printing(self):
        #PENDING
        cluster = Cluster()
        cluster.read_line('chr1 1 100 10:0.76|10:0.001|80:2')
        #print cluster.absolute_split(threshold=0)
        #print cluster.write_line()
        
    def test_split_subtract_result(self):
        sub_result = Cluster(write_half_open=True, cached=True)
        sub_result.read_line('chr4 1 300 20:1|40:0|20:3|20:0.3|10:-6|80:1|10:0')
        clusters = sub_result.absolute_split(threshold=0)
        result = []
        result.append('chr4\t0\t20\t20:1.00\t1.0\t.\t10\t20.0\n')
        result.append('chr4\t60\t100\t20:3.00|20:0.30\t3.0\t.\t70\t66.0\n')
        result.append('chr4\t110\t190\t80:1.00\t1.0\t.\t150\t80.0\n')
        for i in range(0, len(clusters)):
            self.assertEqual(clusters[i].write_line(), result[i])


    def test_bed_to_wig_extended(self):
        """Confirmed visually at the UCSC browser

        track name=unextended_cluster visibility=full
        chr1 1 36 hola 666  +
        chr1 2 37 hola 666  +
        chr1 71 106 hola 666 -
        chr1 73 108 hola 666 -
        track name=extended_cluster visibility=full
        chr1 1 101 hola 666  +
        chr1 2 102 hola 666  +
        chr1 6 106 hola 666 -
        chr1 8 108 hola 666 -
        track type=wiggle_0 name=extended_wig  visibility=full
        chr1    1       2       1
        chr1    2       6       2
        chr1    6       8       3
        chr1    8       101     4
        chr1    101     102     3
        chr1    102     106     2
        chr1    106     108     1
        """
        cluster = Cluster(read=BED, write=WIG, read_half_open=True, write_half_open=True, rounding = True)
        cluster = self.read_and_extend(cluster, 'chr1 1 36 hola 666  +', 100)
        cluster = self.read_and_extend(cluster, 'chr1 2 37 hola 666  +', 100)
        cluster = self.read_and_extend(cluster, 'chr1 71 106 hola 666 -', 100)
        cluster = self.read_and_extend(cluster, 'chr1 73 108 hola 666 -', 100)
        result = Cluster(read=WIG, write=WIG, read_half_open=True, write_half_open=True, rounding = True)
        result.read_line('chr1    1       2       1')
        result.read_line('chr1    2       6       2')
        result.read_line('chr1    6       8       3')
        result.read_line('chr1    8       101     4')
        result.read_line('chr1    101     102     3')
        result.read_line('chr1    102     106     2')
        result.read_line('chr1    106     108     1')
        self.assertEqual(cluster.write_line(), result.write_line())

    def test_sub_fast(self):
       #random/experiment.pk 
       experiment = Cluster(rounding=True)       
       experiment.read_line("chr1   1     1107     101:2|7:1  2.0     .       263     238.0")   
       control = Cluster() 
       control.read_line(  "chr1   46    1222      47:1|54:2|47:1 2.0     .       71331   202.0")
       experiment -= control
       self.assertEqual(experiment.write_line(), 'chr1\t1\t92\t45:2|47:1\t2.0\t.\t23\t137.0\n')#chr1    1       92      45:2|47:1       2.0     .       23      137.0



    """
    def test_sub_fast(self): BACUP
       #random/experiment.pk 
       experiment = Cluster(rounding=True)       
       experiment.read_line("chr17   60   320   59:1.00|42:2.00|52:1.00|101:2.00|7:1.00 2.0     .       71201   404.0")
       print experiment.write_line()
       control = Cluster() 
       #random/control.pk 
       control.read_line("chr17   83   183   101:1.00        1.0     .       71133   101.0")

       experiment -= control
       print experiment._levels
       print experiment.write_line()
       control2 = Cluster() 
       control2.read_line("chr17   258   405   47:1.00|54:2.00|47:1.00 2.0     .       71331   202.0")
       experiment -= control2
       print experiment._levels
       print experiment.write_line()

       #Correcto       
       #chr17 184   304   29:1.00|45:2.00|47:1.00 2.0     .       71235   166.0"""

    def test_strand_add(self):
        c = Cluster(read=BED, cached=True)
        c2 = Cluster(read=BED, cached=True)
        c3 = Cluster(read=BED, cached=True)
        c4 = Cluster(read=BED, cached=True)
        c.read_line("chr1 1 100 0 0 +")
        c2.read_line("chr1 1 100 0 0 +")
        c3.read_line("chr1 1 100 0 0 -")
        c4.read_line("chr1 1 100 0 0 -")
        plus = c + c2
        minus = c3 + c4
        dot = c2 + c3
        self.assertEqual(plus.strand, PLUS_STRAND)
        self.assertEqual(dot.strand, NO_STRAND)
        self.assertEqual(minus.strand, MINUS_STRAND)


    def test_internal_representations(self):
        "Este test prueba la integridad interna de los datos. Si no pasa, MAL"
        bed = Cluster(read=BED, cached=True, read_half_open=True)
        bed.read_line('chr1 0 100 2345 hola +')
        bed._flush_tag_cache()
        pk = Cluster(read=PK)
        pk.read_line('chr1 1 100 100:1')
        open_wig = Cluster(read=WIG, read_half_open=True)
        open_wig.read_line('chr1 0 100 1')
        wig= Cluster(read=WIG)
        wig.read_line('chr1 1 100 1')
        self.assertEqual(bed._levels, pk._levels)
        self.assertEqual(wig._levels, pk._levels)
        self.assertEqual(wig._levels, bed._levels)
        self.assertEqual(wig._levels, open_wig._levels)
        self.assertEqual(wig.start, open_wig.start)
        self.assertEqual(wig.end, open_wig.end)
        self.assertEqual(wig._levels[0], [100, 1.0])

    def test_pk_to_half_open_wig(self):
        cluster = Cluster(read=PK, write=WIG, write_half_open=True, rounding=True)
        cluster.read_line('chr1 1 15 4:1|1:2|2:1|3:4|2:5|2:2|1:1')
        self.assertEqual(cluster.write_line(),'chr1\t0\t4\t1\nchr1\t4\t5\t2\nchr1\t5\t7\t1\nchr1\t7\t10\t4\nchr1\t10\t12\t5\nchr1\t12\t14\t2\nchr1\t14\t15\t1\n')

    def test_wig_read_write(self):
        cluster = Cluster(read=WIG, write=WIG, read_half_open=True, write_half_open=True, rounding=True)
        cluster.read_line('chr2 1 10 1')
        cluster.read_line('chr2 10 16 2')
        cluster.read_line('chr2 16 26 1')
        self.assertEqual(cluster.write_line(), 'chr2\t1\t10\t1\nchr2\t10\t16\t2\nchr2\t16\t26\t1\n')
        cluster.write_as(WIG, False)
        self.assertEqual(cluster.write_line(), 'chr2\t2\t10\t1\nchr2\t11\t16\t2\nchr2\t17\t26\t1\n')

    def test_pk_to_variable_wig(self):
        cluster = Cluster(read=PK, write=VARIABLE_WIG)
        cluster.read_line('chr1    1599889 1600275 61:1.77|52:2.65|7:1.77|4:2.65|6:3.54|9:2.65|19:3.54|6:4.42|14:5.31|1:6.19|11:7.08|1:7.96|20:7.08|9:7.96|1:8.85|13:9.73|1:10.62|8:11.50|5:12.39|6:13.27|2:12.39|1:13.27|1:14.16|11:15.04|19:14.16|6:13.27|14:12.39|1:11.50|11:10.62|21:9.73|9:8.85|1:7.96|13:7.08|1:6.19|8:5.31|5:4.42|2:3.54|6:2.65|1:1.77')
        self.assertEqual(cluster.write_line(), '1599889\t1.77\n1599909\t1.77\n1599929\t1.77\n1599949\t2.606\n1599969\t2.65\n1599989\t2.342\n1600009\t2.9615\n1600029\t3.628\n1600049\t5.2645\n1600069\t7.124\n1600089\t7.476\n1600109\t10.173\n1600129\t12.918\n1600149\t14.556\n1600169\t13.362\n1600189\t11.372\n1600209\t9.73\n1600229\t8.053\n1600249\t5.3085\n')

    def test_bed_reader(self):
        self.assertEqual(self.bed_tag.start, 1)
        self.assertEqual(self.bed_tag.end, 100)
        self.assertEqual(self.bed_tag.name2, 'hola')
        self.assertEqual(self.bed_tag.strand, '+')

    def test_bed_writer(self):
        self.assertEqual(self.bed_tag.write_line(), 'chr1\t1\t100\thola\t666\t+\n')

    def test_pk_reader_writer(self):
        self.assertEqual(self.pk_cluster.write_line(), 'chr1\t1\t15\t4:1|1:2|2:1|3:4|2:5|2:2|1:1\t5.0\t.\t11\t35.0\n')

    def test_pk_to_bed(self):
        self.cluster_short.set_tag_length(10)
        self.cluster_short.write_as(BED)
        self.assertEqual(self.cluster_short.write_line(), 'chr1\t1\t10\tnoname\t0\t.\nchr1\t5\t14\tnoname\t0\t.\n')

    def test_bed_half_open_to_wig_half_open(self):
        cluster = Cluster(read=BED, write=WIG, read_half_open=True, write_half_open=True, rounding=True)
        cluster.read_line('chr1\t0\t100\t666\thola\t+\n')
        cluster.read_line('chr1\t1\t101\t666\thola\t+\n')
        self.assertEqual(cluster.write_line(), 'chr1\t0\t1\t1\nchr1\t1\t100\t2\nchr1\t100\t101\t1\n')

    def test_bed_half_open_to_wig_half_open2(self):
        cluster = Cluster(read=BED, write=WIG, read_half_open=True, write_half_open=True, rounding=True, cached=True)
        cluster2 = Cluster(read=BED, write=WIG, read_half_open=True, write_half_open=True, rounding=True, cached=True)
        cluster3 = Cluster(read=BED, write=WIG, read_half_open=True, write_half_open=True, rounding=True, cached=True)
        result = Cluster(read=BED, write=WIG, read_half_open=True, write_half_open=True, rounding=True)
        extension = 130
        result.read_line('chr1    156     192     id:7043691      1000    +')
        result.extend(extension)
        cluster.read_line('chr1    241     277     id:916714       1000    +')
        cluster.extend(extension)
        result +=cluster
        cluster2.read_line('chr1    241     277     id:916714       1000    +')
        cluster2.extend(extension)
        result +=cluster2
        cluster3.read_line('chr1    241     277     id:6880101      1000    +')
        cluster3.extend(extension)
        result +=cluster3

        self.assertEqual(result.write_line(), 'chr1\t156\t241\t1\nchr1\t241\t286\t4\nchr1\t286\t371\t3\n')

    def test_openwig_to_pk(self):
        cluster = Cluster(read=WIG, write=PK, read_half_open=True, rounding = True)
        cluster.read_line('chr1    1599888 1599949 1.77')
        cluster.read_line('chr1    1599949 1600001 2.65')
        cluster.read_line('chr1    1600001 1600008 1.77')
        cluster.read_line('chr1    1600008 1600012 2.65')
        cluster.read_line('chr1    1600012 1600018 3.54')
        cluster.read_line('chr1    1600018 1600027 2.65')
        cluster.read_line('chr1    1600027 1600046 3.54')
        cluster.read_line('chr1    1600046 1600052 4.42')
        cluster.read_line('chr1    1600052 1600066 5.31')
        cluster.read_line('chr1    1600066 1600067 6.19')
        cluster.read_line('chr1    1600067 1600078 7.08')
        cluster.read_line('chr1    1600078 1600079 7.96')
        cluster.read_line('chr1    1600079 1600099 7.08')
        cluster.read_line('chr1    1600099 1600108 7.96')
        cluster.read_line('chr1    1600108 1600109 8.85')
        cluster.read_line('chr1    1600109 1600122 9.73')
        cluster.read_line('chr1    1600122 1600123 10.62')
        cluster.read_line('chr1    1600123 1600131 11.50')
        cluster.read_line('chr1    1600131 1600136 12.39')
        cluster.read_line('chr1    1600136 1600138 13.27')
        cluster.read_line('chr1    1600138 1600142 13.27')
        cluster.read_line('chr1    1600142 1600144 12.39')
        cluster.read_line('chr1    1600144 1600145 13.27')
        cluster.read_line('chr1    1600145 1600146 14.16')
        cluster.read_line('chr1    1600146 1600157 15.04')
        cluster.read_line('chr1    1600157 1600176 14.16')
        cluster.read_line('chr1    1600176 1600182 13.27')
        cluster.read_line('chr1    1600182 1600196 12.39')
        cluster.read_line('chr1    1600196 1600197 11.50')
        cluster.read_line('chr1    1600197 1600208 10.62')
        cluster.read_line('chr1    1600208 1600229 9.73')
        cluster.read_line('chr1    1600229 1600238 8.85')
        cluster.read_line('chr1    1600238 1600239 7.96')
        cluster.read_line('chr1    1600239 1600252 7.08')
        cluster.read_line('chr1    1600252 1600253 6.19')
        cluster.read_line('chr1    1600253 1600261 5.31')
        cluster.read_line('chr1    1600261 1600266 4.42')
        cluster.read_line('chr1    1600266 1600268 3.54')
        cluster.read_line('chr1    1600268 1600274 2.65')
        cluster.read_line('chr1    1600274 1600275 1.77')

        cluster2 = Cluster(read=PK, write=WIG, write_half_open=True)
        cluster2.read_line('chr1    1599889 1600275 61:1.77|52:2.65|7:1.77|4:2.65|6:3.54|9:2.65|19:3.54|6:4.42|14:5.31|1:6.19|11:7.08|1:7.96|20:7.08|9:7.96|1:8.85|13:9.73|1:10.62|8:11.50|5:12.39|2:13.27|4:13.27|2:12.39|1:13.27|1:14.16|11:15.04|19:14.16|6:13.27|14:12.39|1:11.50|11:10.62|21:9.73|9:8.85|1:7.96|13:7.08|1:6.19|8:5.31|5:4.42|2:3.54|6:2.65|1:1.77')
        self.assertEqual(cluster, cluster2)
    
    def test_bed_to_wig_using_clusters(self):
        cluster = Cluster(read=BED, write=WIG, rounding = True, cached=True)
        cluster.read_line('chr1 1 100 hola 666 +')
        cluster.read_line('chr1 10 130 hola 666 +')
        self.assertEqual(cluster.write_line(), 'chr1\t1\t9\t1\nchr1\t10\t100\t2\nchr1\t101\t130\t1\n')
    
    def test_overlap(self):
        cluster1 = Cluster(read=BED)
        cluster1.read_line('chr1 1 100 hola 666 +')
        cluster2 = Cluster(read=BED)
        cluster2.read_line('chr1 51 200 hola 666 +')
        cluster3 = Cluster(read=PK)
        cluster3.read_line('chr3 1 100 100:1')
        cluster4 = Cluster(read=BED)
        cluster4.read_line('chr5 1 1000 hola 666 +')
        cluster5 = Cluster(read=PK)
        cluster5.read_line('chr5 1 300 300:1')
        cluster6 = Cluster(read=BED)
        cluster6.read_line('chr5 100 900 hola 666 +')

        cluster_discard = Cluster(read=BED, read_half_open=True)
        cluster_discard.read_line('chrX	61836251	61836287	id:105282	1000	+')
        cluster_satellite = Cluster(read=BED)
        cluster_satellite.read_line('chrX	61836270	61837703	Satellite')
        self.assertEqual(cluster_discard.overlap(cluster_satellite), 0.5)
        self.assertEqual(cluster1.overlap(cluster3), 0)
        self.assertEqual(cluster1.overlap(cluster1), 1)
        self.assertEqual(cluster1.overlap(cluster2), 0.5)

        #print cluster4.overlap(cluster5)
        #print cluster5.overlap(cluster4)
        #print cluster4.overlap(cluster6)

    def test_subtraction_and_split(self):

        r5_cluster = Cluster(read=BED, read_half_open=True)
        t0_cluster1 = Cluster(read=BED, read_half_open=True)
        t0_cluster2 = Cluster(read=BED, read_half_open=True)
        t0_cluster3 = Cluster(read=BED, read_half_open=True)
        r5_cluster.normalize_factor = 0.839874264736
        r5_cluster.read_line('chr1    74953355        74953485        id:3920202      1000    +')
        r5_cluster.read_line('chr1    74953361        74953491        id:10182740     1000    +')
        r5_cluster.read_line('chr1    74953455        74953585        id:9188423      1000    +')
        r5_cluster.read_line('chr1    74953455        74953585        id:9188423      1000    +')
        r5_cluster.read_line('chr1    74953558        74953688        id:386762       1000    -')
        r5_cluster.read_line('chr1    74953558        74953688        id:6854318      1000    -')
        r5_cluster.read_line('chr1    74953600        74953730        id:2599120      1000    -')
        r5_cluster.read_line('chr1    74953619        74953749        id:5380695      1000    -')

        t0_cluster1.read_line('chr1    74953283        74953413        id:8980785      1000    +')
        t0_cluster2.read_line('chr1    74953440        74953570        id:9844290      1000    -')
        t0_cluster3.read_line('chr1    74953785        74953915        id:1331588      1000    +')
        """
        TO no extendido

        chr1    283        319        id:8980785      1000    +
        chr1    534        570        id:9844290      1000    -
        chr1    785        821        id:1331588      1000    +
        """
        r5_cluster -= t0_cluster1
        r5_cluster -= t0_cluster2
        r5_cluster -= t0_cluster3
        #print 'AQUI:', r5_cluster.write_line()


        """r5_cluster.read_line('chr1    74953302        74953432        id:8971336      1000    -')
        r5_cluster.read_line('chr1    74953340        74953470        id:7068001      1000    -')
        r5_cluster.read_line('chr1    74953345        74953475        id:5184564      1000    -')
        r5_cluster.read_line('chr1    74953355        74953485        id:3920202      1000    +')"""

        """r5_cluster.read_line('chr1    74953648        74953778        id:994315       1000    +')
        r5_cluster.read_line('chr1    74953655        74953785        id:227378       1000    +')
        r5_cluster.read_line('chr1    74953655        74953785        id:227378       1000    +')
        r5_cluster.read_line('chr1    74953655        74953785        id:227378       1000    +')
        r5_cluster.read_line('chr1    74953655        74953785        id:227378       1000    +')
        r5_cluster.read_line('chr1    74953659        74953789        id:7728529      1000    +')
        r5_cluster.read_line('chr1    74953659        74953789        id:7728529      1000    +')
        r5_cluster.read_line('chr1    74953663        74953793        id:4431027      1000    +')
        r5_cluster.read_line('chr1    74953663        74953793        id:4431027      1000    +')
        r5_cluster.read_line('chr1    74953663        74953793        id:8828390      1000    +')
        r5_cluster.read_line('chr1    74953668        74953798        id:10143275     1000    +')
        r5_cluster.read_line('chr1    74953668        74953798        id:10143275     1000    +')
        r5_cluster.read_line('chr1    74953673        74953803        id:2638800      1000    +')
        r5_cluster.read_line('chr1    74953673        74953803        id:9469850      1000    +')
        r5_cluster.read_line('chr1    74953686        74953816        id:9965178      1000    -')
        r5_cluster.read_line('chr1    74953692        74953822        id:6509113      1000    +')
        r5_cluster.read_line('chr1    74953692        74953822        id:7927956      1000    +')
        r5_cluster.read_line('chr1    74953720        74953850        id:3496791      1000    -')
        r5_cluster.read_line('chr1    74953720        74953850        id:3496791      1000    -')
        r5_cluster.read_line('chr1    74953720        74953850        id:3496791      1000    -')
        r5_cluster.read_line('chr1    74953720        74953850        id:3496791      1000    -')"""

    def test_trim(self):
        cluster = Cluster(rounding=True)
        cluster.read_line('chr21 1 10 1:1|1:2|3:5|1:1|1:4|1:3|2:1')
        cluster.trim(absolute=3)
        self.assertEqual(cluster.write_line(), 'chr21\t3\t8\t3:5|1:1|1:4|1:3\t5.0\t.\t4\t23.0\n')

    def test_eland_read_write(self):
        cluster = Cluster(read=ELAND, write=ELAND)
        cluster.read_line('>EAS38_1_1_113_546      TAGAATAGGCGAGAATAAAGATGTTGTCTTAGAAT     U0      1       0       0       chr2.fa 199668361       R       ..')
        self.assertEqual(cluster.write_line(), '>EAS38_1_1_113_546\tTAGAATAGGCGAGAATAAAGATGTTGTCTTAGAAT\tU0\t1\t0\t0\tchr2.fa\t199668361\tR\t..\t26A\n')

    def test_eland(self):

        cluster = Cluster(read=ELAND)
        cluster.read_line('>EAS38_1_1_113_546      TAGAATAGGCGAGAATAAAGATGTTGTCTTAGAAT     U0      1       0       0       chr2.fa 199668361       R       ..')
        cluster.read_line('>EAS38_1_1_113_546      TAGAATAGGCGAGAATAAAGATGTTGTCTTAGAAT     U0      1       0       0       chr2.fa 199668362       R       ..')
        self.assertEqual(cluster.write_line(), 'chr2\t199668361\t199668396\t1:1.00|34:2.00|1:1.00\t2.0\t-\t199668378\t70.0\n')

    #def test_fixed_wig(self):
        #self.pk_cluster.write_as(FIXED_WIG)
        #print self.pk_cluster.write_line()

    def test_get_max_height_pos_bug2(self):
        cluster = Cluster()
        cluster.read_line("chr1    120967734       120967735       1:0.50|1:0.25")
        self.assertEqual(cluster.write_line(), 'chr1\t120967734\t120967735\t1:0.50|1:0.25\t0.5\t.\t120967734\t0.75\n')
        


    def test_variable_wig(self):
        double_cluster = Cluster(read=PK, write=VARIABLE_WIG, rounding=True)
        double_cluster.read_line('chr1        1        111        5:1|11:2|5:1|10:3|5:5|14:8|10:0.7|5:1|5:4|10:0.8|5:0.7|10:2|5:3|5:8|10:6|6:2')
        """print
        print double_cluster.write_line()
        double_cluster.write_as(WIG)"""

    #####################   END CONVERSION TESTS   ############################################


    """
    Adding disordered is no longer a feature
        def test_add_disordered(self):
        cluster = Cluster(read=BED)
        cluster.read_line('chr1 101 150')
        cluster.read_line('chr1 50 200')
        self.assertEqual(cluster.write_line(), 'chr1\t50\t200\t51:1.00|50:2.00|50:1.00\n')
        """

    def test_normalized_counts(self):
        total_number_reads = 1e7
        region = Region("chr1", 1, 300)
        region_bed12 = Region("chr1", 1, 300, exome_size = 200)
        c = Cluster(read=BED)
        for i in range(0, 5):
            c.read_line("chr1 1 100")
            region.add_tags(c, True)
            region_bed12.add_tags(c, True)
            c.clear()

        self.assertEqual(region.normalized_counts(), 5.) #simple-counts
        self.assertEqual(region.normalized_counts(region_norm=True, total_n_norm=True, total_reads = total_number_reads), 1.666666666666667) #rpkm
        self.assertEqual(region_bed12.normalized_counts(region_norm=True, total_n_norm=True, total_reads = total_number_reads), 2.5) #rpkm with exon_size


        self.assertEqual(region.normalized_counts(pseudocount=True), 6.) #with pseudocounts
        self.assertEqual(region.normalized_counts(region_norm=True, total_n_norm=True, total_reads = total_number_reads, regions_analyzed=10000, pseudocount=True), 1.998001998001998)
        
        
    def test_max_height_pos(self):
        cluster = Cluster(rounding=True, read=PK, write=PK)
        cluster.read_line('chr1 101 142 4:1|1:2|2:1|3:4|29:5|2:2|1:1')
        self.assertEqual(cluster.max_height_pos(), 125)
    
    def test_bug_contiguous_peaks(self):
        cluster = Cluster(rounding=True, read=PK, write=PK)
        cluster2 = Cluster(rounding=True, read=PK, write=PK)
        cluster.read_line('chr1    849917  850408  8:2|10:4|80:5|23:6|29:7|8:5|10:3|39:2|12:3|29:4|5:3|18:4|41:3|30:4|15:5|12:4|34:3|59:2|30:1')
        cluster2.read_line('chr1    850408  850648  66:2|25:3|59:4|66:2|25:1        +')
        result = cluster + cluster2
        self.assertTrue(cluster.intersects(cluster2))

    def test_sub_and_print(self):
        cluster = Cluster()
        cluster2 = Cluster(write_half_open=False)
        cluster.read_line('chr1    1  1000  10:2|10:4|80:5|500:7|100:7|100:5')
        cluster2.read_line('chr1    11  1000  10:4|80:5|500:6|100:7|99:5|1:4.99')
        cluster2 = cluster - cluster2
        self.assertEqual(cluster2.write_line(), 'chr1\t1\t10\t10:2.00\t2.0\t.\t5\t20.0\nchr1\t101\t600\t500:1.00\t1.0\t.\t350\t500.0\nchr1\t800\t800\t1:0.01\t0.01\t.\t800\t0.01\n')
        #cluster2.write_as(PK, True)
        #self.assertEqual(cluster2.write_line(), 'chr1\t1\t10\t10:2.00\t2.0\t.\t5\t520.01\nchr1\t100\t600\t500:1.00\t1.0\t.\t5\t520.01\nchr1\t799\t800\t1:0.01\t0.01\t.\t5\t520.01\n')

    def test_intersects(self):
        self.assertTrue(Cluster('chr1', 1, 10).intersects(Cluster('chr1', 10, 14)))
        self.assertFalse(Cluster('chr2', 1, 10).intersects(Cluster('chr1', 4, 14)))
        self.assertTrue(Cluster('chr1', 1, 10).intersects(Cluster('chr1', 4, 14)))
        cluster = Cluster(read=PK, rounding=True)
        cluster.read_line('chr1  1 100 100:1')
        cluster2 = Cluster(read=PK,rounding=True)
        cluster2.read_line('chr1 100 199 100:1')
        result = cluster + cluster2
        self.assertTrue(cluster.intersects(cluster2))

    def test_intersects2(self):
        cluster = Cluster(read=PK, rounding=True)
        cluster.read_line('chr1  1 100 100:1')
        cluster2 = Cluster(read=PK,rounding=True)
        cluster2.read_line('chr1 5 50 100:1')
        self.assertTrue(cluster.intersects(cluster2))

    def test_subtract_with_gaps(self):
        cluster1 = Cluster()
        cluster2 = Cluster()
        cluster1.read_line("chr2 1 100 30:1|50:2|40:1|3000:3")
        cluster2.read_line("chr2 1 100 30:1|50:0|40:1|200:0|5000:1")
        cluster1 -= cluster2
        self.assertEqual(cluster1._profile, [[50, 2.0], [40, 0.0], [200, 3.0], [2800, 2.0]])

    def test_subtract_with_gaps(self):
        """
        238c238
        < chr17	147132	147296	5:1.00|22:2.00|37:1.00|37:2.00|64:1.00	2.0	.	147180	224.0
        ---
        > chr17	147132	147260	5:1.00|22:2.00|37:1.00|37:2.00|28:1.00	2.0	.	147180	188.0
        
        experiment
        chr17 146970 147070
        chr17 147058 147158
        chr17 147132 147232
        chr17 147196 147296
        chr17 147303 147403
        chr17 147445 147545
        chr17 147461 147561

        control
        chr17 146904 147004
        chr17 147036 147136
        chr17 147261 147361
        chr17 147437 147537
        chr17 147472 147572
        chr17 147472 147572
        """        
        exp_cached = Cluster(cached=True) 
        control0 = Cluster(read=BED, cached=True)
        control1 = Cluster(read=BED, cached=True) 
        control2 = Cluster(read=BED, cached=True)
        control3 = Cluster(read=BED, cached=True)
        control4 = Cluster(read=BED, cached=True)

        #experiment.bed
        exp_cached.read_line("chr17	146970	147296	88:1.00|13:2.00|61:1.00|27:2.00|37:1.00|37:2.00|64:1.00	2.0	.	147139	404.0")
        #print exp_cached.write_line()
        #control.bed
        r = Region("chr17", 1, 2000)
        control0.read_line("chr17 146904 147004")
        control1.read_line("chr17 147036 147136")
        control2.read_line("chr17 147261 147361")
        control3.read_line("chr17 147437 147537")
        control4.read_line("chr17 147472 147572")

        r.add_tags([control1, control2])
        meta = r.get_metacluster()
        #print meta._profile
        exp_cached -= meta
        #print "CACHED:\n", exp_cached.write_line()

        
    def test_bug_is_artifact(self):
        cluster = Cluster(rounding=True)
        cluster.read_line('chr1  10505760  10505928   8:4|3:5|5:6|8:8|2:10|1:11|5:12|3:15|2:16|2:16|3:18|7:20|3:19|57:20|21:19|8:15|3:14|5:13|8:11|2:9|1:8|5:7|3:4|2:3|2:3')
        self.assertTrue(cluster.is_artifact(0.3))


    def test_split(self):
        double_cluster = Cluster(rounding=True)
        double_cluster.read_line('chr1  100  215  5:1|10:5|5:7|5:80|5:1|5:40|15:1|10:2|5:3|5:8|10:6|10:5|10:4|10:3|6:2')

        results = double_cluster.split(0.01)
        correct_clusters = [Cluster(rounding=True), Cluster(rounding=True), Cluster(rounding=True)]
        correct_clusters[0].read_line('chr1    100      125      5:1|10:5|5:7|5:80|2:1')
        correct_clusters[1].read_line('chr1    128      141      2:1|5:40|7:1')
        correct_clusters[2].read_line('chr1    143      215      7:1|10:2|5:3|5:8|10:6|10:5|10:4|10:3|6:2')
        for i in range (0,len(correct_clusters)):
            self.assertEqual(results[i].write_line(), correct_clusters[i].write_line())


    def test_is_artifact(self):
        artifact = Cluster(read=PK, write=PK, rounding=True)
        artifact.read_line('chr1        1        111        5:1|100:4|6:2')
        almost_not_artifact = Cluster(read=PK, write=PK, rounding=True)
        almost_not_artifact.read_line('chr1        1        100        35:1|30:4|35:1')
        almost_artifact = Cluster(read=PK, write=PK, rounding=True)
        almost_artifact.read_line('chr1        1        99        45:1|25:4|30:1')
        almost_artifact._recalculate_end()
        self.assertTrue(artifact.is_artifact())
        self.assertFalse(almost_not_artifact.is_artifact()) #with the changes for version 0.6.1, this line IS an artifact
        self.assertFalse(almost_artifact.is_artifact())

    def test_is_significant(self):
        cluster = Cluster(rounding=True)
        cluster.read_line('chr1 1 15 4:1|1:2|2:1|3:4|2:5|2:2|1:1') #area 35
        self.assertTrue(cluster.is_significant(5, "numreads"))
        self.assertTrue(cluster.is_significant(34, "numreads"))
        self.assertFalse(cluster.is_significant(36, "numreads"))
        self.assertFalse(cluster.is_significant(20))
        self.assertTrue(cluster.is_significant(1))
        self.assertTrue(cluster.is_significant(5))

    def test_tag_eq_override(self):
        tag1 = Cluster('M', 1, 100)
        tag2 = Cluster('M', 1, 100)
        tag3 = Cluster('X', 2, 140)
        self.assertEqual(tag1, tag2)
        self.assertNotEqual(tag1, tag3)

    def test_nonzero(self):
        tag = Cluster()
        self.assertTrue(not tag)
        tag.read_line("chr1 1 100 100:1")
        self.assertFalse(not tag)

    def test_comparison(self):
        cluster = Region("chr1", 1, 100)
        clusterdup = Cluster(read=BED)
        clusterdup.read_line("chr1 1 100")
        cluster2 = Cluster(read=BED)
        cluster2.read_line("chr4 1000 1010")
        cluster3 = Cluster(read=BED)
        cluster3.read_line("chr5 3 103")
        self.assertTrue(cluster < cluster2)
        self.assertTrue(cluster2 < cluster3)
        self.assertFalse(cluster > cluster3)
        self.assertFalse(cluster < clusterdup)
        self.assertTrue(cluster <= clusterdup)

    def test_eq(self):
        cluster = Cluster(read=PK)
        cluster2 = Cluster(read=PK)
        cluster3 = Cluster(read=PK)
        cluster.read_line('chr1 1 15 4:1|1:2|2:1|3:4|2:5|2:2|1:1')
        cluster2.read_line('chr1 1 15 4:1|1:2|2:1|3:4|2:5|2:2|1:1')
        cluster3.read_line('chr1 1 15 4:1|1:2|2:1|3:4|2:5|2:2')
        self.assertEqual(cluster, cluster2)
        self.assertNotEqual(cluster, cluster3)

    def test_extend(self):
        cluster = Cluster(read=BED)
        cluster.read_line('chr3 1 35 noname 666 +')
        cluster2 = Cluster(read=BED)
        cluster2.read_line('chr3 56 100 noname 666 -')
        cluster.extend(100)
        cluster2.extend(100)
        self.assertEqual(cluster.write_line(), 'chr3\t1\t100\t100:1.00\t1.0\t+\t50\t100.0\n')
        self.assertEqual(cluster2.write_line(), 'chr3\t1\t100\t100:1.00\t1.0\t-\t50\t100.0\n')

    def test_extend_bug(self):
        cluster = Cluster(read=BED, write=PK)
        cluster.read_line('chr3 1 35 666 noname +')
        cluster2 = Cluster(read=BED, write=PK)
        cluster2.read_line('chr3 156 200 666 noname -')
        cluster.extend(100)
        cluster2.extend(100)
        result = cluster + cluster2
        self.assertEqual(200, len(result))

    def test_extreme_split(self):
        cluster = Cluster()
        cluster.read_line('chr3 1 35 20:5|20:0|30:4')
        clusters = cluster.absolute_split(threshold=0)
        self.assertEqual(len(clusters), 2)

    def test_is_singleton(self):
        self.assertFalse(self.cluster_short.is_singleton())
        self.assertTrue(self.bed_tag.is_singleton())
        cluster = Cluster(read=PK)
        cluster.read_line("chr1 1 100 100:2")
        self.assertFalse(cluster.is_singleton())

    def test_cluster_to_tag(self):
        self.cluster_short.set_tag_length(10)
        tags = self.cluster_short.decompose()
        for tag in range(0, len(self.tag_list_short)):
            self.assertEqual(self.tag_list_short[tag], tags[tag])

    def test_decompose_alobestia(self):
        super_cluster = Cluster(read=PK, write= BED, tag_length=130)
        super_cluster.read_line("chrX    2895999 2896606 10:1|42:2|14:6|33:7|31:10|6:9|4:10|25:9|2:10|1:12|3:13|5:14|6:15|5:12|4:13|5:14|4:13|8:15|13:16|6:18|2:19|5:16|15:17|17:18|29:17|2:16|1:14|2:13|1:16|5:15|6:14|5:13|4:12|5:11|4:12|8:10|10:9|3:11|6:9|7:8|8:7|7:10|2:9|37:10|12:12|26:9|2:8|2:10|18:12|24:12|9:9|37:8|40:6|2:4|18:2")
        tags = super_cluster.decompose()
        for tag in tags:
            self.assertEqual(len(tag), 130)


    def test_is_contiguous_wig(self):
        cluster = Cluster(read=WIG,  read_half_open=True)
        cluster.read_line('chr1    1599888 1599949 1.77')
        cluster2 = Cluster(read=WIG, write=WIG,  read_half_open=True)
        cluster2.read_line('chr1    1599949 1600001 2.65')
        #print cluster2.write_line()
        self.assertTrue(cluster.is_contiguous(cluster2))

    def test_is_contiguous_pk(self):
        cluster = Cluster(read=PK, rounding=True)
        cluster.read_line('chr1  1 100 100:1')
        cluster2 = Cluster(read=PK,rounding=True)
        cluster2.read_line('chr1 101 200 100:1')
        self.assertTrue(cluster.is_contiguous(cluster2))

    def test_write_pseudoeland(self):
        self.bed_tag.write_as(ELAND)
        self.bed_tag.write_line()

    def test_write_pk(self):
        cluster = Cluster(write=PK, name='chr5', start=1, rounding=True, write_half_open=True)
        cluster.append_level(100, 1)
        cluster.append_level(50, 2)
        cluster.append_level(50, 3)
        cluster.append_level(100, 1)
        cluster.append_level(200, 0)
        cluster.append_level(1, 3)
        cluster.append_level(10, 0)
        cluster.append_level(100, 1)
        self.assertEqual(cluster.write_line(), 'chr5\t0\t300\t100:1|50:2|50:3|100:1\t3.0\t.\t175\t450.0\nchr5\t500\t501\t1:3\t3.0\t.\t501\t3.0\nchr5\t511\t611\t100:1\t1.0\t.\t561\t100.0\n') #HALF OPEN
        cluster.write_as(PK, False) #Now write it as closed
        self.assertEqual(cluster.write_line(),'chr5\t1\t300\t100:1|50:2|50:3|100:1\t3.0\t.\t175\t450.0\nchr5\t501\t501\t1:3\t3.0\t.\t501\t3.0\nchr5\t512\t611\t100:1\t1.0\t.\t561\t100.0\n') #Closed

    def test_read_wig(self):
        cluster1 = Cluster(read=WIG, name='chr5', start=1, rounding=True, read_half_open=True)
        cluster1.read_line('chr6 0 100 1')
        cluster2 = Cluster(read=WIG, name='chr5', start=1, rounding=True, read_half_open=False)
        cluster2.read_line('chr6 1 100 1')
        self.assertEqual(cluster1, cluster2)

    def test_pk_print_empty(self):
        cluster1 = Cluster(write=PK)
        self.assertEqual(cluster1.write_line(), '')
        cluster1.write_as(WIG)
        self.assertEqual(cluster1.write_line(), '')
        cluster1.write_as(BED)
        self.assertEqual(cluster1.write_line(), '')
        cluster1.write_as(ELAND)
        self.assertEqual(cluster1.write_line(), '')

    def test_is_singleton_error(self):
        cluster = Cluster(read=PK, write=PK)
        self.assertEqual(cluster.is_singleton(), False)

    def test_is_empty(self):
        cluster = Cluster(read=BED)
        self.assertEqual(cluster.is_empty(), True)
        cluster.read_line('chr1 10 130 666 hola +')
        self.assertEqual(cluster.is_empty(), False)
        cluster = Cluster(read=PK)
        self.assertEqual(cluster.is_empty(), True)
        cluster.read_line('chr1 1 15 4:1|1:2|2:1|3:4|2:5|2:2|1:1')
        self.assertEqual(cluster.is_empty(), False)
        cluster2 = Cluster(read=PK)
        cluster2.read_line('chr1 1 15 4:1|1:2|2:1|3:4|2:5|2:2|1:1')
        result = cluster - cluster2
        self.assertEqual(result.is_empty(), True)

    def test_max_height(self):
        cluster = Cluster(read=PK)
        cluster.read_line('chr1 1 15 4:1|1:2|2:10|3:4|2:15|2:2|1:1')
        self.assertEqual(cluster.max_height(), 15)


    def test_add_bed(self):
        cluster1 = Cluster(read=BED)
        cluster2  = Cluster(read=BED)
        cluster3 =  Cluster(read=BED)
        cluster4 =  Cluster(read=BED)
        cluster5 =  Cluster(read=BED)
        cluster6 =  Cluster(read=BED)
        cluster1.read_line('chr1 1 100 666 hola +')
        cluster2.read_line('chr1 1 100 666 hola +')
        cluster3.read_line('chr1 1 50 666 hola +')
        cluster4.read_line('chr1 10 145 666 hola +')
        cluster5.read_line('chr1 45 95 666 hola +')
        cluster6.read_line('chr1 1 200 666 hola +')

        result1 = cluster1 +  cluster2
        result2 = cluster1 +  cluster3
        result3 = cluster4 + cluster1
        #result4 = cluster1 +  cluster3 + cluster4
        result5 = cluster1 + cluster5
        result6 = cluster1 + cluster6
        self.assertEqual(result1.write_line(), 'chr1\t1\t100\t100:2.00\t2.0\t+\t50\t200.0\n')
        self.assertEqual(result2.write_line(), 'chr1\t1\t100\t50:2.00|50:1.00\t2.0\t+\t25\t150.0\n')
        self.assertEqual(result3.write_line(), 'chr1\t1\t145\t9:1.00|91:2.00|45:1.00\t2.0\t+\t55\t236.0\n')
        #self.assertEqual(result4.write_line(), 'chr1\t1\t145\t9:2.00|41:3.00|50:2.00|45:1.00\n')
        self.assertEqual(result5.write_line(), 'chr1\t1\t100\t44:1.00|51:2.00|5:1.00\t2.0\t+\t70\t151.0\n')
        #self.assertEqual(result6.write_line(), 'chr1\t1\t200\t100:2.00|100:1.00\n')
    
    def test_add2(self):
        cluster =  Cluster(read=BED)
        cluster.read_line('chr1 1 20000 666 hola +')
        cluster.read_line('chr1 1 20000 666 hola +')
        cluster.read_line('chr1 1 20000 666 hola +')
        cluster.read_line('chr1 1001 20000 666 hola +')
        self.assertEqual(cluster.write_line(), 'chr1\t1\t20000\t1000:3.00|19000:4.00\t4.0\t+\t10500\t79000.0\n')


    def test_add_pk(self):
        cluster1 = Cluster(read=PK)
        cluster2 = Cluster(read=PK)
        cluster1.read_line('chr1\t1\t145\t9:2.00|41:3.00|50:2.00|45:1.00\n')
        cluster2.read_line('chr1\t1\t125\t9:4.00|41:3.00|30:2.00|45:1.00\n')
        result = cluster1 + cluster2
        self.assertEqual(result.write_line(), 'chr1\t1\t145\t50:6.00|30:4.00|20:3.00|25:2.00|20:1.00\t6.0\t.\t25\t550.0\n')

    def test_cluster_pk(self):
        cluster = Cluster(read=PK)
        cluster.read_line('chr1\t1\t145\t9:2.00|41:3.00|50:2.00|45:1.00\n')
        cluster.read_line('chr1\t1\t125\t9:4.00|41:3.00|30:2.00|45:1.00\n')
        self.assertEqual(cluster.write_line(), 'chr1\t1\t145\t50:6.00|30:4.00|20:3.00|25:2.00|20:1.00\t6.0\t.\t25\t550.0\n')

    #def test_add_differentchr(self):
    #    self.assertRaises(DifferentChromosome, self.DifferentChromosome)

    def read_invalid_lines(self):
       cluster = Cluster(read=SAM)
       for i in range(100):
             cluster.read_line('chr1 1 15 4:1|1:2|2:10|3:4|2:15|2:2|1:1')

    def test_read_invalid_lines(self):
        self.assertRaises(InvalidLine, self.read_invalid_lines)

    def DifferentChromosome(self):
        cluster1 = Cluster(read=PK)
        cluster1.read_line('chr1 1 15 4:1|1:2|2:10|3:4|2:15|2:2|1:1')
        cluster2 = Cluster(read=PK)
        cluster2.read_line('chr2 1 15 4:1|1:2|2:10|3:4|2:15|2:2|1:1')
        a = cluster1 +cluster2

def suite():
   suite = unittest.TestSuite()
   suite.addTest(unittest.makeSuite(TestCoreObjects))
   return suite

if __name__ == '__main__':
    unittest.main()
