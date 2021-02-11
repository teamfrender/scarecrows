package com.frenderman.scarecrows.mixin;

import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import com.frenderman.scarecrows.init.SCAdvancementCriteria;
import com.frenderman.scarecrows.init.SCEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Shadow @Mutable
    private LivingEntity target;

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void setTargetEntity(LivingEntity newTarget, CallbackInfo ci) {
        MobEntity $this = MobEntity.class.cast(this);
        if ( $this.isAlive()
          && $this.isUndead()
          && (newTarget instanceof MobEntity || newTarget instanceof PlayerEntity)
          && EnchantmentHelper.getEquipmentLevel(SCEnchantments.DISTRACTING, newTarget) == 1
        ) {
            List<ScarecrowEntity> nearbyScarecrows = $this.world.getEntitiesByClass(ScarecrowEntity.class, $this.getBoundingBox().expand(12.0D), null);
            if (!nearbyScarecrows.isEmpty()) {
                this.target = nearbyScarecrows.get($this.getRandom().nextInt(nearbyScarecrows.size()));

                if ($this.getRandom().nextFloat() <= 0.175F) {
                    ItemStack itemStack = $this.getEquippedStack(EquipmentSlot.HEAD);
                    itemStack.damage(1, $this, ((livingEntity) -> livingEntity.sendEquipmentBreakStatus(EquipmentSlot.HEAD)));
                }

                if (newTarget instanceof ServerPlayerEntity) SCAdvancementCriteria.DISTRACT_ENTITY.trigger((ServerPlayerEntity) newTarget);

                ci.cancel();
            }
        }
    }
}
