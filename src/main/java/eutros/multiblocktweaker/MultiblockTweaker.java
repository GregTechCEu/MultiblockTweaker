package eutros.multiblocktweaker;

import eutros.multiblocktweaker.gregtech.client.MBTTextures;
import eutros.multiblocktweaker.gregtech.tile.MBTMetaTileEntities;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MultiblockTweaker.MOD_ID,
        name = "Multiblock Tweaker",
        version = "@GRADLE:VERSION@",
        dependencies = "required-after:gregtech@[2.0.0-alpha,);")
public class MultiblockTweaker {

    public static final String MOD_ID = "multiblocktweaker";

    public MultiblockTweaker() {
    }

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent evt) {
        MBTTextures.init();
        MBTMetaTileEntities.init();
    }

}
