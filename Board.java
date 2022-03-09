import java.util.ArrayList;

public class Board {

    boolean castlingShortIsPossibleW = true;
    boolean castlingLongIsPossibleW = true;
    boolean castlingShortIsPossibleB = true;
    boolean castlingLongIsPossibleB = true;

    Color movingColor = Color.WHITE;

    EnPassant enPassant = null;

    Piece[][] board = new Piece[8][8];
    ArrayList<Piece> pieces = new ArrayList<>();

    ArrayList<Board> nextBoards = new ArrayList<Board>();

    Board(Board board){

        //Copiem piesele
        for(Piece piece : board.pieces){
            Piece newPiece = null;

            if (piece instanceof Pawn){
                newPiece = new Pawn(piece.coord.row, piece.coord.colomn, piece.color, this);
            }

            if (piece instanceof Rook){
                newPiece = new Rook(piece.coord.row, piece.coord.colomn, piece.color, this);
            }

            if (piece instanceof Knight){
                newPiece = new Knight(piece.coord.row, piece.coord.colomn, piece.color, this);
            }

            if (piece instanceof Bishop){
                newPiece = new Bishop(piece.coord.row, piece.coord.colomn, piece.color, this);
            }

            if (piece instanceof Queen){
                newPiece = new Queen(piece.coord.row, piece.coord.colomn, piece.color, this);
            }

            if (piece instanceof King){
                newPiece = new King(piece.coord.row, piece.coord.colomn, piece.color, this);
                ((King)newPiece).checksGiven = ((King)piece).checksGiven;
            }

            addPiece(newPiece);
        }

        //Castling
        castlingShortIsPossibleW = board.castlingShortIsPossibleW;
        castlingShortIsPossibleB = board.castlingShortIsPossibleB;
        castlingLongIsPossibleW = board.castlingLongIsPossibleW;
        castlingLongIsPossibleB = board.castlingLongIsPossibleB;

        //EnPassant
        if (board.enPassant != null)
            enPassant = new EnPassant(new Coordonate(board.enPassant.coordinates.row,board.enPassant.coordinates.colomn));


        movingColor = board.movingColor;
    }

    public Board() {

    }

    Board boardAfterMove(Move move){
        Board createdBoard = new Board(this);

        createdBoard.makeMove(move.from, move.to, move.promotion);

        return createdBoard;
    }

    void generateNextStates(int depth){
        if (depth <= 0) return;

        ArrayList<Move> possibleMoves = getAllPossibleMoveList();
        if (possibleMoves == null || possibleMoves.size() == 0) return;

        for (Move mutare : possibleMoves) {
            nextBoards.add(boardAfterMove(mutare));
//            System.out.println("POSSIBLE NEXT BOARD\n "+boardAfterMove(mutare));
        }

        for (Board board : nextBoards){
            board.generateNextStates(depth-1);
        }

    }

    ArrayList<Board> getNextStates(){
        generateNextStates(1);
        return nextBoards;
    }

    void setPieces(){

        addPiece(new Rook(0,0,Color.WHITE,this));
        addPiece(new Rook(0,7,Color.WHITE,this));
        addPiece(new Rook(7,0,Color.BLACK,this));
        addPiece(new Rook(7,7,Color.BLACK,this));

        addPiece(new Knight(0,1,Color.WHITE,this));
        addPiece(new Knight(0,6,Color.WHITE,this));
        addPiece(new Knight(7,1,Color.BLACK,this));
        addPiece(new Knight(7,6,Color.BLACK,this));

        addPiece(new Bishop(0,2,Color.WHITE,this));
        addPiece(new Bishop(0,5,Color.WHITE,this));
        addPiece(new Bishop(7,2,Color.BLACK,this));
        addPiece(new Bishop(7,5,Color.BLACK,this));

        addPiece(new King(0,4,Color.WHITE,this));
        addPiece(new King(7,4,Color.BLACK,this));

        addPiece(new Queen(0,3,Color.WHITE,this));
        addPiece(new Queen(7,3,Color.BLACK,this));

        for (int i = 0; i < 8; i++) {
            for (int c = 0 ; c < 2 ; c++){
                addPiece(new Pawn(c == 0 ? 1 : 6, i, c == 0 ? Color.WHITE : Color.BLACK, this));
            }
        }
    }

