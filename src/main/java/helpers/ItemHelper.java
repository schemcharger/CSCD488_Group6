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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magicitem.ItemType;
import magicitem.MagicItem;

public class ItemHelper {
    static ObservableList<MagicItem> itemList; //a sorted list of magic items
    static ArrayList<String> traitList;
    static int sortType; // what parameter to sort on. 0->date created, 1->name, 2->type

    /*public ItemHelper() {
        this.sortType = 0;
        this.traitList = new ArrayList<>();
        File file = new File("itemDb");
        if(file.exists()){ //if the database exists, read the items from it. else create a blank list.
            readDB(file);
        }
    }*/

    /*public MagicItem load(int i){ //pick a magic item from the sorted list.
        if(i<0||i>=itemList.size()){ //check if the index is in the list
            throw new IllegalArgumentException("Invalid item");
        }
        MagicItem item = itemList.get(i);
        itemList.remove(i); //the magic item gets removed from the list to ensure a duplicate is not saved.
        return item;
    }*/

    /*public boolean Save(MagicItem item){
        if (item == null) { //check if the item is valid
            throw new IllegalArgumentException("Cannot save Null item");
        }
        boolean added = this.addItem(item); // add an item to the list.
        if(added){ // if the item was successfully added, write the list to the database
            this.writeDB();
        }
        return added;
    }*/

    public static void SetSortType(int type){
        if(type<0||type>2){ //check if the sort type is valid
            throw new IllegalArgumentException("Unknown sort type");
        }
        sortType = type;
    }

    public static ObservableList<MagicItem> Sort(ObservableList<MagicItem> list){ //Add each MagicItem to a new sorted list  //move to menu controller

        itemList = FXCollections.observableArrayList();
        for(int i=0; i<list.size(); i++){
            addItem(list.get(i));
        }
        return itemList;
    }

    public static void writeDB(ObservableList<MagicItem> list){
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
            for(int i=0; i<list.size(); i++){
                fout.println(list.get(i).toString());
            }
            fout.close();
        }catch(Exception e){
            if(backup.exists()){
                backup.renameTo(file);
            }
            System.out.println(e);
        }

    }

    public static boolean writeArt(MagicItem cur){
		String home = System.getProperty("user.home");
		String dir = String.format("%s%sDocuments%sMagic Item Creator", home, File.separator, File.separator);
		final File artFile = new File(dir, "Item Art");
		artFile.mkdirs();
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
        File file = new File(artFile, cur.getName() + ".png");
        File backup = new File(artFile, cur.getName() + ".png.bak");
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

    public static boolean addItem(MagicItem item) { // add an item to the itemList based on the sort type

        try{
            for(int i=0; i<itemList.size(); i++){
                if(sortType==0){
                    if(item.getCreated().compareTo(itemList.get(i).getCreated())<0){
                        itemList.add(i, item);
                        return true;
                    }
                }else if(sortType==1){
                    if(item.getName().compareTo(itemList.get(i).getName())<0){
                        itemList.add(i, item);
                        return true;
                    }
                }else{
                    if(item.getType().compareTo(itemList.get(i).getType())<0){
                        itemList.add(i, item);
                        return true;
                    }
                }
            }
            itemList.add(item);
            return true;
        }catch(Exception e) {
            return false;
        }
    }


    public static void addTrait(String trait){
        if(trait==null||trait.isEmpty()){
            throw new IllegalArgumentException("Invalid Trait");
        }
        for(int i=0; i< traitList.size(); i++){
            if(trait.toLowerCase().compareTo(traitList.get(i))<0){
                traitList.add(i, trait.toLowerCase());
                return;
            }else if(trait.toLowerCase().equals(traitList.get(i))){
                return;
            }
        }
        traitList.add(trait.toLowerCase());
    }

    public static ObservableList<MagicItem> readDB(){//Create an itemList from the database
        //itemList = FXCollections.observableArrayList();
        try { //Use a scanner to read the file
            File file = new File("itemDb");
            Scanner fin = new Scanner(file);
            while(fin.hasNextLine()) {
                addItem(parseItem(fin.nextLine()));

            }
            fin.close();
        } catch (Exception e) {
            try { //if there is a problem reading the database file, try reading the backup
                File backup = new File("itemDb.bak");
                Scanner fin = new Scanner(backup);
                while(fin.hasNextLine()) {
                    MagicItem item = parseItem(fin.nextLine());
                    addItem(item);
                    ArrayList<String> traits = item.getTraits();
                    for(int i=0; i<traits.size(); i++){
                        addTrait(traits.get(i));
                    }
                }
                fin.close();
            } catch (FileNotFoundException f) {
                e.printStackTrace();
                f.printStackTrace();
            }
        }
        return itemList;
    }

    static MagicItem parseItem(String in){ //Parse a string in the format from MagicItem.toString
        if(in == null || !in.startsWith("{")){
            throw new IllegalArgumentException("Null parser input");
        }
        String[] str = in.substring(1).split(", ");
        @SuppressWarnings("deprecation")
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
        if(str[4].substring(10, str[4].length() - 1) != "") {
        	String[] traits = str[4].substring(10, str[4].length() - 1).split("; ");
        	for(int i=0; i< traits.length; i++){
        		item.addTrait(traits[i]);
        	}
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
    
    public static ObservableList<MagicItem> getItemList() {
    	if(itemList == null) {
    		itemList = FXCollections.observableArrayList();
    		//throw new NullPointerException("ObservableList in ItemHelper is null");
    	}
    	return itemList;
    }
    
}


