package me.wisp.kirbean.core.ext;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the ability to create temporary event listeners.
 * This is useful when one wants to create some form of interaction
 * with the user using some Discord's components api.
 */
public class TempListeners implements EventListener {
    private static final TTLCache<Long, EventListener> cache = new TTLCache<>();

    /**
     * Main handling of events.
     * If the listener has expired, the behavior afterwards depends
     * on the type of the event:
     *
     * Component such as buttons or select menus are disabled
     * Modals are cleared and removed
     *
     * Mapping is done through an id, for components this would be
     * through the id given during creation
     * @param event The Event to handle.
     */
    @Override public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ButtonInteractionEvent buttonEvent) {
            var key = Long.parseLong(buttonEvent.getComponentId());

            EventListener listener = cache.get(key);
            // listener has expired, disable all components
            if (listener == null) {
                buttonEvent.getMessage().getComponents().replaceAll(LayoutComponent::asDisabled);
                return;
            }
            listener.onEvent(buttonEvent);
        }
    }

    /**
     * @param id The id used to lookup events
     * @param listener Handler of the event
     */
    public static void addListener(long id, EventListener listener) {
        cache.put(id, listener);
    }

    /**
     * @param id The id used to lookup events
     * @param listener Handler of the event
     * @param timeout When to expire listeners, default is 5 minutes
     */
    public static void addListener(long id, EventListener listener, long timeout) {
        cache.put(id, listener, timeout);
    }
}
