package net.minestom.vanilla.entity;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.utils.Position;
import net.minestom.server.world.DimensionType;
import net.minestom.vanilla.entity.hostile.Blaze;
import net.minestom.vanilla.entity.hostile.ChickenJockey;
import net.minestom.vanilla.entity.hostile.Creeper;
import net.minestom.vanilla.entity.hostile.Drowned;
import net.minestom.vanilla.entity.hostile.ElderGuardian;
import net.minestom.vanilla.entity.hostile.Endermite;
import net.minestom.vanilla.entity.hostile.Evoker;
import net.minestom.vanilla.entity.hostile.Ghast;
import net.minestom.vanilla.entity.hostile.Guardian;
import net.minestom.vanilla.entity.hostile.Husk;
import net.minestom.vanilla.entity.hostile.Illager;
import net.minestom.vanilla.entity.hostile.MagmaCube;
import net.minestom.vanilla.entity.hostile.Phantom;
import net.minestom.vanilla.entity.hostile.Pillager;
import net.minestom.vanilla.entity.hostile.Ravager;
import net.minestom.vanilla.entity.hostile.Shulker;
import net.minestom.vanilla.entity.hostile.Silverfish;
import net.minestom.vanilla.entity.hostile.Skeleton;
import net.minestom.vanilla.entity.hostile.SkeletonHorseman;
import net.minestom.vanilla.entity.hostile.Slime;
import net.minestom.vanilla.entity.hostile.SpiderJockey;
import net.minestom.vanilla.entity.hostile.Stray;
import net.minestom.vanilla.entity.hostile.Vex;
import net.minestom.vanilla.entity.hostile.Vindicator;
import net.minestom.vanilla.entity.hostile.Warden;
import net.minestom.vanilla.entity.hostile.Witch;
import net.minestom.vanilla.entity.hostile.WitherSkeleton;
import net.minestom.vanilla.entity.hostile.Zombie;
import net.minestom.vanilla.entity.hostile.ZombieVillager;

public class HostileEntityProvider implements EntityProvider {
	
	public static HostileEntityProvider INSTANCE = new HostileEntityProvider();
	
	public EntityCreature nextRandom(InstanceContainer instance, Position position, DimensionType dimension) {
		return getCreature(HostileEntityType.getRandom(), instance, position);
	}
	
	public EntityCreature getCreature(HostileEntityType type, InstanceContainer instance, Position position) {
		switch(type) {
			case BLAZE: return new Blaze(instance, position);
			case CHICKEN_JOCKEY: return new ChickenJockey(instance, position);
			case CREEPER: return new Creeper(instance, position);
			case DROWNED: return new Drowned(instance, position);
			case ELDER_GUARDIAN: return new ElderGuardian(instance, position);
			case ENDERMITE: return new Endermite(instance, position);
			case EVOKER: return new Evoker(instance, position);
			case GHAST: return new Ghast(instance, position);
			case GUARDIAN: return new Guardian(instance, position);
			case HUSK: return new Husk(instance, position);
			case ILLAGER: return new Illager(instance, position);
			case MAGMA_CUBE: return new MagmaCube(instance, position);
			case PHANTOM: return new Phantom(instance, position);
			case PILLAGER: return new Pillager(instance, position);
			case RAVAGER: return new Ravager(instance, position);
			case SHULKER: return new Shulker(instance, position);
			case SILVERFISH: return new Silverfish(instance, position);
			case SKELETON: return new Skeleton(instance, position);
			case SKELETON_HORSEMAN: return new SkeletonHorseman(instance, position);
			case SLIME: return new Slime(instance, position);
			case SPIDER_JOCKEY: return new SpiderJockey(instance, position);
			case STRAY: return new Stray(instance, position);
			case VEX: return new Vex(instance, position);
			case VINDICATOR: return new Vindicator(instance, position);
			case WARDEN: return new Warden(instance, position);
			case WITCH: return new Witch(instance, position);
			case WITHER_SKELETON: return new WitherSkeleton(instance, position);
			case ZOMBIE: return new Zombie(instance, position);
			case ZOMBIE_VILLAGER: return new ZombieVillager(instance, position);
		}
		return null;
	}
}
