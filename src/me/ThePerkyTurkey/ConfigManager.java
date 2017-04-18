package me.ThePerkyTurkey;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
	
	private Plugin p;
	private boolean firstTime = false;
	private File configFile;
	private FileConfiguration config;

	public ConfigManager(Plugin p) {
		this.p = p;
		isFirstTime();
		if(firstTime) {
			loadDefaultConfig();
		}
		loadConfig();
	}
	
	private void isFirstTime() {
		firstTime = !p.getDataFolder().exists();
	}
	
	public void loadDefaultConfig() {
		p.saveResource("config.yml", true);
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public void loadConfig() {
		this.configFile = new File(p.getDataFolder(), "config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
	}
	
	public void saveConfig() {
		try{
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
			p.getLogger().info("Failed to save Config file");
		}
	}
}
