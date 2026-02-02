package io.azod.plugin.event;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.event.IEventDispatcher;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.azod.plugin.component.PlayAnimationComponent;

import javax.annotation.Nonnull;

public record ApplyPlayAnimation(
        @Nonnull Ref<EntityStore> entityRef,
        @Nonnull Store<EntityStore> store,
        @Nonnull PlayAnimationComponent component)
        implements IEvent<Void> {

    public static void dispatch(Ref<EntityStore> entityRef, Store<EntityStore> store, PlayAnimationComponent component) {
        IEventDispatcher<ApplyPlayAnimation, ApplyPlayAnimation> dispatcher =
                HytaleServer.get().getEventBus().dispatchFor(ApplyPlayAnimation.class);

        if (dispatcher.hasListener()) {
            dispatcher.dispatch(new ApplyPlayAnimation(entityRef, store, component));
        }
    }
}
