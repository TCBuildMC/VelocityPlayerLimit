package xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.velocitypowered.api.command.BrigadierCommand;
import net.kyori.adventure.text.Component;
import xyz.tcbuildmc.common.i18n.Translations;
import xyz.tcbuildmc.common.util.base.ObjectUtils;
import xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.VelocityPlayerLimit;
import xyz.tcbuildmc.minecraft.plugin.velocityplayerlimit.config.VelocityPlayerLimitConfig;

public class VelocityPlayerLimitCommand {
    public static BrigadierCommand register() {
        return new BrigadierCommand(BrigadierCommand.literalArgumentBuilder("velocityplayerlimit")
                .then(BrigadierCommand.literalArgumentBuilder("version")
                        .executes(context -> {
                            context.getSource().sendMessage(Component.text(Translations.getTranslation("command.version")
                                    .formatted(ObjectUtils.requiresNonNullOrElse(
                                            VelocityPlayerLimitCommand.class.getPackage().getImplementationVersion(),
                                            "0.0.0+unknown.0"))));

                            return 0;
                        }))
                .then(BrigadierCommand.literalArgumentBuilder("help")
                        .requires(source -> source.hasPermission("velocityplayerlimit.command.help"))
                        .executes(context -> {
                            context.getSource().sendMessage(Component.text(Translations.getTranslation("command.help")));

                            return 0;
                        }))
                .then(BrigadierCommand.literalArgumentBuilder("reload")
                        .requires(source -> source.hasPermission("velocityplayerlimit.command.reload"))
                        .executes(context -> {
                            context.getSource().sendMessage(Component.text(Translations.getTranslation("command.reload")));

                            VelocityPlayerLimit.getInstance().reload();
                            return 0;
                        }))
                .then(BrigadierCommand.literalArgumentBuilder("toggle")
                        .requires(source -> source.hasPermission("velocityplayerlimit.command.toggle"))
                        .executes(context -> {
                            VelocityPlayerLimitConfig config = VelocityPlayerLimit.getInstance().getConfig();
                            config.setEnable(!config.isEnable());

                            context.getSource().sendMessage(Component.text(Translations.getTranslation("command.toggle", config.isEnable())));
                            VelocityPlayerLimit.getInstance().saveConfig();
                            return 0;
                        })
                        .then(BrigadierCommand.requiredArgumentBuilder("state", BoolArgumentType.bool())
                                .requires(source -> source.hasPermission("velocityplayerlimit.command.toggle"))
                                .executes(context -> {
                                    boolean state = BoolArgumentType.getBool(context, "state");
                                    VelocityPlayerLimitConfig config = VelocityPlayerLimit.getInstance().getConfig();
                                    config.setEnable(state);

                                    context.getSource().sendMessage(Component.text(Translations.getTranslation("command.toggle", config.isEnable())));
                                    VelocityPlayerLimit.getInstance().saveConfig();
                                    return 0;
                                })))
                .build());
    }
}
