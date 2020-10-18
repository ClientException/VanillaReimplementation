package net.minestom.vanilla.event.entity;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

public class WriteNBTTagsEvent extends Event {

    private final Entity entity;
    private final NBTCompound destination;

    public WriteNBTTagsEvent(Entity entity, NBTCompound destination) {
        this.entity = entity;
        this.destination = destination;
    }

    public Entity getEntity() {
        return entity;
    }

    public NBTCompound getDestination() {
        return destination;
    }
}
