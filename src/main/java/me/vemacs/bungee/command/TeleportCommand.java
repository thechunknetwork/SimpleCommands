package me.vemacs.bungee.command;

import com.google.common.base.Joiner;
import me.vemacs.bungee.SimpleCommands;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TeleportCommand extends Command {
    private String server;
    private static Joiner joiner = Joiner.on(" ").skipNulls();

    public TeleportCommand(String name, String server) {
        super(name);
        this.server = server;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!SimpleCommands.getBlacklist().get("teleports").contains(
                sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getServer().getInfo().getName().toLowerCase() : "console")) {
            if (sender instanceof ProxiedPlayer && ProxyServer.getInstance().getServerInfo(server) != null)
                ((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo(server));
        } else if (sender instanceof ProxiedPlayer) {
            ((ProxiedPlayer) sender).chat("/" + getName() + " " + joiner.join(args));
        }
    }
}
