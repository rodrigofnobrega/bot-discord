package com.discord.bot.commands;

import com.discord.bot.features.music.PlayMusic;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerVoiceChannel;

public class VoiceChannel {
    final DiscordApi API;
    public VoiceChannel(DiscordApi api) {
        this.API = api;
    }

    public void enterVoiceChannel() {
        API.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!voice")) {
                ServerVoiceChannel voiceChannel = event.getMessageAuthor()
                        .getConnectedVoiceChannel()
                        .get();


                PlayMusic playMusic = new PlayMusic(API, voiceChannel, event.getChannel(), event);
                playMusic.playMusic();
            }
        });
    }
}

