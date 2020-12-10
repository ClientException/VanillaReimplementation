package net.minestom.vanilla.entity.hostile;

import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;
import net.minestom.vanilla.entity.VanillaEntity;

public class Phantom extends VanillaEntity {
	
	public Phantom(Instance instance, Position position) {
		super(EntityType.ZOMBIE, position, instance);
	}
	
	
}
