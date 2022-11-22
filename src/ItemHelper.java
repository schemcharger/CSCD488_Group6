import java.util.ArrayList;

public class ItemHelper {
    ArrayList<MagicItem> itemList = new ArrayList<MagicItem>();
    int sortType = 0; // 0->date created, 1->name, 2->type

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

}
