package net.minestom.vanilla.entity;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.entity.Entity;
import net.minestom.server.utils.NamespaceID;
import net.minestom.vanilla.event.entity.ReadNBTTagsEvent;
import net.minestom.vanilla.event.entity.WriteNBTTagsEvent;
import net.minestom.vanilla.io.NBTSerializable;
import org.jglrxavpok.hephaistos.nbt.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.UUID;

public class VanillaEntityUtils {

    private static Method setUUID;

    static {
        try {
            setUUID = Entity.class.getDeclaredMethod("setUuid", UUID.class);
            setUUID.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void readCommonTags(Entity entity, NBTCompound compound) {
        assert NamespaceID.from(entity.getEntityType().getNamespaceID()).equals(NamespaceID.from(compound.getString("id")));

        Iterator<NBTDouble> it = compound.getList("Pos").<NBTDouble>asListOf().iterator();
        entity.getPosition().setX((float) it.next().getValue());
        entity.getPosition().setY((float) it.next().getValue());
        entity.getPosition().setZ((float) it.next().getValue());

        it = compound.getList("Motion").<NBTDouble>asListOf().iterator();
        entity.getVelocity().setX((float) it.next().getValue());
        entity.getVelocity().setY((float) it.next().getValue());
        entity.getVelocity().setZ((float) it.next().getValue());

        Iterator<NBTFloat> rotation = compound.getList("Rotation").<NBTFloat>asListOf().iterator();
        entity.getPosition().setYaw(rotation.next().getValue());
        entity.getPosition().setPitch(rotation.next().getValue());

        float fallDistance = compound.getAsFloat("FallDistance"); // TODO: falldistance
        short fireTicks = compound.getAsShort("Fire"); // TODO: fire
        short airTicks = compound.getAsShort("Air"); // TODO: air
        boolean onGround = compound.getAsByte("OnGround") == 1;
        boolean noGravity = compound.containsKey("NoGravity") ? (compound.getAsByte("NoGravity") == 1) : false;
        entity.setNoGravity(noGravity);

        boolean invulnerable = compound.containsKey("Invulnerable") ? (compound.getAsByte("Invulnerable") == 1) : false; // TODO: invulnerability

        int portalCooldown = compound.containsKey("PortalCooldown") ? compound.getAsInt("PortalCooldown"): 0; // TODO: portal cooldown
        UUID uuid = NBTSerializable.uuidFromNBTFormat(compound.getIntArray("UUID"));
        try {
            setUUID.invoke(entity, uuid);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if(compound.containsKey("CustomName") && compound.containsKey("CustomNameVisible")) {
            entity.setCustomNameVisible(compound.getAsByte("CustomNameVisible") == 1);
            entity.setCustomName(ColoredText.of(compound.getString("CustomName"))); // TODO: check that this works properly
        }

        if(compound.containsKey("Silent")) {
            entity.setSilent(compound.getAsByte("Silent") == 1);
        }

        if(compound.containsKey("Glowing")) {
            entity.setGlowing(compound.getAsByte("Glowing") == 1);
        }

        if(compound.containsKey("Passengers")) {
            NBTList<NBTCompound> passengerData = compound.getList("Passengers").asListOf();
            if(passengerData.getLength() != entity.getPassengers().size()) {
                System.err.println("Could not load passenger data for entity "+entity+" because the passenger list inside the entity does not have the same length than inside the NBTCompound to read it from");
            } else {
                int count = passengerData.getLength();
                Iterator<Entity> passengers = entity.getPassengers().iterator();
                for (int i = 0; i < count; i++) {
                    Entity passenger = passengers.next();
                    if(passenger instanceof NBTSerializable) {
                        ((NBTSerializable) passenger).readFrom(passengerData.get(i));
                    } else {
                        readCommonTags(passenger, passengerData.get(i));
                    }
                }
            }
        }

        ReadNBTTagsEvent event = new ReadNBTTagsEvent(entity, compound);
        entity.callEvent(ReadNBTTagsEvent.class, event);
    }

    public static void writeCommonTags(Entity entity, NBTCompound compound) {
        compound.setString("id", entity.getEntityType().getNamespaceID());
        NBTList<NBTDouble> pos = new NBTList<>(NBTTypes.TAG_Double);
        pos.add(new NBTDouble(entity.getPosition().getX()));
        pos.add(new NBTDouble(entity.getPosition().getY()));
        pos.add(new NBTDouble(entity.getPosition().getZ()));
        compound.set("Pos", pos);

        NBTList<NBTDouble> motion = new NBTList<>(NBTTypes.TAG_Double);
        motion.add(new NBTDouble(entity.getVelocity().getX()));
        motion.add(new NBTDouble(entity.getVelocity().getY()));
        motion.add(new NBTDouble(entity.getVelocity().getZ()));
        compound.set("Motion", motion);

        NBTList<NBTFloat> rotation = new NBTList<>(NBTTypes.TAG_Float);
        rotation.add(new NBTFloat(entity.getPosition().getYaw()));
        rotation.add(new NBTFloat(entity.getPosition().getPitch()));
        compound.set("Rotation", rotation);

        compound.setFloat("FallDistance", 0f); // TODO: fall distance
        compound.setShort("Fire", (short) 0); // TODO: fire ticks
        compound.setShort("Air", (short) 0); // TODO: air ticks
        compound.setByte("OnGround", (byte) (entity.isOnGround() ? 1 : 0));
        compound.setByte("NoGravity", (byte) (entity.hasNoGravity() ? 1 : 0));
        compound.setByte("Invulnerable", (byte) 0); // TODO: invulnerability
        compound.setInt("PortalCooldown", 0); // TODO: PortalCooldown
        compound.setIntArray("UUID", NBTSerializable.toNBTFormat(entity.getUuid()));

        if(entity.getCustomName() != null) {
            compound.setString("CustomName", entity.getCustomName().toString());
            compound.setByte("CustomNameVisible", (byte) (entity.isCustomNameVisible() ? 1 : 0));
        }
        if(entity.isSilent()) {
            compound.setByte("Silent", (byte) 1);
        }
        compound.setByte("Glowing", (byte) (entity.isGlowing() ? 1 : 0));

        // TODO: scoreboard tags

        NBTList<NBTCompound> passengers = new NBTList<>(NBTTypes.TAG_Compound);
        for(Entity passenger : entity.getPassengers()) {
            NBTCompound passengerNBT = new NBTCompound();
            if(passenger instanceof NBTSerializable) {
                ((NBTSerializable) passenger).writeTo(passengerNBT);
            } else {
                writeCommonTags(passenger, passengerNBT);
            }
            passengers.add(passengerNBT);
        }
        compound.set("Passengers", passengers);

        WriteNBTTagsEvent event = new WriteNBTTagsEvent(entity, compound);
        entity.callEvent(WriteNBTTagsEvent.class, event);
    }
}
