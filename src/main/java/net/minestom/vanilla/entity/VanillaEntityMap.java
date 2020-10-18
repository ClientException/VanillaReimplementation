package net.minestom.vanilla.entity;

import com.google.common.collect.HashBiMap;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.utils.Position;
import net.minestom.vanilla.entity.animal.VanillaCow;
import net.minestom.vanilla.entity.animal.VanillaPig;
import net.minestom.vanilla.io.NBTSerializable;

import java.util.Map;

public final class VanillaEntityMap {

    private static final Map<EntityType, EntityConstructor<?>> entities = HashBiMap.create();

    static {
        entities.put(EntityType.PIG, VanillaPig::new);
        entities.put(EntityType.TNT, PrimedTNT::new);
        entities.put(EntityType.COW, VanillaCow::new);
    }

    public static Entity createEntity(EntityType type, Position initialPosition) {
        if(!entities.containsKey(type))
            return null;
        return entities.get(type).create(initialPosition);
    }

    @FunctionalInterface
    private interface EntityConstructor<T extends Entity & NBTSerializable> {
        T create(Position initialPosition);
    }
}
