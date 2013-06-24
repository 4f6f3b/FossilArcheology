package mods.fossil.items.forge;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemFood;

public class ForgeFood extends ItemFood
{
	String TextureFileName;
	public ForgeFood(int par1, int par2, float par3, boolean par4,String TextureFileName0)
	{
		super(par1,par2,par3,par4);
		this.TextureFileName=TextureFileName0;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("fossil:"+TextureFileName);
    }

}
