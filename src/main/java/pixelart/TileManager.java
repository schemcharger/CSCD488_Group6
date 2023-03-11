package pixelart;

import magicitem.MagicItem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;


public class TileManager {
    GamePanel gp;
    HashMap<Color, tile> tiles;
    Color[][] mapTileNum;
    MagicItem item;

    public TileManager(GamePanel gp, MagicItem item){
        this.gp = gp;
        this.tiles = new HashMap<>();
        getTileImage();
        loadMap(item);
    }

    public void getTileImage(){
        try{
            tile tile = new tile();
            tile.image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/DefaultClear.png"));
            this.tiles.put(new Color(0,0,0,0), tile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(MagicItem item){
        this.item = item;
        mapTileNum = item.getArt().clone();
        this.gp.MasterScreenCol = item.getSize();
        this.gp.MasterScreenRow = item.getSize();
        this.gp.screenHeight = this.gp.ActualTileSize * this.gp.MasterScreenRow;
        this.gp.screenWidth = this.gp.ActualTileSize * this.gp.MasterScreenCol;
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
            tile tile = new tile();
            tile.image = image;
            this.tiles.put(color, tile);
            return tile;
        }
    }

    public void setMapTileNum(int x,int y,Color color, Color[][] mapTileNum) {
        mapTileNum[x][y]= color;
        this.mapTileNum = mapTileNum;
    }

    public Color[][] getMap(){
        return this.mapTileNum;
    }
    public int getNumberOfColors() {return this.tiles.size();}
    public void draw(Graphics2D g2){
        g2.drawImage(getTile(new Color(0,0,0,0)).image,0,0,gp.ActualTileSize,gp.ActualTileSize,null);

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gp.MasterScreenCol && row < gp.MasterScreenRow) {
            Color color = mapTileNum[col][row];
            g2.drawImage(getTile(color).image, x, y, gp.ActualTileSize, gp.ActualTileSize, null);
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
        this.item.setArt(this.mapTileNum.clone());
    }


}
