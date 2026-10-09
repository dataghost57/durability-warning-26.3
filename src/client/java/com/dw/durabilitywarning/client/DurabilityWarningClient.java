package com.dw.durabilitywarning.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import com.dw.durabilitywarning.DurabilityWarning;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;


public class DurabilityWarningClient implements ClientModInitializer {
	private ItemStack lastWarnedStack;
	private long secondBingAt = -1;
	@Override
	public void onInitializeClient() {
		HudElementRegistry.attachElementBefore(
			VanillaHudElements.CHAT,
			DurabilityWarning.id("warning"),
			(graphics, deltaTracker) -> {
				var player = Minecraft.getInstance().player;
if (player == null) {
    lastWarnedStack = null;
    secondBingAt = -1;
    return;
}

var stack = player.getMainHandItem();
if (!stack.isDamageableItem()) {
    lastWarnedStack = null;
    secondBingAt = -1;
    return;
}

				int durabilityLeft = stack.getMaxDamage() - stack.getDamageValue();

				if (durabilityLeft <= 10) {
    if (lastWarnedStack != stack) {
    lastWarnedStack = stack;
    Minecraft.getInstance().getSoundManager().play(
            SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, 1.3F)
    );
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
   lastWarnedStack = null;
secondBingAt = -1;
}
			}
			        );
    }
}