#Ryan Doyle
#Makefile for compression program
#

JC = javac

all: 
	cd mow_client
	$(JC) *.java 
	cd ../mow_server
	$(JC) *.java 

clean:
	cd mow_client
	rm -f *.class
	cd ../mow_server
	rm -f *.class
