package net.minecraft.server;

import org.bukkit.event.player.PlayerFishEvent; // CraftBukkit

public class ItemFishingRod extends Item {

	public ItemFishingRod() {
		this.setMaxDurability(64);
		this.c(1);
		this.a(CreativeModeTab.i);
	}

	@Override
	public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
		if (entityhuman.hookedFish != null) {
			int i = entityhuman.hookedFish.l();

			// itemstack.damage(i, entityhuman); // FalchusSpigot
			entityhuman.bw();
		} else {
			// FalchusSpigot start
			if (itemstack.getData() >= itemstack.j() - 1) {
				return itemstack;
			}
			// FalchusSpigot end
			
			// CraftBukkit start
			EntityFishingHook hook = new EntityFishingHook(world, entityhuman);
			PlayerFishEvent playerFishEvent = new PlayerFishEvent(
					(org.bukkit.entity.Player) entityhuman.getBukkitEntity(), null,
					(org.bukkit.entity.Fish) hook.getBukkitEntity(), PlayerFishEvent.State.FISHING);
			world.getServer().getPluginManager().callEvent(playerFishEvent);

			if (playerFishEvent.isCancelled()) {
				entityhuman.hookedFish = null;
				return itemstack;
			}
			// CraftBukkit end
			world.makeSound(entityhuman, "random.bow", 0.5F, 0.4F / (Item.g.nextFloat() * 0.4F + 0.8F));
			if (!world.isClientSide) {
				world.addEntity(hook); // CraftBukkit - moved creation up
			}

            itemstack.damage(1, entityhuman); // FalchusSpigot

			entityhuman.bw();
			entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
		}

		return itemstack;
	}

	@Override
	public boolean f_(ItemStack itemstack) {
		return super.f_(itemstack);
	}

	@Override
	public int b() {
		return 1;
	}
}
