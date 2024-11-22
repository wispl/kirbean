package me.wisp.kirbean.interactive.vote;

/*
public class Vote implements Interactive {

    private final VotePage page;
    private static final ActionRow BUTTONS = ActionRow.of(
            Button.success("vote:YES", "Yes"),
            Button.danger("vote:NO", "No")
    );

    private final Predicate<Member> check;
    private final Runnable onSuccess;
    private final Set<Long> alreadyVoted;

    public Vote(Predicate<Member> check, VotePage page, Runnable onSuccess) {
        this.check = check;
        this.page = page;
        this.onSuccess = onSuccess;

        this.alreadyVoted = new HashSet<>();
    }

    @Override public Message start() {
        return new MessageBuilder().setEmbeds(page.renderPage()).setActionRows(BUTTONS).build();
    }

    @Override public void onEvent(ButtonInteractionEvent event) {
        if (canVote(event.getMember())) {
            event.reply("You voted already or you not not meet the requirements to vote").setEphemeral(true).queue();
            return;
        }
        alreadyVoted.add(event.getMember().getIdLong());

        switch (event.getButton().getId()) {
            case "vote:YES" -> page.addVote();
            case "vote:NO" -> page.removeVote();
        }

        event.editMessageEmbeds(page.renderPage()).queue();
        if (page.isGoalReached()) {
            onSuccess.run();
            end(event);
        }
    }

    private boolean canVote(Member member) {
        return check.test(member) && !alreadyVoted.contains(member.getIdLong());
    }

    private void end(ButtonInteractionEvent event) {
        event.deferEdit()
                .setActionRows(
                        event.getMessage()
                                .getActionRows()
                                .stream()
                                .map(ActionRow::asDisabled)
                                .toList())
                .queue();
    }
}
*/