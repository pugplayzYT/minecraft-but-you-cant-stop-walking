package io.github.pugplayzyt.cantstopwalking;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class CantStopWalkingMod implements ClientModInitializer {
    public static final String MOD_ID = "cantstopwalking";

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(ClientChallengeEvents::onClientTick);
    }
}
