package net.minestom.vanilla.event.entity;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

public class ReadNBTTagsEvent extends Event {

    private final Entity entity;
    private final NBTCompound source;

    public ReadNBTTagsEvent(Entity entity, NBTCompound source) {
        this.entity = entity;
        this.source = source;
    }

    public Entity getEntity() {
        return entity;
    }

    public NBTCompound getSource() {
        return source;
    }
}
