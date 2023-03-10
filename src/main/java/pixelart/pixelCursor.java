package pixelart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class pixelCursor extends entity{
    private static GamePanel gp;
    private static KeyHandler keyH;


    public pixelCursor(GamePanel gp, KeyHandler keyH){
        pixelCursor.gp = gp;
        pixelCursor.keyH = keyH;
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
            cursorMove = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/Selector.png"));
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
        return x;
    }
    public int getCursorY(){
        return y;
    }
    public Color getColorChoice(){
        return colorChoice;
    }

    public void setColorChoice(Color x){
        colorChoice = x;
    }


}
