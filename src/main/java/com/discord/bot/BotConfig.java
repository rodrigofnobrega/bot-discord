package com.discord.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotConfig {
    private Properties getProp() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(
                "src/main/java/com/discord/bot/config.properties");
        props.load(file);
        return props;
    }

    public String getToken() throws IOException {
        return getProp().getProperty("bot.token");
    }
}
