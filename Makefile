#Ryan Doyle
#Makefile for compression program
#

JC = javac

all: 
	$(JC) -cp "lib/junit/*:." mow/{tests,common,server}/*.java 
clean:
	rm -f mow/{tests,common,server}/*.class
