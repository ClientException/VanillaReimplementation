package net.minestom.vanilla.entity;

import java.util.Random;

public enum HostileEntityType {
	BLAZE,
	CHICKEN_JOCKEY,
	CREEPER,
	DROWNED,
	ELDER_GUARDIAN,
	ENDERMITE,
	EVOKER,
	GHAST,
	GUARDIAN,
	HUSK,
	ILLAGER,
	MAGMA_CUBE,
	PHANTOM,
	PILLAGER,
	RAVAGER,
	SHULKER,
	SILVERFISH,
	SKELETON,
	SKELETON_HORSEMAN,
	SLIME,
	SPIDER_JOCKEY,
	STRAY,
	VEX,
	VINDICATOR,
	WARDEN,
	WITCH,
	WITHER_SKELETON,
	ZOMBIE,
	ZOMBIE_VILLAGER;
	
	private static Random random = new Random();
	
	public static HostileEntityType getRandom() {
		HostileEntityType[] values = HostileEntityType.values();
		return values[random.nextInt(values.length - 1)];
	}
}
