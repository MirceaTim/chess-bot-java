#
# Makefile 2021 Project PA.
#

#
# Variables for the Makefile compiler / executer.
#

JCC = javac
J = java
CLASSES = \
        Main.java \
        Board.java \
        Bishop.java \
        Color.java \
        Coordonate.java \
        King.java \
        Knight.java \
        Logic.java \
        Pawn.java \
        Piece.java \
        Queen.java \
        Rook.java \
	EnPassant.java \
	Move.java \
	Ray.java
	

#
# Variable for compiling with "-g" flag for debugging.
#

JFLAGS = -g

#
# Default case when typing "make"
#

default: run

#
# The build rule compiles the Main.class into the Main.java file.
#

build: $(CLASSES)
	$(JCC) $(JFLAGS) $(CLASSES)

#
# The run rule executes the Main.java file.
#

run: 
	java Main

#
# The clean rule exists but does nothing in order to preserve our sources.
#




clean:
	rm -rf *.class

