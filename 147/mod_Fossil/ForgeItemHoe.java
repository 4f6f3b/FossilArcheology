// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package mod_Fossil;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemHoe;

// Referenced classes of package net.minecraft.src:
//            Item, EnumToolMaterial, EntityPlayer, World,
//            Block, BlockGrass, StepSound, ItemStack

public class ForgeItemHoe extends ItemHoe
{
    public ForgeItemHoe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }
    public String getTextureFile()
    {
        return FossilCommonProxy.FOS_ITEMS_PNG;
    }
}
