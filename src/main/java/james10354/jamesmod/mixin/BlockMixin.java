package james10354.jamesmod.mixin;

import james10354.jamesmod.util.IBlock;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public abstract class BlockMixin implements IBlock {
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, boolean f) {
    }
}
