import java.awt.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.io.*;
import java.util.Date;

public class ItemHelper {
    ArrayList<MagicItem> itemList;
    int sortType; // 0->date created, 1->name, 2->type

    public ItemHelper() {
        this.sortType = 0;
        File file = new File("itemDb");
        if(file.exists()){
            this.itemList = readDB(file);
        }else{
            this.itemList = new ArrayList<>();
        }
    }

    public boolean Save(MagicItem item){
        if (item == null) {
            throw new IllegalArgumentException("Cannot save Null item");
        }
        return(this.addItem(item));
    }

    public void setSortType(int type){
        if(type<0||type>2){
            throw new IllegalArgumentException("Unknown sort type");
        }
        this.sortType = type;
    }

    public void sort(){
        ArrayList<MagicItem> temp = this.itemList;
        this.itemList = new ArrayList<>();
        for(int i=0; i<temp.size(); i++){
            this.addItem(temp.get(i));
        }
    }

    public void writeDB(){
        File backup = new File("itemDb.bak");
        File file = new File("itemDb");
        if(file.exists()){
            if(backup.exists()){
                backup.delete();
            }
            file.renameTo(backup);
            file.delete();
        }
        try{
            file.createNewFile();
            PrintStream fout = new PrintStream(file);
            for(int i=0; i<this.itemList.size(); i++){
                fout.println(itemList.get(i).toString());
            }
            fout.close();
        }catch(Exception e){
            System.out.println(e);
        }

    }

    private boolean addItem(MagicItem item) {
        try{
            for(int i=0; i<this.itemList.size(); i++){
                if(this.sortType==0){
                    if(item.getCreated().compareTo(this.itemList.get(i).getCreated())<0){
                        this.itemList.add(i, item);
                        return true;
                    }
                }else if(this.sortType==1){
                    if(item.getName().compareTo(this.itemList.get(i).getName())<0){
                        this.itemList.add(i, item);
                        return true;
                    }
                }else{
                    if(item.getType().compareTo(this.itemList.get(i).getType())<0){
                        this.itemList.add(i, item);
                        return true;
                    }
                }
            }
            this.itemList.add(item);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    private ArrayList<MagicItem> readDB(File file){
        ArrayList<MagicItem> lin = new ArrayList<>();
        return lin;
    }

    MagicItem parseItem(String in){
        if(in == null || !in.startsWith("{")){
            throw new IllegalArgumentException("Null parser input");
        }
        String[] str = in.substring(1).split(", ");
        Date created = new Date(str[0].substring(11, str[0].length()-1));
        String name = str[1].substring(8, str[1].length()-1);
        ItemType type = ItemType.valueOf(str[2].substring(8, str[2].length()-1));
        String description;
        if(str[3].substring(14).equals("null")){
            description = null;
        }else{
            description = str[3].substring(15, str[3].length()-1);
        }
        MagicItem item = new MagicItem(created, name, type, description);
        item.setSize(Integer.parseInt(str[4].substring(7)));
        String[] art = str[5].substring(8, str[5].length()-3).split("}\\{");
        String[] color;
        int x=0, y=0, rgb = 0;
        for (String s : art) {
            color = s.split("; ");
            x = Integer.parseInt(color[0].substring(4));
            y = Integer.parseInt(color[1].substring(4));
            rgb = Integer.parseInt(color[2].substring(8));
            item.updateArt(new Color(rgb), x, y);
        }

        return item;
    }

}

