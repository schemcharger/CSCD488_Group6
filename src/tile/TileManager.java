package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;


public class TileManager {

    GamePanel gp;
    tile[] tile;
    int mapTileNum[][];

    int numberOfColors = 4;

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new tile[5];
        mapTileNum = new int[gp.MasterScreenCol][gp.MasterScreenRow];
        getTileImage();
        loadMap();
    }

    public void getTileImage(){
        try{
            tile[0] = new tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/DefaultClear.png"));

            tile[1] = new tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/DiamondDark.png"));

            tile[2] = new tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/SwordHandleBrown.png"));

            tile[3] = new tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/res/SwordHandleDarkBrown.png"));

            tile[4] = new tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/res/DiamondBlue.png"));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(){
      try{
          InputStream is = getClass().getResourceAsStream("/ItemMap/MapTestSave.txt");
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

    public void setMapTileNum(int x,int y,int color, int[][] mapTileNum) {
        mapTileNum[x][y]= color;
        this.mapTileNum = mapTileNum;
    }

    public int[][] getMap(){
        return this.mapTileNum;
    }
    public int getNumberOfColors() {return this.numberOfColors;}
    public void draw(Graphics2D g2){
        g2.drawImage(tile[0].image,0,0,gp.ActualTileSize,gp.ActualTileSize,null);

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gp.MasterScreenCol && row < gp.MasterScreenRow) {
            int tileNum = mapTileNum[col][row];
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
