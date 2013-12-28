package me.vemacs.bungee.misc.providers;

import java.util.Set;

public interface ServerInfoProvider {
    public int getTotalPlayerCount();
    public Set<String> getPlayersOnServer(String server);
}
