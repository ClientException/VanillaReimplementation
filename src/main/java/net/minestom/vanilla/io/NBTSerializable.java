package net.minestom.vanilla.io;

import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.UUID;

public interface NBTSerializable {
    void readFrom(NBTCompound compound);
    NBTCompound writeTo(NBTCompound compound);

    static UUID uuidFromNBTFormat(int[] elements) {
        if(elements == null)
            return UUID.randomUUID();
        assert elements.length == 4;
        long most = ((long)elements[0]) << 32;
        most |= elements[1];

        long least = ((long)elements[2]) << 32;
        least |= elements[3];
        return new UUID(most, least);
    }

    static int[] toNBTFormat(UUID uuid) {
        return new int[] {
                (int)(uuid.getMostSignificantBits() >> 32),
                (int)(uuid.getMostSignificantBits()),
                (int)(uuid.getLeastSignificantBits() >> 32),
                (int)(uuid.getLeastSignificantBits())
        };
    }
}
