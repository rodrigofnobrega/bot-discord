package com.discord.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

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


            //Enter voice channel
            if (event.getMessageContent().equalsIgnoreCase("!voice")) {
                ServerVoiceChannel channel = event.getMessageAuthor()
                        .getConnectedVoiceChannel()
                        .get();



                channel.connect().thenAccept(audioConnection -> {
                    // Create a player manager
                    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
                    playerManager.registerSourceManager(new YoutubeAudioSourceManager());
                    AudioPlayer player = playerManager.createPlayer();
                    System.out.println("Create player manager\n");

                    // Create an audio source and add it to the audio connection's queue
                    AudioSource source = new LavaplayerAudioSource(api, player);
                    audioConnection.setAudioSource(source);

                    System.out.println("Create audio source\n");

                    Queue<String> musics = new LinkedList<>();

                    musics.add("https://www.youtube.com/watch?v=s6TtwR2Dbjg&pp=ygUGaGVhdmVu");
                    musics.add("https://www.youtube.com/watch?v=n4RjJKxsamQ");

                    playerManager.loadItem("https://www.youtube.com/playlist?list=PL5T6A9Q53BzwIO0anxMcW8y4DhpvYxRv-", new AudioLoadResultHandler() {
                        @Override
                        public void trackLoaded(AudioTrack track) {
                            System.out.println("Track Loaded\n");
                            player.playTrack(track);
                        }

                        @Override
                        public void playlistLoaded(AudioPlaylist playlist) {
                            System.out.println("Playlist loaded\n");
                            for (AudioTrack track : playlist.getTracks()) {
                                player.playTrack(track);
                            }
                        }

                        @Override
                        public void noMatches() {
                            // Notify the user that we've got nothing
                        }

                        @Override
                        public void loadFailed(FriendlyException throwable) {
                            // Notify the user that everything exploded
                        }
                    });

                }).exceptionally(e -> {
                    // Failed to connect to voice channel (no permissions?)
                    e.printStackTrace();
                    return null;
                });
            }



            if (event.getMessageContent().equalsIgnoreCase("/compo")) {
                TextChannel textChannel = event.getChannel();

                new MessageBuilder()
                        .setContent("Click on one of these Buttons!")
                        .addComponents(
                                ActionRow.of(Button.success("success", "Send a message"),
                                        Button.danger("danger", "Delete this message"),
                                        Button.secondary("secondary", "Remind me after 5 minutes")))
                        .send(textChannel);

            }
        });




        System.out.println("Bot is on");
    }

}