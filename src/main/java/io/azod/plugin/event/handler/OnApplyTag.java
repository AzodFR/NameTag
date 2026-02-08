package io.azod.plugin.event.handler;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import io.azod.plugin.asset.NameTagBehavior;
import io.azod.plugin.asset.codec.ChangeAsset;
import io.azod.plugin.asset.codec.PlayAnimation;
import io.azod.plugin.component.ChangeAssetComponent;
import io.azod.plugin.component.NameTagComponent;
import io.azod.plugin.component.PlayAnimationComponent;
import io.azod.plugin.event.ApplyTagEvent;

import java.util.Arrays;
import java.util.function.Consumer;

public class OnApplyTag implements Consumer<ApplyTagEvent> {

private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
private static AssetStore<String, NameTagBehavior, DefaultAssetMap<String, NameTagBehavior>> assetStore =
        AssetRegistry.getAssetStore(NameTagBehavior.class);

    @Override
    public void accept(ApplyTagEvent event) {
        if (assetStore == null) {
            assetStore = AssetRegistry.getAssetStore(NameTagBehavior.class);
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

        LOGGER.atInfo().log("Removing despawn check from tagged entity");
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
            if (playAnimationComponent.getRemoveOnChange()) {
                npcEntity.playAnimation(entityRef, AnimationSlot.Action, "Idle", store);
            }
            store.removeComponent(entityRef, PlayAnimationComponent.getComponentType());
        }

        assetStore.getAssetMap().getAssetMap().forEach((fileName, asset) -> {
            if (asset.compareTagName(tag)) {
                if (!asset.isInAllowedNPCRoles(npcEntity.getRoleName())) return;
                if (asset.isPrefix()) removePrefix(entityRef, store);
                if (asset.getLogMessage() != null) handleLogMessage(asset.getLogMessage());
                if (asset.getChangeAsset() != null) handleChangeAsset(asset.getChangeAsset(), entityRef, store, npcEntity);
                if (asset.getPlayAnimation() != null) handlePlayAnimation(asset.getPlayAnimation(), entityRef, store, npcEntity);
            }
        });
    }

    private void removePrefix( Ref<EntityStore> entityRef, Store<EntityStore> store) {
        Nameplate nameplate = store.getComponent(entityRef, Nameplate.getComponentType());
        if (nameplate == null) return;

        String[] chunks = nameplate.getText().split(":");
        if  (chunks.length < 2) return;

        StringBuilder text = new StringBuilder();

        for (int i = 1; i < chunks.length; i++) {
            text.append(chunks[i]);
            if (i != chunks.length - 1) text.append(":");
        }

        nameplate.setText(text.toString());
        store.putComponent(entityRef, Nameplate.getComponentType(), nameplate);
    }

    private void handleLogMessage(String logMessage) {
        LOGGER.atInfo().log(logMessage);
    }

    private void handleChangeAsset(ChangeAsset changeAsset, Ref<EntityStore> entityRef, Store<EntityStore> store, NPCEntity npcEntity) {
        ModelComponent modelComponent = store.getComponent(entityRef, ModelComponent.getComponentType());
        if (modelComponent == null) return;

        ModelAsset newModel = ModelAsset.getAssetMap().getAsset(changeAsset.getTargetAssetName());
        if (newModel == null) return;

        npcEntity.setAppearance(entityRef, newModel, store);

        store.putComponent(entityRef, ChangeAssetComponent.getComponentType(), new ChangeAssetComponent(
                modelComponent.getModel().getModelAssetId(),
                changeAsset.getTargetAssetName(),
                changeAsset.getRemoveOnChange())
        );
        LOGGER.atInfo().log("Changing asset to: " + changeAsset.getTargetAssetName());
    }

    public void handlePlayAnimation(PlayAnimation playAnimation, Ref<EntityStore> entityRef, Store<EntityStore> store, NPCEntity npcEntity) {
        npcEntity.playAnimation(entityRef, AnimationSlot.Action, playAnimation.getAnimationName(), store);
        store.putComponent(entityRef, PlayAnimationComponent.getComponentType(), new PlayAnimationComponent(
                playAnimation.getAnimationName(),
                playAnimation.getRemoveOnChange()
        ));
        LOGGER.atInfo().log("Playing animation:" + playAnimation.getAnimationName());
    }
}
