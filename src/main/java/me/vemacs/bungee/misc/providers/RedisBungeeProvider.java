package me.vemacs.bungee.misc.providers;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RedisBungeeProvider implements ServerInfoProvider {
    private static RedisBungeeAPI redisBungee = RedisBungee.getApi();

    @Override
    public int getTotalPlayerCount() {
        return redisBungee.getPlayerCount();
    }

    @Override
    public Set<String> getPlayersOnServer(String server) {
        Set<String> humanPlayersOnServer = new HashSet<>();
        for (UUID uuid : redisBungee.getPlayersOnServer(server))
            humanPlayersOnServer.add(redisBungee.getNameFromUuid(uuid));
        return humanPlayersOnServer;
    }
}
