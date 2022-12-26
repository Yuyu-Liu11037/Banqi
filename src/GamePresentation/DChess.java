package GamePresentation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import GameCore.save;

public class DChess extends JFrame implements ActionListener{
    static Map<String, Image> keyNameValueImage = new HashMap<>();
    static Dimension chessBoardDimension = new Dimension(600, 820);
    /* JTextField status; */
    DChessBoard brd;
    JButton restartButton;
    JButton cheatButton;
    DChessPanel dchesspanel;
    JFrame f;

    /* public boolean getBrdTurn() {
        return brd.getTurn();
    } */

    DChess(){
        brd = new DChessBoard();
        System.out.println(brd.toString());

        restartButton = new JButton();
        restartButton.setText("restart");
        restartButton.setPreferredSize(new Dimension(100,40));
        restartButton.setLocation(0,0);
        restartButton.addActionListener(this);

        cheatButton = new JButton();
        cheatButton.setText("cheatMode");
        cheatButton.setPreferredSize(new Dimension(100, 40));
        cheatButton.setLocation(1,0);
        cheatButton.addActionListener(this);

        dchesspanel = new DChessPanel(brd);
        dchesspanel.add(restartButton);
        dchesspanel.add(cheatButton);

        f = new JFrame("Dark Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout());
        f.setSize(chessBoardDimension);
        f.setResizable(false);
        f.add(dchesspanel);
        f.setVisible(true);

        /* while(true){
            if (getBrdTurn()){
                status.setText("Red turn");
            } else {
                status.setText("Black turn");
            }
        } */

    }//创建屏幕

    /* 
     * when buttons are clicked, perform actions, respectively
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == restartButton){
            f.dispose();
            new DChess();
        }   
        if(e.getSource() == cheatButton){
            dchesspanel.setCheatMode();
            if(dchesspanel.getCheatMode()){
                System.out.println("Cheat Mode");
            } else{
                System.out.println("Normal Mode");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //read images
        Set<String> imgNames = new HashSet<>(Arrays.asList(
                "bj", "bm", "bx", "bs", "bb", "bp", "bz",
                "rj", "rm", "rx", "rs", "rb", "rp", "rz", "back"));
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
    private boolean isCheatMode = false;
    private String chessName;
    private String chessColor;
    //private int initialize = 0;
    public void setCheatMode(){
        isCheatMode = !isCheatMode;
    }

    public boolean getCheatMode(){
        return isCheatMode;
    }

    public String getChessName(){
        return chessName;
    }
    public String getChessColor(){
        return chessColor;
    }
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
                if(!isCheatMode){
                    brd.turnPiece((int)clickColRow.getX(),(int)clickColRow.getY());
                } else {
                    chessName = turningPiece.getRank();
                    if(turningPiece.getColor()){
                        chessColor = "Red";
                    } else {
                        chessColor = "Black";
                    }
                    System.out.println(turningPiece.getRank() + "  " + turningPiece.getColor());
                }
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
        }
    }
    private void drawBackground(Graphics g){
        g.drawImage(DChess.keyNameValueImage.get("bg"), 0, 0, null);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawPieces(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g2.drawString("Red Score : " + brd.getRedScore() + "    Black Score : " + brd.getBlackScore(), 130, 770);
        g2.setColor(Color.WHITE);
        /* 
         * Show the number of captured pieces
         */
        if(!brd.getCapturedNumber(0).equals("0")){g2.drawString(brd.getCapturedNumber(0), 20, 180);}
        if(!brd.getCapturedNumber(1).equals("0")){g2.drawString(brd.getCapturedNumber(1), 520, 720);}
        if(!brd.getCapturedNumber(2).equals("0")){g2.drawString(brd.getCapturedNumber(2), 20, 245);}
        if(!brd.getCapturedNumber(3).equals("0")){g2.drawString(brd.getCapturedNumber(3), 520, 650);}
        if(!brd.getCapturedNumber(4).equals("0")){g2.drawString(brd.getCapturedNumber(4), 20, 315);}
        if(!brd.getCapturedNumber(5).equals("0")){g2.drawString(brd.getCapturedNumber(5), 520, 580);}
        if(!brd.getCapturedNumber(6).equals("0")){g2.drawString(brd.getCapturedNumber(6), 20, 385);}
        if(!brd.getCapturedNumber(7).equals("0")){g2.drawString(brd.getCapturedNumber(7), 520, 505);}
        if(!brd.getCapturedNumber(8).equals("0")){g2.drawString(brd.getCapturedNumber(8), 20, 525);}
        if(!brd.getCapturedNumber(9).equals("0")){g2.drawString(brd.getCapturedNumber(9), 520, 365);}
        if(!brd.getCapturedNumber(10).equals("0")){g2.drawString(brd.getCapturedNumber(10), 20, 460);}
        if(!brd.getCapturedNumber(11).equals("0")){g2.drawString(brd.getCapturedNumber(11), 520, 435);}
        
        /* 
         * Show game status
         */
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        if(brd.getTurn()){
            g2.setColor(new Color(173,52,53));
            g2.drawString("Red   Turn", 5, 35);
        } else {
            g2.setColor(Color.BLACK);
            g2.drawString("Black Turn", 5, 35);
        }

        /* 
         * Show gameOver
         */
        if(brd.isGameOver()){
            g2.setColor(Color.WHITE);
            g2.fillRect(125, 75, 352, 675);

            g2.setColor(Color.BLACK);
            if(brd.getTurn()){
                g2.drawString("Game Over!",orgX,orgY+250);
                g2.drawString("Winner is Black", orgX, orgY+350);
            } else {
                g2.drawString("Game Over!",orgX,orgY+250);
                g2.drawString("Winner is Red", orgX, orgY+350);
            }
        }
        System.out.println(brd);

        /* 
         * perform cheat mode
         */
        if(isCheatMode){
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            g2.drawString(chessName, 487, 25);
            g2.drawString(chessColor, 515, 50);
        }
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
    /* 
     * capturedNumber[0] : red Advisor
     * capturedNumber[1] : black Advisor
     * ...
     * capturedNumber[11] : black Cannon
     */
    private int[] capturedNumber = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
    public String getCapturedNumber(int n){
        String s = String.valueOf(capturedNumber[n]);
        return s;
    }
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
        brdStr += "|\n";
        for (int row = 0; row < rows; row++) {
            brdStr += row + "";
            for (int col = 0; col < cols; col++) {
                Piece p = pieceAt(col, row);
                if (p == null) {
                    brdStr += " .";
                } else {
                    switch (p.rank) {
                        case GENERAL:
                            if(!p.isUp){
                                brdStr += p.isRed ? " G." : " g."; break;//add point means didn`t turn back
                            }else {
                                brdStr += p.isRed ? " G" : " g"; break;
                            }
                        case ADVISOR:
                        if(!p.isUp){
                            brdStr += p.isRed ? " A." : " a."; break;
                        }else {
                            brdStr += p.isRed ? " A" : " a"; break;
                        }
                        case MINISTER:
                        if(!p.isUp){
                            brdStr += p.isRed ? " M." : " m."; break;
                        }else {
                            brdStr += p.isRed ? " M" : " m"; break;
                        }
                        case CHARIOT:
                        if(!p.isUp){
                            brdStr += p.isRed ? " Ch." : " ch."; break;
                        }else {
                            brdStr += p.isRed ? " Ch" : " ch"; break;
                        }
                        case HORSE:
                        if(!p.isUp){
                            brdStr += p.isRed ? " H." : " h."; break;
                        }else {
                            brdStr += p.isRed ? " H" : " h"; break;
                        }
                        case CANNON:
                        if(!p.isUp){
                            brdStr += p.isRed ? " C." : " c."; break;
                        }else {
                            brdStr += p.isRed ? " C" : " c"; break;
                        }
                        case SOLDIER:
                        if(!p.isUp){
                            brdStr += p.isRed ? " S." : " s."; break;
                        }else {
                            brdStr += p.isRed ? " S" : " s"; break;
                        }
                    }}
            }
            brdStr += "|\n";
        }
        String savedata = "";
        if (isRedTurn){
            savedata = brdStr += "Red turn|\n\n";
        } else {
            savedata = brdStr += "Black turn|\n\n";
        }
        save.saveDataFile(savedata);
        save.saveJustDataFile(savedata);
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
            pieces.add(new Piece(column[3], row[2], true, Rank.SOLDIER, "rz" , 1));
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
                        Piece pieceEaten = new Piece(4.5, 7.8, false, Rank.GENERAL, "bb", 30);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        redScore += 30;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){save.clearDataFile();isGameOver = true;}
                    break;
                case ADVISOR:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 0.3, true, Rank.ADVISOR, "rs", 10);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[0] += 1;
                        blackScore += 10;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 6.9, false, Rank.ADVISOR, "bs", 10);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[1] += 1;
                        redScore += 10;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){save.clearDataFile();isGameOver = true;}
                    break;
                case MINISTER:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 1.1, true, Rank.MINISTER, "rx", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[2] += 1;
                        blackScore += 5;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 6.0, false, Rank.MINISTER, "bx", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[3] += 1;
                        redScore += 5;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){save.clearDataFile();isGameOver = true;}
                    break;
                case CHARIOT:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 2.0, true, Rank.CHARIOT, "rj", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[4] += 1;
                        blackScore += 5;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 5.1, false, Rank.CHARIOT, "bj", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[5] += 1;
                        redScore += 5;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){save.clearDataFile();isGameOver = true;}
                    break;
                case HORSE:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 3.0, true, Rank.HORSE, "rm", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[6] += 1;
                        blackScore += 5;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 4.3, false, Rank.HORSE, "bm", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[7] += 1;
                        redScore += 5;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){save.clearDataFile();isGameOver = true;}
                    break;
                case SOLDIER:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 4.6, true, Rank.SOLDIER, "rz", 1);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[8] += 1;
                        blackScore += 1;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 2.5, false, Rank.SOLDIER, "bz", 1);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[9] += 1;
                        redScore += 1;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){save.clearDataFile();isGameOver = true;}
                    break;
                case CANNON:
                    if (targetP.isRed){
                        Piece pieceEaten = new Piece(-1.5, 3.8, true, Rank.CANNON, "rp", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[10] += 1;
                        blackScore += 5;
                    } else {
                        Piece pieceEaten = new Piece(4.5, 3.4, false, Rank.CANNON, "bp", 5);
                        pieceEaten.turnUp(true);
                        pieces.add(pieceEaten);
                        capturedNumber[11] += 1;
                        redScore += 5;
                    }
                    System.out.println("RedScore = " + redScore + "; BlackScore = " + blackScore);
                    if (checkGameOver()){save.clearDataFile();isGameOver = true;}
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
}//棋子排位