package io.azod.plugin.system;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.entity.AnimationUtils;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import io.azod.plugin.component.PlayAnimationComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayAnimationSystem extends RefChangeSystem<EntityStore, PlayAnimationComponent> {
    @Nonnull
    @Override
    public ComponentType<EntityStore, PlayAnimationComponent> componentType() {
        return PlayAnimationComponent.getComponentType();
    }

    private void runAnimation(@Nonnull Ref<EntityStore> ref, @Nonnull PlayAnimationComponent playAnimationComponent, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        AnimationUtils.stopAnimation(ref, AnimationSlot.Action, commandBuffer);

        NPCEntity npcEntity = commandBuffer.getComponent(ref, NPCEntity.getComponentType());
        if (npcEntity == null) return;

        npcEntity.playAnimation(ref, AnimationSlot.Action, playAnimationComponent.getAnimationName(),  commandBuffer);
        //AnimationUtils.playAnimation(ref, AnimationSlot.Action, playAnimationComponent.getAnimationName(), commandBuffer);
    }

    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull PlayAnimationComponent playAnimationComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        runAnimation(ref, playAnimationComponent, commandBuffer);
    }

    @Override
    public void onComponentSet(@Nonnull Ref<EntityStore> ref, @Nullable PlayAnimationComponent oldAnimation, @Nonnull PlayAnimationComponent newAnimation, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        runAnimation(ref, newAnimation, commandBuffer);
    }

    @Override
    public void onComponentRemoved(@Nonnull Ref<EntityStore> ref, @Nonnull PlayAnimationComponent playAnimationComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
//        if (!playAnimationComponent.getRemoveOnChange()) return;
//        NPCEntity npcEntity = commandBuffer.getComponent(ref, NPCEntity.getComponentType());
//        if (npcEntity == null) return;
//
//        npcEntity.playAnimation(ref, AnimationSlot.Action, "Idle",  commandBuffer);
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.of(PlayAnimationComponent.getComponentType());
    }
}
