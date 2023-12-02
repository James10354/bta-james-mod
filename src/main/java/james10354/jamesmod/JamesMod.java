package james10354.jamesmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.material.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;

public class JamesMod implements ModInitializer {
    public static final String MOD_ID = "jamesmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /*public static final Block block = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.DEFAULT)
            .setTextures("test.png")
            .build(new Block("dirt", 1000, Material.dirt));*/

    @Override
    public void onInitialize() {
        Block.fluidLavaFlowing.withBlastResistance(0);
        Block.fluidLavaStill.withBlastResistance(0);

        LOGGER.info("JamesMod initialized.");
    }
}
