import java.util.ArrayList;

public class Ray {
    ArrayList<Coordonate> freeCells = new ArrayList<>();
    ArrayList<Piece> piecesMet = new ArrayList<>();
    Coordonate offset;

    Ray(Board board, Coordonate start, Coordonate offset){
        this.offset = offset;
        Coordonate current = start;
        boolean metFirstPiece = false;

        while( board.inRange(current.getOffset(offset)) ){
            current = current.getOffset(offset);

            if (!board.isPieceAt(current)){
                if (!metFirstPiece) {
                    freeCells.add(current);
                }
            }
            else  {
                piecesMet.add(board.getPiece(current));
                metFirstPiece = true;
            }
        }

        //System.out.println("Ray started at "+start+" with offset "+offset+" returns freeCells: "+freeCells);
    }

    Piece firstPiece(){
        return piecesMet.isEmpty() ? null : piecesMet.get(0);
    }

    Piece firstPieceOfColor(Color color){
        for (Piece piece : piecesMet) {
            if (piece.color == color) {
                return piece;
            }
        }
        return null;
    }

    boolean isUnderAttack(Color myColor){
        Color enemyColor = myColor == Color.WHITE ? Color.BLACK : Color.WHITE;

        Piece firstPiece = firstPiece();
        if (firstPiece == null || firstPieceOfColor(enemyColor) == null) return false;
        return firstPiece == firstPieceOfColor(enemyColor) && (
                ((firstPiece instanceof Queen || firstPiece instanceof Bishop) && offset.isDiagonalOffset())
                        || ((firstPiece instanceof Queen || firstPiece instanceof Rook) && offset.isLineOffset())
        );
    }

    public ArrayList<Coordonate> getFreeCells() {
        return freeCells;
    }

    public ArrayList<Coordonate> getCellsToMove(Color myColor) {
        Color enemyColor = myColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        if (firstPiece() != null && firstPieceOfColor(enemyColor) == firstPiece()){
            freeCells.add(firstPiece().coord);
        }
        return freeCells;
    }



    boolean hasBoundPiece(Color myColor){
        Color enemyColor = myColor == Color.WHITE ? Color.BLACK : Color.WHITE;

        if (piecesMet.size() < 2) return  false;
        Piece firstMet = piecesMet.get(0);
        Piece secondMet = piecesMet.get(1);

        if (firstMet.color != myColor) return false;
        if (secondMet.color != enemyColor) return false;

        return (
                ((secondMet instanceof Queen || secondMet instanceof Bishop) && offset.isDiagonalOffset())
                        || ((secondMet instanceof Queen || secondMet instanceof Rook) && offset.isLineOffset())
        );
    }
}
