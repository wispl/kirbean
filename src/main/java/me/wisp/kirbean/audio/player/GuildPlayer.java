package me.wisp.kirbean.audio.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.wisp.kirbean.audio.tracks.TrackQueue;
import me.wisp.kirbean.audio.tracks.UserInfo;
import me.wisp.kirbean.utils.Time;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class GuildPlayer extends AudioEventAdapter {
    private final DefaultAudioPlayerManager manager;
    private final AudioPlayer player;
    private final TrackQueue queue;

    private final Guild guild;

    private long voiceChannelId = -1;
    private long messageChannelId = -1;


    public GuildPlayer(Guild guild, DefaultAudioPlayerManager manager, AudioPlayer player) {
        this.manager = manager;
        this.player = player;
        this.queue = new TrackQueue();
        this.guild = guild;

        guild.getAudioManager().setSendingHandler(new AudioSender(player));
        player.addListener(this);
    }

    public void play(String query, AudioLoadResultHandler handler) {
        manager.loadItemOrdered(player, query, handler);
    }

    public void reset() {
        messageChannelId = -1;
        voiceChannelId = -1;
        player.stopTrack();
        queue.clear();
    }

    public boolean isIdle() {
        return getCurrent() == null && isQueueEmpty();
    }

    public AudioTrack getCurrent() {
        return player.getPlayingTrack();
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }
    public void join(AudioChannel channel)  {
        resume();
        guild.getAudioManager().openAudioConnection(channel);
        voiceChannelId = channel.getIdLong();
    }

    public void leave() {
        if (!isInVoiceChannel()) {
            return;
        }

        pause();
        guild.getAudioManager().closeAudioConnection();
        voiceChannelId = -1;
    }

    public void setMessageChannelId(long id) {
        this.messageChannelId = id;
    }

    public boolean isInVoiceChannel() {
        return getVoiceChannel() != -1;
    }

    public long getVoiceChannel() {
        return voiceChannelId;
    }

    public int membersInVoiceChannel() {
        return (int) guild.getVoiceChannelById(getVoiceChannel())
                .getMembers()
                .stream()
                .filter(m -> !m.getUser().isBot())
                .count();
    }

    public void enqueue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.add(track);
        }
    }

    public void resume() {
        player.setPaused(false);
    }

    public void pause() {
        player.setPaused(true);
    }

    public boolean isPaused() {
        return player.isPaused();
    }

    public void nextTrack() {
        player.startTrack(queue.provideTrack(), false);
    }

    public List<String> getQueue() {
        return queue.getQueue();
    }

    public void shuffleQueue() {
        queue.shuffle();
    }

    public void unshuffleQueue() {
        queue.unShuffle();
    }

    public void toggleLoop() {
        queue.toggleLoop();
    }

    public boolean getLoop() {
        return queue.getLoop();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        guild.getTextChannelById(messageChannelId).sendMessageEmbeds(getNowPlaying()).queue();
    }

    public MessageEmbed getNowPlaying() {
        AudioTrack current = player.getPlayingTrack();

        long position = current.getPosition();
        long duration = current.getDuration();
        String time = "[" + Time.formatTime(position) + "/" + Time.formatTime(duration) + "]\n";

        EmbedBuilder eb = getTrackInfo(current);
        eb.setTitle("Now Playing...");
        eb.addField("**Progress**", time + Time.progressbar((double) position/duration), false);
        return eb.build();
    }

    public EmbedBuilder getTrackInfo(AudioTrack track) {
        AudioTrackInfo trackInfo = track.getInfo();
        return new EmbedBuilder()
                .addField("**Title:**", "```\n" + trackInfo.title+ "\n```", false)
                .addField("**Requester:**", ((UserInfo) track.getUserData()).getRequester(), true)
                .addField("**Source:**", "[Click](" + trackInfo.uri + ")", true)
                .addField("**Author:**", trackInfo.author, true)
                .setThumbnail("https://img.youtube.com/vi/" + trackInfo.identifier + "/mqdefault.jpg");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason) {
        if (reason.mayStartNext) {
            nextTrack();
        }
    }
}
