package mods.fossil.items;

import java.util.List;

import mods.fossil.Fossil;
import mods.fossil.client.model.armor.ModelHeadbandAztec;
import mods.fossil.client.model.armor.ModelHeadbandRoman;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHeadRelic extends ItemArmor
{
	ModelBiped headband = new ModelHeadbandRoman();
	
	
    @SideOnly(Side.CLIENT)
    private Icon[] headbandIcons;
    public static final String[] headbandItemNames = new String[] {"broken", "roman"};

    
    public ItemHeadRelic(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4)
    {
        super(par1, par2EnumArmorMaterial, par3, par4);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int par1)
    {
        int j = MathHelper.clamp_int(par1, 0, 15);
        return this.headbandIcons[j];
    }
    
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 1);
        return super.getUnlocalizedName() + "." + headbandItemNames[i];
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int j = 0; j < 2; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }
    
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
    	if(slot == 0)
        return "fossil:textures/armor/headband_master.png";
    	
    	return null;
    }
    

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.headbandIcons = new Icon[headbandItemNames.length];

        for (int i = 0; i < headbandItemNames.length; ++i)
        {

            this.headbandIcons[i] = par1IconRegister.registerIcon("fossil:headrelic_"+headbandItemNames[i]);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving,
    ItemStack itemStack, int armorSlot) {

    ModelBiped armorModel = null;
    	
    if(itemStack != null){
    	if(itemStack.getItem() instanceof ItemHeadRelic){
    	int type = ((ItemArmor)itemStack.getItem()).armorType;

    	                         if(type == 1 || type == 3){
    	                                 armorModel = Fossil.proxy.getArmorModel(0);
    	                         }else{
    	                                 armorModel = Fossil.proxy.getArmorModel(1);
    	                         }
    	}
    	if(armorModel != null){
    	armorModel.bipedHead.showModel = armorSlot == 0;
    	armorModel.bipedHeadwear.showModel = armorSlot == 0;
    	armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
    	armorModel.bipedRightArm.showModel = armorSlot == 1;
    	armorModel.bipedLeftArm.showModel = armorSlot == 1;
    	armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
    	armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;

    	armorModel.isSneak = entityLiving.isSneaking();
    	armorModel.isRiding = entityLiving.isRiding();
    	armorModel.isChild = entityLiving.isChild();
    	armorModel.heldItemRight = entityLiving.getCurrentItemOrArmor(0) != null ? 1 :0;
    	if(entityLiving instanceof EntityPlayer){
    	armorModel.aimedBow =((EntityPlayer)entityLiving).getItemInUseDuration() > 2;
    	}
    	return armorModel;
    	}
    	}

    	return null;
    	}
	
}