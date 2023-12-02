package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntityLiving;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.camera.EntityCamera;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Unique protected float currentFovMultiplier;
    @Unique protected float startFovMultiplier;
    @Unique protected float targetFovMultiplier;
    @Unique protected float fovBlendTimer;

    @Redirect(method = "getFOVModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/camera/ICamera;getFov()D"), remap = false)
    private double dynamicFOV(ICamera activeCamera) {

        if (fovBlendTimer < 1.0F) {
            fovBlendTimer *= 1.5F;
            if (targetFovMultiplier < startFovMultiplier) {
                fovBlendTimer *= 0.8F;
            }
            if (fovBlendTimer > 1.0F) {
                fovBlendTimer = 1.0F;
            }
        }

        if (targetFovMultiplier != ((IEntityLiving)((EntityCamera)activeCamera).entity).getMoveSpeed() / 2.0F) {
            fovBlendTimer = 0.1F;
            startFovMultiplier = currentFovMultiplier;
            targetFovMultiplier = ((IEntityLiving)((EntityCamera)activeCamera).entity).getMoveSpeed() / 2.0F;
        }

        currentFovMultiplier = MathHelper.lerp(startFovMultiplier, targetFovMultiplier, fovBlendTimer);

        return activeCamera.getFov() * (currentFovMultiplier + 0.5F);
    }
}
