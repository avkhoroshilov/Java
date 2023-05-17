import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.io.File.separator;
import java.util.Scanner;


public class GameField extends JPanel implements ActionListener{
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private int speed=250;
    private String an;


    public GameField(){
        setBackground(Color.black);
        loadImages();
        initGame();
        setFocusable(true);
        addKeyListener(new FieldKeyListener());

    }

    public void initGame(){
        inGame = true;

        dots = 2;
        for (int i = 0; i < dots; i++) {
            x[i] = 128 - i*DOT_SIZE;
            y[i] = 128;
        }
        timer = new Timer(speed,this);
        timer.start();

        createApple();
    }

    public void createApple(){
        appleX = new Random().nextInt(20)*DOT_SIZE;
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }

    public void loadImages(){

        ImageIcon iia = new ImageIcon("src"+separator+"apple.png");
        apple=iia.getImage();

        ImageIcon iid = new ImageIcon("src"+separator+"dot.png");
        dot=iid.getImage();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(apple,appleX,appleY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        }else{
            String str = "Game Over";
            String again = "Would you like to start again?(Y/N)";

            g.setColor(Color.white);
            g.drawString(str,125,SIZE/2);
            g.drawString(again,50,SIZE/2+30);
            if(an=="N"){
                setBackground(Color.WHITE);
                g.setColor(Color.black);
                String mom = "Goodbye";
                g.drawString(mom,125,SIZE/2);
                setFocusable(false);
            }




        }
    }

    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        } if(up){
            y[0] -= DOT_SIZE;
        } if(down){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            createApple();
        }
    }

    public void checkCollisions(){
        for (int i = dots; i >0 ; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }

        if(x[0]>SIZE){
            inGame = false;
        }
        if(x[0]<0){
            inGame = false;
        }
        if(y[0]>SIZE){
            inGame = false;
        }
        if(y[0]<0){
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollisions();
            move();

        }
        repaint();
    }
    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }

            if(key == KeyEvent.VK_UP && !down){
                right = false;
                up = true;
                left = false;
            }
            if(key == KeyEvent.VK_DOWN && !up){
                right = false;
                down = true;
                left = false;

            }
            if(key == KeyEvent.VK_Y && !inGame){
                timer.stop();
                initGame();
            }
            if(key == KeyEvent.VK_N && !inGame){
                an = "N";
            }
        }
    }
}
