package io.azod.plugin.event;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.event.IEventDispatcher;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.azod.plugin.component.ChangeAssetComponent;

import javax.annotation.Nonnull;

public record ApplyChangeAssetEvent(
        @Nonnull Ref<EntityStore> entityRef,
        @Nonnull CommandBuffer<EntityStore> commandBuffer,
        @Nonnull ChangeAssetComponent component)
        implements IEvent<Void> {

    public static void dispatch(Ref<EntityStore> entityRef, CommandBuffer<EntityStore> commandBuffer, ChangeAssetComponent component) {
        IEventDispatcher<ApplyChangeAssetEvent, ApplyChangeAssetEvent> dispatcher =
                HytaleServer.get().getEventBus().dispatchFor(ApplyChangeAssetEvent.class);

        if (dispatcher.hasListener()) {
            dispatcher.dispatch(new ApplyChangeAssetEvent(entityRef, commandBuffer, component));
        }
    }
}
