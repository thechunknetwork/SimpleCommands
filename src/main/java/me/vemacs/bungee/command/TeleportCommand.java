package me.vemacs.bungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TeleportCommand extends Command {
    private String server;

    public TeleportCommand(String name, String server, String... aliases) {
        super(name, null, aliases);
        this.server = server;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (ProxyServer.getInstance().getServerInfo(server) != null)
                ((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo(server));
        }
    }
}
