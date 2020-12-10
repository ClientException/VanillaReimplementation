package net.minestom.vanilla.entity;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.utils.Position;
import net.minestom.server.world.DimensionType;

public interface EntityProvider {
	
	/**
	 * get the next random hostile mob class
	 * @param position 
	 * @param instance 
	 * @param dimension 
	 * 
	 * @return the entity creature
	 */
	public EntityCreature nextRandom(InstanceContainer instance, Position position, DimensionType dimension);
	
	/**
	 * gets a creature from the HostileEntityType
	 * @param type
	 * @param instance
	 * @param position
	 * @return the entity creature
	 */
	public EntityCreature getCreature(HostileEntityType type, InstanceContainer instance, Position position);
	
}
