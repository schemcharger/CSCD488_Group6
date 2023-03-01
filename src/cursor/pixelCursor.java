package cursor;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class pixelCursor extends entity{
    static GamePanel gp;
    static KeyHandler keyH;


    public pixelCursor(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getCursorImage();
    }
    public void setDefaultValues(){
        x = 0;
        y = 0;
        speed = 32;
        colorChoice = new Color(0,0,0,0);
    }
    public void getCursorImage(){

        try{
            cursorMove = ImageIO.read(getClass().getResourceAsStream("/res/Selector.png"));
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static void update(){


        if(keyH.upPressed==true){
            y -= speed;
        }
        else if (keyH.downPressed==true) {
            y += speed;
        }
        else if (keyH.rightPressed==true) {
            x += speed;
        }
        else if (keyH.leftPressed==true) {
            x -= speed;
        }
    }
    public static void draw(Graphics2D g2){
        BufferedImage image = null;
        image = cursorMove;
        g2.drawImage(image, x, y, gp.ActualTileSize, gp.ActualTileSize, null);
    }

    public int getCursorX(){
        return this.x;
    }
    public int getCursorY(){
        return this.y;
    }
    public Color getColorChoice(){
        return this.colorChoice;
    }

    public void setColorChoice(int r, int g, int b){
        this.colorChoice = new Color(r,g,b);
    }


}
