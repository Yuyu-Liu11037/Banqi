package GamePresentation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.*;
import java.util.List;

public class DChess {
    static Map<String, Image> keyNameValueImage = new HashMap<>();
    static Dimension chessBoardDimension = new Dimension(600, 820);

    DChess(){
        DChessBoard brd = new DChessBoard();
        System.out.println(brd.toString());

        JTextField status = new JTextField(10);
        status.setPreferredSize(new Dimension(200,36));
        status.setFont(new Font("Consolas", Font.PLAIN,30));
        status.setForeground(Color.WHITE);
        status.setBackground(new Color(106,74,43));
        status.setText("Start Game");
        status.setCaretColor(Color.white);
        status.setEditable(false);
        
        DChessPanel dchesspanel = new DChessPanel(brd);
        dchesspanel.add(status);

        JFrame f = new JFrame("Dark Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout());
        f.setSize(chessBoardDimension);
        f.setResizable(false);
        f.add(dchesspanel);
        f.setVisible(true);
    }//创建屏幕

    public static void main(String[] args) throws IOException {
        //read images
        Set<String> imgNames = new HashSet<>(Arrays.asList(
                "bj", "bm", "bx", "bs", "bb", "bp", "bz",
<<<<<<< HEAD:src/GamePresentation/DChess.java
                "rj", "rm", "rx", "rs", "rb", "rp", "rz", "back"));
=======
                "rj", "rm", "rx", "rs", "rb", "rp", "rz","back"));
>>>>>>> 4e17e985ab38341758febb6d64c4dc9cd50347a9:src/DChess.java
        for (String imgName : imgNames) {
            File imgFile = new File("./img/" + imgName + ".png");
            keyNameValueImage.put(imgName, ImageIO.read(imgFile).getScaledInstance(DChessPanel.side, DChessPanel.side , Image.SCALE_SMOOTH));
        }
        keyNameValueImage.put("bg", ImageIO.read(new File("./img/bg.png")).getScaledInstance((int)chessBoardDimension.getWidth(), (int)chessBoardDimension.getHeight() , Image.SCALE_SMOOTH));

        new DChess();
    }
}

class DChessPanel extends JPanel implements MouseListener, MouseMotionListener {
    static int orgX = 135, orgY = 85, side = 82;
    private DChessBoard brd;
    private Point fromColRow;
    private Point toColRow;
    private Point clickColRow;
    private Point movingPieceXY;
    private Image movingPieceImage;
    //private int initialize = 0;

