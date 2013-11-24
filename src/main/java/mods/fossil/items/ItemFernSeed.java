package mods.fossil.items;

import java.util.Random;

import mods.fossil.blocks.BlockFern;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFernSeed extends Item
{
    private int matchingBlockID;

    public ItemFernSeed(int var1, int var2)
    {
        super(var1);
        this.matchingBlockID = var2;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        if (var7 != 1)
        {
            return true;
        }
        else
        {
            int var11 = var3.getBlockId(var4, var5, var6);

            if (var11 == Block.grass.blockID && var3.isAirBlock(var4, var5 + 1, var6) && BlockFern.CheckUnderTree(var3, var4, var5, var6))
            {
                var3.setBlock(var4, var5 + 1, var6, this.matchingBlockID);
                var3.setBlockMetadataWithNotify(var4, var5 + 1, var6, (new Random()).nextInt(2)*8,2);
                --var1.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    @Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("fossil:Fern_Seeds");
    }

}
