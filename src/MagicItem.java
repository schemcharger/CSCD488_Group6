import java.awt.*;

public class MagicItem {
    enum ItemType{
        ARMOUR, CATALYST, GENERIC, WEAPON;
    }
    private String name;
    private ItemType type;
    private String description;
    private Color[][] art;
}
