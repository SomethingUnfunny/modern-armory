package org.ranch.mi_armory.modular_armor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import org.ranch.mi_armory.MiArmoryComponents;
import org.ranch.mi_armory.modular_armor.custom_modules.NullModule;

import java.util.ArrayList;
import java.util.List;

public record EquipmentGrid(int width, int height, List<Entry> modules) {

	public static final Codec<EquipmentGrid> CODEC = RecordCodecBuilder.create(
			(instance) -> instance.group(
							Codec.INT.fieldOf("width").forGetter(EquipmentGrid::width),
							Codec.INT.fieldOf("height").forGetter(EquipmentGrid::height),
							Entry.CODEC.listOf().fieldOf("modules").forGetter(EquipmentGrid::modules))
					.apply(instance, EquipmentGrid::new)
	);

	public static boolean hasGridData(ItemStack itemStack) {
		return itemStack.get(MiArmoryComponents.EQUIPMENT_GRID_COMPONENT) != null;
	}

	public static EquipmentGrid getGridData(ItemStack itemStack) {
		return itemStack.get(MiArmoryComponents.EQUIPMENT_GRID_COMPONENT);
	}

	public static void setGridData(ItemStack itemStack, EquipmentGrid grid) {
		itemStack.set(MiArmoryComponents.EQUIPMENT_GRID_COMPONENT, grid);
	}


	public Entry getAtPos(int x, int y) {
		for (Entry entry : modules) {
			if (entry.touching(x, y)) {
				return entry;
			}
		}
		return null;
	}

	public boolean inBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < width() && y < height();
	}

	public EquipmentGrid remove(Entry entry) {
		List<Entry> entries = new ArrayList<>(modules);
		entries.remove(entry);
		return new EquipmentGrid(width, height, entries);
	}

	public EquipmentGrid add(Entry entry) {
		//if (!canAdd(entry)) return this;
		List<Entry> entries = new ArrayList<>(modules);
		entries.add(entry);
		return new EquipmentGrid(width, height, entries);
	}

	public boolean canAdd(Entry entry) {
		if (!inBounds(entry.x, entry.y)) return false;
		if (entry.x + entry.width() > width() || entry.y + entry.height() > height()) return false;
		for (Entry e : modules) {
			if (e.touching(entry)) {
				return false;
			}
		}
		return true;
	}

	public record Entry(int x, int y, ItemStack stack) {

		public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
				(instance) -> instance.group(
								Codec.INT.fieldOf("x").forGetter(Entry::x),
								Codec.INT.fieldOf("y").forGetter(Entry::y),
								ItemStack.CODEC.fieldOf("stack").forGetter(Entry::stack))
						.apply(instance, Entry::new)
		);

		public Module module() {
			Module m = ModuleList.getFromItem(stack.getItem());
			if (m == null) {
				return new NullModule();
			} else {
				return m;
			}
		}

		public int width() {
			return module().width;
		}

		public int height() {
			return module().height;
		}

		public boolean touching(int x, int y) {
			return x >= this.x && x < this.x + module().width
					&& y >= this.y && y < this.y + module().height;
		}

		public boolean touching(Entry other) {
			return x < other.x() + other.width() && x + width() > other.x()
					&& y < other.y() + other.height() && y + height() > other.y();
		}
	}
}
