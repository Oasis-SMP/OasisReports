package net.charter.orion_pax.OasisReports;

import java.util.TreeSet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OasisReports extends JavaPlugin implements Listener{
	
	TreeSet<String> reports;
	
	@Override
	public void onEnable(){
		reports = new TreeSet<String>(getConfig().getKeys(false));
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable(){
		saveConfig();
	}
	
	@EventHandler
	public void onStaffJoin(PlayerJoinEvent event){
		if(event.getPlayer().hasPermission("oasisreports.staff")){
			event.getPlayer().sendMessage(ChatColor.GRAY + "There are " + ChatColor.RED + "[" + ChatColor.BLUE + reports.size() + ChatColor.RED + "]" + ChatColor.GRAY + " reports to check out!  " + ChatColor.YELLOW + "Have a nice day! :)");
		}
		if(!event.getPlayer().hasPlayedBefore()){
			event.getPlayer().sendMessage(ChatColor.BLUE + "Welcome to Oasis!  If you find a grief, you can report it by doing /report Some one griefed me, while standing near the grief of looking at it!");
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		if((event.getMessage().contains("grief") || event.getMessage().contains("griefed") || event.getMessage().contains("grif")) && !event.getPlayer().hasPermission("oasisreports.staff")){
			event.getPlayer().sendMessage(ChatColor.BLUE + "You can report a grief by doing /report Some one griefed me, while standing near the grief or looking at it!");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("links")){
			sender.sendMessage(ChatColor.GOLD + "Forums - http://oasis-smp.forumotion.com");
			sender.sendMessage(ChatColor.GOLD + "Donations - http://bit.ly/ODonate");
			sender.sendMessage(ChatColor.GOLD + "Vote 1 - http://bit.ly/oasisv");
			sender.sendMessage(ChatColor.GOLD + "Vote 2 - http://bit.ly/oasisvv");
			sender.sendMessage(ChatColor.GOLD + "Member Tutorial - http://bit.ly/OasisMember");
			sender.sendMessage(ChatColor.GOLD + "TeamSpeak IP - ts3.oasis-mc.us");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("forums")){
			sender.sendMessage(ChatColor.GOLD + "Forums - http://oasis-smp.forumotion.com");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("donate")){
			sender.sendMessage(ChatColor.GOLD + "Donations - http://bit.ly/ODonate");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("vote")){
			sender.sendMessage(ChatColor.GOLD + "Vote 1 - http://bit.ly/oasisv");
			sender.sendMessage(ChatColor.GOLD + "Vote 2 - http://bit.ly/oasisvv");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("member")){
			sender.sendMessage(ChatColor.GOLD + "Member Tutorial - http://bit.ly/OasisMember");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("teamspeak")){
			sender.sendMessage(ChatColor.GOLD + "TeamSpeak IP - ts3.oasis-mc.us");
			sender.sendMessage(ChatColor.GOLD + "Download at http://www.teamspeak.com/?page=downloads");
			return true;
		}
		if( sender instanceof ConsoleCommandSender){
			if(reports.isEmpty()){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('~',"~cNo reports to check!"));
				return true;
			} else {
				for (String count : reports) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('~',"*  ~c[~9" + count + "~c] - ~7" + getConfig().getString(count + ".report")));
				}
				return true;
			}
		}
		if (((Player) sender).hasPermission("oasisreports.staff")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					if (sender.hasPermission("oasisreports.list")) {
						if(reports.isEmpty()){
							SendMsg(((Player) sender), "~cNo reports to check!");
							return true;
						} else {
							for (String count : reports) {
								SendMsg(((Player) sender), "*  ~c[~9" + count + "~c] - ~7"
										+ getConfig().getString(count + ".report"));
							}
							return true;
						}
					}
				}

				if (args[0].equalsIgnoreCase("clearall")) {
					if (sender.hasPermission("oasisreports.clearall")) {
						for(String count: reports){
							getConfig().set(count, null);
						}
						reports.clear();
						sender.sendMessage(ChatColor.GREEN + "Reports cleared!");
						return true;
					}
				}

			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("clear")) {
					if (sender.hasPermission("oasisreports.clear")) {
						if (getConfig().getKeys(false) != null) {
							try {
								Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
								SendMsg(((Player) sender), "~4" + args[1]
										+ " is not a number!");
								return true;
							}
							getConfig().set(args[1], null);
							saveConfig();
							reports.clear();
							reports.addAll(getConfig().getKeys(false));
							sender.sendMessage(ChatColor.BLUE + "Report #" + args[1] + " cleared!  " + ChatColor.YELLOW + "Good Job! ;)");
							return true;
						} else {
							SendMsg(((Player) sender), "~cNo reports to check!");
							return true;
						}
					}
				}
				
				if(args[0].equalsIgnoreCase("check")){
					if(reports.contains(args[1])){
						World world = getServer().getWorld(getConfig().getString(args[1] + ".world"));
						int x = getConfig().getInt(args[1] + ".x");
						int y = getConfig().getInt(args[1] + ".y");
						int z = getConfig().getInt(args[1] + ".z");
						float pitch = getConfig().getInt(args[1] + ".pitch");
						float yaw = getConfig().getInt(args[1] + ".yaw");
						Location loc = new Location(world, x, y, z, yaw, pitch);
						((Player) sender).teleport(loc);
						sender.sendMessage(ChatColor.BLUE + "Teleported to the report!");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + args[1] + " is not in the reports list!");
						return true;
					}
				}
			}
		} else {
			if (reports.isEmpty()) {
				sendReport(1,((Player) sender),args);
						return true;
			} else {
				int i=1;
				for(String s: reports){
					if(i!=Integer.parseInt(s)){
						sendReport(i,((Player) sender),args);
						return true;
					}
					i++;
				}
				sendReport(i,((Player) sender),args);
				return true;
			}
		}
		return false;
	}
	
	public void sendReport(int i, Player player, String[] args){
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(player.getName() + ":");
		for(int a=0;a<args.length;a++){
			sbuilder.append(" ");
			sbuilder.append(args[a]);
		}
		getConfig().set(String.valueOf(i)+".report",sbuilder.toString());
		getConfig().set(String.valueOf(i)+".world", player.getLocation().getWorld().getName());
		getConfig().set(String.valueOf(i)+".x", player.getLocation().getBlockX());
		getConfig().set(String.valueOf(i)+".y", player.getLocation().getBlockY());
		getConfig().set(String.valueOf(i)+".z", player.getLocation().getBlockZ());
		getConfig().set(String.valueOf(i)+".pitch", player.getLocation().getPitch());
		getConfig().set(String.valueOf(i)+".yaw", player.getLocation().getYaw());
		saveConfig();
		reports.clear();
		reports.addAll(getConfig().getKeys(false));
		player.sendMessage(ChatColor.BLUE + "Report sent!  Staff will be notified when they join!");
		this.getServer().broadcast(ChatColor.BLUE + "A new report was just submited!", "oasisreports.staff");
	}
	
	public static void SendMsg(Player player, String msg){
		player.sendMessage(ChatColor.translateAlternateColorCodes('~', msg));
	}
}
