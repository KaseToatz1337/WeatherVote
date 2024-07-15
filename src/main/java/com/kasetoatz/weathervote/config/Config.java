package com.kasetoatz.weathervote.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    public static boolean enabled = true;
    public static int votingPercentage = 50;
    public static String completedVoteMessage = "§2[WeatherVote]§r Voting passed. Changing weather to $weather";
    public static String voteMessage = "§2[WeatherVote]§r $name voted for $weather. ($current/$required) required votes.";
    public static String disabledMessage = "§cVoting is currently disabled.";

    private static final File config = new File(MinecraftClient.getInstance().runDirectory, "config/weathervote.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void load()
    {
        if (!config.exists())
        {
            save();
            return;
        }
        try (FileReader reader = new FileReader(config))
        {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            if (json.has("enabled"))
            {
                enabled = json.get("enabled").getAsBoolean();
            }
            if (json.has("votingPercentage"))
            {
                votingPercentage = json.get("votingPercentage").getAsInt();
            }
            if (json.has("completedVoteMessage"))
            {
                completedVoteMessage = json.get("completedVoteMessage").getAsString();
            }
            if (json.has("voteMessage"))
            {
                voteMessage = json.get("voteMessage").getAsString();
            }
            if (json.has("disabledMessage"))
            {
                disabledMessage = json.get("disabledMessage").getAsString();
            }
            save();
        }
        catch (IOException exc)
        {
            throw new CrashException(CrashReport.create(exc, "Loading config file."));
        }
    }

    public static void save()
    {
        JsonObject json = new JsonObject();
        json.addProperty("enabled", enabled);
        json.addProperty("votingPercentage", votingPercentage);
        json.addProperty("completedVoteMessage", completedVoteMessage);
        json.addProperty("voteMessage", voteMessage);
        json.addProperty("disabledMessage", disabledMessage);
        try (FileWriter writer = new FileWriter(config))
        {
            gson.toJson(json, writer);
        }
        catch (IOException exc)
        {
            throw new CrashException(CrashReport.create(exc, "Saving config file."));
        }
    }
}
