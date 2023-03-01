package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import magicitem.*;


public class TileManager {

    GamePanel gp;
    HashMap<Color, tile> tiles;
    Color mapTileNum[][];
    MagicItem cur;


    public TileManager(GamePanel gp, MagicItem cur){
        this.gp = gp;
        tiles = new HashMap<>();
        mapTileNum = new Color[gp.MasterScreenCol][gp.MasterScreenRow];
        this.cur = cur;
        getTileImage();
        loadMap(cur);
    }

    public void getTileImage(){
        try{
            Color color = new Color(0,0,0,0);
            tile tile = new tile(ImageIO.read(getClass().getResourceAsStream("/res/DefaultClear.png")));
            this.tiles.put(color, tile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public tile getTile(Color color){
        if(tiles.containsKey(color)){
            return tiles.get(color);
        }else{
            BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(color);
            graphics.fillRect(0,0,16,16);
            graphics.dispose();
            return new tile(image);
        }
    }

    public void loadMap(MagicItem item){
      this.cur = item;
      this.mapTileNum = cur.getArt();
      this.gp.MasterScreenCol = item.getSize();
      this.gp.MasterScreenRow = item.getSize();
      this.gp.setScreenSize();
    }

    public void setMapTileNum(int x,int y,Color color, Color[][] mapTileNum) {
        mapTileNum[x][y] = color;
        this.mapTileNum = mapTileNum;
    }

    public Color[][] getMap(){
        return this.mapTileNum;
    }
    public int getNumberOfColors() {return this.tiles.size();}
    public void draw(Graphics2D g2){
        g2.drawImage(this.getTile(new Color(0,0,0,0)).image,0,0,gp.ActualTileSize,gp.ActualTileSize,null);

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gp.MasterScreenCol && row < gp.MasterScreenRow) {
            Color tileNum = mapTileNum[col][row];
            g2.drawImage(this.getTile(tileNum).image, x, y, gp.ActualTileSize, gp.ActualTileSize, null);
            col++;
            x += gp.ActualTileSize;
            if(col == gp.MasterScreenCol){
               col = 0;
               x = 0;
               row++;
               y += gp.ActualTileSize;
            }
        }
    }

    public void SaveMap(){
        this.cur.setArt(mapTileNum);
    }
}
