package io.azod.plugin.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class NameplatedEntitySystem extends EntityTickingSystem<EntityStore> {

    @Override
    public void tick(float dt, int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        NPCEntity npcComponent = archetypeChunk.getComponent(i, NPCEntity.getComponentType());
        if (npcComponent == null) return;
        if (npcComponent.tickDespawnCheckRemainingSeconds(dt)) {
            npcComponent.setDespawnCheckRemainingSeconds(Float.MAX_VALUE);
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(NPCEntity.getComponentType(), Nameplate.getComponentType());
    }
}
