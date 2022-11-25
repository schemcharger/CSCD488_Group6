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

    protected MagicItem(Date created, String name, ItemType type, String description) {
        if(name==null||name.equals("")||type==null||created==null){
            throw new IllegalArgumentException("Null Fields");
        }
        this.created = created;
        this.name = name;
        this.type = type;
        this.description = description;
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

    public void updateArt(Color color, int x, int y) {
        if(x<0||y<0||x>=this.size||y>=this.size){
            throw new IndexOutOfBoundsException("Not in grid");
        }

        this.art[x][y] = color;
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
                            "; \"y\":" + j +
                            "; \"color\":" +
                            this.art[i][j].getRGB() + "}";
                }
            }
        }
        out += "]}";
        return out;
    }
}
