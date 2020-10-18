package net.minestom.vanilla.entity;

import net.minestom.server.entity.Entity;
import net.minestom.vanilla.io.NBTSerializable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

/**
 * Use default implementations of {@link NBTSerializable#readFrom(NBTCompound)} and {@link NBTSerializable#writeTo(NBTCompound)}
 */
public interface VanillaEntity extends NBTSerializable {

    @Override
    default void readFrom(NBTCompound compound) {
        VanillaEntityUtils.readCommonTags((Entity) this, compound);
    }

    @Override
    default NBTCompound writeTo(NBTCompound compound) {
        VanillaEntityUtils.writeCommonTags((Entity) this, compound);
        return compound;
    }

    default Entity asEntity() {
        return (Entity)this;
    }
}
