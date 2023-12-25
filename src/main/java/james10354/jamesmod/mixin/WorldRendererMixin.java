package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntityLiving;
import james10354.jamesmod.util.IGameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.camera.EntityCamera;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow private Minecraft mc;
    @Unique protected float currentFovMultiplier;
    @Unique protected float startFovMultiplier;
    @Unique protected float targetFovMultiplier;
    @Unique protected float fovBlendTimer;

    @Redirect(method = "getFOVModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/camera/ICamera;getFov()D"), remap = false)
    private double dynamicFOV(ICamera activeCamera) {
        if (!((IGameSettings)this.mc.gameSettings).getDynamicFoV().value) {
            return activeCamera.getFov();
        }

        if (fovBlendTimer < 1.0F) {
            fovBlendTimer *= 1.5F;
            if (targetFovMultiplier < startFovMultiplier) {
                fovBlendTimer *= 0.8F;
            }
            if (fovBlendTimer > 1.0F) {
                fovBlendTimer = 1.0F;
            }
        }

        float strength = 0.2F;
        if (targetFovMultiplier != (((IEntityLiving)((EntityCamera)activeCamera).entity).getMoveSpeedMultiplier() + (((IEntityLiving)((EntityCamera)activeCamera).entity).isSprinting() ? 0.5F : 0)) * strength) {
            fovBlendTimer = 0.01F;
            startFovMultiplier = currentFovMultiplier;
            targetFovMultiplier = (((IEntityLiving)((EntityCamera)activeCamera).entity).getMoveSpeedMultiplier() + (((IEntityLiving)((EntityCamera)activeCamera).entity).isSprinting() ? 0.5F : 0)) * strength;
        }

        currentFovMultiplier = MathHelper.lerp(startFovMultiplier, targetFovMultiplier, fovBlendTimer);

        return activeCamera.getFov() * (currentFovMultiplier + (1-strength));
    }
}
