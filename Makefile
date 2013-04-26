all:
	javac -d bin -cp bin -sourcepath src src/ox/stackgame/ui/ApplicationFrame.java

run:
	java -ea -cp bin ox.stackgame.ui.ApplicationFrame

lex:
	javac -d bin -cp bin -sourcepath src src/ox/lextest.java
	javac -d bin -cp bin -sourcepath src src/ox/stackgame/stackmachine/instructions/*.java
	java -ea -cp bin ox.lextest
