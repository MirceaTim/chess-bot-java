import java.util.ArrayList;

public class Queen extends Piece {
    Queen(Coordonate coord, Color color, Board board) {
        super(coord, color, board);
    }

    Queen(int row, int colomn, Color color, Board board) {
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

        if (bound == null){
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(0,1)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(0,-1)).getCellsToMove(color));

            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(1,0)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(-1,0)).getCellsToMove(color));

            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(1,-1)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(-1,1)).getCellsToMove(color));

            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(1,1)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(-1,-1)).getCellsToMove(color));
        } else if (bound == BoundType.HORIZONTALLY){
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(0,1)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(0,-1)).getCellsToMove(color));
        } else if (bound == BoundType.VERTICALLY){
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(1,0)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(-1,0)).getCellsToMove(color));
        } else if (bound == BoundType.MAIN_DIAG) {
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(1,1)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(-1,-1)).getCellsToMove(color));
        } else if (bound == BoundType.SECOND_DIAG) {
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(1,-1)).getCellsToMove(color));
            movesCoordonate.addAll(new Ray(board, this.coord, new Coordonate(-1,1)).getCellsToMove(color));
        }

        for (Coordonate possibleCoord : movesCoordonate){
            moves.add(new Move(coord, possibleCoord));
        }
        board.RemoveMoves(this, moves, color);
        return moves;
    }

    @Override
    float getCost() {
        return 11 + getPossibleMoves().size()/28.f - coord.row*((float)coord.row - 7.f)/49.f - coord.colomn*((float)coord.colomn - 7.f)/49.f;
    }
}
