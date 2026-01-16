package io.azod.plugin;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class NameTagMetadata {
    public static final BuilderCodec<NameTagMetadata> CODEC = BuilderCodec.builder(NameTagMetadata.class, NameTagMetadata::new)
            .append(
                    new KeyedCodec<>("NameplateValue", Codec.STRING),
                    (state, o) -> state.nameplateValue = o,
                    state -> state.nameplateValue)
            .add()
            .build();
    public static final KeyedCodec<NameTagMetadata> KEYED_CODEC = new KeyedCodec<>("NameTag", CODEC);
    public static final String KEY = "NameTag";
    private String nameplateValue;

    public String getNameplateValue() {
        return this.nameplateValue;
    }
    public void setNameplateValue(String nameplateValue) {
        this.nameplateValue = nameplateValue;
    }
}
