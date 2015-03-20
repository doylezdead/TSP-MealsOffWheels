#Ryan Doyle
#Makefile for compression program
#

JC = javac

all: 
	$(JC) mow/{client,common,server}/*.java 

clean:
	rm -f mow/{client,common,server}/*.class
