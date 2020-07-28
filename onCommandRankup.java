import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;


public class onCommandRankup implements CommandExecutor {
    private Rankup plugin;
    public RankDataConfigManager Rankdata;
    public PlayerDataManager data;

    public onCommandRankup(Rankup plugin){
        this.plugin = plugin;
        this.Rankdata = this.plugin.getRankdata();
        this.data = this.plugin.getData();
    }
    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String Label, String[] args) {
        if (Label.equalsIgnoreCase("rankup")) {

            if (Sender instanceof Player) {
                Player player = (Player) Sender;
                int size = this.Rankdata.getConfig().getConfigurationSection("Rank").getKeys(false).size();
                int NextRank = this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank") + 1;
                if(NextRank >= size){
                    player.sendMessage("[Rankup] You are already the maxium player rank!");
                    return false;
                }
                if (args.length < 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.Rankdata.getConfig().getString("Rank." +
                            this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank") + ".Rankup-Display")));
                    String Display = this.Rankdata.getConfig().getString("Rank." + NextRank + ".Name");

                    player.sendMessage("Your next available rank is: " + ChatColor.translateAlternateColorCodes('&', Display));
                    player.sendMessage("Cost: " + ChatColor.RED + "$" +  this.Rankdata.getConfig().getString("Rank." + NextRank + ".Amount"));

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.Rankdata.getConfig().getString(".Rankup-Command-Message")));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.Rankdata.getConfig().getString(".Ranks-Command-Message")));

                    if(this.Rankdata.getConfig().getString("Rank." +
                            this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank") + ".Rankup-Display-Footer") != null){

                                String myString = this.Rankdata.getConfig().getString("Rank." +
                                            this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank") + ".Rankup-Display-Footer");

                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',myString));
                    }

                    this.data.getConfig().set("players." + player.getUniqueId().toString() + ".toggle", true);
                    this.data.saveConfig();
                    return true;
                }
                else {
                    if (args[0].equalsIgnoreCase("Confirm")) {

                        if (this.data.getConfig().getBoolean("players." + player.getUniqueId().toString() + ".toggle")) {
                            this.data.getConfig().set("players." + player.getUniqueId().toString() + ".toggle", false);
                            this.data.saveConfig();

                            BigDecimal Balance = null;
                            try {
                                Balance = Economy.getMoneyExact(player.getName());
                            } catch (UserDoesNotExistException e) {
                                e.printStackTrace();
                            }

                            BigDecimal Cost;
                            NextRank = this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank") + 1;

                            String NextRank_S = String.valueOf(NextRank);
                            Cost = BigDecimal.valueOf(this.Rankdata.getConfig().getDouble("Rank." + NextRank_S + ".Amount"));

                            if (Balance.doubleValue() > Cost.doubleValue()) {
                                this.data.getConfig().set("players." + player.getUniqueId().toString() + ".rank", NextRank);
                                this.data.saveConfig();
                                this.data.reloadConfig();

                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco " + "take " + player.getName() + " " + Cost.toBigIntegerExact());
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp " + "user " + player.getName() + " promote " +
                                        this.Rankdata.getConfig().getString(".LuckPerms-Track"));
                                return true;
                            }
                            else{
                                player.sendMessage(ChatColor.RED + "You don't have enough funds to Rankup!");
                            }
                        }
                        else{
                            player.sendMessage("You must type /rankup to rankup.");
                        }
                    }
                    else{
                        player.sendMessage("[Rankup] use command /rankup confirm to rankup");
                    }
                    return true;
                }
            }
        }
        else if (Label.equalsIgnoreCase("Demote")) {
            if(args.length < 1){
                Sender.sendMessage("[Rankup] please user command /demote [username]");
            }
            else if(Bukkit.getPlayerExact(args[0]) != null) {
                Player player = Bukkit.getPlayerExact(args[0]);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp " + "user " + player.getName() + " demote " + this.Rankdata.getConfig().get(".LuckPerms-Track"));
                int Current = this.data.getConfig().getInt("players." + player.getUniqueId().toString() + ".rank");


                if(Current - 1 < 0) {
                 Sender.sendMessage("[Rankup] User is already apart of the lowest rank!");
                 return false;
                }
                Current = Current - 1;

                this.data.getConfig().set("players." + player.getUniqueId().toString() + ".rank", Current);
                this.data.saveConfig();
                player.sendMessage(ChatColor.RED + "You have been demoted to: " +
                        ChatColor.translateAlternateColorCodes('&' , this.Rankdata.getConfig().getString("Rank." + Current + ".Name")));
                return true;
            }
            else{
                Sender.sendMessage(ChatColor.RED + "[Rankup] The user you have tried to demote is not online.");
                return false;
            }
        }
        return true;
    }
}
