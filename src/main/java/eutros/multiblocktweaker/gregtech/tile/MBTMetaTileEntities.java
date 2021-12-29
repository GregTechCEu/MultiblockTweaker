package eutros.multiblocktweaker.gregtech.tile;

import crafttweaker.annotations.ModOnly;
import eutros.multiblocktweaker.MultiblockTweaker;
import eutros.multiblocktweaker.gregtech.tile.holders.BotaniaManaHatchHolder;
import eutros.multiblocktweaker.gregtech.tile.holders.ThaumcraftEssentiaHatchHolder;
import eutros.multiblocktweaker.gregtech.tile.part.TileBotaniaManaHatch;
import eutros.multiblocktweaker.gregtech.tile.part.TileForgeEnergyHatch;
import eutros.multiblocktweaker.gregtech.tile.part.TileMekanismHeatHatch;
import eutros.multiblocktweaker.gregtech.tile.part.TileQMDParticleHatch;
import eutros.multiblocktweaker.gregtech.tile.part.TileThaumcraftEssentiaHatch;
import gregtech.api.GTValues;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;

public class MBTMetaTileEntities {
    public static final TileForgeEnergyHatch[] FORGE_ENERGY_INPUT_HATCH = new TileForgeEnergyHatch[8];
    public static final TileForgeEnergyHatch[] FORGE_ENERGY_OUTPUT_HATCH = new TileForgeEnergyHatch[8];

    public static final TileBotaniaManaHatch[] BOTANIA_MANA_INPUT_HATCH = new TileBotaniaManaHatch[4];
    public static final TileBotaniaManaHatch[] BOTANIA_MANA_OUTPUT_HATCH = new TileBotaniaManaHatch[4];

    public static final TileThaumcraftEssentiaHatch[] THAUMCRAFT_ESSENTIA_INPUT_HATCH = new TileThaumcraftEssentiaHatch[4];
    public static final TileThaumcraftEssentiaHatch[] THAUMCRAFT_ESSENTIA_OUTPUT_HATCH = new TileThaumcraftEssentiaHatch[4];

    public static final TileMekanismHeatHatch[] MEKANISM_HEAT_HATCH = new TileMekanismHeatHatch[4];

    public static final TileQMDParticleHatch[] QMD_PARTICLE_INPUT_HATCH = new TileQMDParticleHatch[4];
    public static final TileQMDParticleHatch[] QMD_PARTICLE_OUTPUT_HATCH = new TileQMDParticleHatch[4];

    public static void init() {
        registerTileEntity();
        int id = 10500;
        for (int i = 0; i < 8; i++) {
            FORGE_ENERGY_INPUT_HATCH[i] = registerMetaTileEntity(id++, new TileForgeEnergyHatch(getMBTId("forge_energy_hatch.input." + GTValues.VN[i]), i, false));
            FORGE_ENERGY_OUTPUT_HATCH[i] = registerMetaTileEntity(id++, new TileForgeEnergyHatch(getMBTId("forge_energy_hatch.output." + GTValues.VN[i]), i,true));
        }
        for (int i = 1; i < 5; i++) {
            BOTANIA_MANA_INPUT_HATCH[i - 1] = registerMetaTileEntity(id++, new TileBotaniaManaHatch(getMBTId("botania_mana_hatch.input." + GTValues.VN[i]), i, false));
            BOTANIA_MANA_OUTPUT_HATCH[i - 1] = registerMetaTileEntity(id++, new TileBotaniaManaHatch(getMBTId("botania_mana_hatch.output." + GTValues.VN[i]), i,true));
        }
        for (int i = 1; i < 5; i++) {
            THAUMCRAFT_ESSENTIA_INPUT_HATCH[i - 1] = registerMetaTileEntity(id++, new TileThaumcraftEssentiaHatch(getMBTId("thaumcraft_essentia_hatch.input." + GTValues.VN[i]), i, false));
            THAUMCRAFT_ESSENTIA_OUTPUT_HATCH[i - 1] = registerMetaTileEntity(id++, new TileThaumcraftEssentiaHatch(getMBTId("thaumcraft_essentia_hatch.output." + GTValues.VN[i]), i,true));
        }
        for (int i = 1; i < 5; i++) {
            MEKANISM_HEAT_HATCH[i - 1] = registerMetaTileEntity(id++, new TileMekanismHeatHatch(getMBTId("mekanism_heat_hatch." + GTValues.VN[i]), i));
        }
        for (int i = 1; i < 5; i++) {
            QMD_PARTICLE_INPUT_HATCH[i - 1] = registerMetaTileEntity(id++, new TileQMDParticleHatch(getMBTId("qmd_particle_hatch.input." + GTValues.VN[i]), i, false));
            QMD_PARTICLE_OUTPUT_HATCH[i - 1] = registerMetaTileEntity(id++, new TileQMDParticleHatch(getMBTId("qmd_particle_hatch.output." + GTValues.VN[i]), i, true));
        }
    }

    public static void registerTileEntity() {
        GameRegistry.registerTileEntity(BotaniaManaHatchHolder.class, new ResourceLocation(MultiblockTweaker.MOD_ID, "machine_botania"));
        GameRegistry.registerTileEntity(ThaumcraftEssentiaHatchHolder.class, new ResourceLocation(MultiblockTweaker.MOD_ID, "machine_thaumcraft"));
    }

    private static ResourceLocation getMBTId(String name) {
        return new ResourceLocation(MultiblockTweaker.MOD_ID, name);
    }
}
