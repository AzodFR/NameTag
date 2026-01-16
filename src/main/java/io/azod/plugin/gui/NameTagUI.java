package io.azod.plugin.gui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class NameTagUI extends InteractiveCustomUIPage<NameTagUI.Data> {

    public static class Data {
        public static final BuilderCodec<Data> CODEC = BuilderCodec.builder(Data.class, Data::new)
                .append(new KeyedCodec<>("@InputField", Codec.STRING), (d, s) -> d.value = s, d -> d.value).add()
                .append(new KeyedCodec<>("Button", Codec.STRING), (d, s) -> d.button = s, d -> d.button).add()
                .build();

        private String value;
        private String button;

    }
    private String chosenTag;
    private Ref<EntityStore> targetEntity;
    private Inventory playerInventory;

    public NameTagUI(@Nonnull PlayerRef playerRef, @Nonnull CustomPageLifetime lifetime) {
        super(playerRef, lifetime, Data.CODEC);
        this.chosenTag = "";
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("NameTagInput.ui");
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.ValueChanged, "#InputField", EventData.of("@InputField", "#InputField.Value"), false);
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#ValidInput", EventData.of("Button", "Confirm"), false);
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, Data data) {
        super.handleDataEvent(ref, store, data);

        if (data.value != null) {
            this.chosenTag = data.value;
        }
        if (data.button != null && data.button.equals("Confirm")) {
            if (!this.chosenTag.isEmpty()) {
                apply(store);
                this.close();
            }
        }
        sendUpdate();
    }

    public void apply(@Nonnull Store<EntityStore> store){
        if (this.targetEntity == null) {
            return;
        }
        Nameplate nameplateComponent = store.getComponent(targetEntity, Nameplate.getComponentType());
        if (nameplateComponent == null) {
            store.addComponent(targetEntity, Nameplate.getComponentType(), new Nameplate(this.chosenTag));
        } else {
            nameplateComponent.setText(this.chosenTag);
        }
        byte activeHotbarSlot = playerInventory.getActiveHotbarSlot();
        this.playerInventory.getHotbar().removeItemStackFromSlot(activeHotbarSlot);
    }

    public void registerEntities( Ref<EntityStore> targetEntity, Inventory playerInventory ){
        this.targetEntity = targetEntity;
        this.playerInventory = playerInventory;
    }
}
