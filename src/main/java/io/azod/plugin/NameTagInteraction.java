package io.azod.plugin;


import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class NameTagInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<NameTagInteraction> CODEC = BuilderCodec.builder(NameTagInteraction.class, NameTagInteraction::new, SimpleInteraction.CODEC)
            .append(new KeyedCodec<>("AcceptedNpcGroups", Codec.STRING_ARRAY), (state, o) -> state.acceptedNpcGroup = o, state -> state.acceptedNpcGroup)
            .add()
            .build();

    protected String[] acceptedNpcGroup;

    @Override
    protected void tick0(boolean firstRun, float time, @NonNullDecl InteractionType type, @NonNullDecl InteractionContext context, @NonNullDecl CooldownHandler cooldownHandler) {
        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        if (commandBuffer == null) {
            context.getState().state = InteractionState.Failed;
        } else {
            ItemStack item = context.getHeldItem();
            if (item == null) {
                context.getState().state = InteractionState.Failed;
            } else {
                Ref<EntityStore> playerRef = context.getEntity();
                LivingEntity playerEntity = (LivingEntity) EntityUtils.getEntity(playerRef, commandBuffer);
                assert playerEntity != null;
                Inventory playerInventory = playerEntity.getInventory();
                byte activeHotbarSlot = playerInventory.getActiveHotbarSlot();
                ItemStack inHandItemStack = playerInventory.getActiveHotbarItem();
                NameTagMetadata existingMeta = item.getFromMetadataOrNull(NameTagMetadata.KEYED_CODEC.getKey(), NameTagMetadata.CODEC);
                if (existingMeta == null) {
                    NameTagMetadata defaultMeta = item.getFromMetadataOrDefault(NameTagMetadata.KEYED_CODEC.getKey(), NameTagMetadata.CODEC);
                    defaultMeta.setNameplateValue("DEFAULT_VALUE");
                    assert inHandItemStack != null;
                    ItemStack newNameTag = inHandItemStack.withMetadata(NameTagMetadata.KEYED_CODEC, defaultMeta);
                    playerInventory.getHotbar().replaceItemStackInSlot(activeHotbarSlot, item, newNameTag);
                    existingMeta = defaultMeta;
                }
                ItemStack nameTag = playerInventory.getActiveHotbarItem();
                Ref<EntityStore> targetEntity = context.getTargetEntity();
                if (targetEntity == null) {
                    context.getState().state = InteractionState.Failed;
                } else {
                    NPCEntity npc = commandBuffer.getComponent(targetEntity, NPCEntity.getComponentType());
                    if (npc == null) {
                        context.getState().state = InteractionState.Failed;
                    } else {

                        Nameplate nameplateComponent = commandBuffer.getComponent(targetEntity, Nameplate.getComponentType());
                        if (nameplateComponent == null) {
                            commandBuffer.addComponent(targetEntity, Nameplate.getComponentType(), new Nameplate(existingMeta.getNameplateValue()));
                        } else {
                            nameplateComponent.setText(existingMeta.getNameplateValue());
                        }
                        playerInventory.getHotbar().removeItemStackFromSlot(activeHotbarSlot);
                        context.getState().state = InteractionState.Finished;

                    }
                }
            }

        }
    }

    @Override
    protected void interactWithBlock(@NonNullDecl World world, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl Vector3i vector3i, @NonNullDecl CooldownHandler cooldownHandler) {

    }

    @Override
    protected void simulateInteractWithBlock(@NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl World world, @NonNullDecl Vector3i vector3i) {

    }
}
