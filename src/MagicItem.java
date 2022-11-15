import java.awt.*;

public class MagicItem {

    private String name;
    private ItemType type;
    private String description;
    private Color[][] art;

    public MagicItem(String name, ItemType type) {
        this.name = name;
        this.type = type;
    }

    public MagicItem(String name, ItemType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }
}
