package me.wisp.kirbean.audio.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Queue;

public class GuildPlayer extends AudioEventAdapter implements AudioSendHandler {
    private final DefaultAudioPlayerManager manager;
    private final AudioPlayer player;
    private final TrackQueue queue;

    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    private long voiceChannelId = -1;
    private MessageChannel broadcastChannel;

    public GuildPlayer(DefaultAudioPlayerManager manager) {
        this.manager = manager;
        this.player = manager.createPlayer();
        this.queue = new TrackQueue();
        player.addListener(this);

        this.frame =  new MutableAudioFrame();
        this.buffer = ByteBuffer.allocate(1024);
        frame.setBuffer(buffer);
    }

    @Override
    public void onTrackEnd(AudioPlayer player,
                           AudioTrack track,
                           AudioTrackEndReason reason)
    {
        if (reason.mayStartNext) {
            nextTrack();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioTrack current = player.getPlayingTrack();
        String time = Duration.ofMillis(current.getDuration()).toString();
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Started Playing")
                .setDescription("```\n" + current.getInfo().title + "\n```")
                .addField("Requested by", current.getUserData().toString(), false)
                .addField("Of length", time, false)
                .build();
        broadcastChannel.sendMessageEmbeds(embed).queue();
    }

    @Override
    public boolean canProvide() {
        return player.provide(frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        ((Buffer) buffer).flip();
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    public void play(String query, AudioLoadResultHandler handler) {
        manager.loadItemOrdered(player, query, handler);
    }

    public void reset() {
        broadcastChannel = null;
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

    public void setBroadcastChannel(MessageChannel channel) {
        broadcastChannel = channel;
    }

    public boolean isInVoiceChannel() {
        return getVoiceChannel() != -1;
    }

    public long getVoiceChannel() {
        return voiceChannelId;
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

    public Queue<AudioTrack> getQueue() {
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
}
