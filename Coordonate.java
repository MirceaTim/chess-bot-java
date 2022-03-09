import java.util.Objects;

public class Coordonate {
    int row,colomn;

    Coordonate(int row, int colomn){
        this.row = row;
        this.colomn = colomn;
    }

    Coordonate(String coordonate){
        this.row = (int)((coordonate.charAt(1)) - '0' - 1);
        this.colomn = (int)((coordonate.charAt(0)) - 'a');
    }

    Coordonate getOffset(int row, int colomn){
        return new Coordonate(this.row + row, this.colomn + colomn);
    }

    Coordonate getOffset(Coordonate offset){
        return getOffset(offset.row, offset.colomn);
    }

    boolean isDiagonalOffset(){
        return row != 0 && colomn != 0;
    }

    boolean isLineOffset(){
        return row == 0 || colomn == 0;
    }

    public boolean isNeighbour(Coordonate coordonate){
        return Math.abs(coordonate.colomn - this.colomn) <= 1 && Math.abs(coordonate.row - this.row) <= 1;
    }

    Piece.BoundType getBoundType(){
        if (row == 0 && colomn != 0) return Piece.BoundType.VERTICALLY;
        if (row != 0 && colomn == 0) return Piece.BoundType.HORIZONTALLY;
        if ((row == 1 && colomn == 1) || (row == -1 && colomn == -1)) return Piece.BoundType.MAIN_DIAG;
        if ((row == 1 && colomn == -1) || (row == -1 && colomn == 1)) return Piece.BoundType.SECOND_DIAG;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordonate that = (Coordonate) o;
        return row == that.row && colomn == that.colomn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, colomn);
    }

    @Override
    public String toString() {
        return ((char)(colomn+'a'))+(Integer.toString(row+1));
    }
}
