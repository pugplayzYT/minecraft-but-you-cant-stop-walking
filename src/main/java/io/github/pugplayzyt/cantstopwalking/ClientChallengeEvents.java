package io.github.pugplayzyt.cantstopwalking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ClientChallengeEvents {
    private static boolean forceWalking = false;
    private static boolean forceJumping = false;
    private static boolean wasInWorld = false;

    private ClientChallengeEvents() {
    }

    public static void onClientTick(Minecraft minecraft) {
        boolean inWorld = minecraft.player != null && minecraft.level != null;

        if (inWorld && !wasInWorld) {
            minecraft.setScreen(new ChallengeScreen());
        }

        if (inWorld) {
            applyForcedControls(minecraft);
        } else if (wasInWorld) {
            releaseForcedControls(minecraft);
        }

        wasInWorld = inWorld;
    }

    private static void applyForcedControls(Minecraft minecraft) {
        if (forceWalking) {
            minecraft.options.keyUp.setDown(true);
            minecraft.options.keyDown.setDown(false);
            minecraft.options.keyLeft.setDown(false);
            minecraft.options.keyRight.setDown(false);
            minecraft.options.keySprint.setDown(false);
            minecraft.options.keyShift.setDown(false);
            minecraft.options.keyJump.setDown(forceJumping);
            return;
        }

        if (forceJumping) {
            minecraft.options.keyJump.setDown(true);
        }
    }

    private static void releaseForcedControls(Minecraft minecraft) {
        minecraft.options.keyUp.setDown(false);
        minecraft.options.keyDown.setDown(false);
        minecraft.options.keyLeft.setDown(false);
        minecraft.options.keyRight.setDown(false);
        minecraft.options.keySprint.setDown(false);
        minecraft.options.keyShift.setDown(false);
        minecraft.options.keyJump.setDown(false);
    }

    private static final class ChallengeScreen extends Screen {
        private ChallengeScreen() {
            super(Component.literal("What do you want? Do you want Minecraft?"));
        }

        @Override
        protected void init() {
            int centerX = this.width / 2;
            int startY = this.height / 2 - 35;

            this.addRenderableWidget(Button.builder(
                    walkingText(),
                    button -> {
                        forceWalking = !forceWalking;
                        button.setMessage(walkingText());
                    }
            ).bounds(centerX - 120, startY, 240, 20).build());

            this.addRenderableWidget(Button.builder(
                    jumpingText(),
                    button -> {
                        forceJumping = !forceJumping;
                        button.setMessage(jumpingText());
                    }
            ).bounds(centerX - 120, startY + 28, 240, 20).build());

            this.addRenderableWidget(Button.builder(
                    Component.literal("Start"),
                    button -> this.onClose()
            ).bounds(centerX - 60, startY + 65, 120, 20).build());
        }

        private Component walkingText() {
            return Component.literal((forceWalking ? "[x] " : "[ ] ") + "You can't stop walking");
        }

        private Component jumpingText() {
            return Component.literal((forceJumping ? "[x] " : "[ ] ") + "You can't stop jumping");
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            this.renderBackground(graphics, mouseX, mouseY, partialTick);
            graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 70, 0xFFFFFF);
            super.render(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }
}
