package me.wisp.kirbean.interaction.voting;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class VotePage {
    
    private final EmbedBuilder builder = new EmbedBuilder();
    private final int goal;
    private int yes = 0;
    private int no = 0;

    public VotePage(String title, String message, int goal) {
        builder.setTitle(title)
                .setDescription(message + "\nNeed " + goal + " votes to succeed");

        this.goal = goal;
    }

    public void addVote() {
        yes++;
    }

    public void removeVote() {
        no++;
    }

    public boolean isGoalReached() {
        return getVotes() >= goal;
    }

    public int getVotes() {
        return yes - no;
    }

    public MessageEmbed renderPage() {
        return builder
                .setFooter("Current vote:\nyes: " + yes + "\nno: " + no + "\ntotal:" + getVotes())
                .build();
    }
}
