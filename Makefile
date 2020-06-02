
all:
	javac src/*.java -d bin/

tester:
	javac -cp src/ src/Tester.java -d bin/
	java -cp bin/ Tester

maze:
	javac -cp src/ src/MazeTester.java -d bin/
	java -cp bin/ MazeTester

solver:
	javac -cp src/ src/SolverTester.java -d bin/
	java -cp bin/ SolverTester

clean:
	rm bin/*.class
