package com.kasetoatz.weathervote.weather;

import com.kasetoatz.weathervote.config.Config;
import com.kasetoatz.weathervote.enums.WeatherType;
import com.kasetoatz.weathervote.votes.VoteResponse;
import com.kasetoatz.weathervote.votes.Votes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Weather {
    public static void update(VoteResponse vote)
    {
        if (!Config.enabled)
        {
            vote.getSource().sendFeedback(() -> Text.of(Config.disabledMessage), false);
        }
        else if (vote.hasVotePassed())
        {
            Votes.removeVotes();
            setWeather(vote.getSource(), vote.getWeather());
        }
        else
        {
            incompleteVote(vote.getSource(), vote.getWeather());
        }
    }

    private static void setWeather(ServerCommandSource source, WeatherType weather)
    {
        switch (weather)
        {
            case CLEAR:
                broadcastVote(source, "clear", -1);
                source.getWorld().setWeather(6000, 0, false, false);
                break;
            case RAIN:
                broadcastVote(source, "rain", -1);
                source.getWorld().setWeather(0, 6000, true, false);
                break;
            case THUNDER:
                broadcastVote(source, "thunder", -1);
                source.getWorld().setWeather(0, 6000, true, true);
                break;
        }
    }

    private static void incompleteVote(ServerCommandSource source, WeatherType weather)
    {
        switch (weather)
        {
            case CLEAR:
                broadcastVote(source, "clear weather", Votes.clearSize());
                break;
            case RAIN:
                broadcastVote(source, "rain", Votes.rainSize());
                break;
            case THUNDER:
                broadcastVote(source, "thunder", Votes.thunderSize());
                break;
        }
    }

    private static void broadcastVote(ServerCommandSource source, String weather, int currentVotes)
    {
        if (source.getPlayer() != null)
        {
            for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList())
            {
                String message = (currentVotes == -1) ? Config.completedVoteMessage : Config.voteMessage;
                message = message.replace("$name", source.getPlayer().getName().getString());
                message = message.replace("$weather", weather);
                message = message.replace("$current", String.valueOf(currentVotes));
                message = message.replace("$required", String.valueOf(Votes.requiredVotes(source)));
                player.sendMessage(Text.of(message));
            }
        }
    }
}
