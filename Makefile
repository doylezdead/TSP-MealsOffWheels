#Ryan Doyle
#Makefile for mow
#

JC = javac

all: 
	$(JC) -cp "lib/junit/*:lib/mysql/*:mow/tests/*:." mow/{tests,common,server}/*.java 
clean:
	rm -f mow/{tests,common,server}/*.class
