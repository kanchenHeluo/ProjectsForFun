import sys

title = set()
department = set()
for line in sys.stdin.readlines():
	arr = line.strip().split('\t')
	if(arr[1]=='title'):
		title.add(arr[2])
	if(arr[1]=='department'):
		department.add(arr[2]);

for t in title:
	print t
	