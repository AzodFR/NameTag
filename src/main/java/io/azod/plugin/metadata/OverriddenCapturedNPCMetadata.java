package io.azod.plugin.metadata;


import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.azod.plugin.component.NameTagComponent;

public class OverriddenCapturedNPCMetadata {
    public static final String KEY = "CapturedEntity";
    public static final BuilderCodec<OverriddenCapturedNPCMetadata> CODEC;
    public static final KeyedCodec<OverriddenCapturedNPCMetadata> KEYED_CODEC;
    private String iconPath;
    private int roleIndex;
    private String npcNameKey;
    private String fullItemIcon;
    private NameTagComponent nameTagComponent;

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

    public NameTagComponent getNameTag() {
        return this.nameTagComponent;
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

    public void setNameTag(NameTagComponent nameTagComponent) {
        this.nameTagComponent = nameTagComponent;
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
                        new KeyedCodec<NameTagComponent>("NameTag", NameTagComponent.CODEC),
                        (meta, s) -> meta.nameTagComponent = s,
                        (meta) -> meta.nameTagComponent,
                        (meta, parent) -> meta.nameTagComponent = parent.nameTagComponent
                )
                .add()
                .build();
        KEYED_CODEC = new KeyedCodec<OverriddenCapturedNPCMetadata>("CapturedEntity", CODEC);
    }
}
