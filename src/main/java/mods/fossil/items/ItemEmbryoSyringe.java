package mods.fossil.items;

import mods.fossil.Fossil;
import mods.fossil.entity.mob.EntityPregnantCow;
import mods.fossil.entity.mob.EntityPregnantHorse;
import mods.fossil.entity.mob.EntityPregnantPig;
import mods.fossil.entity.mob.EntityPregnantSheep;
import mods.fossil.fossilEnums.EnumAnimalType;
import mods.fossil.fossilInterface.IViviparous;
import mods.fossil.handler.FossilAchievementHandler;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemEmbryoSyringe extends Item
{
    //private String[] ItemNames = new String[] {"EmbyoPig", "EmbyoSheep", "EmbyoCow", "EmbyoSmilodon", "EmbyoMammoth"};
    int AnimalType;
    public ItemEmbryoSyringe(int var1, int AnimalType0)
    {
        super(var1);
        //this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.maxStackSize = 64;
        this.AnimalType = AnimalType0;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("fossil:" + EnumAnimalType.values()[AnimalType].name() + "_Syringe");
    }

    /*
     * Gets an icon index based on an item's damage value
     */
    //public int getIconFromDamage(int var1)
    //{
    //    return var1;
    //}

    //public String getItemNameIS(ItemStack var1)
    //{
    //    int var2 = var1.getItemDamage();
    //    return var2 < this.ItemNames.length ? this.ItemNames[var2] : "EmbyoSyring";
    //}

    public static EnumAnimalType GetEmbryo(int var0)
    {
        return EnumAnimalType.values()[var0];
    }

    /**
     * dye sheep, place saddles, etc ...
     */
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase thisEntity)
    {
        if (thisEntity instanceof EntityAnimal && ((EntityAnimal)thisEntity).getGrowingAge() == 0)
        {
            Object pregnantEntity = null;

            if (thisEntity instanceof EntityPig)
            {
                pregnantEntity = new EntityPregnantPig(thisEntity.worldObj);
            }

            if (thisEntity instanceof EntityCow)
            {
                pregnantEntity = new EntityPregnantCow(thisEntity.worldObj);
            }

            if (thisEntity instanceof EntitySheep)
            {
                pregnantEntity = new EntityPregnantSheep(thisEntity.worldObj);
                ((EntitySheep)pregnantEntity).setFleeceColor(((EntitySheep)thisEntity).getFleeceColor());
                ((EntitySheep)pregnantEntity).setSheared(((EntitySheep)thisEntity).getSheared());
            }
            
            if (thisEntity instanceof EntityHorse)
            {
            	
                if ( ((EntityHorse)thisEntity).getHorseType() != 0 )
                {
                	return false;
                }
                pregnantEntity = new EntityPregnantHorse(thisEntity.worldObj);

                
                ((EntityHorse)pregnantEntity).setHorseType(((EntityHorse)thisEntity).getHorseType());
                ((EntityHorse)pregnantEntity).setHorseVariant(((EntityHorse)thisEntity).getHorseVariant());
                ((EntityHorse)pregnantEntity).setHorseTamed(((EntityHorse)thisEntity).isTame());
                ((EntityHorse)pregnantEntity).setHorseSaddled(((EntityHorse)thisEntity).isHorseSaddled());
        		((EntityHorse)pregnantEntity).setOwnerName(((EntityHorse)thisEntity).getOwnerName());
        		((EntityHorse)pregnantEntity).setHorseTamed(((EntityHorse)thisEntity).isTame());
        		((EntityHorse)pregnantEntity).setTemper(((EntityHorse)thisEntity).getTemper());
        		((EntityHorse)pregnantEntity).getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute((((EntityHorse)thisEntity).getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue()));
        		((EntityHorse)pregnantEntity).getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute((((EntityHorse)thisEntity).getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
        		((EntityHorse)pregnantEntity).setGrowingAge(((EntityHorse)thisEntity).getGrowingAge());
            }

            if (pregnantEntity != null)
            {
                EnumAnimalType e0 = null;

                if (itemstack.itemID == Fossil.embryoQuagga.itemID && pregnantEntity instanceof EntityPregnantHorse)
                    e0 = EnumAnimalType.Quagga;
                
                if (itemstack.itemID == Fossil.embryoChicken.itemID)
                {
                    e0 = EnumAnimalType.Chicken;
                }

                if (itemstack.itemID == Fossil.embryoCow.itemID)
                {
                    e0 = EnumAnimalType.Cow;
                }
                
                if (itemstack.itemID == Fossil.embryoHorse.itemID)
                {
                    e0 = EnumAnimalType.Horse;
                }

                if (itemstack.itemID == Fossil.embryoMammoth.itemID)
                {
                    e0 = EnumAnimalType.Mammoth;
                }

                if (itemstack.itemID == Fossil.embryoPig.itemID)
                {
                    e0 = EnumAnimalType.Pig;
                }

                if (itemstack.itemID == Fossil.embryoSmilodon.itemID)
                {
                    e0 = EnumAnimalType.Smilodon;
                }

                if (itemstack.itemID == Fossil.embryoSheep.itemID)
                {
                    e0 = EnumAnimalType.Sheep;
                }
                if (e0 != null)
                {
	                ((IViviparous)pregnantEntity).SetEmbryo(e0);
	                ((EntityAnimal)pregnantEntity).setLocationAndAngles(thisEntity.posX, thisEntity.posY, thisEntity.posZ, thisEntity.rotationYaw, thisEntity.rotationPitch);
	                thisEntity.setDead();
	
	                if (!thisEntity.worldObj.isRemote)
	                {
	                    thisEntity.worldObj.spawnEntityInWorld((EntityAnimal)pregnantEntity);
	                }
	
	                --itemstack.stackSize;
                }
                else
                	return false;
            }

            player.triggerAchievement(FossilAchievementHandler.IceAge);
            return true;
        }

        return false;
    }
}
