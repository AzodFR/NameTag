package io.azod.plugin.event.handler;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import io.azod.plugin.asset.NameTagBehaviour;
import io.azod.plugin.asset.codec.ChangeAsset;
import io.azod.plugin.asset.codec.PlayAnimation;
import io.azod.plugin.component.ChangeAssetComponent;
import io.azod.plugin.component.NameTagComponent;
import io.azod.plugin.component.PlayAnimationComponent;
import io.azod.plugin.event.AppliedTagEvent;
import io.azod.plugin.event.ApplyChangeAssetEvent;
import io.azod.plugin.event.ApplyPlayAnimation;
import io.azod.plugin.event.ApplyTagEvent;

import java.util.function.Consumer;

public class OnApplyTag implements Consumer<ApplyTagEvent> {

private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
private static AssetStore<String, NameTagBehaviour, DefaultAssetMap<String, NameTagBehaviour>> assetStore =
        AssetRegistry.getAssetStore(NameTagBehaviour.class);

    @Override
    public void accept(ApplyTagEvent event) {
        if (assetStore == null) {
            assetStore = AssetRegistry.getAssetStore(NameTagBehaviour.class);
        }

        Ref<EntityStore> entityRef = event.entityRef();
        if (!entityRef.isValid()) return;

        NameTagComponent component = event.component();

        Store<EntityStore> store = event.store();
        store.putComponent(entityRef, NameTagComponent.getComponentType(), component);

        String tag = component.getTag();
        if (tag == null) return;

        store.putComponent(entityRef, Nameplate.getComponentType(), new Nameplate(tag));

        NPCEntity npcEntity = store.getComponent(entityRef, NPCEntity.getComponentType());
        if (npcEntity == null) return;

        npcEntity.setDespawnCheckRemainingSeconds(Float.MAX_VALUE);

        ChangeAssetComponent changeAssetComponent = store.getComponent(entityRef, ChangeAssetComponent.getComponentType());
        if (changeAssetComponent != null) {
            store.removeComponent(entityRef, ChangeAssetComponent.getComponentType());
            if (changeAssetComponent.getRemoveOnChange()) {
                npcEntity.setAppearance(entityRef, changeAssetComponent.getOriginalAsset(), store);
            }
        }

        PlayAnimationComponent playAnimationComponent = store.getComponent(entityRef, PlayAnimationComponent.getComponentType());
        if (playAnimationComponent != null) {
            store.removeComponent(entityRef, PlayAnimationComponent.getComponentType());
            if (playAnimationComponent.getRemoveOnChange()) {
                npcEntity.playAnimation(entityRef, AnimationSlot.Action, "Idle", store);
            }
        }

        assetStore.getAssetMap().getAssetMap().forEach((fileName, asset) -> {
            if (asset.compareTagName(tag)) {
                if (!asset.isInAllowedNPCRoles(npcEntity.getRoleName())) return;
                if (asset.getLogMessage() != null) handleLogMessage(asset.getLogMessage());
                if (asset.getChangeAsset() != null) handleChangeAsset(asset.getChangeAsset(), entityRef, store);
                if (asset.getPlayAnimation() != null) handlePlayAnimation(asset.getPlayAnimation(), entityRef, store);
            }
        });
    }

    private void handleLogMessage(String logMessage) {
        LOGGER.atInfo().log(logMessage);
    }

    private void handleChangeAsset(ChangeAsset changeAsset, Ref<EntityStore> entityRef, Store<EntityStore> store) {
        ModelComponent modelComponent = store.getComponent(entityRef, ModelComponent.getComponentType());
        if (modelComponent == null) return;

        ModelAsset newModel = ModelAsset.getAssetMap().getAsset(changeAsset.getTargetAssetName());
        if (newModel == null) return;

        store.putComponent(entityRef, ChangeAssetComponent.getComponentType(), new ChangeAssetComponent(
                modelComponent.getModel().getModelAssetId(),
                changeAsset.getTargetAssetName(),
                changeAsset.getRemoveOnChange())
        );
    }

    public void handlePlayAnimation(PlayAnimation playAnimation, Ref<EntityStore> entityRef, Store<EntityStore> store) {
        store.putComponent(entityRef, PlayAnimationComponent.getComponentType(), new PlayAnimationComponent(
                playAnimation.getAnimationName(),
                playAnimation.getRemoveOnChange()
        ));

    }
}
