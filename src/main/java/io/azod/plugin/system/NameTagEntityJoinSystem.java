package io.azod.plugin.system;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import io.azod.plugin.component.NameTagComponent;
import io.azod.plugin.component.PlayAnimationComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NameTagEntityJoinSystem extends RefSystem<EntityStore> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    public void onEntityAdded(@Nonnull Ref<EntityStore> ref, @Nonnull AddReason addReason, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        if (addReason != AddReason.LOAD) return;

        NPCEntity npcEntity = store.getComponent(ref, NPCEntity.getComponentType());
        if (npcEntity == null) return;

        NameTagComponent nameTagComponent = store.getComponent(ref, NameTagComponent.getComponentType());
        if (nameTagComponent != null) {
            npcEntity.setDespawnCheckRemainingSeconds(Float.MAX_VALUE);
            LOGGER.atInfo().log("Removed Overpopulation Check for NameTagged Entity");
        }

        PlayAnimationComponent playAnimationComponent = store.getComponent(ref, PlayAnimationComponent.getComponentType());
        if (playAnimationComponent == null) return;

        npcEntity.playAnimation(ref, AnimationSlot.Action, playAnimationComponent.getAnimationName(), commandBuffer);
        LOGGER.atInfo().log("Playing Animation for NameTagged Entity");
    }

    @Override
    public void onEntityRemove(@Nonnull Ref<EntityStore> ref, @Nonnull RemoveReason removeReason, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {

    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Query.or(Archetype.of(NameTagComponent.getComponentType()), Archetype.of(PlayAnimationComponent.getComponentType()));
    }
}
