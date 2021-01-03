package com.frenderman.scarecrows.mixin;

import com.frenderman.scarecrows.entity.scarecrow.ScarecrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity hitEntity = entityHitResult.getEntity();
        if (hitEntity instanceof ScarecrowEntity) {
            PersistentProjectileEntity $this = PersistentProjectileEntity.class.cast(this);
            ScarecrowEntity scarecrowEntity = (ScarecrowEntity)hitEntity;

            int newStuckArrow = scarecrowEntity.getStuckArrowCount() + 1;
            if (newStuckArrow <= scarecrowEntity.getMaxStuckArrowCount() ) {
                scarecrowEntity.setStuckArrowCount(newStuckArrow);
                $this.remove();
            }
        }
    }
}
