package me.wisp.kirbean.messages;

import me.wisp.kirbean.messages.responses.Compliments;
import me.wisp.kirbean.messages.responses.Insults;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener implements EventListener {

    private static final Insults INSULTS = new Insults();
    private static final Compliments COMPLIMENTS = new Compliments();

    // I am hungry -> Hello hungry, I am dad
    private static final Pattern DAD_JOKE = Pattern.compile("\\b(i am|i m|iam|im|i'm|i’m)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern SHUT_UP = Pattern.compile("stfu|shut up|be quiet|silence", Pattern.CASE_INSENSITIVE);
    // private static final Pattern OUR = Pattern.compile("our", Pattern.CASE_INSENSITIVE);
    private static final Pattern INSULTING = Pattern.compile("\\b(dumbass|stupid|fucking|idiot|useless|)+\\b(bot|kirbean)", Pattern.CASE_INSENSITIVE);

    @Override public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent messageEvent) {
            if (messageEvent.getMember().getUser().isBot()) {
                return;
            }

            Message message = messageEvent.getMessage();
            String content = message.getContentDisplay();

            handleDadJoke(content, message);

//            if (content.toLowerCase().contains("my")) {
//                message.reply(OUR.matcher(content).replaceAll("*OUR*")).queue();
//                return;
            if (SHUT_UP.matcher(content).find()) {
                message.reply("Freedom of speech bozo").queue();
                return;
            } else if (INSULTING.matcher(content).find()) {
                message.reply("Feck you too").queue();
                return;
            }

            handleInsult(message);
            handleCompliment(message);
            handleMock(message, content);
        }
    }

    private void handleInsult(Message message) {
        int random = ThreadLocalRandom.current().nextInt(0, 100);
        if (message.getAuthor().getIdLong() == 272120940882558976L && random < 10) {
            message.reply(INSULTS.getInsult()).queue();
        } else if (random < 1) {
            message.reply(INSULTS.getInsult()).queue();
        }
    }

    private void handleCompliment(Message message) {
        if (message.getAuthor().getIdLong() != 272120940882558976L && ThreadLocalRandom.current().nextInt(0, 100) < 2) {
            message.reply(COMPLIMENTS.getCompliment()).queue();
        }
    }

    private void handleMock(Message message, String content) {
       if (ThreadLocalRandom.current().nextInt(0, 100) < 1) {
           message.reply("```\n" + content + "\n```\nHahaha what an idiot").queue();
       }
    }

    private void handleDadJoke(String content, Message message) {
        Matcher matcher = DAD_JOKE.matcher(content);
        if (matcher.find()) {
            if (matcher.results().findAny().isPresent()) {
                message.reply("I suppose you thought that was terribly clever, you many fool with many names.").queue();
                return;
            }

            int end = matcher.end();
            message.reply("Hello,*" + content.substring(end) + "*, I am the great Kirbean, how are you?").queue();
        }
    }
}