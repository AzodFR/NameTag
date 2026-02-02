package io.azod.plugin.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class NameTagComponent implements Component<EntityStore> {

    private static ComponentType<EntityStore, NameTagComponent> TYPE;

    public static void setComponentType(ComponentType<EntityStore, NameTagComponent> componentType) {
        TYPE = componentType;
    }

    public static ComponentType<EntityStore, NameTagComponent> getComponentType() {
        return TYPE;
    }

    public static final BuilderCodec<NameTagComponent> CODEC = BuilderCodec.builder(NameTagComponent.class, NameTagComponent::new)
            .append(
                    new KeyedCodec<String>("Tag", Codec.STRING),
                    (o, s) -> o.tag = s,
                    o -> o.tag
            )
            .add()
            .build();

    private String tag = "";

    protected NameTagComponent() {
    }

    public NameTagComponent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new NameTagComponent(this.tag);
    }

    @Override
    public String toString() {
        return "NameTagComponent{name=" + this.tag + '}';
    }
}
