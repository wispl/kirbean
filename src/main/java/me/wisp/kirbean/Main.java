package me.wisp.kirbean;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) {
        System.out.println("""
                      _  ___      _                     \s
                     | |/ (_)    | |                    \s
                     | ' / _ _ __| |__   ___  __ _ _ __ \s
                     |  < | | '__| '_ \\ / _ \\/ _` | '_ \\\s
                     | . \\| | |  | |_) |  __/ (_| | | | |
                     |_|\\_\\_|_|  |_.__/ \\___|\\__,_|_| |_|
                                                        \s
                                                        \s
                    """);
        Config config = new Config("./config.properties");
        Bot bot;
        try {
            bot = new Bot(config.getToken());
        } catch (LoginException e) {
            throw new RuntimeException("Invalid token, could not login", e);
        }
        try {
            bot.run();
        } catch (InterruptedException e) {
            throw new RuntimeException("Could not run bot", e);
        }
    }
}

