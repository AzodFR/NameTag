package io.azod.plugin.asset;

import com.hypixel.hytale.assetstore.AssetExtraInfo;

import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;

import io.azod.plugin.asset.codec.ChangeAsset;
import io.azod.plugin.asset.codec.PlayAnimation;
import io.azod.plugin.asset.codec.TransformRotation;

import java.util.Arrays;


public class NameTagBehavior implements JsonAssetWithMap<String, DefaultAssetMap<String, NameTagBehavior>> {

    private AssetExtraInfo.Data data;
    private String filename;
    private String tagName;
    private String logMessage;
    private ChangeAsset changeAsset;
    private String[] allowedNPCRoles;
    private Boolean ignoreCase = false;
    private PlayAnimation playAnimation;
    private TransformRotation transformRotation;

    public static final AssetBuilderCodec<String, NameTagBehavior> CODEC = AssetBuilderCodec.builder(NameTagBehavior.class, io.azod.plugin.asset.NameTagBehavior::new,
                    Codec.STRING,
                    (o, v) -> o.filename = v,
                    io.azod.plugin.asset.NameTagBehavior::getId,
                    (o, d) -> o.data = d, o -> o.data)
            .append(
                    new KeyedCodec<String>("TagName", Codec.STRING),
                    (c, v, e) -> c.tagName = v,
                    (c, e) -> c.tagName
            )
            .add()
            .append(
                    new KeyedCodec<String[]>("AllowedNPCRoles", Codec.STRING_ARRAY),
                    (c, v, e) -> c.allowedNPCRoles = v,
                    (c, e) -> c.allowedNPCRoles
            )
            .add()
            .append(
                    new KeyedCodec<String>("LogMessage", Codec.STRING),
                    (c, v, e) -> c.logMessage = v,
                    (c, e) -> c.logMessage
            )
            .add()
            .append(
                    new KeyedCodec<ChangeAsset>("ChangeAsset", ChangeAsset.CODEC),
                    (c, v, e) -> c.changeAsset = v,
                    (c, e) -> c.changeAsset
            )
            .add()
            .append(
                    new KeyedCodec<Boolean>("IgnoreCase", Codec.BOOLEAN),
                    (c, v, e) -> c.ignoreCase = v,
                    (c, e) -> c.ignoreCase
            )
            .add()
            .append(
                    new KeyedCodec<PlayAnimation>("PlayAnimation", PlayAnimation.CODEC),
                    (c, v, e) -> c.playAnimation = v,
                    (c, e) -> c.playAnimation
            )
            .add()
            .append(
                    new KeyedCodec<TransformRotation>("TransformRotation", TransformRotation.CODEC),
                    (c, v, e) -> c.transformRotation = v,
                    (c, e) -> c.transformRotation
            )
            .add()
            .build();

    @Override
    public String getId() {
        return this.filename + "_" + this.tagName;
    }

    public Boolean compareTagName(String tagName) {
        if (this.ignoreCase) return this.tagName.equalsIgnoreCase(tagName);

        return this.tagName.equals(tagName);
    }

    public String getTagName() {
        return this.tagName;
    }

    public String getLogMessage() {
        return this.logMessage;
    }

    public ChangeAsset getChangeAsset() {
        return this.changeAsset;
    }

    public PlayAnimation getPlayAnimation() {
        return this.playAnimation;
    }

    public TransformRotation getTransformRotation() {
        return this.transformRotation;
    }

    public boolean isInAllowedNPCRoles(String npcRole) {
        if (this.allowedNPCRoles == null || this.allowedNPCRoles.length == 0) return true;
        for (String allowedNPCRole : allowedNPCRoles) {
            if (allowedNPCRole.equals(npcRole)) return true;
        }
        return false;
    }

    public String toString() {
        return "NameTagBehavior={TagName=" + this.tagName +
                ", allowedNPCRoles=" + Arrays.toString(this.allowedNPCRoles) +
                ", ignoreCase=" + this.ignoreCase +
                ", LogMessage=" + this.logMessage +
                ", PlayAnimation=" + this.playAnimation +
                ", ChangeAsset=" + this.changeAsset +
                ", TransformRotation=" + this.transformRotation +
                "}";
    }
}
