import java.util.ArrayList;
import java.util.Random;

public class Logic {
    static Board board;
    static int moveCounter;
    static Color myColor;

    static void init(){
        board = new Board();
        board.setPieces();
        myColor = Color.BLACK;
        moveCounter = 0;
    }

    static void makeMove(Move move){
        makeMove(move.from, move.to, move.promotion);
    }

    static void makeMove(Coordonate fromWhere, Coordonate toWhere){
        makeMove(fromWhere, toWhere, null);
    }

    static void makeMove(Coordonate fromWhere, Coordonate toWhere, Character promotion){
        board.makeMove(fromWhere, toWhere, promotion);

        moveCounter++;
    }

    static void drawCurrentState(){
//        System.out.println("Move nr."+moveCounter);
//        if (board != null) System.out.println(board);
    }

    static void generateMove(){
        Move chosen = board.getBestMove();
        makeMove(chosen);
        System.out.println(chosen);
    }

//    static void generateMove(){
//        ArrayList<Move> possibleMoves = board.getAllPossibleMoveList();
//
//        System.out.println("Possible moves:" + possibleMoves);
//
//        if (possibleMoves == null || possibleMoves.size() == 0){
//            System.out.println("resign");
//            return;
//        }
//
//        for(Move move : possibleMoves){
//            if (board.getPiece(move.from) instanceof King && Math.abs(move.to.colomn - move.from.colomn) > 1 ){
//                makeMove(move);
//                System.out.println(move);
//                return;
//            }
//        }
//
//        Random random = new Random();
//
//        Move chosen = possibleMoves.get(random.nextInt(possibleMoves.size()));
//        makeMove(chosen);
//        System.out.println(chosen);
//    }
}
