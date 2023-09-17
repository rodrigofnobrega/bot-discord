package com.discord.bot;

import com.discord.bot.commands.VoiceChannel;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BotConfig token = new BotConfig();

        DiscordApi api = new DiscordApiBuilder()
            .setToken(token.getToken())
            .addIntents(Intent.MESSAGE_CONTENT)
            .login().join();

        VoiceChannel voiceChannel = new VoiceChannel(api);

        voiceChannel.enterVoiceChannel();

        System.out.println("Bot is on");
    }

}