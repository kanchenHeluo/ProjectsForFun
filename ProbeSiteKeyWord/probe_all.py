import sys
import os
import re

class Detector:
	def __init__(self, tag):
		self.tag = tag
		self.count = 0
		self.hit = None

	def KeywordsDetect(self, keywords, data):
		for kw in keywords:
			idx = data.find(kw)
			if idx>=0:
				self.hit = data[idx-10:idx+len(kw)+10]
				return self.Success()
		return self.Fail()

	def RegexDetect(self, regexs, data):
		for r in regexs:
			m = re.search(r, data)
			if m:
				self.hit = m.group()
				return self.Success()
		return self.Fail()

	def Success(self):
		self.count += 1
		return True

	def Fail(self):
		return False

	def jsn(self, name):
		r ='[^\.\w]' + name + "\\b[^>\"'/\\\\]*\.js\W"
		return r

class AngularDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'angular')

	def detect(self, data):
		l = [self.jsn('angular'), ' ng-\w*=']
		return self.RegexDetect(l, data)

class ReactDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'react')

	def detect(self, data):
		l = [self.jsn('react'), '\breact\.']
		if self.RegexDetect(l, data):
			return True
		l = [' data-reactid']
		return self.KeywordsDetect(l, data)
		
class VueDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'vue')

	def detect(self, data):
		l = [self.jsn('vue'), ' v-\w*[=\b]']
		return self.RegexDetect(l, data)


#backbone not complete detect
class BackboneDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'backbone')

	def detect(self, data):
		l = [self.jsn('backbone')]
		return self.RegexDetect(l, data)

class PrototypeJSDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'prototypejs')

	def detect(self, data):
		l = [self.jsn('prototype')]
		return self.RegexDetect(l, data)

class UnderScoreDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'underscore')

	def detect(self, data):
		l = [self.jsn('underscore')]
		return self.RegexDetect(l, data)

#ember not complete detect -- not knowing how?
class EmberDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'ember')

	def detect(self, data):
		l = [self.jsn('ember'), 'data-template-name']
		return self.RegexDetect(l, data)

#knockout: 'data-bind' could posiibly be others type; here include to furture check site js to find ko.
class KnockoutDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'knockout')

	def detect(self, data):
		l = [self.jsn('knockout'), 'data-bind']
		return self.RegexDetect(l, data)

#ext: 'data-bind' could posiibly be others type; here include to furture check site js to confirm.
class ExtJsDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'extjs')

	def detect(self, data):
		l = [self.jsn('ext'), 'data-bind', '-btnEl']
		return self.RegexDetect(l, data)

#kendo: 'obervable' could posiibly be others type; here include to furture check site js to find ko or kendo.
class KendoDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'kendoui')

	def detect(self, data):
		l = [self.jsn('kendo'), 'obervable']
		return self.RegexDetect(l, data)

#spinejs
class SpineDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'spinejs')

	def detect(self, data):
		l = [self.jsn('spine'), 'Spine.']
		return self.RegexDetect(l, data)

#polymer
class PolymerDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'polymer')

	def detect(self, data):
		l = [self.jsn('polymer'), 'shadow-root', 'dom-module', 'shadowRoot', 'shady DOM']
		return self.RegexDetect(l, data)

#aurelia
class AureliaDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'Aurelia')

	def detect(self, data):
		l = ['aurelia']
		return self.KeywordsDetect(l, data)

#mercury
class MercuryDetector(Detector):
	def __init__(self):
		Detector.__init__(self, 'mercury')

	def detect(self, data):
		l = ['mercury']
		return self.KeywordsDetect(l, data)


if __name__ == '__main__':

	detectors = []
	detectors.append(AngularDetector())
	detectors.append(ReactDetector())
	detectors.append(VueDetector())
	detectors.append(BackboneDetector())
	detectors.append(UnderScoreDetector())
	detectors.append(EmberDetector())
	detectors.append(KnockoutDetector())
	detectors.append(ExtJsDetector())
	detectors.append(KendoDetector())
	detectors.append(PrototypeJSDetector())
	detectors.append(SpineDetector())
	detectors.append(PolymerDetector())
	detectors.append(AureliaDetector())
	detectors.append(MercuryDetector())
	

	output_file = file('tags.txt', 'w')
	try_limit = 100000000
	if len(sys.argv)>1:
		try_limit = int(sys.argv[1])
		print >> sys.stderr, 'TryLimit : %d' % (try_limit) 
	count = 0
	for fn in os.listdir('output'):
		filename = 'output/' + fn
		data = file(filename).read()

		tags = []
		for detector in detectors:
			if detector.detect(data):
				tags.append(detector.tag + '(%s)' % (detector.hit) )

		print >> output_file, '%s\t%s' % (filename, ','.join(tags))
		print >> sys.stderr, 'Detect %s over. [%d]' % (filename, count)

		count += 1
		if count > try_limit:
			break

	for detector in detectors:
		print '%s: %d' % (detector.tag, detector.count)