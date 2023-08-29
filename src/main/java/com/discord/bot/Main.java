package com.discord.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.interaction.*;


import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        BotConfig token = new BotConfig();

        DiscordApi api = new DiscordApiBuilder()
            .setToken(token.getToken())
            .addIntents(Intent.MESSAGE_CONTENT)
            .login().join();

        SlashCommand slashCommand = SlashCommand.with("ping", "answer with pong")
                .createGlobal(api)
                .join();

        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();

            if (slashCommandInteraction.getCommandName().equals("ping")) {
                slashCommandInteraction.createImmediateResponder()
                        .setContent("Pong")
                        .respond();
            }

        });

        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
            }

            if (event.getMessageContent().equalsIgnoreCase("/compo")) {
                TextChannel channel = event.getChannel();

                new MessageBuilder()
                        .setContent("Click on one of these Buttons!")
                        .addComponents(
                                ActionRow.of(Button.success("success", "Send a message"),
                                        Button.danger("danger", "Delete this message"),
                                        Button.secondary("secondary", "Remind me after 5 minutes")))
                        .send(channel);

            }
        });




        System.out.println("Bot is on");
    }

}