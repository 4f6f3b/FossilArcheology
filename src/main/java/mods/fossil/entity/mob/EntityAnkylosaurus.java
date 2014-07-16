package mods.fossil.entity.mob;

import mods.fossil.Fossil;
import mods.fossil.client.DinoSound;
import mods.fossil.fossilAI.DinoAIAttackOnCollide;
import mods.fossil.fossilAI.DinoAIEat;
import mods.fossil.fossilAI.DinoAIFollowOwner;
import mods.fossil.fossilAI.DinoAIRideGround;
import mods.fossil.fossilAI.DinoAIWander;
import mods.fossil.fossilEnums.EnumDinoType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityAnkylosaurus extends EntityDinosaur
{
    public boolean isTamed = false;

//    final float PUSHDOWN_HARDNESS = 5.0F;
    //final EntityAIControlledByPlayer aiControlledByPlayer;
    
    public static final double baseHealth = EnumDinoType.Ankylosaurus.Health0;
    public static final double baseDamage = EnumDinoType.Ankylosaurus.Strength0;
    public static final double baseSpeed = EnumDinoType.Ankylosaurus.Speed0;
    
    public static final double maxHealth = EnumDinoType.Ankylosaurus.HealthMax;
    public static final double maxDamage = EnumDinoType.Ankylosaurus.StrengthMax;
    public static final double maxSpeed = EnumDinoType.Ankylosaurus.SpeedMax;

    public EntityAnkylosaurus(World var1)
    {
        super(var1, EnumDinoType.Ankylosaurus);
        this.updateSize();
        /*
         * EDIT VARIABLES PER DINOSAUR TYPE
         */
        this.adultAge = EnumDinoType.Ankylosaurus.AdultAge;
        // Set initial size for hitbox. (length/width, height)
        this.setSize(1.1F, 1.0F);
        // Size of dinosaur at day 0.
        this.minSize = 1.0F;
        // Size of dinosaur at age Adult.
        this.maxSize = 3.0F;
        
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        //this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.0F));
        this.tasks.addTask(4, new DinoAIAttackOnCollide(this, 1.1D, true));
        this.tasks.addTask(5, new DinoAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(7, new DinoAIWander(this, 1.0D));
        this.tasks.addTask(7, new DinoAIEat(this, 100));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        tasks.addTask(1, new DinoAIRideGround(this, 1)); // mutex all
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        //this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    /**
     * Return the AI task for player control.
     */
    /*
    public EntityAIControlledByPlayer getAIControlledByPlayer()
    {
        return this.aiControlledByPlayer;
    }
    */

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(EnumDinoType.Ankylosaurus.Speed0);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(EnumDinoType.Ankylosaurus.Health0);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(EnumDinoType.Ankylosaurus.Strength0);
    }

    /**
     * Returns the texture's file path as a String.
     */
    @Override
    public String getTexture()
    {
        if (this.isModelized())
        {
            return super.getTexture();
        }

        switch (this.getSubSpecies())
        {
            default:
                return "fossil:textures/mob/Ankylosaurus.png";
        }
    }
    
    public int getVerticalFaceSpeed()
    {
        return this.isSitting() ? 70 : super.getVerticalFaceSpeed();
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        //Add special item interaction code here
        return super.interact(var1);
    }

    public EntityAnkylosaurus spawnBabyAnimal(EntityAnimal var1)
    {
        return new EntityAnkylosaurus(this.worldObj);
    }

    public float getEyeHeight()
    {
        return (float)this.getDinoAge() / 3.2F;
    }

    public float getHalfHeight()
    {
        return this.getEyeHeight();
    }

    public float getMountHeight()
    {
        return this.height;
    }
    
    public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
        	 this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountHeight() + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }
    
    /**
     * This gets called when a dinosaur grows naturally or through Chicken Essence.
     */
    @Override
    public void updateSize()
    {

        double healthStep;
        double attackStep;
        double speedStep;
        healthStep = (this.maxHealth - this.baseHealth) / (this.adultAge + 1);
        attackStep = (this.maxDamage - this.baseDamage) / (this.adultAge + 1);
        speedStep = (this.maxSpeed - this.baseSpeed) / (this.adultAge + 1);
        
        
    	if(this.getDinoAge() <= this.adultAge){
    		
	        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(Math.round(this.baseHealth + (healthStep * this.getDinoAge())));
	        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(Math.round(this.baseDamage + (attackStep * this.getDinoAge())));
	        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(this.baseSpeed + (speedStep * this.getDinoAge()));
	        
	        if (this.isTeen()) {
	        	this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setAttribute(0.5D);
	        }
	        else if (this.isAdult()){
	            this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setAttribute(2.0D);
	        }
	        else {
	            this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setAttribute(0.0D);
	        }
    	}
    }

}
