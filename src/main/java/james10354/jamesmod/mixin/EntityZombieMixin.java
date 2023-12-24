package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntity;
import james10354.jamesmod.util.IEntityMonster;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.monster.EntityZombie;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombie.class)
public abstract class EntityZombieMixin extends EntityMonster implements IEntity, IEntityMonster {

    public EntityZombieMixin(World world) {
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
