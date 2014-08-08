package me.vemacs.bungee.misc.providers;

import me.vemacs.bungee.SimpleCommands;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.UUID;

public abstract class ServerInfoProvider {
    protected ProxyServer bungeeCord = SimpleCommands.getPlugin().getProxy();
    private SCConnection scConnection = new SCConnection();

    public int getTotalPlayerCount() {
        try {
            ServerPing ping = new ServerPing();
            return bungeeCord.getPluginManager().callEvent(new ProxyPingEvent(scConnection,
                    ping)).getResponse().getPlayers().getOnline();
        } catch (Exception e) {
            return bungeeCord.getOnlineCount();
        }
    }

    public abstract Set<String> getPlayersOnServer(String server);

    private class SCConnection implements PendingConnection {
        private String name = "SCConnection";
        private UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 55555);

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getVersion() {
            return bungeeCord.getProtocolVersion();
        }

        @Override
        public InetSocketAddress getVirtualHost() {
            return addr;
        }

        @Override
        public ListenerInfo getListener() {
            return bungeeCord.getConfig().getListeners().iterator().next();
        }

        @Override
        public String getUUID() {
            return uuid.toString();
        }

        @Override
        public UUID getUniqueId() {
            return uuid;
        }

        @Override
        public boolean isOnlineMode() {
            return bungeeCord.getConfig().isOnlineMode();
        }

        @Override
        public void setOnlineMode(boolean b) {
        }

        @Override
        public InetSocketAddress getAddress() {
            return addr;
        }

        @Override
        public void disconnect(String s) {
        }

        @Override
        public void disconnect(BaseComponent... baseComponents) {
        }

        @Override
        public void disconnect(BaseComponent baseComponent) {
        }

        @Override
        public Unsafe unsafe() {
            return null;
        }
    }
}
