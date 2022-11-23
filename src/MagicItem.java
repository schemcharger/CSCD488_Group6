import java.awt.*;
import java.util.Date;

public class MagicItem implements Comparable<MagicItem>{

    private Date created;
    private String name;
    private ItemType type;
    private String description;
    private Color[][] art;
    private int size;

    public MagicItem(String name, ItemType type, String description) {
        if(name==null||name.equals("")||type==null){
            throw new IllegalArgumentException("Null Fields");
        }
        this.created = new Date();
        this.name = name;
        this.type = type;
        this.description = description;
        this.size = 8;
        this.art = new Color[this.size][this.size];
        for(int i=0; i<this.art.length; i++){
            for(int j=0; j<this.art[i].length; j++){
                this.art[i][j] = Color.WHITE;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name==null||name==""){
            throw new IllegalArgumentException("Name is null");
        }
        this.name = name;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
       if(type==null){
            throw new IllegalArgumentException("Invalid Item Type");
        }
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color[][] getArt() {
        return art;
    }

    public void updateArt(int[] color, int x, int y) {
        if(x<0||y<0||x>=this.size||y>=this.size){
            throw new IndexOutOfBoundsException("Not in grid");
        }
        if(color.length!=3){
            throw new IllegalArgumentException("Not a valid color");
        }
        for(int i=0; i<color.length;i++){
            if(color[i]<0||color[i]>255){
                throw new IllegalArgumentException("invalid rgb value");
            }
        }
        this.art[x][y] = new Color(color[0], color[1], color[2]);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.art = new Color[size][size];
        for(int i=0; i<this.art.length; i++){
            for(int j=0; j<this.art[i].length; j++){
                this.art[i][j] = Color.WHITE;
            }
        }
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public int compareTo(MagicItem o) {
        if(o==null){
            throw new IllegalArgumentException("No Magic Item");
        }
        if(this.equals(o)){
            return 0;
        }
        return created.compareTo(o.getCreated());
    }

    @Override
    public String toString() {
        String out = '{' +
                "\"created\":\"" + created +
                "\", \"name\":\"" + name +
                "\", \"type\":\"" + type +
                "\", \"description\":";
            if(this.description==null){
                out+="null";
            }else{
                out+="\""+this.description+"\"";
            }
        out+=", \"size\":" + size +", \"art\":[";

        for(int i=0; i<this.art.length; i++){
            for(int j=0; j<this.art[i].length; j++){
                if(!this.art[i][j].equals(Color.WHITE)){
                    out+="{\"x\":" + i +
                            ", \"y\":" + j +
                            ", \"color\":" +
                            this.art[i][j].getRGB() + "}";
                }
            }
        }
        out += "]}";
        return out;
    }
}
