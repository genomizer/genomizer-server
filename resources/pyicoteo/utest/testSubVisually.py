import matplotlib.pyplot as plt
from picos.core import Cluster

p = Cluster(rounding = True)
p.read_line("chr1    142740079   142740546  9:1|2:2|9:4|2:5|14:6|3:6|1:7|5:9|2:8|4:8|5:11|2:10|3:10|5:11|2:13|4:15|3:14|1:13|3:11|1:13|3:14|4:12|1:9|3:10|3:11|3:10|5:9|1:7|1:8|1:6|9:7|1:9|1:7|2:7|5:8|1:9|3:8|8:7|4:8|2:7|5:6|4:8|2:6|2:5|5:4|12:3|10:2|1:4|1:2|28:3|3:6|2:8|1:10|2:9|1:10|9:12|2:13|1:14|12:17|2:18|1:19|2:17|1:18|2:17|1:17|2:16|1:14|9:13|2:12|1:11    +")
p2 = Cluster(rounding = True)
p2.read_line("chr1    15 152 21:1|14:2|4:3|2:5|5:6|2:7|2:6|13:8|14:12|3:13|5:14|1:15|14:12|8:10|3:9|1:17|3:7|1:5|5:8|14:5|2:3")
#p = Cluster("chr1    142740079   142740546  9:1|2:2|9:4|2:5|14:6|3:6|1:7|5:9|2:8|4:8|5:11|2:10|3:10|5:11|2:13|4:15|3:14|1:13|3:11|1:13|3:14|4:12|1:9|3:10|3:11|3:10|5:9|1:7|1:8|1:6|9:7|1:9|1:7|2:7|5:8|1:9|3:8|8:7|4:8|2:7|5:6|4:8|2:6|2:5|5:4|12:3|10:2|1:4|1:2|28:3|3:6|2:8|1:10|2:9|1:10|9:12|2:13|1:14|12:17|2:18|1:19|2:17|1:18|2:17|1:17|2:16|1:14|9:13|2:12|1:11    +", rounding = True)
#p2 = Cluster("chr1   142740099   142740176  9:1|2:2|14:3|1:5|5:6|5:9|3:9|2:10|4:12|2:12|2:11|1:12|2:15|1:16|2:17|6:20|1:18|4:17|1:18|5:15|3:14|2:13|4:11|4:10|1:9|2:6|1:5|1:4|1:5|10:2|1:3|1:2|1:3|2:4|11:7|1:8|3:9|5:10|11:9|2:8|1:7|1:6|1:7|1:4|2:5|1:6|3:7|2:8|2:9|1:8|3:7|20:6|2:5|2:4|1:3|3:2|2:3|3:2|9:3|22:5|5:3|9:2    -", rounding = True)
#p = Cluster("chr1    1   2  3:11|1:13|3:14|4:12|1:9|3:10|3:11|3:10|5:9|1:7|1:8|1:6|1:7   +", rounding = True)
#p2 = Cluster("chr1   1   2  1:13|4:11|4:10|1:9|2:6|1:5|1:4|1:5|6:2   -", rounding = True)

#p = Cluster("chr1    1   50 14:6|3:6|1:7|5:9  +", rounding = True)
#p2 = Cluster("chr1   5   50 5:6|8:9|2:10|4:12  -", rounding = True)

#p = Cluster("chr1    10 545 10:1|10:2|10:3|10:2", rounding = True)
#p2 = Cluster("chr1   8 485 15:1|25:2", rounding = True)
def int_heights(heights):
    intheights = []
    for value in p.get_heights():
        intheights.append(int(value))
    return intheights

solution = p - p2

p._clean_levels()
p2._clean_levels()
print p.write_line()
print p2.write_line()
print solution.write_line()
print solution._levels
intheights = []
for value in p.get_heights():
    intheights.append(int(value))

plt.subplot(2,1,1)
plt.bar(range(p.start, p.end), intheights(p.get_heights()))
plt.bar(range(p2.start, p2.end), intheights(p2.get_heights()), color='red')
plt.subplot(2,1,2)
plt.bar(range(solution.start, solution.end), intheights(solution.get_heights()))
plt.show()
