package me.wisp.kirbean.interaction;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public enum Buttons {

    // pagination
    LEFT("\u25C0", ButtonStyle.PRIMARY), // returns "◀"
    STOP("\u25A0", ButtonStyle.DANGER), // returns "■"
    RIGHT("\u25B6", ButtonStyle.PRIMARY), // returns "▶"

    // supplier
    CANCEL("CANCEL", ButtonStyle.DANGER),
    REPLACE("REPLACE", ButtonStyle.PRIMARY),
    NEW("NEW", ButtonStyle.SUCCESS),

    // voting
    YES("Yes", ButtonStyle.SUCCESS),
    NO("No", ButtonStyle.DANGER);

    private final String label;
    private final ButtonStyle style;

    private enum TYPE {
        PAGINATE, SUPPLY, VOTE;
    }

    Buttons(String label, ButtonStyle style) {
        this.label = label;
        this.style = style;
    }

    private static Button createDefault(Buttons button, String id) {
        return Button.of(button.style, id + ":" + button.name(), button.label);
    }

    public static ActionRow createDefaultPagination() {
        String id = "paginate";
        return ActionRow.of(
                createDefault(Buttons.LEFT, id),
                createDefault(Buttons.STOP, id),
                createDefault(Buttons.RIGHT, id)
        );
    }

    public static ActionRow createDefaultSupplier() {
        String id = "supply";
        return ActionRow.of(
                createDefault(Buttons.CANCEL, id),
                createDefault(Buttons.REPLACE, id),
                createDefault(Buttons.NEW, id)
        );
    }

    public static ActionRow createDefaultVote() {
        String id = "vote";
        return ActionRow.of(createDefault(Buttons.YES, id), createDefault(Buttons.NO, id));
    }
}
