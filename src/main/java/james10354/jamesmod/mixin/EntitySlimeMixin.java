package james10354.jamesmod.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.EntitySlime;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntitySlime.class)
public abstract class EntitySlimeMixin extends Entity {

    public EntitySlimeMixin(World world) {
        super(world);
    }

    @Shadow public abstract int getSlimeSize();

    @Unique private boolean isStuck = false;

    @Override
    public boolean collidesWith(Entity entity) {
        return !this.isStuck || !(entity instanceof EntityPlayer);
    }

    @Inject(method = "updatePlayerActionState", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/monster/EntitySlime;faceEntity(Lnet/minecraft/core/entity/Entity;FF)V"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void stickToPlayer(CallbackInfo ci, EntityPlayer entityplayer, boolean targetPlayer) {
        if (this.distanceTo(entityplayer) > 0.8 * this.getSlimeSize() || ((EntitySlime)(Object)this).hurtTime > 0) {
            this.isStuck = false;
        }

        if (((EntitySlime)(Object)this).distanceTo(entityplayer) < 0.8 * this.getSlimeSize() && random.nextInt(8) == 0) {
            this.isStuck = true;
        }

        if (this.isStuck) {
            this.xd = (entityplayer.x - this.x) / 2;
            this.yd = (entityplayer.y - 0.6 - this.y) / 2;
            this.zd = (entityplayer.z - this.z) / 2;
        }
    }
}
