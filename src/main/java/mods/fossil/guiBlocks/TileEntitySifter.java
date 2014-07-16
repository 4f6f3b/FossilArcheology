package mods.fossil.guiBlocks;

import mods.fossil.Fossil;
import mods.fossil.client.LocalizationStrings;
import mods.fossil.fossilEnums.EnumDinoType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileEntitySifter extends TileEntity implements IInventory, ISidedInventory
{

    
    private static final int[] slots_sides = new int[] {}; // input
    private static final int[] slots_bottom = new int[] {1,2,3,4,5};  //output
    private static final int[] slots_top = new int[] {0};//fuel
    
    private String customName;
    
    private ItemStack[] sifterItemStacks;
    public int sifterBurnTime = 0;
    public int currentItemBurnTime = 0;
    public int sifterCookTime = 0;
    private int RawIndex = -1;
    private int SpaceIndex = -1;

    public TileEntitySifter()
    {
        sifterItemStacks = new ItemStack[6];
    }
    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.sifterItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int var1)
    {
        return this.sifterItemStacks[var1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.sifterItemStacks[var1] != null)
        {
            ItemStack var3;

            if (this.sifterItemStacks[var1].stackSize <= var2)
            {
                var3 = this.sifterItemStacks[var1];
                this.sifterItemStacks[var1] = null;
                return var3;
            }
            else
            {
                var3 = this.sifterItemStacks[var1].splitStack(var2);

                if (this.sifterItemStacks[var1].stackSize == 0)
                {
                    this.sifterItemStacks[var1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.sifterItemStacks[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return this.isInvNameLocalized() ? this.customName : "tile." + LocalizationStrings.BLOCK_SIFTER_IDLE + ".name";
    }
    
    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public boolean isInvNameLocalized()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        NBTTagList var2 = var1.getTagList("Items");
        this.sifterItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.sifterItemStacks.length)
            {
                this.sifterItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.sifterBurnTime = var1.getShort("BurnTime");
        this.sifterCookTime = var1.getShort("CookTime");
        this.currentItemBurnTime = 100;
        
        if (var1.hasKey("CustomName"))
        {
            this.customName = var1.getString("CustomName");
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setShort("BurnTime", (short)this.sifterBurnTime);
        var1.setShort("CookTime", (short)this.sifterCookTime);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.sifterItemStacks.length; ++var3)
        {
            if (this.sifterItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.sifterItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        
        if (this.isInvNameLocalized())
        {
        	var1.setString("CustomName", this.customName);
        }

        var1.setTag("Items", var2);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getCookProgressScaled(int var1)
    {
        return this.sifterCookTime * var1 / 200;
    }

    public int getBurnTimeRemainingScaled(int var1)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 100;
        }

        return this.sifterBurnTime * var1 / this.currentItemBurnTime;
    }

    public boolean isBurning()
    {
        return this.sifterBurnTime > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        boolean var1 = this.sifterBurnTime > 0;
        boolean var2 = false;
        if (this.sifterBurnTime > 0)
        {
            --this.sifterBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.sifterBurnTime == 0 && this.canSmelt())
            {
                this.currentItemBurnTime = this.sifterBurnTime = 100;

                if (this.sifterBurnTime > 0)
                {
                    var2 = true;
                }
            }

            if (this.isBurning() && this.canSmelt())
            {
                ++this.sifterCookTime;

                if (this.sifterCookTime == 200)
                {
                    this.sifterCookTime = 0;
                    this.smeltItem();
                    var2 = true;
                }
            }
            else
            {
                this.sifterCookTime = 0;
            }

            if (var1 != this.sifterBurnTime > 0)
            {
                var2 = true;
                BlockSifter.updateFurnaceBlockState(this.sifterBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (var2)
        {
            this.onInventoryChanged();
        }

    }

    private boolean canSmelt()
    {
        this.SpaceIndex = -1;
        this.RawIndex = -1;
        int var1;

        for (var1 = 0; var1 < 1; ++var1)
        {
            if (this.sifterItemStacks[var1] != null)
            {
                int var2 = this.sifterItemStacks[var1].itemID;
                
                ItemStack itemstack = this.sifterItemStacks[var1];

                if ((var2 == Block.sand.blockID)
                		|| (var2 == Block.dirt.blockID)
                		|| (var2 == Block.gravel.blockID)
                		|| (var2 == Block.blockClay.blockID)
                		|| (var2 == Fossil.volcanicAsh.blockID)
                		)
                {
                    this.RawIndex = var1;
                    break;
                }
            }
        }

        if (this.RawIndex == -1)
        {
            return false;
        }
        else
        {
            for (var1 = 5; var1 > 0; --var1)
            {
                if (this.sifterItemStacks[var1] == null)
                {
                    this.SpaceIndex = var1;
                    break;
                }
            }

            return this.SpaceIndex != -1 && this.RawIndex != -1;
        }
    }

    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack result = null;
            int randomloot = (new Random()).nextInt(100);
            int random = (new Random()).nextInt(100);
            int var3;
            
            if (this.sifterItemStacks[this.RawIndex].itemID == Block.sand.blockID
            	|| this.sifterItemStacks[this.RawIndex].itemID ==Block.dirt.blockID
        		|| this.sifterItemStacks[this.RawIndex].itemID ==Block.gravel.blockID
        		|| this.sifterItemStacks[this.RawIndex].itemID ==Block.blockClay.blockID
        		|| this.sifterItemStacks[this.RawIndex].itemID ==Fossil.volcanicAsh.blockID

            ) {
            	if (randomloot < 80){
            		if(Fossil.DebugMode())
            		Fossil.Console("Sifter no result: "+randomloot);
            		if (random < 75){
            			result = null;
            		}
            		else {
            			result = this.sifterItemStacks[this.SpaceIndex];
            		}
            	}
            	else {
            		if(Fossil.DebugMode())
            		Fossil.Console("Sifter successful loot: "+randomloot);
                if (random < 15)
                {
                	result = new ItemStack(Fossil.brokenSapling, 1);
                }
                
                if (random < 30)
                {
                	result = new ItemStack(Fossil.sarracina, 1);
                }

                else if (random < 60)
                {
                	result = new ItemStack(Item.dyePowder, 1, 15);
                }

                else if (random < 80)
                {
                	result = new ItemStack(Block.sand, 1);
                }

                else if (random < 90)
                {
                	result = new ItemStack(Fossil.fernSeed, 2);
                }
                
                else if (random < 95)
                {
                	result = new ItemStack(Fossil.potteryShards, 3);
                }

                else if (random <= 100)
                {
                    int i = (new Random()).nextInt(EnumDinoType.values().length + 1); //+1 for the sapling
                    Item i0 = null;

                    if (i == 0)
                    {
                        i0 = Fossil.brokenSapling;
                    }
                    else
                    {
                        i0 = EnumDinoType.values()[i - 1].DNAItem;
                    }

                    result = new ItemStack(i0, 1);
                }
            	}
            }
            if (result != null) {
            if (result.stackSize != 0 && this.sifterItemStacks[this.SpaceIndex] == null)
            {
                this.sifterItemStacks[this.SpaceIndex] = result.copy();
            }
            else if (this.sifterItemStacks[this.SpaceIndex].isItemEqual(result))
            {
            	sifterItemStacks[this.SpaceIndex].stackSize += result.stackSize;
            }
        }

            --this.sifterItemStacks[0].stackSize;

            if (this.sifterItemStacks[0].stackSize <= 0)
            {
                this.sifterItemStacks[0] = null;
            }
            
        }
    }

    private static int getItemBurnTime(ItemStack var1)
    {
        return 100;
    }

    /**
     * Return true if item is a fuel source (getItemBurnTime() > 0).
     */
    public static boolean isItemFuel(ItemStack par0ItemStack)
    {
        return getItemBurnTime(par0ItemStack) > 0;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return par1 > 8 ? false : (par1 < 8 ? isItemFuel(par2ItemStack) : true);
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return null;
    }
    
    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
    	return par1 == 0 ? isItemFuel(par2ItemStack) : false;
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int par1)
    {
    	return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_sides);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return this.isItemValidForSlot(par1, par2ItemStack);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return par3 != 0 || par1 != 1 || par2ItemStack.itemID == Item.bucketEmpty.itemID;
    }
}
