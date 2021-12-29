package eutros.multiblocktweaker.gregtech.tile.part;


import eutros.multiblocktweaker.gregtech.tile.holders.AbstractHatchHolder;
import gregtech.api.GTValues;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.SimpleTextWidget;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractTileModHatch<T> extends MetaTileEntityMultiblockPart implements IMultiblockAbilityPart<T> {
    public final boolean isOutputHatch;
    public final MultiblockAbility<T> INPUT_ABILITY;
    public final MultiblockAbility<T> OUTPUT_ABILITY;
    public final Class<? extends AbstractHatchHolder<?>> clazz;
    protected final String unit;

    public AbstractTileModHatch(ResourceLocation metaTileEntityId,
                                int tier,
                                boolean isOutputHatch,
                                MultiblockAbility<T> input_ability,
                                MultiblockAbility<T> output_ability,
                                Class<? extends AbstractHatchHolder<?>> clazz,
                                String unit) {
        super(metaTileEntityId, tier);
        this.isOutputHatch = isOutputHatch;
        INPUT_ABILITY = input_ability;
        OUTPUT_ABILITY = output_ability;
        this.clazz = clazz;
        this.unit = unit;
    }

    public AbstractTileModHatch(ResourceLocation metaTileEntityId,
                                int tier, MultiblockAbility<T> ability,
                                Class<? extends AbstractHatchHolder<?>> clazz,
                                String unit) {
        this(metaTileEntityId, tier, false, ability, null, clazz, unit);
    }

    int getCapacityByTier() {
        return 0;
    }

    int getMaxTransferByTier() {
        return 0;
    }

    int getAmount() {
        return 0;
    }

    @Override
    public void update() {
        if (clazz != null) {
            World world = getWorld();
            if (world != null && !clazz.isInstance(getHolder())) {
                if (world.isRemote) {
                    this.scheduleRenderUpdate();
                } else {
                    this.notifyBlockUpdate();
                }
                BlockPos pos = getPos();
                world.removeTileEntity(pos);
                AbstractHatchHolder<?> holder = null;
                try {
                    holder = clazz.newInstance();
                    holder.setAbstractTileModHatch(this);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                world.setTileEntity(pos, holder);

            }
        }
        super.update();
    }

    @Override
    protected boolean openGUIOnRightClick() {
        return true;
    }

    @Override
    public ICubeRenderer getBaseTexture() {
        MultiblockControllerBase controller = getController();
        if (controller != null) {
            this.hatchTexture = controller.getBaseTexture(this);
        }
        if (controller == null && this.hatchTexture != null) {
            return this.hatchTexture;
        }
        if (controller == null) {
            this.setPaintingColor(DEFAULT_PAINTING_COLOR);
            return getUnFormedBaseTexture();
        }
        this.setPaintingColor(0xFFFFFF);
        return controller.getBaseTexture(this);
    }

    public ICubeRenderer getUnFormedBaseTexture() {
        return Textures.VOLTAGE_CASINGS[getTier()];
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return ModularUI.builder(GuiTextures.BACKGROUND, 300, 150)
                .label(5, 5, this.getMetaFullName())
                .label(5, 25, getCapacityByTier() + "")
                .widget(new SimpleTextWidget(5, 50, "", 0xffff0000, () -> "" + getAmount() ))
                .build(this.getHolder(), entityPlayer);
    }

    @Override
    public MultiblockAbility<T> getAbility() {
        return OUTPUT_ABILITY == null ? INPUT_ABILITY : isOutputHatch ? OUTPUT_ABILITY : INPUT_ABILITY;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        String tierName = GTValues.VN[getTier()];
        String unit = I18n.format("multiblocktweaker.machine.part." + this.unit);
        if (OUTPUT_ABILITY != null) {
            if (isOutputHatch) {
                tooltip.add(I18n.format("multiblocktweaker.machine.part.output.tooltip", unit));
            } else {
                tooltip.add(I18n.format("multiblocktweaker.machine.part.input.tooltip", unit));
            }
        }
        tooltip.add(I18n.format("multiblocktweaker.machine.part.capacity", unit, getCapacityByTier(), unit, tierName));
        tooltip.add(I18n.format("multiblocktweaker.machine.part.maxTransfer", unit, getMaxTransferByTier()));
        tooltip.add(I18n.format("gregtech.universal.enabled"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerAbilities(List<T> abilityList) {
        abilityList.add((T)this);
    }
}
