package org.ranch.mi_armory;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class MiArmoryDamageTypes {
	public static final ResourceKey<DamageType> NUCLEAR_SHOCKWAVE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MiArmory.MODID, "nuclear_shockwave"));
	;
	public static final ResourceKey<DamageType> NUCLEAR_FLASH = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MiArmory.MODID, "nuclear_flash"));
	;
}
