package net.ccmob.mc.plugin.mgames.base;

import net.ccmob.mc.plugin.mgames.lms.LMS;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MGamesBase extends JavaPlugin{
	
	@Override
	public void onEnable() {
		super.onEnable();
		new LMS();
		LMS.instance.load();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		LMS.instance.unload();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			if(LMS.instance.handleCommand(command.getName(), args, (Player) sender)){
				return true;
			}
		}
		return super.onCommand(sender, command, label, args);
	}
	
}
