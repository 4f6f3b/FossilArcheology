// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package mod_Fossil;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;

// Referenced classes of package net.minecraft.src:
//            ItemTool, Block, EnumToolMaterial, Material

public class ForgeItemPickaxe extends ItemPickaxe
{
    protected ForgeItemPickaxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }
    public String getTextureFile()
    {
        return FossilCommonProxy.FOS_ITEMS_PNG;
    }
}
