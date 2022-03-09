import java.util.ArrayList;

public abstract class Piece {
    Coordonate coord;
    Color color;
    Board board;

    BoundType bound;

    Piece(Coordonate coord, Color color, Board board){
        this.coord = coord;
        this.color = color;
        this.board = board;
    }

    Piece(int row, int colomn, Color color, Board board){
        this(new Coordonate(row, colomn),color,board);
    }

    abstract ArrayList<Coordonate> getFieldsUnderAttack();

    abstract ArrayList<Move> getPossibleMoves();

    abstract float getCost();

    void makeMove(Coordonate toWhere){
        board.removePiece(board.getPiece(toWhere));
        board.movePiece(this, toWhere);
        board.enPassant = null;
    }

    public void setBound(BoundType bound){
        this.bound = bound;
    }

    enum BoundType{
        HORIZONTALLY,
        VERTICALLY,
        MAIN_DIAG,
        SECOND_DIAG
    }
}
