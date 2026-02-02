package io.azod.plugin.system;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import io.azod.plugin.component.ChangeAssetComponent;
import io.azod.plugin.component.NameTagComponent;
import io.azod.plugin.component.PlayAnimationComponent;
import io.azod.plugin.event.AppliedTagEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NameTagSystem extends RefChangeSystem<EntityStore, NameTagComponent> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nonnull
    @Override
    public ComponentType<EntityStore, NameTagComponent> componentType() {
        return NameTagComponent.getComponentType();
    }

    private void setNameplate(@Nonnull Ref<EntityStore> ref, @Nonnull NameTagComponent nameTagComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        commandBuffer.putComponent(ref, Nameplate.getComponentType(),new Nameplate(nameTagComponent.getTag()) );
        LOGGER.atInfo().log("Nametag set to " + nameTagComponent.getTag());
       // AppliedTagEvent.dispatch(ref, commandBuffer, nameTagComponent);
    }

    private void removeFromOverpopulationCheck(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store) {
        NPCEntity npcEntity = store.getComponent(ref, NPCEntity.getComponentType());
        if (npcEntity == null) return;

        npcEntity.setDespawnCheckRemainingSeconds(Float.MAX_VALUE);
        LOGGER.atInfo().log("Removed Overpopulation Check for NameTagged Entity");
    }

    private void setter(@Nonnull Ref<EntityStore> ref, @Nonnull NameTagComponent nameTagComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        this.setNameplate(ref, nameTagComponent, store, commandBuffer);
        this.removeFromOverpopulationCheck(ref, store);
    }

    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull NameTagComponent nameTagComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        this.setter(ref, nameTagComponent, store, commandBuffer);
    }

    @Override
    public void onComponentSet(@Nonnull Ref<EntityStore> ref, @Nullable NameTagComponent oldComponent, @Nonnull NameTagComponent newComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        this.setter(ref, newComponent, store, commandBuffer);
    }

    @Override
    public void onComponentRemoved(@Nonnull Ref<EntityStore> ref, @Nonnull NameTagComponent nameTagComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.of(NameTagComponent.getComponentType());
    }
}
