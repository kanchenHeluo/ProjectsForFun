import sys
import re
import time
import re

cur_time = time.time()
for line in sys.stdin.readlines():
	arr = line.strip().split('\t')
	S, P, O = arr

	# replace CN.
	m = re.findall('CN=([^,]*),',O)
	if m:
		O = ','.join(m)

	
	# change title.
	if P=='title' and (O == 'SDE 2' or O=='IT SDE 2'):
		O = 'SDE II'

	if P=='title' and (O == 'MACH IT SDE'):
		O = 'SDE'

	if P=='title' and (O == 'SENIOR PM MANAGER'):
		O = 'SENIOR PROGRAM MANAGER'

	if (S=='kanchen' or S=='wenxu') and (P=='department'):
		O += ',ECIT COGS VL Eng Chi'

	if (S=='jejiang' and P=='title'):
		O = 'GPM'

	if (S=='tonyu' and P=='title'):
		O = 'General Manager'

	if (S=='xuyou' and P=='sn'):
		O = '"you"'

	if P=='whenCreated':
		# 1/11/2011 6:51:15 PM
		m = re.search("(\d+)/(\d+)/(\d+)", O)
		if m:
			month, day, year = m.groups()[:3]
			that_time = time.mktime( (int(year), int(month), int(day), 0, 0, 0, 0, 0, 0) )
			diff = (cur_time - that_time) / (24*3600)
			print '%s\tJoinMS\t%s' % (S, int(diff))



	print '%s\t%s\t%s' % (S, P, O)


	

