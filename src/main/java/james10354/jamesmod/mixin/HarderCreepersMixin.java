package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntity;
import james10354.jamesmod.util.IEntityPathfinder;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityPathfinder;
import net.minecraft.core.entity.monster.EntityCreeper;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.Explosion;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.signum;
import static net.minecraft.core.util.helper.MathHelper.PI;


@Mixin(EntityCreeper.class)
public abstract class HarderCreepersMixin extends EntityPathfinder implements IEntityPathfinder, IEntity {
    @Shadow public abstract boolean getPowered();
    @Shadow protected abstract void attackEntity(Entity entity, float distance);

    public HarderCreepersMixin(World world) { super(world); }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injected(CallbackInfo ci) {
        this.setKnockBack(1.2F);
    }

    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    public void explodeIfOnFire(CallbackInfo ci) {
        if (this.isOnFire() && !this.getPowered()) this.entityToAttack = this;
    }

    @Redirect(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;createExplosion(Lnet/minecraft/core/entity/Entity;DDDF)Lnet/minecraft/core/world/Explosion;"), remap = false)
    public Explosion doExplosion(World worldObj, Entity entity, double d, double d1, double d2, float f) {
        return worldObj.newExplosion(entity, d, d1, d2, f, getSharedFlag(0), false);
    }

    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    public void creep(CallbackInfo ci) {

        float radius = 48.0F;

        for(int i = 0; i < this.world.players.size(); ++i) {
            EntityPlayer player = this.world.players.get(i);
            double currentDistance = player.distanceToSqr(x, y, z);
            if (currentDistance < radius * radius) {

                float lookDir = (player.yRot) * (PI/180);
                float dx = MathHelper.cos(lookDir);
                float dz = MathHelper.sin(lookDir);

                if (signum((player.x + dx - player.x) * (this.z - player.z) - (player.z + dz - player.z) * (this.x - player.x)) > -1) {
                    this.moveSpeed = 0.7F;
                    setAwareRadius(10);
                    setSightRadius(12);
                    if (this.entityToAttack != null && this.distanceTo(this.entityToAttack) > this.getSightRadius()) {
                        this.entityToAttack = null;
                        this.pathToEntity = null;
                    }
                    return;
                }
            }
        }

        this.setSightRadius(radius);
        this.setAwareRadius(radius);
        if (hasCurrentTarget()) {
            this.moveSpeed = 2.8F;
        } else {
            this.moveSpeed = 0.7F;
        }
    }
}