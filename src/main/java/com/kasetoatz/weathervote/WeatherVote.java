package com.kasetoatz.weathervote;

import com.kasetoatz.weathervote.commands.Commands;
import com.kasetoatz.weathervote.config.Config;
import net.fabricmc.api.ModInitializer;

public class WeatherVote implements ModInitializer {
    @Override
    public void onInitialize() {
        Config.load();
        Commands.registerCommands();
    }
}
