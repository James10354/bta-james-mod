package james10354.jamesmod.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityTNT;
import net.minecraft.core.world.Explosion;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTNT.class)
public abstract class EntityTNTMixin extends Entity {

    public EntityTNTMixin(World world) {
        super(world);
    }

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;createExplosion(Lnet/minecraft/core/entity/Entity;DDDF)Lnet/minecraft/core/world/Explosion;"), remap = false)
    private Explosion doExplosion(World world, Entity entity, double d, double d1, double d2, float f) {
        return world.newExplosion(null, d, d1, d2, f, this.remainingFireTicks > 0, false);
    }
}
