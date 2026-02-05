package io.azod.plugin.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class ChangeAssetComponent implements Component<EntityStore> {

    private static ComponentType<EntityStore, ChangeAssetComponent> TYPE;

    public static void setComponentType(ComponentType<EntityStore, ChangeAssetComponent> componentType) {
        TYPE = componentType;
    }

    public static ComponentType<EntityStore, ChangeAssetComponent> getComponentType() {
        return TYPE;
    }

    public static final BuilderCodec<ChangeAssetComponent> CODEC = BuilderCodec.builder(
                    ChangeAssetComponent.class, ChangeAssetComponent::new)
            .append(
                    new KeyedCodec<String>("OriginalAssetName", Codec.STRING),
                    (o, v) -> o.originalAssetName = v,
                    (o) -> o.originalAssetName
            )
            .add()
            .append(
                    new KeyedCodec<String>("TargetAssetName", Codec.STRING),
                    (o, v) -> o.targetAssetName = v,
                    (o) -> o.targetAssetName
            )
            .add()
            .append(
                    new KeyedCodec<Boolean>("RemoveOnChange", Codec.BOOLEAN),
                    (o, v) -> o.removeOnChange = v,
                    (o) -> o.removeOnChange
            )
            .add()
            .afterDecode((data) -> {
                if (data == null) return;
                data.originalAsset = ModelAsset.getAssetMap().getAsset(data.originalAssetName);
                data.targetAsset = ModelAsset.getAssetMap().getAsset(data.targetAssetName);
            })
            .build();

    private String originalAssetName;
    private String targetAssetName;

    private ModelAsset originalAsset;
    private ModelAsset targetAsset;

    private Boolean removeOnChange;

    public ModelAsset getOriginalAsset() {
        return originalAsset;
    }

    public ModelAsset getTargetAsset() {
        return targetAsset;
    }

    public Boolean getRemoveOnChange() {
        return removeOnChange;
    }

    protected ChangeAssetComponent() {
    }

    public ChangeAssetComponent(String originalAssetName, String targetAssetName, Boolean removeOnChange) {
        this.originalAssetName = originalAssetName;
        this.originalAsset = ModelAsset.getAssetMap().getAsset(originalAssetName);

        this.targetAssetName = targetAssetName;
        this.targetAsset = ModelAsset.getAssetMap().getAsset(targetAssetName);

        this.removeOnChange = removeOnChange;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new ChangeAssetComponent(this.originalAssetName, this.targetAssetName, this.removeOnChange);
    }

    @Override
    public String toString() {
        return "ChangeAssetComponent{originalAssetName=" + originalAssetName +
                ";orignalAsset=" + originalAsset +
                ";targetAssetName=" + targetAssetName +
                ";targetAsset=" + targetAsset +
                ";removeOnChange=" + removeOnChange +
                "}";
    }
}
