package net.minestom.vanilla.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.scoreboard.Team;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTList;
import org.jglrxavpok.hephaistos.nbt.NBTTypes;

public interface VanillaMob extends VanillaEntity {

    @Override
    default void readFrom(NBTCompound compound) {
        VanillaEntity.super.readFrom(compound);
        LivingEntity entity = asEntity();
        entity.setHealth(compound.getAsFloat("Health"));
        // TODO Absorption
        // TODO Hurt time
        // TODO Hurt By Timestamp
        // TODO: DeathTime
        // TODO: FallFlying (elytra)
        // TODO: sleeping coordinates
        // TODO: Brain (villagers & piglins)
        // TODO: Attributes
        // TODO: ActiveEffects
        NBTList<NBTCompound> handItems = compound.getList("HandItems");
        if(handItems.getLength() != 0) {
            if(handItems.get(0).getSize() != 0) entity.setItemInMainHand(ItemStack.fromNBT(handItems.get(0)));
        }
        if(handItems.getLength() > 1) {
            if(handItems.get(1).getSize() != 0) entity.setItemInOffHand(ItemStack.fromNBT(handItems.get(1)));
        }

        NBTList<NBTCompound> armorItems = compound.getList("ArmorItems");
        if(armorItems.getLength() != 0) {
            if(armorItems.get(0).getSize() != 0) entity.setBoots(ItemStack.fromNBT(armorItems.get(0)));
        }
        if(armorItems.getLength() > 1) {
            if(armorItems.get(1).getSize() != 0) entity.setLeggings(ItemStack.fromNBT(armorItems.get(1)));
        }
        if(armorItems.getLength() > 2) {
            if(armorItems.get(2).getSize() != 0) entity.setChestplate(ItemStack.fromNBT(armorItems.get(2)));
        }
        if(armorItems.getLength() > 3) {
            if(armorItems.get(3).getSize() != 0) entity.setHelmet(ItemStack.fromNBT(armorItems.get(3)));
        }

        // TODO: HandDropChances
        // TODO: ArmorDropChances
        // TODO: DeathLootTable
        // TODO: DeathLootTableSeed
        entity.setCanPickupItem(compound.getAsByte("CanPickUpLoot") == 1);
        // TODO: NoAI
        // TODO: PersistenceRequired
        // TODO: LeftHanded
        if(compound.containsKey("Team")) {
            Team team = MinecraftServer.getTeamManager().getTeam(compound.getString("Team"));
            entity.setTeam(team);
        }
        // TODO: Leash
    }

    @Override
    default NBTCompound writeTo(NBTCompound compound) {
        LivingEntity entity = asEntity();
        compound.setFloat("Health", entity.getHealth());
        // TODO Absorption
        // TODO Hurt time
        // TODO Hurt By Timestamp
        // TODO: DeathTime
        // TODO: FallFlying (elytra)
        // TODO: sleeping coordinates
        // TODO: Brain (villagers & piglins)
        // TODO: Attributes
        // TODO: ActiveEffects
        NBTList<NBTCompound> handItems = new NBTList<>(NBTTypes.TAG_Compound);
        handItems.add(entity.getItemInMainHand().toNBT());
        handItems.add(entity.getItemInOffHand().toNBT());

        NBTList<NBTCompound> armorItems = new NBTList<>(NBTTypes.TAG_Compound);
        armorItems.add(entity.getBoots().toNBT());
        armorItems.add(entity.getLeggings().toNBT());
        armorItems.add(entity.getChestplate().toNBT());
        armorItems.add(entity.getHelmet().toNBT());

        // TODO: HandDropChances
        // TODO: ArmorDropChances
        // TODO: DeathLootTable
        // TODO: DeathLootTableSeed
        compound.setByte("CanPickUpLoot", (byte) (entity.canPickupItem() ? 1 : 0));
        // TODO: NoAI
        // TODO: PersistenceRequired
        // TODO: LeftHanded
        if(entity.getTeam() != null) {
            compound.setString("Team", entity.getTeam().getTeamName());
        }
        // TODO: Leash
        VanillaEntity.super.writeTo(compound);
        return compound;
    }

    @Override
    default LivingEntity asEntity() {
        return (LivingEntity)this;
    }
}
