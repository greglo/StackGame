all:
	javac -d bin -cp bin -sourcepath src src/ox/stackgame/ui/ApplicationFrame.java

run:
	java -cp bin ox.stackgame.ui.ApplicationFrame
