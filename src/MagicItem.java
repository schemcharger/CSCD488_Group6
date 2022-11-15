import java.awt.*;

public class MagicItem {

    private String name;
    private ItemType type;
    private String description;
    private Color[][] art;
    private int size;

    public MagicItem(String name, ItemType type) {
        this.name = name;
        this.type = type;
        this.size = 8;
        this.art = new Color[this.size][this.size];
    }

    public MagicItem(String name, ItemType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.size = 8;
        this.art = new Color[this.size][this.size];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
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
        this.art[x][y] = new Color(color[0], color[1], color[2]);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
