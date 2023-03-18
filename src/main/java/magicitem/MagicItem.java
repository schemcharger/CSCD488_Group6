package magicitem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

public class MagicItem implements Comparable<MagicItem>{

    private Date created; //the date and time the object was first created.
    private String name; //The item's name.
    private ItemType type; //The item's type from the ItemType enum
    private String description; //The item's description, can be blank.
    private ArrayList<String> traits;
    private Color[][] art; //The two dimensional array that holds the art for the item. Default color is white.
    private int size; //The size of the art grid.

    public MagicItem(String name, ItemType type, String description) { //used to create a new MagicItem.
        if(name==null||name.equals("")||type==null){ //check for valid name and type.
            throw new IllegalArgumentException("Null Fields");
        }
        this.created = new Date(); //create date at current moment.
        this.name = name;
        this.type = type;
        this.traits = new ArrayList<>();
        this.setDescription(description);
        this.setSize(8);
    }

    public MagicItem(Date created, String name, ItemType type, String description) { //used when creating item from database entry
        if(name==null||name.equals("")||type==null||created==null){ //check for valid name, type, and date
            throw new IllegalArgumentException("Null Fields");
        }
        this.created = created;
        this.name = name;
        this.type = type;
        this.traits = new ArrayList<>();
        this.setDescription(description);
        this.setSize(8);
    }

	public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if(name==null||name==""){ //check for valid item name
            throw new IllegalArgumentException("Name is null");
        }
        this.name = name;
    }

    public ItemType getType() {
        return this.type;
    }

    public void setType(ItemType type) {
       if(type==null){ //check for valid item type
            throw new IllegalArgumentException("Invalid Item Type");
        }
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if(description==null){ //if description is null, set it to blank
            this.description = "";
        }else{
            this.description = description;
        }
    }

    public ArrayList<String> getTraits(){return this.traits;}

    public void addTrait(String trait){
        if(trait==null||trait.isEmpty()){
            throw new IllegalArgumentException("Invalid Trait");
        }
        for(int i=0; i< this.traits.size(); i++){
            if(trait.toLowerCase().compareTo(this.traits.get(i))<0){
                this.traits.add(i, trait.toLowerCase());
                return;
            }else if(trait.toLowerCase().equals(traits.get(i))){
                return;
            }
        }
        this.traits.add(trait.toLowerCase());
    }

    public void removeTrait(String trait){
        if(trait==null||trait.isEmpty()){
            throw new IllegalArgumentException("Invalid Trait");
        }
        this.traits.remove(trait);
    }
    
    public ArrayList<String> setTraits(ArrayList<String> list) {
    	return this.traits = list;
    }

    public Color[][] getArt() {
        return this.art;
    }

    public void updateArt(Color color, int x, int y) { //sets the block of art at (x,y) to the given color
        if(x<0||y<0||x>=this.size||y>=this.size){ //checks for valid (x,y)
            throw new IndexOutOfBoundsException("Not in grid");
        }

        this.art[x][y] = color;
    }

    public void setArt(Color[][] art){
        this.art = art;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) { //sets the art to a new size with white squares
        this.size = size;
        this.art = new Color[size][size];
        for(int i=0; i<this.art.length; i++){
            for(int j=0; j<this.art[i].length; j++){
                this.art[i][j] = new Color(0, 0, 0, 0);;
            }
        }
    }

    public Date getCreated() {
        return this.created;
    }

    @Override
    public int compareTo(MagicItem o) { //compares item based on date created
        if(o==null){
            throw new IllegalArgumentException("No Magic Item");
        }
        if(this.equals(o)){
            return 0;
        }
        return this.created.compareTo(o.getCreated());
    }

    @Override
    public String toString() { //returns the item as a string is json format
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
        out+=", \"traits\":[";
        if(!this.traits.isEmpty()) {
            for (int i = 0; i < this.traits.size() - 1; i++) {
                out += this.traits.get(i)+"; ";
            }
            out += this.traits.get(this.traits.size()-1);
        }
        out+="], \"size\":" + size +", \"art\":[";

        for(int i=0; i<this.art.length; i++){ //Save the coordinates and color of any non-white pixels
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

