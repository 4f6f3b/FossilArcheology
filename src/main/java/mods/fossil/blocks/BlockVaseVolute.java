package mods.fossil.blocks;

import java.util.List;
import java.util.Random;

import mods.fossil.Fossil;
import mods.fossil.guiBlocks.TileEntityVase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockVaseVolute extends BlockContainer
{
    public static final String[] longname =
    {
        "Damaged Volute",
        "Restored Volute",
        "Red-Figure Volute",
        "Black-Figure Volute",
        "Porcelain Volute",
    };
    public static final String[] shortname =
    {
        "damaged_volute",
        "restored_volute",
        "redFigure_volute",
        "blackFigure_volute",
        "porcelain_volute",
    };

    private Icon[] icons;
	private int getMeta;

    public BlockVaseVolute(int par1)
    {
        super(par1, Material.clay);
        this.setCreativeTab(Fossil.tabFFigurines);
    }

    
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubBlocks(int par1, CreativeTabs creativetabs, List list)
    {
        for (int j = 0; j < shortname.length; ++j)
        {
            list.add(new ItemStack(par1, 1, j));
        }
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return -1;
    }
    
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();

            return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 0.9F, 0.9F);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 1.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
        TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
        ((TileEntityVase)tileentity).setVaseTypeMeta(par6ItemStack.getItemDamage());
        ((TileEntityVase)tileentity).setVaseType(0);
        ((TileEntityVase)tileentity).setVaseRotation(1);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int oldBlock, int oldMeta)
    {
        TileEntity tileentity = world.getBlockTileEntity(x, y, z);
        this.getMeta = getDamageValue(world, x, y, z);
        super.breakBlock(world, x, y, z, oldBlock, oldMeta);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityVase();
    }

    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return this.blockID;
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World par1World, int par2, int par3, int par4)
    {
        TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
        return tileentity != null && tileentity instanceof TileEntityVase ? ((TileEntityVase)tileentity).getVaseTypeMeta() : super.getDamageValue(par1World, par2, par3, par4);
    }
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    
    @Override
    public int damageDropped(int meta)
    {
        return this.getMeta;
    }
    
    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
    	return this.blockID;
    }

    @SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int meta)
    {
        return icons[meta];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister)
    {
        icons = new Icon[shortname.length];

        for (int i = 0; i < shortname.length; ++i)
        {
        	
           // icons[i] = iconregister.registerIcon(Fossil.modid + ":figurines/" + (this.getUnlocalizedName().substring(5)) + i);
            icons[i] = iconregister.registerIcon(Fossil.modid + ":vases/icons/" + "vase_icon_volute_" + i);
        }
    }
    
}
