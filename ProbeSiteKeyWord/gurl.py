import sys
import os
import ssl
import urllib2
import threading

class SeekThread(threading.Thread):
	def __init__(self, threadid, totalnum, urllist):
		threading.Thread.__init__(self)
		self.urllist = urllist
		self.threadid = threadid
		self.totalnum = totalnum

	def run(self):
		print 'thread [%d] is running..' % (self.threadid)

		for idx in range(self.threadid, len(self.urllist), self.totalnum):
			url = self.urllist[idx]
			output_filename = 'output/%s.html' % idx
			print >> sys.stderr, '[%d] processing: %d' % (self.threadid, idx)
			if not os.path.exists(output_filename):
				try:
					print 'begin download [%s]' % url
					fd = urllib2.urlopen(url, timeout=30)
					data = fd.read()
					outfile = file(output_filename, 'w')
					outfile.write(data)
					print '[%d] complete [%d] [%s] len=%d' % (self.threadid, idx, url, len(data))
				except Exception, e:
					print '[%d] error [%d] [%s] [%s]' % (self.threadid, idx, url, e)
			else:
				print '[%d] exists file [%d]' % (self.threadid, idx)
			idx += 1

if __name__ == '__main__':
	ssl._create_default_https_context = ssl._create_unverified_context
	
	idx = 0
	urllist = []
	for url in file('url.txt').readlines():
		url = url.strip()
		urllist.append(url)

	threads = []
	ThreadNum = 20
	for i in range(ThreadNum):
		th = SeekThread(i, 20, urllist)
		th.start()
		threads.append(th)

	for th in threads:
		th.join()


		