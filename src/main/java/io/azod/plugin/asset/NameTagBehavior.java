package io.azod.plugin.asset;

import com.hypixel.hytale.assetstore.AssetExtraInfo;

import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;

import io.azod.plugin.asset.codec.ChangeAsset;
import io.azod.plugin.asset.codec.PlayAnimation;

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
    private boolean isPrefix;

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
                    new KeyedCodec<Boolean>("IsPrefix", Codec.BOOLEAN),
                    (c, v, e) -> c.isPrefix = v,
                    (c, e) -> c.isPrefix
            )
            .add()
            .build();

    @Override
    public String getId() {
        return this.filename + "_" + this.tagName;
    }

    private Boolean trueCompare(String s) {
        if (this.ignoreCase) return this.tagName.equalsIgnoreCase(s);
        return this.tagName.equals(s);
    }

    public Boolean compareTagName(String tagName) {
        if (this.isPrefix) {
            String[] chunks = tagName.split(":");
            if (chunks.length < 2) {
                return false;
            }
            String[] prefixs = chunks[0].split("_");
            for (String prefix : prefixs) {
                if (trueCompare(prefix)) return true;
            }
            return false;
        }

        return trueCompare(tagName);
    }

    public String getTagName() {
        return this.tagName;
    }

    public boolean isPrefix() {
        return this.isPrefix;
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
                " ,PlayAnimation=" + this.playAnimation +
                ", ChangeAsset=" + this.changeAsset + "}";
    }
}
