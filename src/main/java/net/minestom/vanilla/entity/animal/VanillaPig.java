package net.minestom.vanilla.entity.animal;

import net.minestom.server.entity.type.animal.EntityPig;
import net.minestom.server.utils.Position;
import net.minestom.vanilla.entity.VanillaBreedableMob;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

public class VanillaPig extends EntityPig implements VanillaBreedableMob {
    public VanillaPig(Position spawnPosition) {
        super(spawnPosition);
    }

    @Override
    public void readFrom(NBTCompound compound) {
        VanillaBreedableMob.super.readFrom(compound);
        if(compound.containsKey("Saddle")) {
            setSaddle(compound.getAsByte("Saddle") == 1);
        }
    }

    @Override
    public NBTCompound writeTo(NBTCompound compound) {
        compound.setByte("Saddle", (byte) (hasSaddle() ? 1 : 0));
        return VanillaBreedableMob.super.writeTo(compound);
    }
}
