package me.ThePerkyTurkey;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class TablistNames extends JavaPlugin {
	
	public static Logger logger = Logger.getLogger("Minecraft");
	public static List<Player> onlinePlayers = new ArrayList<>();
	private ConfigManager cm;
	private Scoreboard s;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new TabListener(), this);
		s = Bukkit.getScoreboardManager().getMainScoreboard();
		for(Player p : getServer().getOnlinePlayers()) onlinePlayers.add(p);
		logger.info("TablistNames has started!");
		this.cm = new ConfigManager(this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {

			@Override
			public void run() {
				updateNames();
			}
			
		}, 20l, 20l);
		updateNames();
	}
	
	public void updateNames() {
		for(Player p : onlinePlayers) {
			PermissionUser peu = PermissionsEx.getUser(p);
			
			List<String> group = peu.getParentIdentifiers();
			String rank = group.get(0);
			
			String prefix = cm.getConfig().getString(rank + ".prefix");
			
			if(prefix == null) {
				p.setPlayerListName(p.getName());
				String header = cm.getConfig().getString("header");
				String finalHeader;
				if(header == null) {
					finalHeader = "";
				} else finalHeader = ChatColor.translateAlternateColorCodes('&', header);
				
				String footer = cm.getConfig().getString("footer");
				String finalFooter;
				if(footer == null) {
					finalFooter = "";
				} else finalFooter = ChatColor.translateAlternateColorCodes('&', footer);
				
				PacketUtils.sendTabList(p, finalHeader, finalFooter);
				
				registerNameTag(p);
				
				p.setDisplayName(p.getName());
				p.setCustomName(p.getName());
				return;
			}
			
			String finalName = ChatColor.translateAlternateColorCodes('&', prefix) + p.getName();
			p.setPlayerListName(finalName);
			
			String header = cm.getConfig().getString("header");
			String finalHeader;
			if(header == null) {
				finalHeader = "";
			} else finalHeader = ChatColor.translateAlternateColorCodes('&', header);
			
			String footer = cm.getConfig().getString("footer");
			String finalFooter;
			if(footer == null) {
				finalFooter = "";
			} else finalFooter = ChatColor.translateAlternateColorCodes('&', footer);
			
			PacketUtils.sendTabList(p, finalHeader, finalFooter);
			
			registerNameTag(p);
			
			p.setDisplayName(p.getName());
			p.setCustomName(p.getName());
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("updatelist")) {
			updateNames();
			sender.sendMessage("Tablist Updated!");
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void registerNameTag(Player p) {
		
		PermissionUser pu = PermissionsEx.getUser(p);
		
		List<String> rank = pu.getParentIdentifiers();
		String group = rank.get(0);
		String prefix = cm.getConfig().getString(group + ".prefix");
		if(prefix == null) {
			return;
		}
		
		String finalprefix = ChatColor.translateAlternateColorCodes('&', prefix);
		
		if(s.getTeam(group) != null) {
			s.getTeam(group).unregister();
		}
		Team t = s.registerNewTeam(group);
		t.setPrefix(finalprefix + ""); 
		
		t.addPlayer(p);
	}
	


}
