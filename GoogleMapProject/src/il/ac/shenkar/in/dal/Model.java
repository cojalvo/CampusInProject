package il.ac.shenkar.in.dal;

import java.util.HashMap;
import java.util.LinkedList;

import il.ac.shenkar.cadan.R;
import il.ac.shenkar.common.*;

public class Model {

    public static HashMap<Integer, ListItem> listItems;

    public static void LoadModel() 
    {
    	listItems=new HashMap<Integer, ListItem>();
    	listItems.put(1,new ListItem(1,R.drawable.message_ico ,"cadan", "ojalvo"));
    	listItems.put(2, new ListItem(1, R.drawable.message_ico ,"moshe", "dddd"));
    }

    public static ListItem GetbyId(int id){
    	return listItems.get(id);
    }
    public static String[] getIds()
    {
    	LinkedList<String> keys=new LinkedList<String>();
    	for (Integer key : listItems.keySet()) {
			keys.add(key.toString());
		}
    	return  keys.toArray(new String[keys.size()]);
    	
    }
    public static void removeIten(int id)
    {
    	listItems.remove(id);
    }
    public static void addItem(int key, ListItem val)
    {
    	listItems.put(key, val);
    }
}