package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IEntityLiving;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin implements IEntityLiving {

    @Unique protected float attackStrength = 1.0F;
    @Unique protected int deathSaveTimer = 0;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injected(CallbackInfo ci) {
        this.setMoveSpeed(1.0F);
    }

    @Redirect(method = "damageEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityLiving;damageEntity(ILnet/minecraft/core/util/helper/DamageType;)V"), remap = false)
    private void takeDamage(EntityLiving instance, int i, DamageType damageType) {
        ((EntityPlayer)(Object)this).health -= i;

        if (((EntityPlayer)(Object)this).health < 1 && ((EntityPlayer)(Object)this).prevHealth != 1 && damageType == DamageType.COMBAT) {
            ((EntityPlayer)(Object)this).health = 1;
            this.setMoveSpeed(1.4F);
            this.attackStrength = 1.3F;
            this.deathSaveTimer = 30;
        }
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"), remap = false)
    private void lastStand(CallbackInfo ci) {
        if (this.deathSaveTimer > 0) {
            this.deathSaveTimer--;
        }
        if (this.deathSaveTimer == 1) {
            this.setMoveSpeed(1.0F);
            this.attackStrength = 1.0F;
        }
    }

    @Redirect(method = "hurt", at = @At(value = "FIELD", target = "Lnet/minecraft/core/player/gamemode/Gamemode;isPlayerInvulnerable:Z"), remap = false)
    private boolean isPlayerInvulnerable(Gamemode instance) {
        return ((EntityPlayer)(Object)this).gamemode.isPlayerInvulnerable || deathSaveTimer > 0;
    }

    @Redirect(method = "attackTargetEntityWithCurrentItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/InventoryPlayer;getDamageVsEntity(Lnet/minecraft/core/entity/Entity;)I"), remap = false)
    private int useAttackStrength(InventoryPlayer inventory, Entity entity) {
        return (int)(inventory.getDamageVsEntity(entity) * this.attackStrength);
    }
}
