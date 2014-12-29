package me.vemacs.bungee.misc.providers;

import me.vemacs.bungee.SimpleCommands;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class ServerInfoProvider {
    protected ProxyServer bungeeCord = SimpleCommands.getPlugin().getProxy();
    private SCConnection scConnection = new SCConnection();
    private ListenerInfo listener = bungeeCord.getConfig().getListeners().iterator().next();
    private Callback<ProxyPingEvent> callback;

    private int lastCount;

    public ServerInfoProvider() {
        callback = new Callback<ProxyPingEvent>() {
            @Override
            public void done(ProxyPingEvent event, Throwable throwable) {
                lastCount = event.getResponse().getPlayers().getOnline();
            }
        };
        bungeeCord.getScheduler().schedule(SimpleCommands.getPlugin(), new Runnable() {
            @Override
            public void run() {
                ServerPing ping = new ServerPing(
                        new ServerPing.Protocol(bungeeCord.getGameVersion(), bungeeCord.getProtocolVersion()),
                        new ServerPing.Players(listener.getMaxPlayers(), bungeeCord.getOnlineCount(), null),
                        listener.getMotd(), bungeeCord.getConfig().getFaviconObject());
                bungeeCord.getPluginManager().callEvent(new ProxyPingEvent(scConnection,
                        ping, callback));
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public int getTotalPlayerCount() {
        return lastCount;
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
            return listener;
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
        public boolean isLegacy() {
            return false;
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
