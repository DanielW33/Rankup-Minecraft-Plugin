import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rank implements CommandExecutor {
    private PlayerDataManager PlayerData;
    private RankDataConfigManager RankData;
    private Rankup Plugin;

    public Rank(Rankup plugin){
        Plugin = plugin;
        PlayerData = Plugin.getData();
        RankData = Plugin.getRankdata();
    }

    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String label, String[] args){
        if(label.equalsIgnoreCase("Rank")){
            if(args.length < 1){
                if(Sender instanceof Player){
                    Player player = (Player) Sender;
                    int Rank = this.PlayerData.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank");
                    Sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"You are rank " + this.RankData.getConfig().getString("Rank." + Rank + ".Name") ));
                }
                else{
                    Sender.sendMessage("Improper syntax from console: /rank [playername]");
                }
            }
            else{
                if(Bukkit.getPlayer(args[0]) != null){
                    Player player = Bukkit.getPlayer(args[0]);
                    int Rank = this.PlayerData.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank");
                    Sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7"+player.getName() + " &7is rank " + this.RankData.getConfig().getString("Rank." + Rank + ".Name") ));
                }
                else{
                    this.PlayerData.getConfig().getConfigurationSection("players").getKeys(false).forEach(id ->{
                        if(args[0].equalsIgnoreCase(this.PlayerData.getConfig().getString("players." + id + ".Player-Name"))){
                            int Rank = this.PlayerData.getConfig().getInt("players." + id + ".rank");
                            Sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7"+ args[0] + " &7is rank " + this.RankData.getConfig().getString("Rank." + Rank + ".Name") ));
                            Sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + args[0] + " is offline."));
                            args[0] = "!";
                            return;
                        }
                    });
                    if(!args[0].equalsIgnoreCase("!")){
                      Sender.sendMessage("Invalid Username.");
                    }
                }
            }
        }
        return false;
    }
}
