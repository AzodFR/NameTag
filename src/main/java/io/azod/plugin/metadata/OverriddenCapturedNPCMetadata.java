package io.azod.plugin.metadata;


import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class OverriddenCapturedNPCMetadata {
    public static final String KEY = "CapturedEntity";
    public static final BuilderCodec<OverriddenCapturedNPCMetadata> CODEC;
    public static final KeyedCodec<OverriddenCapturedNPCMetadata> KEYED_CODEC;
    private String iconPath;
    private int roleIndex;
    private String npcNameKey;
    private String fullItemIcon;
    private String nameTag;

    public int getRoleIndex() {
        return this.roleIndex;
    }

    public String getIconPath() {
        return this.iconPath;
    }

    public String getNpcNameKey() {
        return this.npcNameKey;
    }

    public String getFullItemIcon() {
        return this.fullItemIcon;
    }

    public String getNameTag() {
        return this.nameTag;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setRoleIndex(int roleIndex) {
        this.roleIndex = roleIndex;
    }

    public void setNpcNameKey(String npcNameKey) {
        this.npcNameKey = npcNameKey;
    }

    public void setFullItemIcon(String fullItemIcon) {
        this.fullItemIcon = fullItemIcon;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    static {
        CODEC = BuilderCodec.builder(OverriddenCapturedNPCMetadata.class, OverriddenCapturedNPCMetadata::new)
                .appendInherited(
                        new KeyedCodec<String>("IconPath", Codec.STRING),
                        (meta, s) -> meta.iconPath = s,
                        (meta) -> meta.iconPath,
                        (meta, parent) -> meta.iconPath = parent.iconPath
                )
                .add()
                .appendInherited(
                        new KeyedCodec<Integer>("RoleIndex", Codec.INTEGER),
                        (meta, s) -> meta.roleIndex = s,
                        (meta) -> meta.roleIndex,
                        (meta, parent) -> meta.roleIndex = parent.roleIndex
                )
                .add()
                .appendInherited(
                        new KeyedCodec<String>("NpcNameKey", Codec.STRING),
                        (meta, s) -> meta.npcNameKey = s,
                        (meta) -> meta.npcNameKey,
                        (meta, parent) -> meta.npcNameKey = parent.npcNameKey
                )
                .add()
                .appendInherited(
                        new KeyedCodec<String>("FullItemIcon", Codec.STRING),
                        (meta, s) -> meta.fullItemIcon = s,
                        (meta) -> meta.fullItemIcon,
                        (meta, parent) -> meta.fullItemIcon = parent.fullItemIcon
                )
                .add()
                .appendInherited(
                        new KeyedCodec<String>("NameTag", Codec.STRING),
                        (meta, s) -> meta.nameTag = s,
                        (meta) -> meta.nameTag,
                        (meta, parent) -> meta.nameTag = parent.nameTag
                )
                .add()
                .build();
        KEYED_CODEC = new KeyedCodec<OverriddenCapturedNPCMetadata>("CapturedEntity", CODEC);
    }
}
