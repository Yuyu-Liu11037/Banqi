package GamePresentation;

public class Piece {
    double col;
    double row;
    boolean isRed;
    Rank rank;
    String imgName;
    int points;
    int isReturn;
    /* 
     * Defaut : back side up
     */
    boolean isUp = false;

    Piece(Double col, Double row, boolean isRed, Rank rank, String imgName, int points) {
        this.col = col;
        this.row = row;
        this.isRed = isRed;
        this.rank = rank;
        this.imgName = imgName;
        this.points = points;
        this.isReturn = 0;
    }
    public String getRank(){
        return String.valueOf(this.rank);
    }
    public void turnUp(boolean faceUp){
        this.isUp = faceUp;
    }
}//棋子
