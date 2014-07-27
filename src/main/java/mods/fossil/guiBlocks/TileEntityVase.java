package mods.fossil.guiBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityVase extends TileEntity
{
    /** Vase Type (volute/amphora/kylix) */
    private int vaseType;
    
    /** metadata for vase type */
    private int vaseTypeMeta;

    /** The figurine's rotation. */
    private int vaseRotation;

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setByte("VaseType", (byte)(this.vaseType & 255));
        par1NBTTagCompound.setByte("VaseTypeMeta", (byte)(this.vaseTypeMeta & 255));
        par1NBTTagCompound.setByte("Rot", (byte)(this.vaseRotation & 255));
    }
    
    @Override
    public boolean canUpdate()
    {
        return false;
    }

    
    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.vaseType = par1NBTTagCompound.getByte("VaseType");
        this.vaseRotation = par1NBTTagCompound.getByte("Rot");
        this.vaseTypeMeta = par1NBTTagCompound.getByte("VaseTypeMeta");
    }

    /**
     * Overriden in a sign to provide the text.
     */
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 5, nbttagcompound);
    }

    @Override
    public void onDataPacket(INetworkManager netManager, Packet132TileEntityData packet)
    {
        readFromNBT(packet.data);
    }

    /**
     * Set the entity type for the vase
     */
    public void setVaseType(int par1)
    {
        this.vaseType = par1;
    }

    /**
     * Get the entity type for the vase
     */
    public int getVaseType()
    {
        return this.vaseType;
    }
    
    /**
     * Set the meta for the vase
     */
    public void setVaseTypeMeta(int par1)
    {
        this.vaseTypeMeta = par1;
    }

    /**
     * Get the meta for the vase
     */
    public int getVaseTypeMeta()
    {
        return this.vaseTypeMeta;
    }

    /**
     * Set the vase's rotation
     */
    public void setVaseRotation(int par1)
    {
        this.vaseRotation = par1;
    }

    @SideOnly(Side.CLIENT)
    public int func_82119_b()
    {
        return this.vaseRotation;
    }
}
