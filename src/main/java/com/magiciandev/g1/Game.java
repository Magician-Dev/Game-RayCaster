package com.magiciandev.g1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable{
    public static final long serialVersionUID = 1L;

    public static final String NAME = "Untitled game";
    public static final int HEIGHT = 240;
    public static final int WIDTH = HEIGHT*4/3;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    private boolean running = false;
    private int tickCount;

    public void start(){
        running = true;
        new Thread(this).start();
    }

    public void stop(){
        running = false;
    }

    public void run(){
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double nsPerTick = 1000000000.0 / 35.0;
        int frames = 0;
        int ticks = 0;
        long lastTimer1 = System.currentTimeMillis();

        while(running){
            long now = System.nanoTime();
            unprocessed += (now-lastTime)/nsPerTick;
            lastTime = now;
            boolean shouldRender = true;
            while(unprocessed>=1) {
                ticks++;
                tick();
                unprocessed--;
                shouldRender = true;
            }
            if(shouldRender){
                frames++;
                render();
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            if(System.currentTimeMillis()-lastTimer1>1000){
                lastTimer1 += 1000;
                System.out.println(ticks + " ticks, " + frames + " fps");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick(){
        tickCount++;
    }

    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }

        for(int i=0; i<pixels.length; i++){
            pixels[i] = i+tickCount;
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args){
        Game game = new Game();
        game.setMinimumSize(new Dimension(WIDTH*2, HEIGHT*2));
        game.setMaximumSize(new Dimension(WIDTH*2, HEIGHT*2));
        game.setPreferredSize(new Dimension(WIDTH*2, HEIGHT*2));


        JFrame frame = new JFrame(Game.NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }
}
