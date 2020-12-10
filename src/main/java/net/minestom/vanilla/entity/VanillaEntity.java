package net.minestom.vanilla.entity;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;

public class VanillaEntity extends EntityCreature {

	public VanillaEntity(EntityType entityType, Position spawnPosition, Instance instance) {
		super(entityType, spawnPosition, instance);
	}
	
}
