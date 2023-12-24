package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IBlock;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.Explosion;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow private boolean destroyBlocks;
    @Shadow private boolean isFlaming;
    @Shadow public Set<ChunkPosition> destroyedBlockPositions;

    @Shadow protected abstract void createFire();

    @Inject(method = "doExplosionA", at = @At(value = "FIELD", target = "Lnet/minecraft/core/world/Explosion;isFlaming:Z"), cancellable = true, remap = false)
    public void removeCreateFire(CallbackInfo ci) { ci.cancel(); }

    @Inject(method = "doExplosionB", at = @At("TAIL"), remap = false)
    private void putCreateFire(CallbackInfo ci) {
        if (this.destroyBlocks && this.isFlaming) {
            this.createFire();
        }
    }

    @Redirect(method = "doExplosionB", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;onBlockDestroyedByExplosion(Lnet/minecraft/core/world/World;III)V"), remap = false)
    private void destroyedByExplosion(Block block, World world, int x, int y, int z) {
        ((IBlock)block).onBlockDestroyedByExplosion(world, x, y, z, this.isFlaming);
    }

    @Inject(method = "calculateBlocksToDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/chunk/ChunkPosition;<init>(III)V"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void ignoreLava(CallbackInfo ci, int i, int j, int l, int j1, double d, double d1, double d2, double d3, float f1, double d5, double d7, double d9, float f2, int j4, int k4, int l4, int i5) {
        if (i5 != 272 && i5 != 273) this.destroyedBlockPositions.add(new ChunkPosition(j4, k4, l4));
    }

    @Redirect(method = "calculateBlocksToDestroy", at = @At(value = "FIELD", target = "Lnet/minecraft/core/world/Explosion;destroyedBlockPositions:Ljava/util/Set;"), remap = false)
    private Set<ChunkPosition> destroyBlocksDefaultRemove(Explosion instance) {
        return new HashSet();
    }

    @Inject(method = "damageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Entity;hurt(Lnet/minecraft/core/entity/Entity;ILnet/minecraft/core/util/helper/DamageType;)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void fireDamage(CallbackInfo ci, float explosionSize2, int x1, int x2, int y1, int y2, int z1, int z2, List list, Vec3d vec3d, int k2, Entity entity, double d4, double d6, double d8, double d10, double d11, double d12, double d13) {
        if (this.isFlaming) entity.fireHurt();
    }
}
