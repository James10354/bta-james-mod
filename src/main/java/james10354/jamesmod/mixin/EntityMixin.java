package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {
    @Unique protected float knockBack = 0.4F;
    @Shadow public double yd;

    @Shadow protected abstract void causeFallDamage(float f);


    @Override
    public float getKnockBack() {
        return this.knockBack;
    }

    @Override
    public void setKnockBack(float knockBack) {
        this.knockBack = knockBack;
    }

    @Redirect(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Entity;causeFallDamage(F)V"), remap = false)
    private void doFallDamage(Entity instance, float f) {
        if (this.yd < -0.7) this.causeFallDamage(f);
    }

    @Redirect(method = "baseTick", at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/Entity;fallDistance:F"), remap = false)
    private void keepFallDistance(Entity instance, float value) { }

}