    public DChessPanel(DChessBoard brd){
        this.brd = brd;
        addMouseListener(this);
    }
    private Point xyToColRow(Point xy) {
        return new Point((xy.x - orgX)/side, (xy.y - orgY)/side);
    }//计算鼠标点击的位置
    public void mousePressed(MouseEvent me) {
        fromColRow = xyToColRow(me.getPoint());
        /**
         *  Test code
         */        
        Point mouseTip = me.getPoint();
        System.out.println("mousePressed at (" + fromColRow.getX() + "," + fromColRow.getY() + ");(" + mouseTip.x + ", " + mouseTip.y + ")");

        Piece movingPiece = brd.pieceAt(fromColRow.x, fromColRow.y);
        if (movingPiece != null) {
            movingPieceImage = DChess.keyNameValueImage.get(movingPiece.imgName);
        }
    }
    public void mouseReleased(MouseEvent me) {
        if (fromColRow == null) return;
        toColRow = xyToColRow(me.getPoint());
        /**
         *  Test code
         */ 
        Point mouseTip = me.getPoint();
        System.out.println("mouseReleased at (" + toColRow.getX() + "," + toColRow.getY() + ");(" + mouseTip.x + ", " + mouseTip.y + ")");


        if (brd.validMove(fromColRow.x, fromColRow.y, toColRow.x, toColRow.y)) {
            brd.movePiece(fromColRow.x, fromColRow.y, toColRow.x, toColRow.y);
            System.out.println(brd);
        }
        /* int x=(me.getX() - orgX) / side;
        int y=(me.getY() - orgY) / side;
        if(findPiece(x, y).isReturn==0){
            findPiece(x, y).isReturn++;
        } */

        fromColRow = null;
        movingPieceXY = null;
        movingPieceImage = null;
        repaint(); // redraw the updated game board
    }
    public void mouseClicked(MouseEvent me) {
        clickColRow = xyToColRow(me.getPoint());
        Piece turningPiece = brd.pieceAt(clickColRow.x, clickColRow.y);
        if (turningPiece != null){
            if (turningPiece.isUp == false){
                brd.turnPiece((int)clickColRow.getX(),(int)clickColRow.getY());
            }
        }
        repaint();
    }
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}
    public void mouseDragged(MouseEvent me) {
        Point mouseTip = me.getPoint();
        movingPieceXY = new Point(mouseTip.x - side/2, mouseTip.y - side/2);
        repaint();
    }
    public void mouseMoved(MouseEvent me) {}
    /* private void drawPiecesBack(Graphics g) {
        for (Piece p : brd.getPieces()) {
            if (fromColRow != null && fromColRow.x == p.col && fromColRow.y == p.row) {
                continue; // drawn as movingPieceImage already
            }
            Image img = DChess.keyNameValueImage.get(p.imgName);
            g.drawImage(img, orgX + side * p.col, orgY + side * p.row, this);
            g.drawImage(DChess.keyNameValueImage.get("back") , orgX + side * p.col, orgY + side * p.row, this);
<<<<<<< HEAD:src/GamePresentation/DChess.java
            //System.out.println("output1");
        }
    } */

    /* private void drawPieces(Graphics g,Piece p) {
            Image img = DChess.keyNameValueImage.get(p.imgName);
            g.drawImage(img, orgX + side * p.col, orgY + side * p.row, this);
            //g.drawImage(DChess.keyNameValueImage.get("back") , orgX + side * p.col, orgY + side * p.row, this);
            //System.out.println("output2");
    } */
    
    private void drawPieces(Graphics g) {
        for (Piece p : brd.getPieces()) {
            if (fromColRow != null && fromColRow.x == p.col && fromColRow.y == p.row) {
                continue; // drawn as movingPieceImage already
            }
            if (p.isUp == false){
                Image img = DChess.keyNameValueImage.get("back");
                g.drawImage(img, (int)(orgX + side * p.col), (int)(orgY + side * p.row), this);
            } else {
                Image img = DChess.keyNameValueImage.get(p.imgName);
                g.drawImage(img, (int)(orgX + side * p.col), (int)(orgY + side * p.row), this);
            }
=======
>>>>>>> 4e17e985ab38341758febb6d64c4dc9cd50347a9:src/DChess.java
        }
    }
    private void drawBackground(Graphics g){
        g.drawImage(DChess.keyNameValueImage.get("bg"), 0, 0, null);
    }
    //Abundant method. pieceAt() already exists.
    /* public Piece findPiece(int x,int y) {
        for (Piece p : brd.getPieces()) {
            if (x == p.col) {
                if (y == p.row) {
                    return p;
                }
            }
        }
        return null;
    } */
    @Override
    public void paintComponent(Graphics g) {
        /* if(initialize<2){
            drawGrid(g);
            drawPiecesBack(g);  
            initialize++;
            }
        for(Piece p: brd.getPieces()){
            if (p.isReturn==1){
                drawPieces(g,p);
                p.isReturn++;
            }
        }
        System.out.println("______________________"); */
        super.paintComponent(g);
        drawBackground(g);
        drawPieces(g);

        /* if (movingPieceImage != null) {
            g.drawImage(movingPieceImage, movingPieceXY.x, movingPieceXY.y, null);
        } */
    }
}

class DChessBoard {
    final static int rows = 8;
    final static int cols = 4;
    private boolean isRedTurn = true;
    private Set<Piece> pieces = new HashSet<>();
    private int redScore = 0;
    private int blackScore = 0;
    private boolean isGameOver = false;
    Set<Piece> getPieces() {
        return pieces;
    }

