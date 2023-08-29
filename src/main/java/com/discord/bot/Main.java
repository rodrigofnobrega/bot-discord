package com.discord.bot;

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

        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
            }
        });

    }
}