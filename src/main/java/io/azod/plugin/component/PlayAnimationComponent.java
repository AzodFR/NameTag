package io.azod.plugin.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class PlayAnimationComponent implements Component<EntityStore> {

    private static ComponentType<EntityStore, PlayAnimationComponent> TYPE;

    public static void setComponentType(ComponentType<EntityStore, PlayAnimationComponent> componentType) {
        PlayAnimationComponent.TYPE = componentType;
    }

    public static ComponentType<EntityStore, PlayAnimationComponent> getComponentType() {
        return TYPE;
    }

    public static final BuilderCodec<PlayAnimationComponent> CODEC = BuilderCodec.builder(
                    PlayAnimationComponent.class, PlayAnimationComponent::new
            )
            .append(
                    new KeyedCodec<String>("AnimationName", Codec.STRING),
                    (o, v) -> o.animationName = v,
                    (o) -> o.animationName
            )
            .add()
            .append(
                    new KeyedCodec<Boolean>("RemoveOnChange", Codec.BOOLEAN),
                    (o, v) -> o.removeOnChange = v,
                    (o) -> o.removeOnChange
            )
            .add()
            .build();

    private String animationName;
    private Boolean removeOnChange;

    public String getAnimationName() {
        return animationName;
    }

    public Boolean getRemoveOnChange() {
        return removeOnChange;
    }

    protected PlayAnimationComponent() {
    }

    public PlayAnimationComponent(String animationName, Boolean removeOnChange) {
        this.animationName = animationName;
        this.removeOnChange = removeOnChange;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new PlayAnimationComponent(animationName, removeOnChange);
    }

    @Override
    public String toString() {
        return "PlayAnimationComponent{animationName=" + animationName + ", removeOnChange=" + removeOnChange + '}';
    }
}
