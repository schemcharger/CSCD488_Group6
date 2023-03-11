package helpers;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;

import magicitem.ItemType;
import magicitem.MagicItem;

public class ItemHelper {
    ArrayList<MagicItem> itemList; //a sorted list of magic items
    ArrayList<String> traitList;
    int sortType; // what parameter to sort on. 0->date created, 1->name, 2->type

    public ItemHelper() {
        this.sortType = 0;
        this.traitList = new ArrayList<>();
        File file = new File("itemDb");
        if(file.exists()){ //if the database exists, read the items from it. else create a blank list.
            readDB(file);
        }else{
            this.itemList = new ArrayList<>();
        }
    }

    public MagicItem load(int i){ //pick a magic item from the sorted list.
        if(i<0||i>=itemList.size()){ //check if the index is in the list
            throw new IllegalArgumentException("Invalid item");
        }
        MagicItem item = itemList.get(i);
        itemList.remove(i); //the magic item gets removed from the list to ensure a duplicate is not saved.
        return item;
    }

    public boolean Save(MagicItem item){
        if (item == null) { //check if the item is valid
            throw new IllegalArgumentException("Cannot save Null item");
        }
        boolean added = this.addItem(item); // add an item to the list.
        if(added){ // if the item was successfully added, write the list to the database
            this.writeDB();
        }
        return added;
    }

    public void SetSortType(int type){
        if(type<0||type>2){ //check if the sort type is valid
            throw new IllegalArgumentException("Unknown sort type");
        }
        this.sortType = type;
        this.Sort(); //Once the sort type is changed, sort the list.
    }

    public void Sort(){ //Add each MagicItem to a new sorted list

        ArrayList<MagicItem> temp = this.itemList;
        this.itemList = new ArrayList<>();
        for(int i=0; i<temp.size(); i++){
            this.addItem(temp.get(i));
        }
    }

    public void writeDB(){
        File backup = new File("itemDb.bak");//the database backup
        File file = new File("itemDb"); // the database
        if(file.exists()){ //if the database file already exists, copy it to the backup file
            if(backup.exists()){
                backup.delete();
            }
            file.renameTo(backup);
            file.delete();
        }
        try{ // write the itemlist out to the database file
            file.createNewFile();
            PrintStream fout = new PrintStream(file);
            for(int i=0; i<this.itemList.size(); i++){
                fout.println(itemList.get(i).toString());
            }
            fout.close();
        }catch(Exception e){
            if(backup.exists()){
                backup.renameTo(file);
            }
            System.out.println(e);
        }

    }

    public boolean writeArt(int index){
		String home = System.getProperty("user.home");
		String dir = String.format("%s%sDocuments%sMagic Item Creator", home, File.separator, File.separator);
		final File artFile = new File(dir, "Item Art");
		artFile.mkdirs();
        MagicItem cur = this.itemList.get(index);
        Color[][] art = cur.getArt();
        int size = 512/cur.getSize();
        BufferedImage artOut = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = artOut.createGraphics();
        for(int i=0;i<art.length; i++){
            for(int j=0; j< art[i].length; j++){
                graphics.setColor(art[i][j]);
                graphics.fillRect((i*size)+1, (j*size)+1, size, size);
            }
        }
        graphics.dispose();
        File file = new File(artFile, this.itemList.get(index).getName() + ".png");
        File backup = new File(artFile, this.itemList.get(index).getName() + ".png.bak");
        if(file.exists()){
            if(backup.exists()){
                backup.delete();
            }
            file.renameTo(backup);
            file.delete();
        }
        try {
            ImageIO.write(artOut, "PNG", file);
        }catch (IOException e){
            if(backup.exists()){
                backup.renameTo(file);
            }
            return false;
        }
        return true;
    }

    private boolean addItem(MagicItem item) { // add an item to the itemList based on the sort type

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


    public void addTrait(String trait){
        if(trait==null||trait.isEmpty()){
            throw new IllegalArgumentException("Invalid Trait");
        }
        for(int i=0; i< this.traitList.size(); i++){
            if(trait.toLowerCase().compareTo(this.traitList.get(i))<0){
                this.traitList.add(i, trait.toLowerCase());
                return;
            }else if(trait.toLowerCase().equals(traitList.get(i))){
                return;
            }
        }
        this.traitList.add(trait.toLowerCase());
    }

    private void readDB(File file){//Create an itemList from the database
        this.itemList = new ArrayList<>();
        try { //Use a scanner to read the file
            Scanner fin = new Scanner(file);
            while(fin.hasNextLine()) {
                this.addItem(parseItem(fin.nextLine()));

            }
            fin.close();
        } catch (Exception e) {
            try { //if there is a problem reading the database file, try reading the backup
                File backup = new File("itemDb.bak");
                Scanner fin = new Scanner(backup);
                while(fin.hasNextLine()) {
                    MagicItem item = parseItem(fin.nextLine());
                    this.addItem(item);
                    ArrayList<String> traits = item.getTraits();
                    for(int i=0; i<traits.size(); i++){
                        this.addTrait(traits.get(i));
                    }
                }
                fin.close();
            } catch (FileNotFoundException f) {
                e.printStackTrace();
                f.printStackTrace();
            }
        }
    }

    static MagicItem parseItem(String in){ //Parse a string in the format from MagicItem.toString
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
        String[] traits = str[4].substring(1, str[4].length()).split("; ");
        for(int i=0; i< traits.length; i++){
            item.addTrait(traits[i]);
        }
        item.setSize(Integer.parseInt(str[5].substring(7)));
        if(!str[6].substring(6, str[6].length()-1).equals("[]")){
            String[] art = str[6].substring(8, str[6].length() - 3).split("}\\{");
            String[] color;
            int x = 0, y = 0, rgb = 0;
            for (String s : art) {
                color = s.split("; ");
                x = Integer.parseInt(color[0].substring(4));
                y = Integer.parseInt(color[1].substring(4));
                rgb = Integer.parseInt(color[2].substring(8));
                item.updateArt(new Color(rgb, true), x, y);
            }
        }

        return item;
    }

}


