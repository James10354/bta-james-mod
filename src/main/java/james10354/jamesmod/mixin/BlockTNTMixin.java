package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IBlock;
import net.minecraft.core.block.BlockTNT;
import net.minecraft.core.entity.EntityTNT;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockTNT.class)
public abstract class BlockTNTMixin implements IBlock {
        @Shadow public abstract void onBlockDestroyedByExplosion(World world, int x, int y, int z);

        @Override
        public void onBlockDestroyedByExplosion(World world, int x, int y, int z, boolean isFireExplosion) {
                this.onBlockDestroyedByExplosion(world, x, y, z);
                if (isFireExplosion) world.getLoadedEntityList().get(world.getLoadedEntityList().size() - 1).remainingFireTicks = 300;
        }
}
