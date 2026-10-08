package com.dw.durabilitywarning.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import com.dw.durabilitywarning.DurabilityWarning;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.Minecraft;


public class DurabilityWarningClient implements ClientModInitializer {
	private boolean warningSoundPlayed = false;
	private long secondBingAt = -1;
	@Override
	public void onInitializeClient() {
		HudElementRegistry.attachElementBefore(
			VanillaHudElements.CHAT,
			DurabilityWarning.id("warning"),
			(graphics, deltaTracker) -> {
				var player = Minecraft.getInstance().player;
				if (player == null) return;

				var stack = player.getMainHandItem();
				if (!stack.isDamageableItem()) return;

				int durabilityLeft = stack.getMaxDamage() - stack.getDamageValue();

				if (durabilityLeft <= 10) {
    if (!warningSoundPlayed) {
        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, 1.3F)
        );
        warningSoundPlayed = true;
        secondBingAt = Util.getMillis() + 180;
    } else if (secondBingAt != -1 && Util.getMillis() >= secondBingAt) {
        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, 1.6F)
        );
        secondBingAt = -1;
    }

    Minecraft minecraft = Minecraft.getInstance();
String warning = "Tool almost broken!";

int x = (minecraft.getWindow().getGuiScaledWidth()
        - minecraft.font.width(warning)) / 2;
int y = minecraft.getWindow().getGuiScaledHeight() - 50;

graphics.text(
        minecraft.font,
        warning,
        x,
        y,
        0xFFFF5555,
        true
);
} else {
    warningSoundPlayed = false;
    secondBingAt = -1;
}
			}
			        );
    }
}