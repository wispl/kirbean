package me.wisp.kirbean.polls;

public class PollPage {

    private final String author;
    private final String authorAvatar;
    private final String title;
    private final String question;

    private int yes = 0;
    private int no = 0;

    public PollPage(String author, String authorAvatar, String title, String question) {
        this.author = author;
        this.authorAvatar = authorAvatar;
        this.title = title;
        this.question = question;
    }

    public void addVote() {
        yes++;
    }

    public void removeVote() {
        no++;
    }

    public String getVotes() {
        return "```\n" + "Votes: " + (yes - no) + "\n```" + "Yes: " + yes + "\nNo: " + no;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }
}
