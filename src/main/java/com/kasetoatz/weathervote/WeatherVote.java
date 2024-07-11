package com.kasetoatz.weathervote;

import com.kasetoatz.weathervote.enums.WeatherType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.logging.Logger;

public class WeatherVote implements ModInitializer {
    private final HashSet<ServerPlayerEntity> clearVotes = new HashSet<>();
    private final HashSet<ServerPlayerEntity> rainVotes = new HashSet<>();
    private final HashSet<ServerPlayerEntity> thunderVotes = new HashSet<>();
    private static final Logger LOGGER = Logger.getLogger("WeatherVote");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("weathervote")
                .then(CommandManager.literal("clear").executes(context -> {
                    this.update(context.getSource(), WeatherType.CLEAR);
                    return 1;
                }))
                .then(CommandManager.literal("rain").executes(context -> {
                    this.update(context.getSource(), WeatherType.RAIN);
                    return 1;
                }))
                .then(CommandManager.literal("thunder").executes(context -> {
                    this.update(context.getSource(), WeatherType.THUNDER);
                    return 1;
                }))
            ));
    }

    private void update(ServerCommandSource source, WeatherType weather)
    {
        clearVotes.remove(source.getPlayer());
        rainVotes.remove(source.getPlayer());
        thunderVotes.remove(source.getPlayer());
        switch (weather)
        {
            case CLEAR:
                clearVotes.add(source.getPlayer());
                if (clearVotes.size() >= source.getServer().getCurrentPlayerCount() / 2)
                {
                    source.getServer().sendMessage(Text.literal("Weather vote complete. Changing weather to clear."));
                    source.getWorld().setWeather(6000, 0, false, false);
                    this.resetVotes();
                    return;
                }
                source.sendFeedback(() -> Text.literal("You voted for clear weather. (" + clearVotes.size() + " / " + Math.round((float)source.getServer().getCurrentPlayerCount() / 2) + " required votes."), false);
                LOGGER.info(source.getName() + " voted for clear weather.");
                break;
            case RAIN:
                rainVotes.add(source.getPlayer());
                if (rainVotes.size() >= source.getServer().getCurrentPlayerCount() / 2)
                {
                    source.getServer().sendMessage(Text.literal("Weather vote complete. Changing weather to rain."));
                    source.getWorld().setWeather(0, 6000, true, false);
                    this.resetVotes();
                    return;
                }
                source.sendFeedback(() -> Text.literal("You voted for rain. (" + rainVotes.size() + " / " + Math.round((float)source.getServer().getCurrentPlayerCount() / 2) + " required votes."), false);
                LOGGER.info(source.getName() + " voted for rain.");
                break;
            case THUNDER:
                thunderVotes.add(source.getPlayer());
                if (thunderVotes.size() >= source.getServer().getCurrentPlayerCount() / 2)
                {
                    source.getServer().sendMessage(Text.literal("Weather vote complete. Changing weather to thunder."));
                    source.getWorld().setWeather(0, 6000, true, true);
                    this.resetVotes();
                    return;
                }
                source.sendFeedback(() -> Text.literal("You voted for thunder. (" + thunderVotes.size() + " / " + Math.round((float)source.getServer().getCurrentPlayerCount() / 2) + " required votes."), false);
                LOGGER.info(source.getName() + " voted for thunder.");
                break;
        }
    }

    private void resetVotes()
    {
        clearVotes.clear();
        rainVotes.clear();
        thunderVotes.clear();
    }
}
