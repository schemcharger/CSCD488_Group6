package tile;

import java.awt.image.BufferedImage;

public class tile {
    public BufferedImage image;
    public boolean collision = false;

    public tile(BufferedImage image){
        this.image = image;
    }
}
