package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntityPathfinder;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntitySkeleton;
import net.minecraft.core.entity.projectile.EntityArrow;
import net.minecraft.core.util.helper.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntitySkeleton.class)
public abstract class EntitySkeletonMixin implements IEntityPathfinder {

    @Unique protected float maxRange = 16.0F;
    @Unique protected int minDrawTime = 10;
    @Unique protected int maxDrawTime = 35;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injected(CallbackInfo ci) {
        this.setAwareRadius(24);
        this.setSightRadius(24);
    }

    @ModifyConstant(method = "attackEntity", constant = @Constant(floatValue = 10.0F), remap = false)
    private float range(float constant) { return maxRange; }

    @Inject(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/projectile/EntityArrow;setArrowHeading(DDDFF)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void setHeading(Entity entity, float distance, CallbackInfo ci, double d, double d1, EntityArrow entityArrow, double d2, float f1) {

        //float leadAmount = new Random().nextFloat();

        d += entity.xd * (distance * 1.5F);
        d1 += entity.zd * (distance * 1.5F);

        float force = distance / 10.0F;
        f1 = MathHelper.sqrt_double(d * d + d1 * d1) * (1 / distance);

        entityArrow.setArrowHeading(d, d2 + (double)f1, d1, force, maxRange - distance);
    }

    @Inject(method = "attackEntity", at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/monster/EntitySkeleton;attackTime:I", ordinal = 1, shift = At.Shift.AFTER), remap = false)
    private void setDrawTime(Entity entity, float distance, CallbackInfo ci) {
        ((EntityLiving)(Object)this).attackTime = minDrawTime + (int)((maxDrawTime - minDrawTime) * (distance / maxRange));
    }

    @Inject(method = "attackEntity", at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/projectile/EntityArrow;y:D", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void setArrowPos(Entity entity, float distance, CallbackInfo ci, double d, double d1, EntityArrow entityarrow) {
        entityarrow.y -= 1.2;
    }

}
