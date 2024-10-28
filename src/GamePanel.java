import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.Random;


import javax.swing.Timer;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

    //Declaration of variables

    static final int SCREEN_WIDTH = 900;
    static final int SCREEN_HEIGHT = 900;

    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH)/UNIT_SIZE;
    static final int DELEY = 150;
    
    final int x[] = new  int[GAME_UNITS];
    final int y[] = new  int[GAME_UNITS];

    int bodyParts = 5;
    int numOfApples;
    int appleX;
    int appleY;

    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    
    GamePanel(){
        //Creating a game panel
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        
        startGame();
    }
    public void startGame(){
        //Creat apple and start new game
        newApple();
        running = true;
        timer = new Timer(DELEY, this);
        timer.start();
        
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        if(running){
            //Draw gridlines
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            //Draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //Draw snake
            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(216, 137, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // and score
            g.setColor(Color.red);
            g.setFont(new Font("Gonzaga", Font.ITALIC, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + numOfApples, (SCREEN_WIDTH - metrics.stringWidth("Score: " + numOfApples))/2, SCREEN_HEIGHT-40);
        }else{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameOver(g);
        }
    }
    public void newApple(){
        //put apple in a random space
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }
    public void move(){
        //snake moves
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        //change direction
        switch (direction) {
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkEating(){

        //check if snakes collades apple
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            numOfApples++;

            //call newApple method
            newApple();      
        }
    }
    public void checkCollisions(){

        //check head collision whis body
        for(int i = bodyParts; i > 0; i--){
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //check head collision whis borders
        if (x[0] < 0) {
            running = false;
        }
        if (x[0] > SCREEN_WIDTH-UNIT_SIZE) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT-UNIT_SIZE) {
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Draw score after death
        g.setColor(Color.red);
        g.setFont(new Font("Gonzaga", Font.ITALIC, 70));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.drawString("Score: " + numOfApples, (SCREEN_WIDTH - metrics.stringWidth("Score: " + numOfApples))/2, SCREEN_HEIGHT/2+70);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running){
            move();
            checkCollisions();
            checkEating();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
    
        //Moving keys

        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
        
    }

}
