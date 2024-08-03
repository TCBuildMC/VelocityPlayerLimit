package xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import xyz.tcbuildmc.common.i18n.Translations;
import xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.VelocityPlayerLimit;
import xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.config.VelocityPlayerLimitConfig;

public class PlayerListener {
    @Subscribe
    public void onPlayerLogin(PostLoginEvent e) {
        ProxyServer proxyServer = VelocityPlayerLimit.getInstance().getServer();
        VelocityPlayerLimitConfig config = VelocityPlayerLimit.getInstance().getConfig();

        if (config.isEnable()) {
            int limit = config.getLimit();

            if (limit == -1) {
                if (proxyServer.getPlayerCount() >= proxyServer.getConfiguration().getShowMaxPlayers()) {
                    e.getPlayer().disconnect(Component.text(Translations.getTranslation("server.full")));
                }
            }

            if (limit > 0) {
                if (limit < proxyServer.getConfiguration().getShowMaxPlayers()) {
                    if (proxyServer.getPlayerCount() >= limit) {
                        e.getPlayer().disconnect(Component.text(Translations.getTranslation("server.full")));
                    }

                } else {
                    if (proxyServer.getPlayerCount() >= proxyServer.getConfiguration().getShowMaxPlayers()) {
                        e.getPlayer().disconnect(Component.text(Translations.getTranslation("server.full")));
                    }
                }
            }
        }
    }
}
