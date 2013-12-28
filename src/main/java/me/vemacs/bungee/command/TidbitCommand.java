package me.vemacs.bungee.command;

import com.google.common.base.Joiner;
import me.vemacs.bungee.SimpleCommands;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Set;

public class TidbitCommand extends Command {
    private String[] response;
    private static Joiner joiner = Joiner.on(" ").skipNulls();
    private static Joiner listJoiner = Joiner.on(", ").skipNulls();

    public TidbitCommand(String name, String response) {
        super(name);
        this.response = response.split("\\n");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!SimpleCommands.getBlacklist().get("tidbits").contains(
                sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getServer().getInfo().getName() : "console")) {
            String serverName = sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getServer().getInfo().getName() : null;
            for (String line : response) {
                Set<String> onCurrentServer = null;
                if (line.contains("{SERVLIST}") || line.contains("{SERVNUM}"))
                    onCurrentServer = serverName != null ? SimpleCommands.getProvider().getPlayersOnServer(serverName) : null;
                sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                        line
                                .replace("{PLAYER}", sender.getName())
                                .replace("{NUM}", Integer.toString(SimpleCommands.getProvider().getTotalPlayerCount()))
                                .replace("{SERVER}", serverName != null ? serverName : "CONSOLE")
                                .replace("{SERVLIST}", onCurrentServer != null ? listJoiner.join(onCurrentServer) : "CONSOLE")
                                .replace("{SERVNUM}", onCurrentServer != null ? Integer.toString(onCurrentServer.size()) : "1")
                )));
            }
        } else {
            if (sender instanceof ProxiedPlayer)
                ((ProxiedPlayer) sender).chat("/" + getName() + " " + joiner.join(args));
        }
    }
}
