#!/usr/bin/env python

import unittest

from utest import testCore, testOperation, testUtils, testRegions, testParser

suite1 = testCore.suite()
suite2 = testOperation.suite()
suite3 = testParser.suite()
suite4 = testUtils.suite()
suite5 = testRegions.suite()

suite = unittest.TestSuite()
suite.addTest(suite1)
suite.addTest(suite2)
suite.addTest(suite3)
suite.addTest(suite4)
suite.addTest(suite5)

unittest.TextTestRunner(verbosity=2).run(suite)


