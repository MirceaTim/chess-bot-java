import java.util.ArrayList;

public class Rook extends Piece {
    boolean hasMoved;

    Rook(Coordonate coord, Color color, Board board) {
        super(coord, color, board);
        hasMoved = false;
    }

    Rook(int row, int colomn, Color color, Board board) {
        super(row, colomn, color, board);
    }

    @Override
    void makeMove(Coordonate toWhere) {
        Coordonate oldCoordonate = coord.getOffset(0,0);
        super.makeMove(toWhere);
        if (oldCoordonate.equals(new Coordonate(0,0)) || oldCoordonate.equals(new Coordonate(7,0)))
            if (color == Color.WHITE){
                board.castlingLongIsPossibleW = false;
            } else {
                board.castlingLongIsPossibleB = false;
            }

        if (oldCoordonate.equals(new Coordonate(0,7)) || oldCoordonate.equals(new Coordonate(7,7)))
            if (color == Color.WHITE){
                board.castlingShortIsPossibleW = false;
            } else {
                board.castlingShortIsPossibleB = false;
            }
    }

    @Override
    ArrayList<Coordonate> getFieldsUnderAttack() {
        return null;
    }

    @Override
    ArrayList<Move> getPossibleMoves() {
        ArrayList<Coordonate> movesCoordonate = new ArrayList<>();
        ArrayList<Move> moves = new ArrayList<>();

        if (bound == null){
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(1,0)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(0,1)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(-1,0)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(0,-1)).getCellsToMove(color));
        } else if (bound == BoundType.HORIZONTALLY){
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(0,1)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(0,-1)).getCellsToMove(color));
        } else if (bound == BoundType.VERTICALLY){
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(1,0)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(-1,0)).getCellsToMove(color));
        }

        for (Coordonate possibleCoord : movesCoordonate){
            moves.add(new Move(coord, possibleCoord));
        }
        board.RemoveMoves(this, moves, color);
        return moves;
    }

    @Override
    float getCost() {
        return 5;
    }
}
