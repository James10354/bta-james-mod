package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntity;
import james10354.jamesmod.util.IEntityLiving;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin extends Entity implements IEntityLiving{



    @Shadow protected float moveSpeed;

    @Unique protected boolean isSprinting = false;
    @Unique protected float sprintBoost = 0.0F;

    @Unique protected float moveSpeedMultiplier = 1.0F;

    public EntityLivingMixin(World world) {
        super(world);
    }

    public float getMoveSpeed() {
        return this.moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public boolean isSprinting() {
        return this.isSprinting;
    }

    public void setSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
    }

    public float getSprintBoost() {
        return this.sprintBoost;
    }

    public void setSprintBoost(float sprintBoost) {
        this.sprintBoost = sprintBoost;
    }

    public float getMoveSpeedMultiplier() { return this.moveSpeedMultiplier; }

    public void setMoveSpeedMultiplier(float multiplier) { this.moveSpeedMultiplier = multiplier; }

    @Inject(method = "knockBack", at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/EntityLiving;xd:D", ordinal = 1), cancellable = true, remap = false)
    private void cancelLimitVelY(Entity entity, int i, double d, double d1, CallbackInfo ci) {
        float f = MathHelper.sqrt_double(d * d + d1 * d1);
        this.xd -= d / (double)f * (double)((IEntity)entity).getKnockBack();
        this.yd +=  0.3200000047683716 + 0.20000000298023225 * (double)((IEntity)entity).getKnockBack();
        this.zd -= d1 / (double)f * (double)((IEntity)entity).getKnockBack();
        ci.cancel();
    }

    @ModifyVariable(method = "causeFallDamage", at = @At(value = "STORE"), ordinal = 0, remap = false)
    private int velocityFallDamage(int value) {

        double vel = 0;
        double distance = 0;

        while ((vel - 0.08)*0.98 >= this.yd) {
            vel -= 0.08;
            vel *= 0.98;
            distance -= vel;
        }

        distance *= 0.95;

        if (this.isInWater()) distance *= 0.75;

        return (int)Math.round(distance) - 3;
    }

    @ModifyArg(method = "moveEntityWithHeading", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityLiving;moveRelative(FFF)V"), index = 2, remap = false)
    private float useMoveSpeed(float multiplier) {
        return this.moveSpeed * (this.moveSpeedMultiplier + this.sprintBoost) * multiplier;
    }
}
