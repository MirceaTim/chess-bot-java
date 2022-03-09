import java.util.ArrayList;

public class Knight extends Piece {
    Knight(Coordonate coord, Color color, Board board) {
        super(coord, color, board);
    }

    Knight(int row, int colomn, Color color, Board board) {
        super(row, colomn, color, board);
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

        if (bound != null) return moves;

        if (board.isPieceAtOfColor(coord.getOffset(-1,-2),enemyColor) || (!board.isPieceAt(coord.getOffset(-1,-2))) )
            movesCoordonate.add(coord.getOffset(-1,-2));

        if (board.isPieceAtOfColor(coord.getOffset(-2,-1),enemyColor) || (!board.isPieceAt(coord.getOffset(-2,-1))) )
            movesCoordonate.add(coord.getOffset(-2,-1));

        if (board.isPieceAtOfColor(coord.getOffset(-2,1),enemyColor) || (!board.isPieceAt(coord.getOffset(-2,1))) )
            movesCoordonate.add(coord.getOffset(-2,1));

        if (board.isPieceAtOfColor(coord.getOffset(-1,2),enemyColor) || (!board.isPieceAt(coord.getOffset(-1,2))) )
            movesCoordonate.add(coord.getOffset(-1,2));

        if (board.isPieceAtOfColor(coord.getOffset(1,2),enemyColor) || (!board.isPieceAt(coord.getOffset(1,2))) )
            movesCoordonate.add(coord.getOffset(1,2));

        if (board.isPieceAtOfColor(coord.getOffset(2,1),enemyColor) || (!board.isPieceAt(coord.getOffset(2,1))) )
            movesCoordonate.add(coord.getOffset(2,1));

        if (board.isPieceAtOfColor(coord.getOffset(2,-1),enemyColor) || (!board.isPieceAt(coord.getOffset(2,-1))) )
            movesCoordonate.add(coord.getOffset(2,-1));

        if (board.isPieceAtOfColor(coord.getOffset(1,-2),enemyColor) || (!board.isPieceAt(coord.getOffset(1,-2))) )
            movesCoordonate.add(coord.getOffset(1,-2));


        ArrayList<Coordonate> toDeletion = new ArrayList<>();
        for (Coordonate cord: movesCoordonate) {
            if (!board.inRange(cord)) toDeletion.add(cord);//dc mutarile posibile nu sunt in range(pe board) le sterg
        }
        movesCoordonate.removeAll(toDeletion);


        for (Coordonate possibleCoord : movesCoordonate){
            moves.add(new Move(coord, possibleCoord));
        }
        board.RemoveMoves(this, moves, color);
        return moves;
    }

    @Override
    float getCost() {
        return 2.7f + getPossibleMoves().size()/14f - coord.row*((float)coord.row - 7.f)/49.f - coord.colomn*((float)coord.colomn - 7.f)/49.f;
    }
}
