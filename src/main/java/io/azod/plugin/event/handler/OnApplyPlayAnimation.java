package io.azod.plugin.event.handler;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.azod.plugin.component.PlayAnimationComponent;
import io.azod.plugin.event.ApplyPlayAnimation;

import java.util.function.Consumer;

public class OnApplyPlayAnimation implements Consumer<ApplyPlayAnimation> {

    @Override
    public void accept(ApplyPlayAnimation event) {
        Ref<EntityStore> entityRef = event.entityRef();
        if (!entityRef.isValid()) return;

        Store<EntityStore> store = event.store();

        PlayAnimationComponent playAnimationComponent = store.getComponent(entityRef, PlayAnimationComponent.getComponentType());

        if  (playAnimationComponent != null) {
            store.removeComponent(entityRef, PlayAnimationComponent.getComponentType());
        }
        store.addComponent(entityRef, PlayAnimationComponent.getComponentType() ,event.component());

    }
}
