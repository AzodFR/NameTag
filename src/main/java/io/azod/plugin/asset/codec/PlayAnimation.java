package io.azod.plugin.asset.codec;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class PlayAnimation {

    public final static BuilderCodec<PlayAnimation> CODEC = BuilderCodec.builder(
                    PlayAnimation.class, PlayAnimation::new)
            .append(
                    new KeyedCodec<Boolean>("RemoveOnChange", Codec.BOOLEAN),
                    (o, v) -> o.removeOnChange = v,
                    (o) -> o.removeOnChange
            )
            .add()
            .append(
                    new KeyedCodec<String>("AnimationName", Codec.STRING),
                    (o, v) -> o.animationName = v,
                    (o) -> o.animationName
            )
            .add()
            .build();

    private String animationName;
    public Boolean removeOnChange = false;

    public String getAnimationName() {
        return animationName;
    }

    public Boolean getRemoveOnChange() {
        return removeOnChange;
    }
}
