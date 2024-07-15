package com.kasetoatz.weathervote.votes;

import com.kasetoatz.weathervote.config.Config;
import com.kasetoatz.weathervote.enums.WeatherType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;

public class Votes {
    private static final HashSet<ServerPlayerEntity> clearVotes = new HashSet<>();
    private static final HashSet<ServerPlayerEntity> rainVotes = new HashSet<>();
    private static final HashSet<ServerPlayerEntity> thunderVotes = new HashSet<>();

    private static void removeVote(ServerPlayerEntity player)
    {
        clearVotes.remove(player);
        rainVotes.remove(player);
        thunderVotes.remove(player);
    }

    public static VoteResponse voteClear(ServerCommandSource source)
    {
        removeVote(source.getPlayer());
        clearVotes.add(source.getPlayer());
        return new VoteResponse(source, WeatherType.CLEAR, clearVotes.size() >= requiredVotes(source));
    }

    public static int clearSize()
    {
        return clearVotes.size();
    }

    public static VoteResponse voteRain(ServerCommandSource source)
    {
        removeVote(source.getPlayer());
        rainVotes.add(source.getPlayer());
        return new VoteResponse(source, WeatherType.RAIN, rainVotes.size() >= requiredVotes(source));
    }

    public static int rainSize()
    {
        return rainVotes.size();
    }

    public static VoteResponse voteThunder(ServerCommandSource source)
    {
        removeVote(source.getPlayer());
        thunderVotes.add(source.getPlayer());
        return new VoteResponse(source, WeatherType.THUNDER, thunderVotes.size() >= requiredVotes(source));
    }

    public static int thunderSize()
    {
        return thunderVotes.size();
    }

    public static void removeVotes()
    {
        clearVotes.clear();
        rainVotes.clear();
        thunderVotes.clear();
    }

    public static int requiredVotes(ServerCommandSource source)
    {
        return Math.round((float)source.getServer().getCurrentPlayerCount() / (100.f / Config.votingPercentage));
    }
}
