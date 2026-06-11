package org.ranch.mi_armory;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ranch.mi_armory.client.rendering.nuke.EntityNukeEffects;
import org.ranch.mi_armory.explosions.EntityNukeExplosion;
import org.ranch.mi_armory.modular_armor.EquipmentGridContainerMenu;
import org.ranch.mi_armory.network.PacketDetonation;
import org.ranch.mi_armory.network.PacketEquipmentGridClick;
import org.ranch.mi_armory.network.PacketSyncEnergyShield;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(MiArmory.MODID)
public class MiArmory {
	public static final String MODID = "mi_armory";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MiArmory.MODID);

	public static final Supplier<MenuType<EquipmentGridContainerMenu>> EQUIPMENT_GRID_MENU = MENUS.register("equipment_grid", () -> {
		return new MenuType<>(EquipmentGridContainerMenu::new, FeatureFlags.VANILLA_SET);
	});

	public MiArmory(IEventBus modEventBus, ModContainer modContainer) {
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(MiArmoryItems::onRegisterCapabilities);

		MiArmoryEntities.register(modEventBus);
		MiArmoryItems.register(modEventBus);
		MiArmoryComponents.register(modEventBus);
		MiArmorySounds.register(modEventBus);
		MiArmoryArmorMaterials.register(modEventBus);
		MiArmoryAttributes.register(modEventBus);
		MiArmoryBlocks.register(modEventBus);
		MiArmoryAttachmentTypes.register(modEventBus);
		MENUS.register(modEventBus);

		// Register our mod's ModConfigSpec so that FML can create and load the config file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	public static double speedOfSound(int tick) {
		return (tick * 1.5) * 1.5;
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		// Some common setup code
	}

	@EventBusSubscriber(modid = MODID)
	public static class ModEvents {
		@SubscribeEvent
		public static void register(final RegisterPayloadHandlersEvent event) {
			final PayloadRegistrar registrar = event.registrar(MODID).versioned("1");

			registrar.playToClient(PacketDetonation.TYPE, PacketDetonation.STREAM_CODEC, PacketDetonation::handle);
			registrar.playToServer(PacketEquipmentGridClick.TYPE, PacketEquipmentGridClick.STREAM_CODEC, PacketEquipmentGridClick::handle);
			registrar.playToClient(PacketSyncEnergyShield.TYPE, PacketSyncEnergyShield.STREAM_CODEC, PacketSyncEnergyShield::handle);
		}

		@SubscribeEvent
		public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {
			event.add(
					EntityType.PLAYER,
					MiArmoryAttributes.SHOCKWAVE_RESISTANCE
			);
			event.add(
					EntityType.PLAYER,
					MiArmoryAttributes.ENERGY_SHIELD
			);
		}
	}

	public static void decimate(BlockPos pos, int strength, boolean visuals, Entity cause, Level level) {
		if (!level.isClientSide() && level instanceof ServerLevel sLevel) {
			EntityNukeExplosion explosion = EntityNukeExplosion.create(pos.getCenter(), level, strength, cause);
			level.addFreshEntity(explosion);
			explosion.loadChunk();
		}

		if (visuals) {
			EntityNukeEffects effects = EntityNukeEffects.create(pos.getCenter(), level, strength);
			level.addFreshEntity(effects);
		}
	}
}
