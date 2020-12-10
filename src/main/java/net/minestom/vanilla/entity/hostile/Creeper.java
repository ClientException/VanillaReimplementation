package net.minestom.vanilla.entity.hostile;

import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;
import net.minestom.vanilla.entity.VanillaEntity;

public class Creeper extends VanillaEntity {
	
	public Creeper(Instance instance, Position position) {
		super(EntityType.ZOMBIE, position, instance);
	}
	
	
}
