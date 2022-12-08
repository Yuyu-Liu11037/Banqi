package GameCore;
import GamePresentation.Piece;

public class GameProcess {
    final static int winScore = 60 ;
    public boolean levelComparson() {

        return false;
    }
    public boolean ifEaten(Piece p1, Piece p2 ) {
        switch (p1.getRank()){
            case "GENERAL":
                if(p2.getRank().equals("SOLDIER")){
                 return false;
                }else {
                    return true;
                }
            case "ADVISOR":
                if(p2.getRank().equals("GENERAL")){

                }
                break;
        }

        return false;
    }
}