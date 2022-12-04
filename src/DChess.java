import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class DChess {
    static Map<String, Image> keyNameValueImage = new HashMap<>();
    DChess(){
        DChessBoard brd = new DChessBoard();
        System.out.println(brd);

        JFrame f = new JFrame("Dark Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500, 700);
        f.setResizable(false);
        f.add(new DChessPanel(brd));
        f.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Set<String> imgNames = new HashSet<>(Arrays.asList(
                "bj", "bm", "bx", "bs", "bb", "bp", "bz",
                "rj", "rm", "rx", "rs", "rb", "rp", "rz"));
        for (String imgName : imgNames) {
            File imgFile = new File("./img/" + imgName + ".png");
            keyNameValueImage.put(imgName, ImageIO.read(imgFile).getScaledInstance(DChessPanel.side, DChessPanel.side, Image.SCALE_SMOOTH));
        }
        new DChess();
    }
}

class DChessPanel extends JPanel implements MouseListener, MouseMotionListener {
    static int orgX = 110, orgY = 50, side = 67;
    private DChessBoard brd;
    private Point fromColRow;
    private Point toColRow;
    private Point movingPieceXY;
    private Image movingPieceImage;

    DChessPanel(DChessBoard brd){
        this.brd = brd;
        addMouseListener(this);
    }
    private Point xyToColRow(Point xy) {
        return new Point((xy.x - orgX)/side, (xy.y - orgY)/side);
    }
    public void mousePressed(MouseEvent me) {
        /**
         *  Test code
         */
        /*Point mouseTip = me.getPoint();
        System.out.println("mousePressed at (" + mouseTip.x + ", " + mouseTip.y + ")");*/

        fromColRow = xyToColRow(me.getPoint());
        System.out.println(fromColRow);
        Piece movingPiece = brd.pieceAt(fromColRow.x, fromColRow.y);
        if (movingPiece != null) {
            movingPieceImage = DChess.keyNameValueImage.get(movingPiece.imgName);
        }
    }
    public void mouseReleased(MouseEvent me) {
        /**
         *  Test code
         */
        /*Point mouseTip = me.getPoint();
        System.out.println("mouseReleased at (" + mouseTip.x + ", " + mouseTip.y + ")");*/

        if (fromColRow == null) return;
        toColRow = xyToColRow(me.getPoint());
        System.out.println(toColRow);
        if (brd.validMove(fromColRow.x, fromColRow.y, toColRow.x, toColRow.y)) {
            brd.movePiece(fromColRow.x, fromColRow.y, toColRow.x, toColRow.y);
            System.out.println(brd);
        }
        fromColRow = null;
        movingPieceXY = null;
        movingPieceImage = null;
        repaint(); // redraw the updated game board
    }
    public void mouseClicked(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}
    public void mouseDragged(MouseEvent me) {
        Point mouseTip = me.getPoint();
        movingPieceXY = new Point(mouseTip.x - side/2, mouseTip.y - side/2);
        repaint();
    }
    public void mouseMoved(MouseEvent me) {}
    private void drawPieces(Graphics g) {
        for (Piece p : brd.getPieces()) {
            if (fromColRow != null && fromColRow.x == p.col && fromColRow.y == p.row) {
                continue; // drawn as movingPieceImage already
            }
            Image img = DChess.keyNameValueImage.get(p.imgName);
            g.drawImage(img, orgX + side * p.col, orgY + side * p.row, this);
        }
    }
    private void drawGrid(Graphics g) {
        for (int i = 0; i <= DChessBoard.cols; i++) {
            g.drawLine(orgX + i * side, orgY,
                    orgX + i * side, orgY + 8 * side);
        }
        for (int i = 0; i <= DChessBoard.rows; i++) {
            g.drawLine(orgX,            orgY + i * side,
                    orgX + 4 * side, orgY + i * side);
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        drawGrid(g);
        drawPieces(g);
        if (movingPieceImage != null) {
            g.drawImage(movingPieceImage, movingPieceXY.x, movingPieceXY.y, null);
        }
    }
}

class DChessBoard {
    final static int rows = 8;
    final static int cols = 4;
    private boolean isRedTurn = true;
    private Set<Piece> pieces = new HashSet<>();
    Set<Piece> getPieces() {
        return pieces;
    }

    @Override
    public String toString() {
        String brdStr = "";
        brdStr += " ";
        for (int i = 0; i < cols; i++) {
            brdStr += " " + i;
        }
        brdStr += "\n";
        for (int row = 0; row < rows; row++) {
            brdStr += row + "";
            for (int col = 0; col < cols; col++) {
                Piece p = pieceAt(col, row);
                if (p == null) {
                    brdStr += " .";
                } else {
                    switch (p.rank) {
                        case GENERAL: brdStr += p.isRed ? " G" : " g"; break;
                        case ADVISOR: brdStr += p.isRed ? " A" : " a"; break;
                        case MINISTER: brdStr += p.isRed ? " M" : " m"; break;
                        case CHARIOT: brdStr += p.isRed ? " Ch" : " ch"; break;
                        case HORSE: brdStr += p.isRed ? " H" : " h"; break;
                        case CANNON: brdStr += p.isRed ? " C" : " c"; break;
                        case SOLDIER: brdStr += p.isRed ? " S" : " s"; break;
                    }}
                }
            brdStr += "\n";
        }
        return brdStr;
    }
    DChessBoard(){
        Integer[] row = {0,1,2,3,4,5,6,7};
        List<Integer> intRow = Arrays.asList(row);
        Collections.shuffle(intRow);
        intRow.toArray(row);
        Integer[] column = {0,1,2,3};
        List<Integer> intColumn = Arrays.asList(column);
        Collections.shuffle(intColumn);
        intRow.toArray(column);

        //fill the 2D-chessboard with pieces in random order
        {
            pieces.add(new Piece(column[0], row[0], true, Rank.GENERAL, "rb" , 30));
            pieces.add(new Piece(column[0], row[1], false, Rank.GENERAL, "bb" , 30));
            pieces.add(new Piece(column[0], row[2], true, Rank.ADVISOR, "rs" , 10));
            pieces.add(new Piece(column[0], row[3], true, Rank.ADVISOR, "rs" , 10));
            pieces.add(new Piece(column[0], row[4], false, Rank.ADVISOR, "bs" , 10));
            pieces.add(new Piece(column[0], row[5], false, Rank.ADVISOR, "bs" , 10));
            pieces.add(new Piece(column[0], row[6], true, Rank.MINISTER, "rx" , 5));
            pieces.add(new Piece(column[0], row[7], true, Rank.MINISTER, "rx" , 5));
            pieces.add(new Piece(column[1], row[0], false, Rank.MINISTER, "bx" , 5));
            pieces.add(new Piece(column[1], row[1], false, Rank.MINISTER, "bx" , 5));
            pieces.add(new Piece(column[1], row[2], true, Rank.CHARIOT, "rj" , 5));
            pieces.add(new Piece(column[1], row[3], true, Rank.CHARIOT, "rj" , 5));
            pieces.add(new Piece(column[1], row[4], false, Rank.CHARIOT, "bj" , 5));
            pieces.add(new Piece(column[1], row[5], false, Rank.CHARIOT, "bj" , 5));
            pieces.add(new Piece(column[1], row[6], true, Rank.HORSE, "rm" , 5));
            pieces.add(new Piece(column[1], row[7], true, Rank.HORSE, "rm" , 5));
            pieces.add(new Piece(column[2], row[0], false, Rank.HORSE, "bm" , 5));
            pieces.add(new Piece(column[2], row[1], false, Rank.HORSE, "bm" , 5));
            pieces.add(new Piece(column[2], row[2], true, Rank.CANNON, "rp" , 5));
            pieces.add(new Piece(column[2], row[3], true, Rank.CANNON, "rp" , 5));
            pieces.add(new Piece(column[2], row[4], false, Rank.CANNON, "bp" , 5));
            pieces.add(new Piece(column[2], row[5], false, Rank.CANNON, "bp" , 5));
            pieces.add(new Piece(column[2], row[6], true, Rank.SOLDIER, "rz" , 1));
            pieces.add(new Piece(column[2], row[7], true, Rank.SOLDIER, "rz" , 1));
            pieces.add(new Piece(column[3], row[0], true, Rank.SOLDIER, "rz" , 1));
            pieces.add(new Piece(column[3], row[1], true, Rank.SOLDIER, "rz" , 1));
            pieces.add(new Piece(column[3], row[2], true, Rank.SOLDIER, "bz" , 1));
            pieces.add(new Piece(column[3], row[3], false, Rank.SOLDIER, "bz" , 1));
            pieces.add(new Piece(column[3], row[4], false, Rank.SOLDIER, "bz" , 1));
            pieces.add(new Piece(column[3], row[5], false, Rank.SOLDIER, "bz" , 1));
            pieces.add(new Piece(column[3], row[6], false, Rank.SOLDIER, "bz" , 1));
            pieces.add(new Piece(column[3], row[7], false, Rank.SOLDIER, "bz" , 1));
        }
    }
    void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        Piece movingP = pieceAt(fromCol, fromRow);
        Piece targetP = pieceAt(toCol, toRow);
        pieces.remove(movingP);
        pieces.remove(targetP);
        pieces.add(new Piece(toCol, toRow, movingP.isRed,
                movingP.rank, movingP.imgName , movingP.points));
        isRedTurn = !isRedTurn;
    }
    Piece pieceAt(int col, int row) {
        for (Piece piece : pieces) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }
        return null;
    }
    //Constraints of moving:
    private int steps(int fromCol, int fromRow, int toCol, int toRow) {
        if (fromCol == toCol) {
            return Math.abs(fromRow - toRow);
        } else if (fromRow == toRow) {
            return Math.abs(fromCol - toCol);
        }
        return 0; // not straight
    }
    private int numPiecesBetween(int fromCol, int fromRow,
                                 int toCol, int toRow) {
        if (!isStraight(fromCol, fromRow, toCol, toRow)) {
            return 0;
        }
        int cnt = 0, head, tail;
        if (fromCol == toCol) { // vertical
            head = Math.min(fromRow, toRow);
            tail = Math.max(fromRow, toRow);
            for (int row = head + 1; row < tail; row++) {
                cnt += (pieceAt(fromCol, row) == null) ? 0 : 1;
            }
        } else {
            head = Math.min(fromCol, toCol);
            tail = Math.max(fromCol, toCol);
            for (int col = head + 1; col < tail; col++) {
                cnt += (pieceAt(col, fromRow) == null) ? 0 : 1;
            }
        }
        return cnt;
    }
    private boolean outOfBoard(int col, int row) {
        return col < 0 || col > 3 || row < 0 || row > 7;
    }
    private boolean isStraight(int fromCol, int fromRow, int toCol, int toRow) {
        return fromCol == toCol || fromRow == toRow;
    }
    private boolean selfKilling(int fromCol, int fromRow,
                                int toCol, int toRow, boolean isRed) {
        Piece target = pieceAt(toCol, toRow);
        return target != null && target.isRed == isRed;
    }
    private boolean isValidGeneralMove(int fromCol, int fromRow,
                                      int toCol,   int toRow, boolean isRed) {
        if(pieceAt(toCol,toRow) == null){
            return true;
        }
        else if (steps(fromCol, fromRow, toCol, toRow) != 1 || pieceAt(toCol,toRow).rank == Rank.GENERAL) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isValidAdvisorMove(int fromCol, int fromRow,
                                      int toCol,   int toRow, boolean isRed) {
        if(pieceAt(toCol,toRow) == null){
            return true;
        }
        else if (steps(fromCol, fromRow, toCol, toRow) != 1 || pieceAt(toCol,toRow).rank == Rank.GENERAL) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isValidMinisterMove(int fromCol, int fromRow,
                                      int toCol,   int toRow, boolean isRed) {
        if(pieceAt(toCol,toRow) == null){
            return true;
        }
        else if (steps(fromCol, fromRow, toCol, toRow) != 1 || pieceAt(toCol,toRow).rank == Rank.GENERAL || pieceAt(toCol,toRow).rank == Rank.ADVISOR) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isValidChariotMove(int fromCol, int fromRow,
                                      int toCol,   int toRow, boolean isRed) {
        if(pieceAt(toCol,toRow) == null){
            return true;
        }
        else if (steps(fromCol, fromRow, toCol, toRow) != 1 || pieceAt(toCol,toRow).rank == Rank.GENERAL || pieceAt(toCol,toRow).rank == Rank.ADVISOR || pieceAt(toCol,toRow).rank == Rank.MINISTER) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isValidHorseMove(int fromCol, int fromRow,
                                      int toCol,   int toRow, boolean isRed) {
        if(pieceAt(toCol,toRow) == null){
            return true;
        }
        else if (steps(fromCol, fromRow, toCol, toRow) != 1 || pieceAt(toCol,toRow).rank == Rank.GENERAL || pieceAt(toCol,toRow).rank == Rank.ADVISOR || pieceAt(toCol,toRow).rank == Rank.MINISTER || pieceAt(toCol,toRow).rank == Rank.CHARIOT) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isValidSoldierMove(int fromCol, int fromRow,
                                      int toCol,   int toRow, boolean isRed) {
        if(pieceAt(toCol,toRow) == null){
            return true;
        }
        else if (steps(fromCol, fromRow, toCol, toRow) != 1 || pieceAt(toCol,toRow).rank == Rank.HORSE || pieceAt(toCol,toRow).rank == Rank.ADVISOR || pieceAt(toCol,toRow).rank == Rank.MINISTER || pieceAt(toCol,toRow).rank == Rank.CHARIOT || pieceAt(toCol,toRow).rank == Rank.CANNON) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isValidCannonMove(int fromCol, int fromRow,
                                      int toCol,   int toRow, boolean isRed) {
        if(pieceAt(toCol,toRow) == null){
            return false;
        }
        else if (steps(fromCol, fromRow, toCol, toRow) != 2 || numPiecesBetween(fromCol, fromRow,
        toCol, toRow) != 1) {
            return false;
        } else {
            return true;
        }
    }
    boolean validMove(int fromC, int fromR, int toC, int toR) {
        if (fromC == toC && fromR == toR || outOfBoard(toC, toR)) {
            return false;
        }
        Piece p = pieceAt(fromC, fromR);
        if (p == null || p.isRed != isRedTurn || selfKilling(fromC, fromR, toC, toR, p.isRed)) {
            return false;
        }
        boolean ok = false;
        switch (p.rank) {
            case GENERAL:
                ok = isValidGeneralMove(fromC, fromR, toC, toR, p.isRed);
                break;
            case ADVISOR:
                ok = isValidAdvisorMove(fromC, fromR, toC, toR, p.isRed);
                break;
            case MINISTER:
                ok = isValidMinisterMove(fromC, fromR, toC, toR, p.isRed);
                break;
            case CHARIOT:
                ok = isValidChariotMove(fromC, fromR, toC, toR, p.isRed);
                break;
            case HORSE:
                ok = isValidHorseMove(fromC, fromR, toC, toR, p.isRed);
                break;
            case SOLDIER:
                ok = isValidSoldierMove(fromC, fromR, toC, toR, p.isRed);
                break;
            case CANNON:
                ok = isValidCannonMove(fromC, fromR, toC, toR, p.isRed);
                break;
        }
        return ok;
    }
}
enum Rank {
    GENERAL,
    ADVISOR,
    MINISTER,
    CHARIOT,
    HORSE,
    SOLDIER,
    CANNON
}

class Piece {
    int col;
    int row;
    boolean isRed;
    Rank rank;
    String imgName;
    int points;
    boolean isReturn;
    Piece(int col, int row, boolean isRed,
          Rank rank, String imgName , int points) {
        this.col = col;
        this.row = row;
        this.isRed = isRed;
        this.rank = rank;
        this.imgName = imgName;
        this.points = points;
        this.isReturn = false;
    }
}
