package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntity;
import james10354.jamesmod.util.IEntityMonster;
import james10354.jamesmod.util.IEntityPathfinder;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.monster.EntityZombie;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombie.class)
public abstract class HarderZombiesMixin extends EntityMonster implements IEntity, IEntityMonster {

    public HarderZombiesMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injected(CallbackInfo ci) {
        this.moveSpeed = this.moveSpeed * 0.8F + random.nextFloat() * (this.moveSpeed * 0.6F);

        this.attackStrength = 2;
        this.setAttackReach(2.3F);
        this.setKnockBack(-0.6F);
        this.setAttackDelay(0);
    }
}
