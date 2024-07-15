package com.kasetoatz.weathervote.commands;

import com.kasetoatz.weathervote.votes.Votes;
import com.kasetoatz.weathervote.weather.Weather;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

public class Commands {
    public static void registerCommands()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("weathervote")
                .then(CommandManager.literal("clear").executes(context -> {
                    Weather.update(Votes.voteClear(context.getSource()));
                    return 1;
                }))
                .then(CommandManager.literal("rain").executes(context -> {
                    Weather.update(Votes.voteRain(context.getSource()));
                    return 1;
                }))
                .then(CommandManager.literal("thunder").executes(context -> {
                    Weather.update(Votes.voteThunder(context.getSource()));
                    return 1;
                }))
        ));
    }
}
