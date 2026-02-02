package io.azod.plugin;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;
import com.hypixel.hytale.server.core.asset.LoadAssetEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.azod.plugin.asset.NameTagBehaviour;
import io.azod.plugin.component.ChangeAssetComponent;
import io.azod.plugin.component.NameTagComponent;
import io.azod.plugin.component.PlayAnimationComponent;
import io.azod.plugin.event.ApplyTagEvent;
import io.azod.plugin.event.handler.OnApplyTag;
import io.azod.plugin.interaction.NameTagInteraction;
import io.azod.plugin.interaction.OverriddenUseCaptureCrateInteraction;
import io.azod.plugin.system.NameTagEntityJoinSystem;

import javax.annotation.Nonnull;


public class NameTag extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public NameTag(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        super.setup();

        LOGGER.atInfo().log("Setting up plugin " + this.getName());

        // Register custom nametag json behaviour
        HytaleAssetStore<String, NameTagBehaviour, DefaultAssetMap<String, NameTagBehaviour>> assetStore = HytaleAssetStore.builder(
                        NameTagBehaviour.class, new DefaultAssetMap<>()
                )
                .setPath("NameTag")
                .setCodec(NameTagBehaviour.CODEC)
                .setKeyFunction(NameTagBehaviour::getId)
                .build();
        this.getAssetRegistry().register(assetStore);
        // Debug to print loaded json
        this.getEventRegistry().register(LoadAssetEvent.class, this::onLoadAssets);

        // Register Components
        ComponentType<EntityStore, NameTagComponent> nameTagComponentType = this.getEntityStoreRegistry().registerComponent(
                NameTagComponent.class,
                "NameTag",
                NameTagComponent.CODEC
        );
        NameTagComponent.setComponentType(nameTagComponentType);

        ComponentType<EntityStore, ChangeAssetComponent> changeAssetComponentType = this.getEntityStoreRegistry().registerComponent(
                ChangeAssetComponent.class,
                "ChangeAsset",
                ChangeAssetComponent.CODEC
        );
        ChangeAssetComponent.setComponentType(changeAssetComponentType);

        ComponentType<EntityStore, PlayAnimationComponent> playAnimationComponentComponentType = this.getEntityStoreRegistry().registerComponent(
                PlayAnimationComponent.class,
                "PlayAnimation",
                PlayAnimationComponent.CODEC
        );
        PlayAnimationComponent.setComponentType(playAnimationComponentComponentType);

        // Register Events
        this.getEventRegistry().register(ApplyTagEvent.class, new OnApplyTag());

        // Register Systems
        this.getEntityStoreRegistry().registerSystem(new NameTagEntityJoinSystem());


        // Register Interaction CODEC
        this.getCodecRegistry(Interaction.CODEC).register("UseNameTag", NameTagInteraction.class, NameTagInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register("UseCaptureCrate", OverriddenUseCaptureCrateInteraction.class, OverriddenUseCaptureCrateInteraction.CODEC);
    }

    private void onLoadAssets(LoadAssetEvent event) {
        var store = AssetRegistry.getAssetStore(NameTagBehaviour.class);
        store.getAssetMap().getAssetMap().forEach((name, asset) -> {
            LOGGER.atInfo().log("AAAA = " + name + " // " + asset.toString());
        });
    }

    @Override
    protected void start() {
        super.start();
        LOGGER.atInfo().log("Starting plugin " + this.getName());


    }
}