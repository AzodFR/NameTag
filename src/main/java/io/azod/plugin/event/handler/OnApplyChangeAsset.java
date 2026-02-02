package io.azod.plugin.event.handler;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.azod.plugin.component.ChangeAssetComponent;
import io.azod.plugin.event.ApplyChangeAssetEvent;

import java.util.function.Consumer;

public class OnApplyChangeAsset implements Consumer<ApplyChangeAssetEvent> {

    @Override
    public void accept(ApplyChangeAssetEvent event) {
        Ref<EntityStore> entityRef = event.entityRef();
        if (!entityRef.isValid()) return;

        CommandBuffer<EntityStore> commandBuffer = event.commandBuffer();

        commandBuffer.putComponent(entityRef, ChangeAssetComponent.getComponentType(), event.component());
    }
}
