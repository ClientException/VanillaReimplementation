package net.minestom.vanilla.entity.animal;

import net.minestom.server.entity.type.animal.EntityCow;
import net.minestom.server.utils.Position;
import net.minestom.vanilla.entity.VanillaBreedableMob;

public class VanillaCow extends EntityCow implements VanillaBreedableMob {
    public VanillaCow(Position spawnPosition) {
        super(spawnPosition);
    }
}
