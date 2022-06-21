package me.wisp.kirbean.audio.tracks;

import net.dv8tion.jda.api.entities.User;

public class UserInfo {
    private final long requesterId;
    private final String requester;

    public UserInfo(User requester) {
        this.requesterId = requester.getIdLong();
        this.requester = requester.getAsTag();
    }

    public long getRequesterId() {
        return requesterId;
    }

    public String getRequester() {
        return requester;
    }
}
