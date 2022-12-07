package GamePresentation;

public class Piece {
    int col;
    int row;
    boolean isRed;
    Rank rank;
    String imgName;
    int points;
    int isReturn;

    Piece(int col, int row, boolean isRed,
          Rank rank, String imgName, int points) {
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


}//棋子
