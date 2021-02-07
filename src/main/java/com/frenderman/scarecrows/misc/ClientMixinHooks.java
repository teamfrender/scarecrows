package com.frenderman.scarecrows.misc;

import com.frenderman.scarecrows.common.entity.ScarecrowEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ClientMixinHooks {

    /**
     * Practically all copied from {@link Minecraft}.
     * Checks if the player has picked a Scarecrow, and sets their current, or closest available, hotbar slot to Scarecrow item.
     */
    public static void middleClickMouse(CallbackInfo ci, Minecraft mc) {
        if (mc.objectMouseOver != null && mc.objectMouseOver.getType() != RayTraceResult.Type.MISS) {
            assert mc.player != null;

            if (mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult)mc.objectMouseOver).getEntity();
                if (entity instanceof ScarecrowEntity) {
                    ItemStack pickedStack = new ItemStack(ScarecrowEntity.getDrops().get(((ScarecrowEntity) entity).getColor()).get());
                    PlayerInventory playerInventory = mc.player.inventory;

                    int slotIndexWithScarecrow = playerInventory.getSlotFor(pickedStack);
                    if (mc.player.abilities.isCreativeMode) {
                        playerInventory.setPickedItemStack(pickedStack);
                        assert mc.playerController != null;
                        mc.playerController.sendSlotPacket(mc.player.getHeldItem(Hand.MAIN_HAND), 36 + playerInventory.currentItem);
                    } else if (slotIndexWithScarecrow != -1) {
                        if (PlayerInventory.isHotbar(slotIndexWithScarecrow)) {
                            playerInventory.currentItem = slotIndexWithScarecrow;
                        } else {
                            assert mc.playerController != null;
                            mc.playerController.pickItem(slotIndexWithScarecrow);
                        }
                    }
                }
            }
        }
    }
}
