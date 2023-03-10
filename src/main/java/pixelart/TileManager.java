package pixelart;

import magicitem.MagicItem;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;


public class TileManager {
    GamePanel gp;
    tile[] tile;
    Color[][] mapTileNum;
    MagicItem item;

    public TileManager(GamePanel gp, MagicItem item){
        this.gp = gp;
        tile = new tile[5];
        this.item = item;
        mapTileNum = item.getArt().clone();
        this.gp.MasterScreenCol = item.getSize();
        this.gp.MasterScreenRow = item.getSize();
        this.gp.screenHeight = this.gp.ActualTileSize * this.gp.MasterScreenRow;
        this.gp.screenWidth = this.gp.ActualTileSize * this.gp.MasterScreenCol;
        loadMap();
    }

    public void getTileImage(){
        try{
            tile[0] = new tile();
            tile[0].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/DefaultClear.png"));

            tile[1] = new tile();
            tile[1].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/DiamondDark.png"));

            tile[2] = new tile();
            tile[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/SwordHandleBrown.png"));

            tile[3] = new tile();
            tile[3].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/SwordHandleDarkBrown.png"));

            tile[4] = new tile();
            tile[4].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tiles/DiamondBlue.png"));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(){
      try{
          InputStream is = getClass().getClassLoader().getResourceAsStream("MapTestSave.txt");
          BufferedReader br = new BufferedReader(new InputStreamReader(is));

          int col = 0;
          int row = 0;

          while(col < gp.MasterScreenCol && row < gp.MasterScreenRow){
              String line = br.readLine();
              while(col < gp.MasterScreenCol){
                  String numbers[] = line.split(" ");

                  int num = Integer.parseInt(numbers[col]);

                  mapTileNum[col][row]=num;
                  col++;
              }
              if(col == gp.MasterScreenCol){
                 col = 0;
                 row++;
              }
          }
          br.close();
      }catch (Exception e){

      }
    }

    public void setMapTileNum(int x,int y,Color color, Color[][] mapTileNum) {
        mapTileNum[x][y]= color;
        this.mapTileNum = mapTileNum;
    }

    public Color[][] getMap(){
        return this.mapTileNum;
    }
    public int getNumberOfColors() {return this.tile.length;}
    public void draw(Graphics2D g2){
        g2.drawImage(tile[0].image,0,0,gp.ActualTileSize,gp.ActualTileSize,null);

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gp.MasterScreenCol && row < gp.MasterScreenRow) {
            Color tileNum = mapTileNum[col][row];
            g2.drawImage(tile[tileNum].image, x, y, gp.ActualTileSize, gp.ActualTileSize, null);
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


}
