package xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import xyz.tcbuildmc.common.config.api.ConfigApi;
import xyz.tcbuildmc.common.config.api.parser.DefaultParsers;
import xyz.tcbuildmc.common.i18n.Translations;
import xyz.tcbuildmc.minecraft.plugin.velocityhub.BuildConstants;
import xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.command.VelocityPlayerLimitCommand;
import xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.config.VelocityPlayerLimitConfig;
import xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.listener.PlayerListener;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

@Plugin(
        id = "velocityplayerlimit",
        name = "VelocityPlayerLimit",
        version = BuildConstants.VERSION
)
public class VelocityPlayerLimit {
    @Getter
    private static VelocityPlayerLimit instance;

    @Inject
    @Getter
    private Logger logger;

    @Inject
    @Getter
    private ProxyServer server;

    @Inject
    @DataDirectory
    @Getter
    private Path dataDir;

    @Getter
    private VelocityPlayerLimitConfig config;
    @Getter
    private File configFile;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        this.configFile = this.dataDir.resolve("config.toml").toFile();

        if (!this.configFile.exists()) {
            this.config = new VelocityPlayerLimitConfig();

            if (!this.configFile.getParentFile().exists()) {
                this.configFile.getParentFile().mkdirs();
            }

            this.saveConfig();
        } else {
            this.loadConfig();
        }

        instance = this;
        Translations.addLanguageSupport("zh_cn");
        Map<String, String> translations = Translations.getTranslationsFromClasspath("lang", Translations.getLocalLanguage(), "json", DefaultParsers.gson());
        Translations.setTranslations(translations);

        this.server.getCommandManager().register(VelocityPlayerLimitCommand.register());
        this.server.getEventManager().register(this, new PlayerListener());
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent e) {
        this.reload();
    }

    public void reload() {
        this.saveConfig();
        this.loadConfig();

        Map<String, String> translations = Translations.getTranslationsFromClasspath("lang", Translations.getLocalLanguage(), "json", DefaultParsers.gson());
        Translations.setTranslations(translations);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {
        this.saveConfig();
    }

    public void loadConfig() {
        this.config = ConfigApi.getInstance().read(VelocityPlayerLimitConfig.class,
                this.configFile,
                DefaultParsers.toml4j(false));
    }

    public void saveConfig() {
        ConfigApi.getInstance().write(VelocityPlayerLimitConfig.class,
                this.config,
                this.configFile,
                DefaultParsers.toml4j(false));
    }
}
