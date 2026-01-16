package io.azod.plugin.interaction;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.azod.plugin.gui.NameTagUI;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class NameTagInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec<NameTagInteraction> CODEC = BuilderCodec.builder(NameTagInteraction.class, NameTagInteraction::new, SimpleInteraction.CODEC)
            .build();

    @Override
    protected void tick0(boolean firstRun, float time, @NonNullDecl InteractionType type, @NonNullDecl InteractionContext context, @NonNullDecl CooldownHandler cooldownHandler) {
        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        if (commandBuffer == null) {
            context.getState().state = InteractionState.Failed;
            return;
        }
        Ref<EntityStore> playerEntityRef = context.getEntity();

        LivingEntity playerEntity = (LivingEntity) EntityUtils.getEntity(playerEntityRef, commandBuffer);
        if (playerEntity == null) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        Inventory playerInventory = playerEntity.getInventory();
        if (playerInventory == null) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        Ref<EntityStore> targetEntity = context.getTargetEntity();
        if (targetEntity == null) {
            context.getState().state = InteractionState.Failed;
            return;
        }
        Player targetPlayer = commandBuffer.getComponent(targetEntity, Player.getComponentType());
        if (targetPlayer != null) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        Player player = commandBuffer.getComponent(playerEntityRef, Player.getComponentType());
        if (player == null) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        PlayerRef playerRef = commandBuffer.getComponent(playerEntityRef, PlayerRef.getComponentType());
        if  (playerRef == null) {
            context.getState().state = InteractionState.Failed;
            return;
        }

        NameTagUI ui = new NameTagUI(playerRef, CustomPageLifetime.CanDismiss);
        ui.registerEntities(targetEntity, playerInventory);
        player.getPageManager().openCustomPage(playerEntityRef, playerEntityRef.getStore(), ui);

        context.getState().state = InteractionState.Finished;
    }

    @Override
    protected void interactWithBlock(@NonNullDecl World world, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl Vector3i vector3i, @NonNullDecl CooldownHandler cooldownHandler) {

    }

    @Override
    protected void simulateInteractWithBlock(@NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl World world, @NonNullDecl Vector3i vector3i) {

    }
}
