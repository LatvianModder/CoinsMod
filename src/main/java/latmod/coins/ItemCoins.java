package latmod.coins;
import cpw.mods.fml.relauncher.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import latmod.core.base.*;

public class ItemCoins extends ItemLM
{
	public ItemCoins(String s)
	{
		super(Coins.mod, s);
		setMaxStackSize(1);
		
		CoinsItems.COINS_1 = create(1);
		CoinsItems.COINS_10 = create(10);
		CoinsItems.COINS_100 = create(100);
		CoinsItems.COINS_1000 = create(1000);
		CoinsItems.COINS_10000 = create(10000);
	}
	
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab()
	{ return CreativeTabs.tabMisc; }
	
	public String getUnlocalizedName(ItemStack is)
	{ return super.getUnlocalizedName(is) + (isPlural(getCoins(is)) ? "_p" : ""); }
	
	public String getItemStackDisplayName(ItemStack is)
	{ return "" + EnumChatFormatting.GOLD + getCoins(is) + EnumChatFormatting.RESET + " " + super.getItemStackDisplayName(is); }
	
	public void onPostLoaded()
	{
		itemsAdded.add(CoinsItems.COINS_1);
		itemsAdded.add(CoinsItems.COINS_10);
		itemsAdded.add(CoinsItems.COINS_100);
		itemsAdded.add(CoinsItems.COINS_1000);
		itemsAdded.add(CoinsItems.COINS_10000);
	}
	
	public ItemStack create(int i)
	{
		ItemStack is = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("Coins", i);
		is.setTagCompound(tag);
		return is;
	}
	
	public long getCoins(ItemStack is)
	{
		if(is == null || !is.hasTagCompound()) return 0;
		return is.stackTagCompound.getLong("Coins");
	}
	
	public boolean isPlural(long l)
	{
		String s = "" + l;
		return !s.endsWith("1") || s.endsWith("11");
	}
	
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer ep)
	{
		for (int j = 0; j < 20; ++j)
		{
			Vec3 vec3 = ep.worldObj.getWorldVec3Pool().getVecFromPool(((double)w.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			vec3.rotateAroundX(-ep.rotationPitch * (float)Math.PI / 180.0F);
			vec3.rotateAroundY(-ep.rotationYaw * (float)Math.PI / 180.0F);
			Vec3 vec31 = ep.worldObj.getWorldVec3Pool().getVecFromPool(((double)w.rand.nextFloat() - 0.5D) * 0.3D, (double)(-w.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
			vec31.rotateAroundX(-ep.rotationPitch * (float)Math.PI / 180.0F);
			vec31.rotateAroundY(-ep.rotationYaw * (float)Math.PI / 180.0F);
			vec31 = vec31.addVector(ep.posX, ep.posY + (double)ep.getEyeHeight(), ep.posZ);
			String s = "iconcrack_" + Item.getIdFromItem(this);
			ep.worldObj.spawnParticle(s, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord);
		}

		ep.playSound("random.break", 1F, 0.7F + w.rand.nextFloat() * 0.3F);
		
		long c = getCoins(is);
		PlayerCoins.add(ep, c);
		//LatCore.printChat(ep, "Added " + c + " coins");
		
		if(!ep.capabilities.isCreativeMode) is.stackSize--;
		
		return is;
	}
}