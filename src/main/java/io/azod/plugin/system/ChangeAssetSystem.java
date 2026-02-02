package io.azod.plugin.system;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import io.azod.plugin.component.ChangeAssetComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChangeAssetSystem extends RefChangeSystem<EntityStore, ChangeAssetComponent> {

    @Nonnull
    @Override
    public ComponentType<EntityStore, ChangeAssetComponent> componentType() {
        return ChangeAssetComponent.getComponentType();
    }

    public void setAppearance(@Nonnull Ref<EntityStore> ref, @Nonnull ModelAsset asset, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        NPCEntity npcEntity = store.getComponent(ref, NPCEntity.getComponentType());
        if (npcEntity == null) return;

        npcEntity.setAppearance(ref, asset, commandBuffer);
    }

    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull ChangeAssetComponent changeAssetComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        setAppearance(ref, changeAssetComponent.getTargetAsset(), store, commandBuffer);
    }

    @Override
    public void onComponentSet(@Nonnull Ref<EntityStore> ref, @Nullable ChangeAssetComponent oldComponent, @Nonnull ChangeAssetComponent newComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        setAppearance(ref, newComponent.getTargetAsset(), store, commandBuffer);
    }

    @Override
    public void onComponentRemoved(@Nonnull Ref<EntityStore> ref, @Nonnull ChangeAssetComponent changeAssetComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
//        if (!changeAssetComponent.getRemoveOnChange()) return;

//        setAppearance(ref, changeAssetComponent.getOriginalAsset(), store, commandBuffer);
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.of(ChangeAssetComponent.getComponentType());
    }
}
