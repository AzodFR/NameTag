package io.azod.plugin.asset.codec;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class TransformRotation {
    public final static BuilderCodec<TransformRotation> CODEC = BuilderCodec.builder(TransformRotation.class, TransformRotation::new)
            .append(
                    new KeyedCodec<Boolean>("RemoveOnChange", Codec.BOOLEAN),
                    (o, v) -> o.removeOnChange = v,
                    (o) -> o.removeOnChange
            )
            .add()
            .append(
                    new KeyedCodec<Float>("Z", Codec.FLOAT),
                    (o, v) -> o.z = v,
                    (o) -> o.z
            )
            .add()
            .build();

    protected boolean removeOnChange = false;
    protected float z;

    public Boolean getRemoveOnChange() {
        return this.removeOnChange;
    }

    public float getZRotation() {
        return this.z;
    }

    public String toString(){
        return "{z=" + this.z + ", RemoveOnChange=" + this.removeOnChange + "}";
    }
}
