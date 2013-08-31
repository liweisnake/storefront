#!/usr/bin/env python

from xml.dom import minidom
import os
import sys

global checkoutFolder
global rpmtargetFolder
global rpmbuildFolder
global conffile
def getXMLAttribute(element) :
 try:
   xmldoc = minidom.parse(conffile)
   elementlist = xmldoc.getElementsByTagName(element)
   return elementlist
   
 except IOError:
   print "exit with error: "+conffile+" not found"
   
   
def doCheckout(linkurl) :
  version = linkurl.split("/")[-1]
  elements=getXMLAttribute('module')
  folder=checkoutFolder+"/"+version
  command="rm -rf "+folder
  command1="mkdir -p "+folder
  if os.system(command) != 0:
		print "exit with error: delete folder "+folder+" Failed"
  if os.system(command1) != 0:
		print "exit with error: create folder "+folder+" Failed"
	
	
  for e in elements:
      src = linkurl+e.attributes["src"].value
      target = e.attributes["target"].value
      
      print "src:  "+src
      print "target:   "+target
     
      command2="svn co "+src+" "+folder+"/"+target
      print command2
      if os.system(command2) !=0:
          print "error: checkout from "+src+" Failed"
          return 0
  return 1


def doMavenInstall(version, test=1) :
  elements=getXMLAttribute('pom')
  for e in elements:
  	path=e.attributes["path"]
  	folder=checkoutFolder+"/"+version
  	if test == 1:
		command="mvn -f "+folder+"/"+path.value+" install"
  	else:
		command="mvn -Dmaven.test.skip=true -f "+folder+"/"+path.value+" install"
  	if os.system(command) !=0:
     		print "error: rpm install Failed with command ["+command+"]" 
     		return 0
  return 1
  
def doMavenTest(version) :
  elements=getXMLAttribute('pom')
  for e in elements:
  	path=e.attributes["path"]
  	folder=checkoutFolder+"/"+version
  	command="mvn -f "+folder+"/"+path.value+" test"
  	if os.system(command) !=0:
     		print "maven test failed"
     		return 0
  return 1
  
def doRPM(version) :
	elements=getXMLAttribute('rpm')
	for e in elements:
  		path=e.attributes["path"]
  		folder=checkoutFolder+"/"+version
  		command="mvn -f "+folder+"/"+path.value+" install"
  		if os.system(command) !=0:
     			print "maven rpm install failed"
     			return 0 
	print "rpm build done, located under rpm"
  	return 1
  
def doAll(linkurl, test = 1) :
  version = linkurl.split("/")[-1]
  print "link is:   "+linkurl
  print "version is:   "+version
  if doCheckout(linkurl) == 1 :
     if doMavenInstall(version,test) == 1 :
	os.system("cp src/"+version+"/hpsdf-ngp-application-core/target/*.ear target/")
	print "maven install succeed"
	if doRPM(version) ==1:
	   os.system("cp src/"+version+"/hpsdf-ngp-application-rpm/target/rpm/storefront/RPMS/noarch/*.rpm target/")	
	   print "maven rpm succeed"
	else:
	   print "Error: Maven rpm Failed"
     else:
        print "Error: Maven install Failed"
  else:
     print "Error: Check out from repository Failed"
         
  
  
checkoutFolder="src"
rpmtargetFolder="target"
rpmbuildFolder="rpm"
conffile="conf.xml"
user =  os.getenv("USER")
if user == "root" :
  print "This script can not run by 'root' role"
  sys.exit()
if len(sys.argv)==1 :
  print "Missing option, type -help for help"
else:
  javahome = os.getenv("JAVA_HOME")
  li = javahome.split("/")
  version = li[-1]
  
  if version != "jdk1.5.0_14" :
    print "Error: jdk1.5.0_14 is reqiured"
    sys.exit()
  opt=sys.argv[1]
  if opt=="-help" :
    print "Usage: release.sh OPTION <version>\nMandatory options\n\n-co, check out resources from svn repository\n     release.sh -co <version>\n     e.g. release.sh -co trunk\n          release.sh -co 1.0.0-20080613RC3-Bharti\n-install, do maven install upon the checked out resources\n          release.sh -install <version>\n          e.g. release.sh -install trunk, release.sh -install 1.0.0-20080613RC3-Bharti\n-rpm, do rpm generation upon checked out resources\n      release.sh -rpm <version>\n      e.g. release.sh -rpm trunk, release.sh -rpm 1.0.0-20080613RC3-Bharti\n-all, do the above(checkout, mvn install and rpm) in sequence\n-alltestskip, do the above(checkout, mvn install and rpm) in sequence (skip maven test)\n              release.sh -alltestskip <version>\n              e.g. release.sh -alltestskip trunk\n                   release.sh -alltestskip 1.0.0-20080613RC3-Bharti\n-test do maven test upon the checked out resources\n      release.sh -test <version>\n      e.g. release.sh -test trunk , release.sh -test 1.0.0-20080613RC3-Bharti\n-installtestskip, do maven install(test skip) upon the checkedout resources\n                  release.sh -installtestskip <version>\n                  e.g. release.sh -installtestskip trunk, release.sh -installtestskip 1.0.0-20080613RC3-Bharti\n-help, display this help"
  elif opt=="-co" :
    if len(sys.argv)==2 :
      print "Missing version parameter type -help for help"
    else:
      linkurl=sys.argv[2]
      
      doCheckout(linkurl)
      
  elif opt=="-test" :
    if len(sys.argv)==2 :
      print "Missing version parameter type -help for help"
    else:
      linkurl=sys.argv[2]
      version = linkurl.split("/")[-1]
      doMavenTest(version)
      
  elif opt=="-install" :
    if len(sys.argv)==2 :
      print "Missing version parameter type -help for help"
    else:
      linkurl=sys.argv[2]
      
      version = linkurl.split("/")[-1]
      doMavenInstall(version)
      
  elif opt=="-rpm" :
    if len(sys.argv)==2 :
      print "Missing version parameter type -help for help"
    else:
      linkurl=sys.argv[2]
      
      version = linkurl.split("/")[-1]
      doRPM(version)     
       
  elif opt=="-all" :
    if len(sys.argv)==2 :
      print "Missing version parameter type -help for help"
    else:
      linkurl=sys.argv[2]
      
      doAll(linkurl)
    
  elif opt=="-alltestskip" :
    if len(sys.argv)==2 :
      print "Missing version parameter type -help for help"
    else:
      linkurl=sys.argv[2]
      
      doAll(linkurl,0)
      
  elif opt=="-installtestskip" :
    if len(sys.argv)==2 :
      print "Missing version parameter type -help for help"
    else:
      linkurl=sys.argv[2]
      version = linkurl.split("/")[-1]
      doMavenInstall(version,0)
     
      
  else:
    print "Error: unknown option: "+opt+" type -help for help"
    








