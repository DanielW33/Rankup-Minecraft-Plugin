import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class onCommandRanks implements CommandExecutor {
    private Rankup plugin;
    public RankDataConfigManager Rankdata;
    public PlayerDataManager data;

    public onCommandRanks(Rankup plugin){
        this.plugin = plugin;
        this.Rankdata = this.plugin.getRankdata();
        this.data = this.plugin.getData();
    }
    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String Label, String[] args) {
        if(Label.equalsIgnoreCase("Ranks")){
            if(Sender instanceof Player){
                Player player =  (Player) Sender;
                ArrayList<GUI_Item> ItemList = new ArrayList<>();

                this.Rankdata.getConfig().getConfigurationSection("Rank").getKeys(false).forEach(Rank ->{
                    GUI_Item Item = new GUI_Item();
                    List<String> Mylist;
                    String[] MyArray;
                    Item.setName(ChatColor.translateAlternateColorCodes('&', this.Rankdata.getConfig().getString("Rank." + Rank + ".Name")));

                    ArrayList<String> ItemLore = new ArrayList<>();

                    Mylist = this.Rankdata.getConfig().getStringList("Rank." + Rank + ".Abilities");
                    MyArray = new String[Mylist.size()];
                    Mylist.toArray(MyArray);
                    for(int i = 0; i < MyArray.length; i++){
                        MyArray[i] = ChatColor.translateAlternateColorCodes('&', MyArray[i]);
                    }
                    ItemLore.addAll(Arrays.asList(MyArray));
                    Item.setItemLore(ItemLore);

                    Item.setItemMaterial(Material.matchMaterial(this.Rankdata.getConfig().getString("Rank." + Rank + ".Block-Material")));

                    Item.setCost(this.Rankdata.getConfig().getInt("Rank." + Rank + ".Amount"));

                    Item.setEnchant(this.Rankdata.getConfig().getBoolean("Rank." + Rank + ".Enchanted"));
                    ItemList.add(Item);
                });

                ItemStack[] MenuContents = new ItemStack[ItemList.size()];

                for(int i = 0; i < ItemList.size(); i++){
                    MenuContents[i] = new ItemStack(ItemList.get(i).getItemMaterial());

                    ItemMeta Meta = MenuContents[i].getItemMeta();

                    if(ItemList.get(i).getEnchant()) {
                        Meta.addEnchant(Enchantment.MENDING, 0, true);
                        Meta.addItemFlags((ItemFlag.HIDE_ENCHANTS));
                    }

                    Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    Meta.setDisplayName(ItemList.get(i).getName());

                    Meta.setLore(ItemList.get(i).getItemLore());


                    MenuContents[i].setItemMeta(Meta);

                }

                Inventory Menu = Bukkit.createInventory(player, this.Rankdata.getConfig().getInt("GUI-SIZE"), ChatColor.translateAlternateColorCodes('&',this.Rankdata.getConfig().getString(".GUI-NAME")));

                Menu.setContents(MenuContents);
                player.openInventory(Menu);
                return true;

            }
            else{
                plugin.getLogger().log(Level.INFO, "[Rankup] Please view Rank GUI from ingame.");
            }
        }
        return false;
    }

}