    void addPiece(Piece piece){
        board[piece.coord.row][piece.coord.colomn] = piece;
        pieces.add(piece);
    }

    void movePiece(Piece piece, Coordonate toWhere){

        if (castlingLongIsPossibleW){
            if (piece.coord.equals(new Coordonate(0,0)) || toWhere.equals(new Coordonate(0,0)))
                castlingLongIsPossibleW = false;
        }

        if (castlingLongIsPossibleB){
            if (piece.coord.equals(new Coordonate(7,0)) || toWhere.equals(new Coordonate(7,0)))
                castlingLongIsPossibleB = false;
        }

        if (castlingShortIsPossibleW){
            if (piece.coord.equals(new Coordonate(0,7)) || toWhere.equals(new Coordonate(0,7)))
                castlingShortIsPossibleW = false;
        }

        if (castlingShortIsPossibleB){
            if (piece.coord.equals(new Coordonate(7,7)) || toWhere.equals(new Coordonate(7,7)))
                castlingShortIsPossibleB = false;
        }

        board[piece.coord.row][piece.coord.colomn]=null;
        board[toWhere.row][toWhere.colomn]=piece;
        piece.coord = toWhere;
    }

    void makeMove(Coordonate fromWhere, Coordonate toWhere, Character promotion){
        Piece movingPiece = getPiece(fromWhere);
        if (movingPiece == null) return;
        movingPiece.makeMove(toWhere);

        if (promotion != null){
            switch (promotion){
                case 'q' : replacePiece(toWhere, new Queen(toWhere, movingColor, this)); break;
                case 'r' : replacePiece(toWhere, new Rook(toWhere, movingColor, this)); break;
                case 'b' : replacePiece(toWhere, new Bishop(toWhere, movingColor, this)); break;
                case 'n' : replacePiece(toWhere, new Knight(toWhere, movingColor, this)); break;
            }
        }

        for (Piece piece : pieces) {
            if (piece instanceof King && piece.color != movingColor){
                if (isUnderAttack(piece.coord, piece.color))
                    ((King)piece).checksGiven++;
            }
        }

        movingColor = movingColor == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    void removePiece(Piece piece){
        if (piece == null) return;
        pieces.remove(piece);
        board[piece.coord.row][piece.coord.colomn] = null;
    }

    void removePiece(Coordonate where){
        removePiece(board[where.row][where.colomn]);
    }

    void replacePiece(Coordonate where, Piece piece){
        removePiece(where);
        addPiece(piece);
    }

    Piece getPiece(Coordonate coordonate){
        if (!inRange(coordonate)) return null;
        return board[coordonate.row][coordonate.colomn];
    }

    boolean isPieceAt(Coordonate coordonate){
        if (!inRange(coordonate)) return false;
        return !(getPiece(coordonate) == null);
    }

    boolean isPieceAtOfColor(Coordonate coordonate, Color color){
        if (!inRange(coordonate)) return false;
        return (isPieceAt(coordonate) && getPiece(coordonate).color == color);
    }

     boolean isEnPassant(Coordonate coordonate){
         if (!inRange(coordonate)) return false;
         if (enPassant == null) {
             return false;
         }
         if (enPassant.coordinates == null)
             return false;
         return (enPassant.coordinates.equals(coordonate));
     }

    boolean inRange(Coordonate coordonate){
        return coordonate.row <= 7 && coordonate.row >= 0 && coordonate.colomn <= 7
                && coordonate.colomn >= 0;
    }

    boolean isUnderAttack(Coordonate coordonate, Color myColor){
        return attackingPiecesNum(coordonate, myColor) > 0;
    }

    int attackingPiecesNum(Coordonate coordonate, Color myColor){
        return attackingPieces(coordonate,myColor).size();
    }

    float evaluate(Color evaluatingColor){
        Color oldColor = movingColor;
        this.movingColor = evaluatingColor;
        float value = evaluate();
        this.movingColor = oldColor;
        return value;
    }

    float evaluate(){
        float result = 0;

        for(Piece piece : pieces){
            if (piece.color == movingColor){
                if (attackingPiecesNum(piece.coord, piece.color) == 0)
                result += piece.getCost();

            } else {
                if (attackingPiecesNum(piece.coord, piece.color) == 0)
                result -= piece.getCost();
            }
        }

        return result;
    }

    float alphabeta(Board board, Color initialColor, int depth, float A, float B, boolean maximizing){
        if (depth == 0 || board.getAllPossibleMoveList().size() == 0){
            return board.evaluate(initialColor);
        }

        float value;

        if (maximizing){
            value = -Float.MAX_VALUE;
            for (Board nextBoard : board.getNextStates()){
                value = Math.max(value, alphabeta(nextBoard, initialColor, depth-1, A, B, false));
                A = Math.max(A, value);
                if (A >= B){
                    break;
                }
            }
        }
        else {
            value = Float.MAX_VALUE;
            for (Board nextBoard : board.getNextStates()) {
                value = Math.min(value, alphabeta(nextBoard, initialColor,depth - 1, A, B, true));
                B = Math.min(B, value);
                if (B <= A) {
                    break;
                }
            }
        }
        return value;
    }

    Move getBestMove(){

        Move bestMove = null;
        float bestScore = -Float.MAX_VALUE;

        for(Move move : getAllPossibleMoveList()){
            Board board = new Board(this);
            board.makeMove(move.from, move.to, move.promotion);

            float eval = alphabeta(board, movingColor, 3, -Float.MAX_VALUE, Float.MAX_VALUE,false);
//            System.out.println("Evaluated after "+ move +" with " + eval);

            if (eval > bestScore){
                bestMove = move;
                bestScore = eval;
            }
        }

//        System.out.println("GetAllPossibleMoves is "+getAllPossibleMoveList());

        return bestMove;
    }

    ArrayList<Piece> attackingPieces(Coordonate coordonate, Color myColor){
        ArrayList<Piece> attackingPieces = new ArrayList<>();
        Color enemyColor = myColor == Color.WHITE ? Color.BLACK : Color.WHITE;

        //Check Rays
        if (!inRange(coordonate)) return null;
        for (int rowOff = -1 ; rowOff <=1 ; rowOff += 1){
            for (int colOff = -1 ; colOff <=1 ; colOff += 1){
                if (rowOff == 0 && colOff == 0) continue;
                Ray ray = new Ray(this, coordonate, new Coordonate(rowOff,colOff));
                if (ray.isUnderAttack(myColor)) attackingPieces.add(ray.firstPiece());
            }
        }

        //Check Pawns
        Piece possiblePawn;
        int rowOffset = myColor == Color.WHITE ? 1 : -1;

        possiblePawn = getPiece(coordonate.getOffset(rowOffset,-1));

        if (possiblePawn != null && possiblePawn.color == enemyColor && possiblePawn instanceof Pawn){
            attackingPieces.add(possiblePawn);
        }

        possiblePawn = getPiece(coordonate.getOffset(rowOffset,1));

        if (possiblePawn != null && possiblePawn.color == enemyColor && possiblePawn instanceof Pawn){
            attackingPieces.add(possiblePawn);
        }

        //Check Knights
        for (int rowOff = -2 ; rowOff <=2 ; rowOff++ ){
            if (rowOff == 0) continue;
            for (int colOff = -2 ; colOff <= 2 ; colOff++ ){
                if (colOff == 0) continue;

                if (Math.abs(rowOff) == Math.abs(colOff)) continue;

                Piece possibleKnight = getPiece(coordonate.getOffset(rowOff,colOff));
                if (!(possibleKnight instanceof Knight)) continue;
                if (possibleKnight.color == enemyColor){
                    attackingPieces.add(possibleKnight);
                }
            }
        }

        //Check King
        Piece enemyKing = null;
        for (Piece piece: pieces) {
            if (piece instanceof King && piece.color == enemyColor){
                enemyKing = piece;
            }
        }
        assert enemyKing != null;
        if (coordonate.isNeighbour(enemyKing.coord)) attackingPieces.add(enemyKing);

        return attackingPieces;
    }

    ArrayList<Piece> boundPieces(Color myColor){
        for (Piece piece : pieces) {
            if (piece instanceof King && piece.color == myColor){
                boundPieces(piece.coord , myColor);
            }
        }

        return null;
    }

    ArrayList<Piece> boundPieces(Coordonate coordonate, Color myColor){
        ArrayList<Piece> boundPieces = new ArrayList<>();

        //Check Rays
        if (!inRange(coordonate)) return null;
        for (int rowOff = -1 ; rowOff <=1 ; rowOff += 1){
            for (int colOff = -1 ; colOff <=1 ; colOff += 1){
                if (rowOff == 0 && colOff == 0) continue;
                Ray ray = new Ray(this, coordonate, new Coordonate(rowOff,colOff));
                if (ray.hasBoundPiece(myColor)){
                    boundPieces.add(ray.firstPiece());
                    ray.firstPiece().setBound(new Coordonate(rowOff,colOff).getBoundType());
                }
            }
        }

//        System.out.println("Bound pieces are "+boundPieces);
        return boundPieces;
    }


    void RemoveMoves(Piece p, ArrayList<Move> moves, Color myColor) {
        Coordonate kingCoordonate = null;
        for (Piece piece : pieces) {
            if (piece instanceof King && piece.color == myColor) {
                kingCoordonate = piece.coord;
            }
        }

//        System.out.println("King coord is" + kingCoordonate+ " in board\n"+this);

        ArrayList<Move> toDeletion = new ArrayList<>();

        if (moves != null && !moves.isEmpty()) {
            if (this.attackingPiecesNum(kingCoordonate, myColor) == 1) {
                if (this.boundPieces(kingCoordonate, myColor).contains(p)) {
                    moves.clear();
                } else {
                    for (Move c : moves) {
                        if (c.to.row == this.attackingPieces(kingCoordonate, myColor).get(0).coord.row && c.to.colomn == this.attackingPieces(kingCoordonate, myColor).get(0).coord.colomn) {
                            int ok;
                        } else {
                            if (board[c.to.row][c.to.colomn] != null) {
                                toDeletion.add(c);
                            } else {
                                board[p.coord.row][p.coord.colomn] = null;
                                board[c.to.row][c.to.colomn] = p;
                                if (this.attackingPiecesNum(kingCoordonate, myColor) != 0) {
                                    board[c.to.row][c.to.colomn] = null;
                                    board[p.coord.row][p.coord.colomn] = p;
                                    toDeletion.add(c);
                                } else {
                                    board[c.to.row][c.to.colomn] = null;
                                    board[p.coord.row][p.coord.colomn] = p;
                                }
                            }
                        }
                    }
                }
            } else if (this.attackingPiecesNum(kingCoordonate, myColor) == 0) {
                if (this.boundPieces(kingCoordonate, myColor).contains(p)) {
                    for (Move c : moves) {
                        if (board[c.to.row][c.to.colomn] != null) {
                            board[p.coord.row][p.coord.colomn] = null;
                            Coordonate aux;
                            aux = this.attackingPieces(kingCoordonate, myColor).get(0).coord;
                            if (c.to.row != aux.row || c.to.colomn != aux.colomn) {
                                toDeletion.add(c);
                            }
                            board[p.coord.row][p.coord.colomn] = p;
                        } else {
                            board[p.coord.row][p.coord.colomn] = null;
                            board[c.to.row][c.to.colomn] = p;
                            if (this.attackingPiecesNum(kingCoordonate, myColor) != 0) {
                                board[c.to.row][c.to.colomn] = null;
                                board[p.coord.row][p.coord.colomn] = p;
                                toDeletion.add(c);
                            } else {
                                board[c.to.row][c.to.colomn] = null;
                                board[p.coord.row][p.coord.colomn] = p;
                            }
                        }
                    }
                }

                for (Move c : moves) {
                    if (isEnPassant(new Coordonate(c.to.row, c.to.colomn))) {
                        Coordonate enemyPawnCoordonate = myColor == Color.WHITE ? c.to.getOffset(-1, 0) : c.to.getOffset(1, 0);
                        Piece enemyPawn = getPiece(enemyPawnCoordonate);
                        board[enemyPawnCoordonate.row][enemyPawnCoordonate.colomn] = null;
                            /*board[c.to.row][c.to.colomn] = board[c.from.row][c.from.colomn];
                            board[c.from.row][c.from.colomn] = null;*/

                        board[p.coord.row][p.coord.colomn] = null;
                        board[c.to.row][c.to.colomn] = p;

//                        System.out.println("Deletion enPassant?");
//                        System.out.println(this);
                        if (attackingPiecesNum(kingCoordonate, myColor) != 0) {
                            toDeletion.add(c);
                        }

                            /*board[c.from.row][c.from.colomn] = board[c.to.row][c.to.colomn];
                            board[c.to.row][c.to.colomn] = null;*/
                        board[enemyPawnCoordonate.row][enemyPawnCoordonate.colomn] = enemyPawn;
                        board[p.coord.row][p.coord.colomn] = p;
                        board[c.to.row][c.to.colomn] = null;
                    }
                }
            } else moves.clear();
            moves.removeAll(toDeletion);
        }
    }

    ArrayList<Move> getAllPossibleMoveList(){
        return getAllPossibleMoveList(movingColor);
    }

    ArrayList<Move> getAllPossibleMoveList(Color color){
        ArrayList<Move> allPossibleMoveList = new ArrayList<>();

        for (Piece piece : pieces) {
            if (piece instanceof King && ((King)piece).checksGiven == 3) {
                return allPossibleMoveList;
            }
        }

//        for (int x = 7 ; x >= 0 ; x--){
//            for (int y = 0 ; y < 8 ; y++){
//                System.out.print(attackingPiecesNum(new Coordonate(x,y),color) + " ");
//            }
//            System.out.println();
//        }

        Coordonate kingCoordonate = null;
        for (Piece piece : pieces) {
            if (piece instanceof King && piece.color == color){
                kingCoordonate = piece.coord;
            }
            piece.setBound(null);
        }

        boundPieces(kingCoordonate, color);

        for (Piece piece: pieces) {
            if (piece.color == color)
                allPossibleMoveList.addAll(piece.getPossibleMoves());
        }

        return allPossibleMoveList;
    }

    @Override
    public String toString() {
        StringBuilder rezult = new StringBuilder("Board{"
//                + "castlingShortIsPossible=" + castlingShortIsPossible
//                + ", castlingLongIsPossible=" + castlingLongIsPossible
                + "\n");

        for (int i = 7 ; i >= 0 ; i--){
            for (int j = 0 ; j < 8 ; j++){
                if (board[i][j] != null)
                    rezult.append(board[i][j].getClass().getName().charAt(0)).append(" ");
                else
                    rezult.append("_ ");
            }
            rezult.append("\n");
        }

        return rezult.toString();
    }
}
