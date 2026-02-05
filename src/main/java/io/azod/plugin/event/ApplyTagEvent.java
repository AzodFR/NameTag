package io.azod.plugin.event;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.event.IEventDispatcher;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.azod.plugin.component.NameTagComponent;

import javax.annotation.Nonnull;

public record ApplyTagEvent(
        @Nonnull Ref<EntityStore> entityRef,
        @Nonnull Store<EntityStore> store,
        @Nonnull NameTagComponent component)
        implements IEvent<Void> {

    public static void dispatch(Ref<EntityStore> entityRef, Store<EntityStore> store, NameTagComponent component) {
        IEventDispatcher<ApplyTagEvent, ApplyTagEvent> dispatcher =
                HytaleServer.get().getEventBus().dispatchFor(ApplyTagEvent.class);

        if (dispatcher.hasListener()) {
            dispatcher.dispatch(new ApplyTagEvent(entityRef, store, component));
        }
    }
}
