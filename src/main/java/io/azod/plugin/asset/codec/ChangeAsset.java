package io.azod.plugin.asset.codec;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class ChangeAsset {
    public final static BuilderCodec<ChangeAsset> CODEC = BuilderCodec.builder(ChangeAsset.class, ChangeAsset::new)
            .append(
                    new KeyedCodec<Boolean>("RemoveOnChange", Codec.BOOLEAN),
                    (o, v) -> o.removeOnChange = v,
                    (o) -> o.removeOnChange
            )
            .add()
            .append(
                    new KeyedCodec<String>("TargetAsset", Codec.STRING),
                    (o, v) -> o.targetAssetName = v,
                    (o) -> o.targetAssetName
            )
            .add()
            .build();

    protected boolean removeOnChange = false;
    protected String targetAssetName;

    public Boolean getRemoveOnChange() {
        return this.removeOnChange;
    }

    public String getTargetAssetName() {
        return this.targetAssetName;
    }
}
