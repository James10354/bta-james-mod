package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntityPathfinder;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityPathfinder;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPathfinder.class)
public abstract class EntityPathfinderMixin implements IEntityPathfinder {
    @Shadow protected Entity entityToAttack;
    @Unique protected float awareRadius = 16.0F;
    @Unique protected float sightRadius = 16.0F;

    @Override
    public float getAwareRadius() {
        return this.awareRadius;
    }

    @Override
    public float getSightRadius() {
        return this.sightRadius;
    }

    @Override
    public void setAwareRadius(float radius) {
        this.awareRadius = radius;
    }

    @Override
    public void setSightRadius(float radius) {
        this.sightRadius = radius;
    }

    @Redirect(method = "updatePlayerActionState",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/core/world/World;getPathToEntity(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/entity/Entity;F)Lnet/minecraft/core/world/pathfinder/Path;"),
        remap = false)
    public Path getPathToEntity(World world, Entity entity, Entity entityToTravelTo, float distance) {
        return world.getPathToEntity(entity, entityToTravelTo, getSightRadius());
    }

    @Inject(method = "updatePlayerActionState", at = @At("HEAD"), remap = false)
    private void stopTrackingOutOfRange(CallbackInfo ci) {
        if (this.entityToAttack != null) {
            if (this.entityToAttack.distanceTo(((Entity)(Object)this)) > sightRadius) {
                this.entityToAttack = null;
            }
        }
    }
}
