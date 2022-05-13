import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'd';
    boolean running = false;
    Timer timer;
    Random random;
    static int HIGH_SCORE;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (running) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.GRAY);
            g.setFont(new Font("Dialog", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

        }else{
            gameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    public void move(){
        for (int i = bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'w':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 's':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'a':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'd':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if ( (x[0] == appleX) && (y[0] == appleY) ) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        //checks head collision with body
        for (int i = bodyParts; i > 0; i--) {
            if( (x[0] == x[i]) && (y[0] == y[i]) ){
                running = false;
            }
        }
        //checks head collision with left border
        if(x[0] < 0) running = false;
        //checks head collision with right border
        if(x[0] > SCREEN_WIDTH) running = false;
        //checks head collision with top border
        if(y[0] < 0) running = false;
        //checks head collision with bottom border
        if(y[0] > SCREEN_HEIGHT) running = false;

        if(!running) timer.stop();
    }

    public void gameOver(Graphics g)  {
        High_Score high_score = new High_Score();
        high_score.high(applesEaten);
        System.out.println(high_score.count);


        try {
            String filePath = "C:\\Users\\elbek\\IdeaProjects\\SnakeGame\\src\\HighScore.txt";
            FileOutputStream f = new FileOutputStream(filePath, true);
            String lineToAppend = Integer.toString(applesEaten);
            byte[] byteArr = lineToAppend.getBytes(); //converting string into byte array
            f.write(byteArr);
            f.close();
        } catch(Exception e) {
            System.out.println(e);
        }

        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        //High Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("High Score: " + HIGH_SCORE, (SCREEN_WIDTH - metrics3.stringWidth("High Score: " + HIGH_SCORE))/2, SCREEN_HEIGHT - 30);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'd') direction = 'a';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'a') direction = 'd';
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 's') direction = 'w';
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'w') direction = 's';
                    break;
            }
        }
    }


}
