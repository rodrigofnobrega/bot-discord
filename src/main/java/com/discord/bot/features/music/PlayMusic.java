package com.discord.bot.features.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;

public class PlayMusic {
    final DiscordApi API;
    ServerVoiceChannel voiceChannel;
    TextChannel textChannel;
    MessageCreateEvent messageCreateEvent;

    public PlayMusic(DiscordApi API, ServerVoiceChannel voiceChannel, TextChannel textChannel, MessageCreateEvent messageCreateEvent) {
        this.API = API;
        this.voiceChannel = voiceChannel;
        this.textChannel = textChannel;
        this.messageCreateEvent = messageCreateEvent;
    }

    private String convertMilissecondsToMinutes(long milisseconds) {
        long seconds = ( milisseconds / 1000 ) % 60;
        long minutes  = ( milisseconds / 60000 ) % 60;

        return String.format("%d:%d", minutes, seconds);
    }

    private EmbedBuilder createEmbed(String title, String thumbnailVideo, String youtubeLink, long timeVideo) {
        String time = convertMilissecondsToMinutes(timeVideo);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Music")
                .setDescription(title)
                .addField("Youtube link", youtubeLink)
                .addInlineField("Duration (Minutes)", time)
                .setAuthor(messageCreateEvent.getMessageAuthor().getName(), "http://google.com/", messageCreateEvent.getMessageAuthor().getAvatar())
                .setColor(Color.BLUE)
                .setFooter(messageCreateEvent.getMessageAuthor().getName(), messageCreateEvent.getMessageAuthor().getAvatar())
                .setImage(thumbnailVideo);

        return embed;
    }

    public void playMusic() {
        voiceChannel.connect().thenAccept(audioConnection -> {
            // Create a player manager
            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            playerManager.registerSourceManager(new YoutubeAudioSourceManager());
            AudioPlayer player = playerManager.createPlayer();
            System.out.println("Create player manager\n");

            // Create an audio source and add it to the audio connection's queue
            AudioSource source = new LavaplayerAudioSource(API, player);
            audioConnection.setAudioSource(source);

            System.out.println("Create audio source\n");

            String music = "";

            playerManager.loadItem(music, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    System.out.println("Track Loaded\n");

                    textChannel.sendMessage(createEmbed(track.getInfo().title, track.getInfo().artworkUrl,
                            track.getInfo().uri, track.getDuration()));

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
}
