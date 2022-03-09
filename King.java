import java.util.ArrayList;

public class King extends Piece {
    int checksGiven = 0;

    King(Coordonate coord, Color color, Board board) {
        super(coord, color, board);
    }

    King(int row, int colomn, Color color, Board board) {
        super(row, colomn, color, board);
    }

    @Override
    void makeMove(Coordonate toWhere) {

        System.out.println("Making move with king");

        int rowForCastling = color == Color.WHITE ? 0 : 7;
        Coordonate startingCoord = new Coordonate(rowForCastling,4);

        System.out.println("To "+toWhere + " while cord = "+coord + " ; starting = "+startingCoord);

        if (coord.equals(startingCoord)){
            System.out.println("Coord eq startCoord");

            if (toWhere.colomn == 2)
                board.movePiece(board.getPiece(new Coordonate(rowForCastling, 0)), new Coordonate(rowForCastling,3));

            if (toWhere.colomn == 6)
                board.movePiece(board.getPiece(new Coordonate(rowForCastling, 7)), new Coordonate(rowForCastling,5));
        }

        super.makeMove(toWhere);

        if (color == Color.WHITE){
            board.castlingLongIsPossibleW = false;
            board.castlingShortIsPossibleW = false;
        } else {
            board.castlingLongIsPossibleB = false;
            board.castlingShortIsPossibleB = false;
        }
    }

    @Override
    ArrayList<Coordonate> getFieldsUnderAttack() {
        return null;
    }

    @Override
    ArrayList<Move> getPossibleMoves() {
        //ArrayList<Coordonate> possibleMoves = new ArrayList<>();

        ArrayList<Coordonate> movesCoordonate = new ArrayList<>();
        ArrayList<Move> moves = new ArrayList<>();

        Color enemyColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;

        if ( (board.isPieceAtOfColor(coord.getOffset(-1,0),enemyColor) || (!board.isPieceAt(coord.getOffset(-1,0)))))
            movesCoordonate.add(coord.getOffset(-1,0));

        if ( (board.isPieceAtOfColor(coord.getOffset(1,0),enemyColor) || (!board.isPieceAt(coord.getOffset(1,0)))))
            movesCoordonate.add(coord.getOffset(1,0));

        if ( (board.isPieceAtOfColor(coord.getOffset(0,-1),enemyColor) || (!board.isPieceAt(coord.getOffset(0,-1)))))
            movesCoordonate.add(coord.getOffset(0,-1));

        if ( (board.isPieceAtOfColor(coord.getOffset(0,1),enemyColor) || (!board.isPieceAt(coord.getOffset(0,1)))))
            movesCoordonate.add(coord.getOffset(0,1));

        if ( (board.isPieceAtOfColor(coord.getOffset(-1,-1),enemyColor) || (!board.isPieceAt(coord.getOffset(-1,-1)))))
            movesCoordonate.add(coord.getOffset(-1,-1));

        if ( (board.isPieceAtOfColor(coord.getOffset(-1,1),enemyColor) || (!board.isPieceAt(coord.getOffset(-1,1)))))
            movesCoordonate.add(coord.getOffset(-1,1));

        if ( (board.isPieceAtOfColor(coord.getOffset(1,-1),enemyColor) || (!board.isPieceAt(coord.getOffset(1,-1)))))
            movesCoordonate.add(coord.getOffset(1,-1));

        if ( (board.isPieceAtOfColor(coord.getOffset(1,1),enemyColor) || (!board.isPieceAt(coord.getOffset(1,1)))))
            movesCoordonate.add(coord.getOffset(1,1));


        ArrayList<Coordonate> toDeletion = new ArrayList<>();
        for (Coordonate cord: movesCoordonate) {
            if (!board.inRange(cord)) toDeletion.add(cord);//dc mutarile posibile nu sunt in range(pe board) le sterg
        }

        movesCoordonate.removeAll(toDeletion);
        toDeletion.clear();

        if(!movesCoordonate.isEmpty()) {

            for (Coordonate c : movesCoordonate) {
                board.board[coord.row][coord.colomn] = null;
                if(board.isUnderAttack(c,color)) {
                    toDeletion.add(c);
                }
                board.board[coord.row][coord.colomn] = this;
            }
        }

        movesCoordonate.removeAll(toDeletion);

        for (Coordonate possibleCoord : movesCoordonate){
            moves.add(new Move(coord, possibleCoord));
        }

        int rowForCastling = color == Color.WHITE ? 0 : 7;

        if (!board.isPieceAt(new Coordonate(rowForCastling, 1))
                && !board.isPieceAt(new Coordonate(rowForCastling, 2))
                && !board.isPieceAt(new Coordonate(rowForCastling, 3))
                && !board.isUnderAttack(new Coordonate(rowForCastling, 2), color)
                && !board.isUnderAttack(new Coordonate(rowForCastling, 3), color)
                && !board.isUnderAttack(new Coordonate(rowForCastling, 4), color)
                && (color == Color.WHITE ? board.castlingLongIsPossibleW : board.castlingLongIsPossibleB)
                && (board.getPiece(new Coordonate(rowForCastling, 0)) instanceof Rook)
                && (board.getPiece(new Coordonate(rowForCastling, 0)).color == color)){
            moves.add(new Move(coord, new Coordonate(rowForCastling, 2)));
        }

        if (!board.isPieceAt(new Coordonate(rowForCastling, 6))
                && !board.isPieceAt(new Coordonate(rowForCastling, 5))
                && !board.isUnderAttack(new Coordonate(rowForCastling, 6), color)
                && !board.isUnderAttack(new Coordonate(rowForCastling, 5), color)
                && !board.isUnderAttack(new Coordonate(rowForCastling, 4), color)
                && (color == Color.WHITE ? board.castlingShortIsPossibleW : board.castlingShortIsPossibleB)
                && (board.getPiece(new Coordonate(rowForCastling, 7)) instanceof Rook)
                && (board.getPiece(new Coordonate(rowForCastling, 7)).color == color)){
            moves.add(new Move(coord, new Coordonate(rowForCastling, 6)));
        }
        return moves;
    }

    @Override
    float getCost() {
        float result = 0;

        //Piese vecine
        for (int dif_x = -1; dif_x <= 1 ; dif_x++){
            for (int dif_y = -1; dif_y <= 1; dif_y++){
                Piece neighbour = board.getPiece(coord.getOffset(dif_x,dif_y));

                if (neighbour != null){
                    if (neighbour.color == color){
                        result += 0.1;
                    } else {
                        result -= 0.1;
                    }
                }

            }
        }

        if (checksGiven == 1){
            result -= 4;
        }

        if (checksGiven == 2){
            result -= 7;
        }

        if (checksGiven == 3){
            result -= 999999;
        }

        //Cu cat mai departe de centru - cu atat mai bine
        result += 0.25 + coord.row*((float)coord.row - 7.f)/49.f;
        result += 0.25 + coord.colomn*((float)coord.colomn - 7.f)/49.f;

        return result;
    }
}
