package org.ranch.mi_armory.modular_armor;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryAttachmentTypes;
import org.ranch.mi_armory.MiArmoryAttributes;
import org.ranch.mi_armory.MiArmoryConstants;
import org.ranch.mi_armory.items.ModularArmor;
import org.ranch.mi_armory.modular_armor.custom_modules.ShieldModule;
import org.ranch.mi_armory.network.PacketSyncEnergyShield;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = MiArmory.MODID)
public class ModularArmorEvents {
	@SubscribeEvent // todo use different event? before armor defense is applied
	public static void useShields(LivingDamageEvent.Pre event) {
		if (event.getEntity() instanceof Player p) {
			double maxShield = event.getEntity().getAttributeValue(MiArmoryAttributes.ENERGY_SHIELD);
			if (p.getData(MiArmoryAttachmentTypes.ENERGY_SHIELD) > maxShield) {
				p.setData(MiArmoryAttachmentTypes.ENERGY_SHIELD, (float) maxShield);
			}
			float shield = p.getData(MiArmoryAttachmentTypes.ENERGY_SHIELD);
			float damage = event.getNewDamage();
			float blocked = Math.min(shield, damage);
			if (blocked > 0) {
				event.setNewDamage(damage - blocked);
				p.setData(MiArmoryAttachmentTypes.ENERGY_SHIELD, shield - blocked);
			}
		}
	}

	@SubscribeEvent
	public static void tickShields(PlayerTickEvent.Pre event) {

		if (event.getEntity().level().isClientSide()) return;

		float shield = event.getEntity().getData(MiArmoryAttachmentTypes.ENERGY_SHIELD);

		double maxShield = event.getEntity().getAttributeValue(MiArmoryAttributes.ENERGY_SHIELD);
		if (shield < maxShield && maxShield > 0) {
			float chargeAmount = 0;
			HashMap<EquipmentSlot, Integer> shields = new HashMap<>();
			for (EquipmentSlot slot : ModularArmor.EQUIPMENT_SLOTS) {
				ItemStack stack = event.getEntity().getItemBySlot(slot);
				EquipmentGrid grid = EquipmentGrid.getGridData(stack);
				if (grid != null) {
					int amount = 0;
					for (EquipmentGrid.Entry entry : grid.modules()) {
						if (entry.module() instanceof ShieldModule sm) {
							chargeAmount += sm.getChargeRate();
							shields.put(slot, amount++);
						}
					}
				}
			}

			if (shields.isEmpty()) {
				PacketSyncEnergyShield.sendToPlayer((ServerPlayer) event.getEntity(), shield);
				return;
			}

			float missing = (float) (maxShield - shield);
			float toCharge = Math.min(chargeAmount, missing);
			long chargeCost = (long) (toCharge * MiArmoryConstants.SHIELD_COST);

			long perPiece = chargeCost / shields.size();
			float chargePerPiece = toCharge / shields.size();
			for (Map.Entry<EquipmentSlot, Integer> entry : shields.entrySet()) {
				ItemStack stack = event.getEntity().getItemBySlot(entry.getKey());
				if (stack.getItem() instanceof ModularArmor armor) {
					if (armor.takeEnergy(stack, perPiece)) {
						event.getEntity().setData(MiArmoryAttachmentTypes.ENERGY_SHIELD, event.getEntity().getData(MiArmoryAttachmentTypes.ENERGY_SHIELD) + chargePerPiece);
					}
				}
			}
		}

		PacketSyncEnergyShield.sendToPlayer((ServerPlayer) event.getEntity(), shield);
	}
}
