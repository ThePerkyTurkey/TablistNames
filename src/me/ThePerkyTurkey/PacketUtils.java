package me.ThePerkyTurkey;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
//import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_11_R1.PlayerConnection;

//import net.minecraft.server.v1_10_R1.IChatBaseComponent;
//import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
//import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
//import net.minecraft.server.v1_10_R1.PlayerConnection;

public class PacketUtils {

	
	public static void sendTabList(Player p, String header, String footer) {
		
		CraftPlayer craftplayer = (CraftPlayer) p;
		
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		
		IChatBaseComponent headerJSON = ChatSerializer.a("{\"text\": \"" + header + "\"}");
		IChatBaseComponent footerJSON = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		
		try {
			Field headerField = packet.getClass().getDeclaredField("a");
			headerField.setAccessible(true);
			headerField.set(packet, headerJSON);
			headerField.setAccessible(!headerField.isAccessible());
			
			Field footerField = packet.getClass().getDeclaredField("b");
			footerField.setAccessible(true);
			footerField.set(packet, footerJSON);
			footerField.setAccessible(!footerField.isAccessible());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		connection.sendPacket(packet);
	}
	
}
