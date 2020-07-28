import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Rankup extends JavaPlugin implements Listener {
    public PlayerDataManager data;
    public RankDataConfigManager Rankdata;
    public onCommandRankup Command;
    public onCommandRanks RCommand;

    @Override
    public void onEnable() {
        this.data = new PlayerDataManager(this);
        this.Rankdata = new RankDataConfigManager(this);

        this.Rankdata.getConfig().options().copyDefaults(true);
        this.Rankdata.saveDefaultConfig();

        this.Command = new onCommandRankup(this);
        this.RCommand = new onCommandRanks(this);

        this.getCommand("RankUp").setExecutor(Command);
        this.getCommand("Demote").setExecutor(Command);
        this.getCommand("Ranks").setExecutor(RCommand);
        this.getCommand("Rank").setExecutor(new Rank(this));

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void whenFirstJoin(PlayerLoginEvent Event) {
        Player player = Event.getPlayer();
        if (!this.data.getConfig().contains("players." + player.getUniqueId().toString())) {
            int rank = 0;
            if (this.data.getConfig().contains("players." + player.getUniqueId().toString() + ".rank")) {
                rank = this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank");
            }
            this.data.getConfig().set("players." + player.getUniqueId().toString() + ".Player-Name", player.getName());
            //0 at the end is the first rank when joining.
            this.data.getConfig().set("players." + player.getUniqueId().toString() + ".rank", 0);
            this.data.getConfig().set("players." + player.getUniqueId().toString() + ".toggle", false);
            this.data.saveConfig();
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent Event) {
        if (Event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', this.Rankdata.getConfig().getString(".GUI-NAME")))) {
            Event.setCancelled(true);

        }
    }

    public void setData(PlayerDataManager PData) {
        this.data = PData;
        this.data.saveConfig();
    }

    public PlayerDataManager getData() {
        return this.data;
    }

    public void setRankdata(RankDataConfigManager PRankData) {
        this.Rankdata = PRankData;
        this.Rankdata.saveConfig();
    }

    public RankDataConfigManager getRankdata() {
        return this.Rankdata;
    }
//Getter function to return a players Rank! must receive a player and will return the position of their rank.(integer)
    public int getRankPosition(Player player) {
        int RankPosition;
        RankPosition = this.data.getConfig().getInt("players." + player.getUniqueId() + ".rank");
        return RankPosition;
    }
}