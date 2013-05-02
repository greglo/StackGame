all:
	rm -rf bin/*
	javac -d bin -cp bin -sourcepath src src/ox/stackgame/ui/ApplicationFrame.java

run:
	java -ea -cp bin:resources ox.stackgame.ui.ApplicationFrame

tests:
	javac -d bin -cp bin:/usr/share/java/junit.jar -sourcepath src test/ox/stackgame/stackmachine/*.java
	for f in test/ox/stackgame/stackmachine/*.java; do \
		java -cp bin:/usr/share/java/junit.jar org.junit.runner.JUnitCore ox.stackgame.stackmachine.`basename -s .java $$f`; \
	done
