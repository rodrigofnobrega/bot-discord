package com.discord.bot.utils;

public class ConvertTime {
    static public String convertMilissecondsToMinutes(long milisseconds) {
        long seconds = ( milisseconds / 1000 ) % 60;
        long minutes  = ( milisseconds / 60000 ) % 60;

        return String.format("%d:%d", minutes, seconds);
    }

    static public String convertMilissecondsToMinutes(int milisseconds) {
        int seconds = ( milisseconds / 1000 ) % 60;
        int minutes  = ( milisseconds / 60000 ) % 60;

        return String.format("%d:%d", minutes, seconds);
    }
}