    public boolean getTurn(){
        return isRedTurn;
    }
    public int getRedScore(){
        return redScore;
    }
    public int getBlackScore(){
        return blackScore;
    }
    public boolean isGameOver(){
        return isGameOver;
    }
    public boolean checkGameOver() {
		if (redScore >= 60 || blackScore >= 60){
            return true;
        } else {
            return false;
        }
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
    //随机初始化棋子
    DChessBoard(){
        Double[] row = {0.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0};
        List<Double> doubleRow = Arrays.asList(row);
        Collections.shuffle(doubleRow);//shuffle函数以打乱行列
        doubleRow.toArray(row);
        Double[] column = {0.0,1.0,2.0,3.0};
        List<Double> doubleColumn = Arrays.asList(column);
        Collections.shuffle(doubleColumn);
        doubleColumn.toArray(column);

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
        }//随机填入棋子
    }
    void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        Piece movingP = pieceAt(fromCol, fromRow);
        Piece targetP = pieceAt(toCol, toRow);
        //when a targetPiece is captured, display the captured piece beside the board
        if (targetP != null){
            switch(targetP.rank){
                case GENERAL:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, -0.5, true, Rank.GENERAL, "rb", 30);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        blackScore += 30;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 8.0, false, Rank.GENERAL, "bb", 30);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        redScore += 30;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){System.exit(0);}
                    break;
                case ADVISOR:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 0.3, true, Rank.ADVISOR, "rs", 10);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        blackScore += 10;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 6.9, false, Rank.ADVISOR, "bs", 10);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        redScore += 10;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){System.exit(0);}
                    break;
                case MINISTER:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 1.1, true, Rank.MINISTER, "rx", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        blackScore += 5;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 6.0, false, Rank.MINISTER, "bx", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        redScore += 5;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){System.exit(0);}
                    break;
                case CHARIOT:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 2.0, true, Rank.CHARIOT, "rj", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        blackScore += 5;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 5.1, false, Rank.CHARIOT, "bj", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        redScore += 5;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){System.exit(0);}
                    break;
                case HORSE:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 3.0, true, Rank.HORSE, "rm", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        blackScore += 5;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 4.3, false, Rank.HORSE, "bm", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        redScore += 5;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){System.exit(0);}
                    break;
                case SOLDIER:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 4.6, true, Rank.SOLDIER, "rz", 1);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        blackScore += 1;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 2.5, false, Rank.SOLDIER, "bz", 1);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        redScore += 1;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){System.exit(0);}
                    break;
                case CANNON:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 3.8, true, Rank.CANNON, "rp", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        blackScore += 5;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 3.4, false, Rank.CANNON, "bp", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        redScore += 5;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){System.exit(0);}
                    break;
            }
        }

        pieces.remove(movingP);
        pieces.remove(targetP);
        Piece newP = new Piece((double)toCol, (double)toRow, movingP.isRed, movingP.rank, movingP.imgName , movingP.points);
        newP.turnUp(true);
        pieces.add(newP);
        isRedTurn = !isRedTurn;
        if (isRedTurn){
            System.out.println("Red turn");
        } else {
            System.out.println("Black turn");
        }
    }
    void turnPiece(int turnCol, int turnRol){
        Piece turningPieceFalse = pieceAt(turnCol, turnRol);
        boolean isInitial = true;
        for (Piece p : pieces){
            if (p.isUp == true){
                isInitial = false;
                break;
            }
        }
        if (isInitial){isRedTurn = turningPieceFalse.isRed;}
        Piece turningPieceTrue = new Piece((double)turnCol, (double)turnRol, turningPieceFalse.isRed, turningPieceFalse.rank, turningPieceFalse.imgName, turningPieceFalse.points);
        turningPieceTrue.turnUp(true);
        pieces.remove(turningPieceFalse);
        pieces.add(turningPieceTrue);
        isRedTurn = !isRedTurn;
        if (isRedTurn){
            System.out.println("Red turn");
        } else {
            System.out.println("Black turn");
        }
    }
    //get the piece at location (col, row)
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
        else if (steps(fromCol, fromRow, toCol, toRow) != 2 || numPiecesBetween(fromCol, fromRow, toCol, toRow) != 1) {
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
        if ((p == null || p.isRed != isRedTurn || selfKilling(fromC, fromR, toC, toR, p.isRed) && p.rank != Rank.CANNON)) {
            return false;
        }
        if (p.rank == Rank.CANNON){
            Piece targetOfCannon = pieceAt(toC, toR);
            if (targetOfCannon == null){
                return false;
            } else {
                if (targetOfCannon.isUp == true && selfKilling(fromC, fromR, toC, toR, p.isRed)){
                    return false;
                }
            }
        }
        Piece targetP = pieceAt(toR, toR);
        if (p.isUp == false || (targetP != null && targetP.isUp == false && p.rank != Rank.CANNON)){
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
<<<<<<< HEAD:src/GamePresentation/DChess.java
}//棋子排位
=======
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
>>>>>>> 4e17e985ab38341758febb6d64c4dc9cd50347a9:src/DChess.java
