package me.vemacs.bungee.command;

import me.vemacs.bungee.SimpleCommands;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ScCommand extends Command {
    public ScCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("simplecommands.reload")) {
            if (args.length > 0 && args[0].equals("reload")) {
                SimpleCommands.load();
                sender.sendMessage(TextComponent.fromLegacyText(ChatColor.GREEN + "SimpleCommands reloaded."));
            }
            else
                sender.sendMessage(TextComponent.fromLegacyText("Usage: /sc <reload>"));
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "You don't have permission to do this."));
        }
    }
}
