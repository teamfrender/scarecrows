package com.frenderman.scarecrows.mixin.client;

import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import com.frenderman.scarecrows.init.SCItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "doItemPick", at = @At("HEAD"))
    private void doItemPick(CallbackInfo ci) {
        MinecraftClient $this = MinecraftClient.class.cast(this);
        if ($this.crosshairTarget != null && $this.crosshairTarget.getType() != HitResult.Type.MISS) {
            assert $this.player != null;
            if ($this.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult)$this.crosshairTarget).getEntity();
                if (entity instanceof ScarecrowEntity) {
                    ItemStack pickedStack = new ItemStack(SCItems.SCARECROW);
                    PlayerInventory playerInventory = $this.player.inventory;

                    int slotIndexWithScarecrow = playerInventory.getSlotWithStack(pickedStack);
                    if ($this.player.abilities.creativeMode) {
                        playerInventory.addPickBlock(pickedStack);
                        assert $this.interactionManager != null;
                        $this.interactionManager.clickCreativeStack($this.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.selectedSlot);
                    } else if (slotIndexWithScarecrow != -1) {
                        if (PlayerInventory.isValidHotbarIndex(slotIndexWithScarecrow)) {
                            playerInventory.selectedSlot = slotIndexWithScarecrow;
                        } else {
                            assert $this.interactionManager != null;
                            $this.interactionManager.pickFromInventory(slotIndexWithScarecrow);
                        }
                    }
                }
            }
        }
    }
}
