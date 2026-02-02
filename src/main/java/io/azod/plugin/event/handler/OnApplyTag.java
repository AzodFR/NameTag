package io.azod.plugin.event.handler;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.azod.plugin.component.ChangeAssetComponent;
import io.azod.plugin.component.NameTagComponent;
import io.azod.plugin.component.PlayAnimationComponent;
import io.azod.plugin.event.AppliedTagEvent;
import io.azod.plugin.event.ApplyTagEvent;

import java.util.function.Consumer;

public class OnApplyTag implements Consumer<ApplyTagEvent> {
    @Override
    public void accept(ApplyTagEvent event) {
        Ref<EntityStore> entityRef = event.entityRef();
        if (!entityRef.isValid()) return;

        Store<EntityStore> store = event.store();

        NameTagComponent aaa = event.component();

        NameTagComponent component = store.getComponent(entityRef, NameTagComponent.getComponentType());
        if (component != null) {
            store.removeComponent(entityRef, NameTagComponent.getComponentType());
        }
        store.addComponent(entityRef, NameTagComponent.getComponentType(), event.component());

//        ChangeAssetComponent changeAssetComponent = store.getComponent(entityRef, ChangeAssetComponent.getComponentType());
//        if (changeAssetComponent != null) {
//            store.removeComponent(entityRef, ChangeAssetComponent.getComponentType());
//        }
//
//        PlayAnimationComponent playAnimationComponent = store.getComponent(entityRef, PlayAnimationComponent.getComponentType());
//        if (playAnimationComponent != null) {
//            store.removeComponent(entityRef, PlayAnimationComponent.getComponentType());
//        }
        //store.putComponent(entityRef, NameTagComponent.getComponentType(), event.component());

        Nameplate nameplate = store.getComponent(entityRef, Nameplate.getComponentType());
        if (nameplate == null) {
            store.addComponent(entityRef, Nameplate.getComponentType(), new Nameplate(event.component().getTag()));
        } else {
            nameplate.setText(event.component().getTag());
        }

        AppliedTagEvent.dispatch(entityRef, store, event.component());
    }
}
