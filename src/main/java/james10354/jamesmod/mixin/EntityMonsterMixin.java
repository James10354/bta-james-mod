package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntity;
import james10354.jamesmod.util.IEntityMonster;
import james10354.jamesmod.util.IEntityPathfinder;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntityCreeper;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMonster.class)
public abstract class EntityMonsterMixin extends Entity implements IEntityPathfinder, IEntity, IEntityMonster {
    @Unique protected float attackReach = 2.0F;
    @Unique protected int attackDelay = 20;

    public EntityMonsterMixin(World world) {
        super(world);
    }

    @Redirect(method = "findPlayerToAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getClosestPlayerToEntity(Lnet/minecraft/core/entity/Entity;D)Lnet/minecraft/core/entity/player/EntityPlayer;"), remap = false)
    public EntityPlayer getClosestPlayer(World world, Entity entity, double radius) {
        return world.getClosestPlayerToEntity(entity, this.getAwareRadius());
    }

    @Inject(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Entity;hurt(Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z"), remap = false)
    private void attackFire(Entity entity, float distance, CallbackInfo ci) {
        ((EntityMonster)(Object)this).attackTime = attackDelay;

        if (this.isOnFire() && entity.remainingFireTicks < this.remainingFireTicks / 2 && this.random.nextInt(10) == 1) {
            entity.remainingFireTicks = this.remainingFireTicks / 2;
        }
    }

    @ModifyConstant(method = "attackEntity", constant = @Constant(floatValue = 2.0F), remap = false)
    private float useAttackReach(float constant) {
        return this.attackReach;
    }

    public void setAttackReach(float distance) { this.attackReach = distance; }

    public void setAttackDelay(int delay) {
        this.attackDelay = delay;
    }
}
