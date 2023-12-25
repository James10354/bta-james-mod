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

    @Inject(method = "onLivingUpdate", at = @At("HEAD"), remap = false)
    private void doSprint(CallbackInfo ci) {

        ((IEntityLiving)this).setSprintBoost(0.0F);

        if (!(((EntityPlayerSP)(Object)this).input).sneak && ((IInput)((EntityPlayerSP)(Object)this).input).getSprint() && ((((EntityPlayerSP)(Object)this).input).moveForward != 0 || (((EntityPlayerSP)(Object)this).input).moveStrafe != 0)) {
            ((IEntityLiving)this).setSprinting(true);
        }

        if (((IEntityLiving)this).isSprinting() && (((EntityPlayerSP)(Object)this).health < 14 || (MathHelper.abs((float)((EntityPlayerSP)(Object)this).xd) < 0.05F && MathHelper.abs((float)((EntityPlayerSP)(Object)this).zd) < 0.05F))) {
            ((IEntityLiving) this).setSprinting(false);
        }

        if (((IEntityLiving)this).isSprinting()) {
            float moveForward = (((EntityPlayerSP)(Object)this).input).moveForward;
            float moveStrafe = (((EntityPlayerSP)(Object)this).input).moveStrafe;

            float mag = MathHelper.sqrt_float(moveForward * moveForward + moveStrafe * moveStrafe) + Float.MIN_VALUE;

            moveForward /= mag;
            moveStrafe /= mag;

            if (moveForward < 0) moveForward *= 0.2F;
            moveStrafe *= 0.5F;

            mag = MathHelper.sqrt_float(moveForward * moveForward + moveStrafe * moveStrafe);

            ((IEntityLiving)this).setSprintBoost(mag * 0.5F);

            //((IEntityLiving)this).setMoveSpeedMultiplier(((IEntityLiving)this).getMoveSpeedMultiplier() + ((IEntityLiving)this).getSprintBoost());
        }
    }
}
