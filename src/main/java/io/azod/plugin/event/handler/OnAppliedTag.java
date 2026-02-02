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

import java.util.function.Consumer;

public class OnAppliedTag implements Consumer<AppliedTagEvent> {

    private static AssetStore<String, NameTagBehaviour, DefaultAssetMap<String, NameTagBehaviour>> assetStore =
            AssetRegistry.getAssetStore(NameTagBehaviour.class);
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    public void accept(AppliedTagEvent event) {
        if (assetStore == null) {
            assetStore = AssetRegistry.getAssetStore(NameTagBehaviour.class);
        }
        Ref<EntityStore> ref = event.entityRef();
        if (!ref.isValid()) return;

        CommandBuffer<EntityStore> commandBuffer = event.commandBuffer();

        NameTagComponent component = event.component();
        String tag = component.getTag();
        if (tag == null) return;

        NPCEntity npcEntity = commandBuffer.getComponent(ref, NPCEntity.getComponentType());
        if (npcEntity == null) return;

        ChangeAssetComponent changeAssetComponent = commandBuffer.getComponent(ref, ChangeAssetComponent.getComponentType());
        if (changeAssetComponent != null) {
            commandBuffer.removeComponent(ref, ChangeAssetComponent.getComponentType());
            if (changeAssetComponent.getRemoveOnChange()) {
                npcEntity.setAppearance(ref, changeAssetComponent.getOriginalAsset(), commandBuffer);
            }
        }

        PlayAnimationComponent playAnimationComponent = commandBuffer.getComponent(ref, PlayAnimationComponent.getComponentType());
        if (playAnimationComponent != null) {
            commandBuffer.removeComponent(ref, PlayAnimationComponent.getComponentType());
        }

        assetStore.getAssetMap().getAssetMap().forEach((fileName, asset) -> {
            if (asset.compareTagName(tag)) {
                if (!asset.isInAllowedNPCRoles(npcEntity.getRoleName())) return;
                if (asset.getLogMessage() != null) handleLogMessage(asset.getLogMessage());
                if (asset.getChangeAsset() != null) handleChangeAsset(asset.getChangeAsset(), ref, commandBuffer);
                if (asset.getPlayAnimation() != null) handlePlayAnimation(asset.getPlayAnimation(), ref, commandBuffer);
            }
        });
    }

    private void handleLogMessage(String logMessage) {
        LOGGER.atInfo().log(logMessage);
    }

    private void handleChangeAsset(ChangeAsset changeAsset, Ref<EntityStore> entityRef, CommandBuffer<EntityStore> commandBuffer) {
        ModelComponent modelComponent = commandBuffer.getComponent(entityRef, ModelComponent.getComponentType());
        if (modelComponent == null) return;

        ModelAsset newModel = ModelAsset.getAssetMap().getAsset(changeAsset.getTargetAssetName());
        if (newModel == null) return;

        ApplyChangeAssetEvent.dispatch(entityRef,
                commandBuffer, new ChangeAssetComponent(
                        modelComponent.getModel().getModelAssetId(),
                        changeAsset.getTargetAssetName(),
                        changeAsset.getRemoveOnChange()
                )
        );
    }

    public void handlePlayAnimation(PlayAnimation playAnimation, Ref<EntityStore> entityRef, CommandBuffer<EntityStore> commandBuffer) {
        ApplyPlayAnimation.dispatch(entityRef,
                commandBuffer, new PlayAnimationComponent(
                        playAnimation.getAnimationName(),
                        playAnimation.getRemoveOnChange()
                )
        );
    }
}
