package org.ranch.mi_armory.modular_armor.custom_modules;

import aztech.modern_industrialization.items.PortableStorageUnit;
import aztech.modern_industrialization.materials.Material;
import aztech.modern_industrialization.materials.part.BatteryPart;
import aztech.modern_industrialization.materials.part.MaterialItemPart;
import aztech.modern_industrialization.materials.part.PartKeyProvider;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import org.ranch.mi_armory.modular_armor.Module;

import java.util.List;

public class BatteryModule extends Module {

	Item batteryMaterial;

	public BatteryModule(String id, int width, int height, Material batteryMaterial, List<AddedAttribute> attributes) {
		super(id, width, height, 0, attributes, EquipmentSlotGroup.ANY);
		MaterialItemPart part = batteryMaterial.getPart(new BatteryPart());
		this.batteryMaterial = part.asItem();
	}

	@Override
	public long addedEU() {
		return PortableStorageUnit.CAPACITY_PER_BATTERY.getLong(batteryMaterial);
	}
}
