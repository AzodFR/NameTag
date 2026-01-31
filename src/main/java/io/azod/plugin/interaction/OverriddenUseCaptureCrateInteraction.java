package io.azod.plugin.interaction;


import com.hypixel.hytale.builtin.adventure.farming.states.CoopBlock;
import com.hypixel.hytale.builtin.tagset.TagSetPlugin;
import com.hypixel.hytale.builtin.tagset.config.NPCGroup;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.function.consumer.TriConsumer;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;

import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockFace;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.npc.metadata.CapturedNPCMetadata;
import io.azod.plugin.metadata.OverriddenCapturedNPCMetadata;
import it.unimi.dsi.fastutil.Pair;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.server.npc.entities.NPCEntity;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bson.BsonDocument;

public class OverriddenUseCaptureCrateInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<OverriddenUseCaptureCrateInteraction> CODEC = BuilderCodec.builder(OverriddenUseCaptureCrateInteraction.class, OverriddenUseCaptureCrateInteraction::new, SimpleInteraction.CODEC)
            .appendInherited(
                    new KeyedCodec<String[]>(
                            "AcceptedNpcGroups", NPCGroup.CHILD_ASSET_CODEC_ARRAY),
                    (o, v) -> o.acceptedNpcGroupIds = v,
                    (o) -> o.acceptedNpcGroupIds,
                    (o, p) -> o.acceptedNpcGroupIds = p.acceptedNpcGroupIds
            )
            .addValidator(NPCGroup.VALIDATOR_CACHE.getArrayValidator())
            .add()
            .appendInherited(
                    new KeyedCodec<String>("FullIcon", Codec.STRING),
                    (o, v) -> o.fullIcon = v, (o) -> o.fullIcon,
                    (o, p) -> o.fullIcon = p.fullIcon
            )
            .add()
            .afterDecode(
                    (captureData) -> {
                        if (captureData.acceptedNpcGroupIds != null) {
                            captureData.acceptedNpcGroupIndexes = new int[captureData.acceptedNpcGroupIds.length];

                            for (int i = 0; i < captureData.acceptedNpcGroupIds.length; ++i) {
                                int assetIdx = NPCGroup.getAssetMap().getIndex(captureData.acceptedNpcGroupIds[i]);
                                captureData.acceptedNpcGroupIndexes[i] = assetIdx;
                            }
                        }
                    })
            .build();


    protected String[] acceptedNpcGroupIds;
    protected int[] acceptedNpcGroupIndexes;
    protected String fullIcon;

    protected void tick0(boolean firstRun, float time, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nonnull CooldownHandler cooldownHandler) {
        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        if (commandBuffer == null) {
            context.getState().state = InteractionState.Failed;
        } else {
            ItemStack item = context.getHeldItem();
            if (item == null) {
                context.getState().state = InteractionState.Failed;
            } else {
                Ref<EntityStore> ref = context.getEntity();
                Entity var10 = EntityUtils.getEntity(ref, commandBuffer);
                if (!(var10 instanceof LivingEntity)) {
                    context.getState().state = InteractionState.Failed;
                } else {
                    LivingEntity livingEntity = (LivingEntity) var10;
                    Inventory inventory = livingEntity.getInventory();
                    byte activeHotbarSlot = inventory.getActiveHotbarSlot();
                    ItemStack inHandItemStack = inventory.getActiveHotbarItem();
                    OverriddenCapturedNPCMetadata existingMeta = (OverriddenCapturedNPCMetadata) item.getFromMetadataOrNull("CapturedEntity", OverriddenCapturedNPCMetadata.CODEC);
                    if (existingMeta != null) {
                        super.tick0(firstRun, time, type, context, cooldownHandler);
                    } else {
                        Ref<EntityStore> targetEntity = context.getTargetEntity();
                        if (targetEntity == null) {
                            context.getState().state = InteractionState.Failed;
                        } else {
                            NPCEntity npcComponent = (NPCEntity) commandBuffer.getComponent(targetEntity, NPCEntity.getComponentType());
                            if (npcComponent == null) {
                                context.getState().state = InteractionState.Failed;
                            } else {
                                TagSetPlugin.TagSetLookup tagSetPlugin = TagSetPlugin.get(NPCGroup.class);
                                boolean tagFound = false;

                                for (int group : this.acceptedNpcGroupIndexes) {
                                    if (tagSetPlugin.tagInSet(group, npcComponent.getRoleIndex())) {
                                        tagFound = true;
                                        break;
                                    }
                                }

                                if (!tagFound) {
                                    context.getState().state = InteractionState.Failed;
                                } else {
                                    PersistentModel persistentModel = (PersistentModel) commandBuffer.getComponent(targetEntity, PersistentModel.getComponentType());
                                    if (persistentModel == null) {
                                        context.getState().state = InteractionState.Failed;
                                    } else {
                                        ModelAsset modelAsset = (ModelAsset) ModelAsset.getAssetMap().getAsset(persistentModel.getModelReference().getModelAssetId());
                                        OverriddenCapturedNPCMetadata meta = (OverriddenCapturedNPCMetadata) inHandItemStack.getFromMetadataOrDefault("CapturedEntity", OverriddenCapturedNPCMetadata.CODEC);
                                        if (modelAsset != null) {
                                            meta.setIconPath(modelAsset.getIcon());
                                        }

                                        meta.setRoleIndex(npcComponent.getRoleIndex());
                                        String npcName = NPCPlugin.get().getName(npcComponent.getRoleIndex());
                                        if (npcName != null) {
                                            meta.setNpcNameKey(npcName);
                                        }

                                        if (this.fullIcon != null) {
                                            meta.setFullItemIcon(this.fullIcon);
                                        }

                                        Nameplate nameplate = commandBuffer.getComponent(targetEntity, Nameplate.getComponentType());
                                        if  (nameplate != null) {
                                            meta.setNameTag(nameplate.getText());
                                        }

                                        ItemStack itemWithNPC = inHandItemStack.withMetadata(OverriddenCapturedNPCMetadata.KEYED_CODEC, meta);
                                        inventory.getHotbar().replaceItemStackInSlot((short) activeHotbarSlot, item, itemWithNPC);
                                        commandBuffer.removeEntity(targetEntity, RemoveReason.REMOVE);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldownHandler) {
        ItemStack item = context.getHeldItem();
        if (item == null) {
            context.getState().state = InteractionState.Failed;
        } else {
            Ref<EntityStore> ref = context.getEntity();
            Entity var11 = EntityUtils.getEntity(ref, commandBuffer);
            if (var11 instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) var11;
                Inventory inventory = livingEntity.getInventory();
                byte activeHotbarSlot = inventory.getActiveHotbarSlot();
                OverriddenCapturedNPCMetadata existingMeta = (OverriddenCapturedNPCMetadata) item.getFromMetadataOrNull("CapturedEntity", OverriddenCapturedNPCMetadata.CODEC);
                if (existingMeta == null) {
                    context.getState().state = InteractionState.Failed;
                } else {
                    BlockPosition pos = context.getTargetBlock();
                    if (pos == null) {
                        context.getState().state = InteractionState.Failed;
                    } else {
                        WorldChunk worldChunk = world.getChunk(ChunkUtil.indexChunkFromBlock(pos.x, pos.z));
                        Ref<ChunkStore> blockRef = worldChunk.getBlockComponentEntity(pos.x, pos.y, pos.z);
                        if (blockRef == null) {
                            blockRef = BlockModule.ensureBlockEntity(worldChunk, pos.x, pos.y, pos.z);
                        }

                        ItemStack noMetaItemStack = item.withMetadata((BsonDocument) null);
                        if (blockRef != null) {
                            Store<ChunkStore> chunkStore = world.getChunkStore().getStore();
                            CoopBlock coopBlockState = (CoopBlock) chunkStore.getComponent(blockRef, CoopBlock.getComponentType());
                            if (coopBlockState != null) {
                                WorldTimeResource worldTimeResource = (WorldTimeResource) commandBuffer.getResource(WorldTimeResource.getResourceType());

                                CapturedNPCMetadata originalMetadata = new CapturedNPCMetadata();

                                originalMetadata.setFullItemIcon(existingMeta.getFullItemIcon());
                                originalMetadata.setIconPath(existingMeta.getIconPath());
                                originalMetadata.setNpcNameKey(existingMeta.getNpcNameKey());
                                originalMetadata.setRoleIndex(existingMeta.getRoleIndex());

                                if (coopBlockState.tryPutResident(originalMetadata, worldTimeResource)) {
                                    world.execute(() -> coopBlockState.ensureSpawnResidentsInWorld(world, world.getEntityStore().getStore(), new Vector3d((double) pos.x, (double) pos.y, (double) pos.z), (new Vector3d()).assign(Vector3d.FORWARD)));
                                    inventory.getHotbar().replaceItemStackInSlot((short) activeHotbarSlot, item, noMetaItemStack);
                                } else {
                                    context.getState().state = InteractionState.Failed;
                                }

                                return;
                            }
                        }

                        Vector3d spawnPos = new Vector3d((double) ((float) pos.x + 0.5F), (double) pos.y, (double) ((float) pos.z + 0.5F));
                        if (context.getClientState() != null) {
                            BlockFace blockFace = BlockFace.fromProtocolFace(context.getClientState().blockFace);
                            if (blockFace != null) {
                                spawnPos.add(blockFace.getDirection());
                            }
                        }

                        NPCPlugin npcModule = NPCPlugin.get();
                        Store<EntityStore> store = context.getCommandBuffer().getStore();
                        int roleIndex = existingMeta.getRoleIndex();
                        String nameTag = existingMeta.getNameTag();
                        commandBuffer.run((_store) -> {
                                    Pair<Ref<EntityStore>, NPCEntity> pair = npcModule.spawnEntity(store, roleIndex, spawnPos, Vector3f.ZERO, (Model) null, (TriConsumer) null);
                                    if (nameTag != null && !nameTag.isEmpty()) {
                                        store.addComponent(pair.first(), Nameplate.getComponentType() , new Nameplate(nameTag));
                                    }
                                }
                        );
                        inventory.getHotbar().replaceItemStackInSlot((short) activeHotbarSlot, item, noMetaItemStack);
                    }
                }
            } else {
                context.getState().state = InteractionState.Failed;
            }
        }
    }

    protected void simulateInteractWithBlock(@Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull World world, @Nonnull Vector3i targetBlock) {
    }

}

