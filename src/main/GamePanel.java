package main;

import magicitem.MagicItem;
import cursor.pixelCursor;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class GamePanel extends JPanel implements Runnable{

    // fields
    private BufferedImage img;


    //Tile Screen Settings
    final int MasterTileSize = 16; // this is the main tile size of 16 pixels
    final int TileScale = 2; //this is the scaling multiplier for the tile size for easier use
    public final int ActualTileSize = MasterTileSize * TileScale;
    public int MasterScreenCol = 32;
    public int MasterScreenRow = 32;
    public int screenWidth = ActualTileSize * MasterScreenCol;
    public int screenHeight = ActualTileSize * MasterScreenRow;
    //FPS
    int FPS = 10;

    TileManager tileM;
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    pixelCursor cursor = new pixelCursor(this, keyH);



    public GamePanel(MagicItem item) {
        tileM = new TileManager(this, item);
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;





        while (gameThread != null){
            //System.out.println("Game is Running");

            //1 UPDATE
            update();
            keyH.setRightPressed();
            keyH.setLeftPressed();
            keyH.setDownPressed();
            keyH.setUpPressed();
            //Save
            if(keyH.savePressed==true){
                this.tileM.SaveMap();
            }
            keyH.setSavePressed();
            //Color Swap
            if(keyH.changeColorPlus==true){
                Scanner kb = new Scanner(System.in);
                int in[] = new int[3];
                for(int i =0; i<in.length; i++){
                    in[i] = kb.nextInt();
                }
                kb.close();
                this.cursor.setColorChoice(in[0], in[1], in[2]);
            }
            keyH.setChangeColorPlus();
            /*if(keyH.changeColorMinus==true){
                if(  currentColor > 1){
                    this.cursor.setColorChoice(currentColor-1);
                }
                else{
                    this.cursor.setColorChoice(1);
                }
            }
            keyH.setChangeColorMinus();*/
            //Place
            if(keyH.PlaceTile==true){
                tileM.setMapTileNum((this.cursor.getCursorX()/ActualTileSize),(this.cursor.getCursorY()/ActualTileSize),this.cursor.getColorChoice(),this.tileM.getMap());
            }
            keyH.setPlaceTile();
            //Delete
            if(keyH.deletePressed==true){
                tileM.setMapTileNum((this.cursor.getCursorX()/ActualTileSize),(this.cursor.getCursorY()/ActualTileSize),new Color(0,0,0,0),this.tileM.getMap());
            }
            keyH.setDeletePressed();

            //2 DRAW
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

                } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void update(){
        pixelCursor.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);
        pixelCursor.draw(g2);
        g2.dispose();
    }



    public void setScreenSize(){
        this.screenHeight = ActualTileSize * MasterScreenRow;
        this.screenWidth = ActualTileSize * MasterScreenCol;
    }
}
