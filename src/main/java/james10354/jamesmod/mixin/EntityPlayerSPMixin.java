package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntityLiving;
import james10354.jamesmod.util.IInput;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin {

    @Unique protected boolean isSprinting = false;
    @Unique protected float sprintBoost = 0.0F;

    @Inject(method = "onLivingUpdate", at = @At("HEAD"), remap = false)
    private void doSprint(CallbackInfo ci) {
        if (this.isSprinting) {
            this.isSprinting = false;
            ((IEntityLiving)this).setMoveSpeedMultiplier(((IEntityLiving)this).getMoveSpeedMultiplier() - sprintBoost);
        }
        if (!(((EntityPlayerSP)(Object)this).input).sneak && ((IInput)((EntityPlayerSP)(Object)this).input).getSprint() && ((((EntityPlayerSP)(Object)this).input).moveForward != 0 || (((EntityPlayerSP)(Object)this).input).moveStrafe != 0)) {
            this.isSprinting = true;

            float moveForward = (((EntityPlayerSP)(Object)this).input).moveForward;
            float moveStrafe = (((EntityPlayerSP)(Object)this).input).moveStrafe;

            float mag = MathHelper.sqrt_float(moveForward * moveForward + moveStrafe * moveStrafe);

            moveForward /= mag;
            moveStrafe /= mag;

            if (moveForward < 0) moveForward *= 0.4F;
            moveStrafe *= 0.6F;

            mag = MathHelper.sqrt_float(moveForward * moveForward + moveStrafe * moveStrafe);

            this.sprintBoost = mag * 0.3F;

            ((IEntityLiving)this).setMoveSpeedMultiplier(((IEntityLiving)this).getMoveSpeedMultiplier() + sprintBoost);
        }
    }
}
