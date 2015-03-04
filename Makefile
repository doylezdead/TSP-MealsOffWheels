#Ryan Doyle
#Makefile for compression program
#

JC = javac

all: 
	$(JC) mow/*/*.java 

clean:
	rm -f mow/*/*.class
