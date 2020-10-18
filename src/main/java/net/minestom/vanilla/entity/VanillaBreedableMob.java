package net.minestom.vanilla.entity;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;

public interface VanillaBreedableMob extends VanillaMob {

    @Override
    default void readFrom(NBTCompound compound) {
        // TODO: InLove
        // TODO: Age
        // TODO: ForcedAge
        // TODO: LoveCause
        VanillaMob.super.readFrom(compound);
    }

    @Override
    default NBTCompound writeTo(NBTCompound compound) {
        // TODO: InLove
        // TODO: Age
        // TODO: ForcedAge
        // TODO: LoveCause

        return VanillaMob.super.writeTo(compound);
    }
}
