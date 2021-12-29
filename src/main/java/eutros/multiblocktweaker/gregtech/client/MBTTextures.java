package eutros.multiblocktweaker.gregtech.client;

import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;

public class MBTTextures {
    public static ICubeRenderer FORGE_ENERGY_IN;
    public static ICubeRenderer FORGE_ENERGY_OUT;

    public static ICubeRenderer[] BOTANIA_MANA_BASE;
    public static ICubeRenderer BOTANIA_MANA_IN;
    public static ICubeRenderer BOTANIA_MANA_OUT;

    public static ICubeRenderer[] THAUMCRAFT_ESSENTIA_BASE;
    public static ICubeRenderer THAUMCRAFT_ESSENTIA_IN;
    public static ICubeRenderer THAUMCRAFT_ESSENTIA_OUT;

    public static ICubeRenderer[] MEKANISM_HEAT_BASE;
    public static ICubeRenderer MEKANISM_HEAT_IO;

    public static ICubeRenderer QMD_PARTICLE_BASE;
    public static ICubeRenderer QMD_PARTICLE_IN;
    public static ICubeRenderer QMD_PARTICLE_OUT;


    public static void init() {
        FORGE_ENERGY_IN = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/forge_energy/overlay_forge_energy_in");
        FORGE_ENERGY_OUT = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/forge_energy/overlay_forge_energy_out");

        BOTANIA_MANA_BASE = new ICubeRenderer[4];
        for (int i = 0; i < 4; i++) {
            BOTANIA_MANA_BASE[i] = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/botania/overlay_living_rock_" + i);
        }
        BOTANIA_MANA_IN = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/botania/overlay_botania_mana_in");
        BOTANIA_MANA_OUT = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/botania/overlay_botania_mana_out");

        THAUMCRAFT_ESSENTIA_BASE = new ICubeRenderer[4];
        for (int i = 0; i < 4; i++) {
            THAUMCRAFT_ESSENTIA_BASE[i] = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/thaumcraft/overlay_ancient_stone_" + i);
        }
        THAUMCRAFT_ESSENTIA_IN = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/botania/overlay_botania_mana_in");
        THAUMCRAFT_ESSENTIA_OUT = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/botania/overlay_botania_mana_out");

        MEKANISM_HEAT_BASE = new ICubeRenderer[4];
        for (int i = 0; i < 4; i++) {
            MEKANISM_HEAT_BASE[i] = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/mekanism/overlay_induction_cell_" + i);
        }
        MEKANISM_HEAT_IO = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/mekanism/overlay_induction_cell_io");

        QMD_PARTICLE_BASE = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/qmd/overlay_beam_base");
        QMD_PARTICLE_IN = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/qmd/overlay_beam_input");
        QMD_PARTICLE_OUT = new SimpleOverlayRenderer("multiblocktweaker:overlay/part/qmd/overlay_beam_output");
    }
}
