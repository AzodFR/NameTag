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
        Nameplate nameplate = store.getComponent(ref, Nameplate.getComponentType());
        if (nameplate == null) {
            commandBuffer.addComponent(ref, Nameplate.getComponentType(), new Nameplate(nameTagComponent.getTag()));
        } else {
            commandBuffer.replaceComponent(ref, Nameplate.getComponentType(), new Nameplate(nameTagComponent.getTag()));
        }

        LOGGER.atInfo().log("Nametag set to " + nameTagComponent.getTag());
    }

    private void removeNameplate(@Nonnull Ref<EntityStore> ref, @Nonnull NameTagComponent nameTagComponent, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        commandBuffer.tryRemoveComponent(ref, Nameplate.getComponentType());
    }

    private void removeFromOverpopulationCheck(@Nonnull Ref<EntityStore> ref, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        NPCEntity npcEntity = commandBuffer.getComponent(ref, NPCEntity.getComponentType());
        if (npcEntity == null) return;

        npcEntity.setDespawnCheckRemainingSeconds(Float.MAX_VALUE);
        LOGGER.atInfo().log("Removed Overpopulation Check for NameTagged Entity");
    }

    private void setter(@Nonnull Ref<EntityStore> ref, @Nonnull NameTagComponent nameTagComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
       // this.setNameplate(ref, nameTagComponent, store, commandBuffer);
        this.removeFromOverpopulationCheck(ref, commandBuffer);
    }

    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull NameTagComponent nameTagComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
//        if (nameTagComponent.getTag().isEmpty()) {
//            commandBuffer.tryRemoveComponent(ref, NameTagComponent.getComponentType());
//        } else {
//            this.setter(ref, nameTagComponent, store, commandBuffer);
//        }
    }

    @Override
    public void onComponentSet(@Nonnull Ref<EntityStore> ref, @Nullable NameTagComponent oldComponent, @Nonnull NameTagComponent newComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
//        if (newComponent.getTag().isEmpty()) {
//            commandBuffer.removeComponent(ref, NameTagComponent.getComponentType());
//        } else {
//            this.setter(ref, newComponent, store, commandBuffer);
//        }
    }

    @Override
    public void onComponentRemoved(@Nonnull Ref<EntityStore> ref, @Nonnull NameTagComponent nameTagComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
//        this.removeNameplate(ref, nameTagComponent, commandBuffer);
        ChangeAssetComponent changeAssetComponent = store.getComponent(ref, ChangeAssetComponent.getComponentType());
        if (changeAssetComponent != null) {
            commandBuffer.removeComponent(ref, ChangeAssetComponent.getComponentType());
        }

        PlayAnimationComponent playAnimationComponent = store.getComponent(ref, PlayAnimationComponent.getComponentType());
        if (playAnimationComponent != null) {
            commandBuffer.removeComponent(ref, PlayAnimationComponent.getComponentType());
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.of(NameTagComponent.getComponentType());
    }
}
