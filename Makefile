all:
	javac -d bin -cp bin -sourcepath src src/ox/stackgame/ui/ApplicationFrame.java

run:
	java -ea -cp bin ox.stackgame.ui.ApplicationFrame

lex:
	javac -d bin -cp bin -sourcepath src src/ox/lextest.java
	javac -d bin -cp bin -sourcepath src src/ox/stackgame/stackmachine/instructions/*.java
	java -ea -cp bin ox.lextest

tests:
	javac -d bin -cp bin:/usr/share/java/junit.jar -sourcepath src test/ox/stackgame/stackmachine/*.java
	for f in test/ox/stackgame/stackmachine/*.java; do \
		java -cp bin:/usr/share/java/junit.jar org.junit.runner.JUnitCore ox.stackgame.stackmachine.`basename -s .java $$f`; \
	done
