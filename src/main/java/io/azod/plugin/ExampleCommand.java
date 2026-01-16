package io.azod.plugin;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.protocol.Vector3f;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.group.EntityGroup;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.DisplayNameComponent;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PersistentModel;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

/**
 * This is an example command that will simply print the name of the plugin in chat when used.
 */
public class ExampleCommand extends CommandBase {

    private final String pluginName;
    private final String pluginVersion;

    public ExampleCommand(String pluginName, String pluginVersion) {
        super("test", "Prints a test message from the " + pluginName + " plugin.");
        this.setPermissionGroup(GameMode.Adventure); // Allows the command to be used by anyone, not just OP
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Hello from the " + pluginName + " v" + pluginVersion + " plugin!"));
        UUID playerUUID = ctx.sender().getUuid();
        PlayerRef playerRef = Universe.get().getPlayer(playerUUID);
        if (playerRef == null) return;
        UUID worldUUID = playerRef.getWorldUuid();
        if (worldUUID == null) return;
        World world = Universe.get().getWorld(worldUUID);
        if (world == null) return;

        world.execute(() -> {
            assert playerRef.getReference() != null;
            Ref<EntityStore> entityStore = TargetUtil.getTargetEntity(playerRef.getReference(), playerRef.getReference().getStore());
            assert entityStore != null;
            ModelComponent model = entityStore.getStore().getComponent(entityStore, EntityModule.get().getModelComponentType());
            if (model == null) return;


            ctx.sendMessage(Message.raw(model.getModel().getModel()));

            if (!model.getModel().getModel().contains("Horse")) return;

            entityStore.getStore().addComponent(entityStore, Nameplate.getComponentType(), new Nameplate("Cheval"));
        });

//        Store<EntityStore> store = world.getEntityStore().getStore();
//
//        world.execute(() -> {
//            Holder<EntityStore> holder = EntityStore.REGISTRY.newHolder();
//            ModelAsset modelAsset = ModelAsset.getAssetMap().getAsset("Minecart");
//            if  (modelAsset == null) return;
//            Model model = Model.createScaledModel(modelAsset, 0.01f);
//
//            TransformComponent transform = store.getComponent(playerRef.getReference(), EntityModule.get().getTransformComponentType());
//            if (transform == null) return;
//            var targetBlockPos = TargetUtil.getTargetLocation(
//                    playerRef.getReference(),
//                    5,
//                    playerRef.getReference().getStore()
//            );
//
//            assert targetBlockPos != null;
//            var targetBlock = TargetUtil.getTargetBlock(
//                    playerRef.getReference(),
//                    5,
//                    playerRef.getReference().getStore()
//            );
//            ctx.sendMessage(Message.raw("%f".formatted(targetBlock.length())));
//            DisplayNameComponent displayNameComponent = store.getComponent(playerRef.getReference(), EntityModule.get().getDisplayNameComponentType());
//            holder.addComponent(TransformComponent.getComponentType(), new TransformComponent(targetBlockPos, transform.getRotation()));
//            holder.addComponent(PersistentModel.getComponentType(), new PersistentModel(model.toReference()));
//            holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(model));
//            assert model.getBoundingBox() != null;
//            holder.addComponent(BoundingBox.getComponentType(), new BoundingBox(model.getBoundingBox()));
//            holder.addComponent(NetworkId.getComponentType(), new NetworkId(store.getExternalData().takeNextNetworkId()));
//            holder.addComponent(Nameplate.getComponentType(), new Nameplate(displayNameComponent.getDisplayName().getRawText()));
//
//            holder.addComponent(UUIDComponent.getComponentType(), new UUIDComponent(UUID.randomUUID()));
//            holder.ensureComponent(UUIDComponent.getComponentType());
//            Ref<EntityStore> entityRef = store.addEntity(holder, AddReason.SPAWN);
//            assert entityRef != null;
//            TransformComponent entityT = store.getComponent(entityRef, EntityModule.get().getTransformComponentType());
//            assert entityT != null;
//        });
    }
}