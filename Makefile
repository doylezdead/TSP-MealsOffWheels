#Ryan Doyle
#Makefile for mow
#

JC = javac

all: 
	$(JC) -cp "lib/junit/*:lib/mysql/*:com/mealsoffwheels/dronedelivery/tests/*:." com/mealsoffwheels/dronedelivery/{tests,common,server}/*.java 
clean:
	rm -f com/mealsoffwheels/dronedelivery/{tests,common,server}/*.class
