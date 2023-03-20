package pixelart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import application.ColorPickerController;
import magicitem.MagicItem;

public class GamePanel extends JPanel implements Runnable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	// fields
    //private BufferedImage img;
	private boolean exit;

    //Tile Screen Settings
    final int MasterTileSize = 16; // this is the main tile size of 16 pixels
    final int TileScale = 2; //this is the scaling multiplier for the tile size for easier use
    public final int ActualTileSize = MasterTileSize * TileScale;
    public int MasterScreenCol;
    public int MasterScreenRow;
    public int screenWidth;
    public int screenHeight;
    //FPS
    int FPS = 10;

    TileManager tileM;
    KeyHandler keyH;
    Thread gameThread;
    pixelCursor cursor;

    public static boolean gamePanelSave = false;


    public GamePanel(MagicItem item) {
        this.tileM= new TileManager(this, item);
        this.keyH = new KeyHandler();
        this.cursor = new pixelCursor(this, keyH);
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        exit = false;
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;





        while (!exit){
            //System.out.println("Game is Running");

            //1 UPDATE
            update();
            keyH.setRightPressed();
            keyH.setLeftPressed();
            keyH.setDownPressed();
            keyH.setUpPressed();
            //Save
            if(keyH.savePressed==true){
            	gamePanelSave = true;
                this.tileM.SaveMap();
                /*FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Menu.fxml"));
        		try {
					Parent root = loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		MenuController menuController = loader.getController();
                menuController.save();
                gamePanelSave = false;*/
            }
            keyH.setSavePressed();
            keyH.setChangeColorPlus();
            //Place
            if(keyH.PlaceTile==true){
            	
                javafx.scene.paint.Color color = null;
                if (ColorPickerController.color == null) {
                	color = javafx.scene.paint.Color.WHITE;
                } else {
                	color = ColorPickerController.color;
                }
            	Color c = new Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
            	this.cursor.setColorChoice(c);
            	tileM.setMapTileNum((this.cursor.getCursorX()/ActualTileSize),(this.cursor.getCursorY()/ActualTileSize),this.cursor.getColorChoice(),this.tileM.getMap());
            }
            keyH.setPlaceTile();
            //Delete
            if(keyH.deletePressed==true){
                tileM.setMapTileNum((this.cursor.getCursorX()/ActualTileSize),(this.cursor.getCursorY()/ActualTileSize),new Color(0,0,0,0),this.tileM.getMap());
                //this.tileM.SaveMap();
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
    
    public void stop() {
    	exit = true;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);
        pixelCursor.draw(g2);
        g2.dispose();
    }
}
