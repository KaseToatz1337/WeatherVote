package com.kasetoatz.weathervote.votes;

import com.kasetoatz.weathervote.WeatherVote;
import com.kasetoatz.weathervote.enums.WeatherType;
import net.minecraft.server.command.ServerCommandSource;

public class VoteResponse {
    private final ServerCommandSource source;
    private final WeatherType weather;
    private final boolean votePassed;

    public VoteResponse(ServerCommandSource source, WeatherType weather, boolean votePassed)
    {
        this.source = source;
        this.weather = weather;
        this.votePassed = votePassed;
    }

    public ServerCommandSource getSource() {
        return source;
    }

    public WeatherType getWeather() {
        return weather;
    }

    public boolean hasVotePassed() {
        return votePassed;
    }
}
