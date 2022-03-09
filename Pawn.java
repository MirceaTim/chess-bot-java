import java.util.ArrayList;

public class Pawn extends Piece {
    Pawn(Coordonate coord, Color color, Board board) {
        super(coord, color, board);
    }

    Pawn(int row, int colomn, Color color, Board board) {
        super(row, colomn, color, board);
    }

    void makeMove(Coordonate toWhere){
        Coordonate oldCoordonate = coord.getOffset(0,0);
        super.makeMove(toWhere);
        int koef = color == Color.WHITE ? 1 : -1;

        if (Math.abs(toWhere.row - oldCoordonate.row) > 1)
            board.enPassant = new EnPassant(new Coordonate(coord.row - koef, coord.colomn));
    }

    @Override
    ArrayList<Coordonate> getFieldsUnderAttack() {
        return null;
    }

    @Override
    ArrayList<Move> getPossibleMoves() {
        ArrayList<Coordonate> movesCoordonate = new ArrayList<>();
        ArrayList<Move> moves = new ArrayList<>();

        Color enemyColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;
        int koef = color == Color.WHITE ? 1 : -1;

        //moving forwards or double forwards
        if (!board.isPieceAt(coord.getOffset(koef,0))) {
            movesCoordonate.add(coord.getOffset(koef, 0));

            if (coord.row == (color == Color.WHITE ? 1 : 6)
                    && !board.isPieceAt(coord.getOffset(koef*2, 0))){
                movesCoordonate.add(coord.getOffset(koef*2, 0));
            }
        }

        //Capturing pieces
        if (board.isPieceAtOfColor(coord.getOffset(koef,1),enemyColor) )
            movesCoordonate.add(coord.getOffset(koef,1));

        if (board.isPieceAtOfColor(coord.getOffset(koef,-1),enemyColor))
            movesCoordonate.add(coord.getOffset(koef,-1));

        if (board.isEnPassant(coord.getOffset(koef,1)) )
            movesCoordonate.add(coord.getOffset(koef,1));

        if (board.isEnPassant(coord.getOffset(koef,-1)))
            movesCoordonate.add(coord.getOffset(koef,-1));

        ArrayList<Coordonate> toDeletion = new ArrayList<>();
        for (Coordonate cord: movesCoordonate) {
            if (!board.inRange(cord)) toDeletion.add(cord);
        }
        movesCoordonate.removeAll(toDeletion);

        movesCoordonate.removeIf(coord -> bound != null && new Coordonate(coord.row - this.coord.row, coord.colomn - this.coord.colomn).getBoundType() != bound);

        for (Coordonate possibleCoord : movesCoordonate){
            if (possibleCoord.row == 0 || possibleCoord.row == 7){
                moves.add(new Move(coord, possibleCoord, 'q'));
                moves.add(new Move(coord, possibleCoord, 'b'));
                moves.add(new Move(coord, possibleCoord, 'n'));
                moves.add(new Move(coord, possibleCoord, 'r'));
            } else {
                moves.add(new Move(coord, possibleCoord));
            }
        }
        board.RemoveMoves(this, moves, color);
        return moves;
    }

    @Override
    float getCost() {
        return 1 + (color == Color.WHITE ? coord.row/7f : (7 - coord.row)/7f);
    }
}
