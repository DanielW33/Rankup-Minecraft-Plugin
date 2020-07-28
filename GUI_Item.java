import org.bukkit.Material;

import java.util.ArrayList;

public class GUI_Item {
    private String Name;
    private Material ItemMaterial;
    private ArrayList<String> ItemLore;
    private int Cost;
    private boolean Enchant;
    public GUI_Item(){
        Name = "Big Chungus";
        ItemMaterial = Material.DIAMOND_BLOCK;
        Cost = 1000000000;
        Enchant = false;
    }
    /////////////////////////////////////
    //  Getters
    //
    public String getName(){
        return Name;
    }
    public Material getItemMaterial(){
        return ItemMaterial;
    }
    public ArrayList<String> getItemLore(){
        return ItemLore;
    }
    public int getCost(){
        return Cost;
    }
    public boolean getEnchant(){
        return Enchant;
    }
    ///////////////////////////////////
    //  Setters
    //
    public void setName(String C_Name){
        Name = C_Name;
    }
    public void setItemMaterial(Material C_Material){
        ItemMaterial = C_Material;
    }
    public void setItemLore(ArrayList<String> Lore){
        ItemLore = Lore;
    }
    public void setCost(int Price){
        Cost = Price;
    }
    public void setEnchant(boolean TF){
        Enchant = TF;
    }
}
