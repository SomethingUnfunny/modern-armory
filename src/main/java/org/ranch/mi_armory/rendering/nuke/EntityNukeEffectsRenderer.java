package org.ranch.mi_armory.rendering.nuke;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryRenderTypes;
import org.ranch.mi_armory.MiArmorySounds;
import org.ranch.mi_armory.rendering.Cloudlet;

public class EntityNukeEffectsRenderer extends EntityRenderer<EntityNukeEffects> {

	private static final ResourceLocation CLOUDLET_TEXTURE = ResourceLocation.fromNamespaceAndPath(MiArmory.MODID, "textures/particle_base.png");
	private static final ResourceLocation FLASH_TEXTURE = ResourceLocation.fromNamespaceAndPath(MiArmory.MODID, "textures/flare.png");

	private static final RenderType CLOUDLET_RENDER_TYPE = MiArmoryRenderTypes.cloudlet(CLOUDLET_TEXTURE);
	//private static final RenderType CLOUDLET_RENDER_TYPE = RenderType.itemEntityTranslucentCull(CLOUDLET_TEXTURE);
	private static final RenderType FLASH_RENDER_TYPE = MiArmoryRenderTypes.flash(FLASH_TEXTURE);


	public EntityNukeEffectsRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityNukeEffects entityNukeEffects) {
		return null;
	}

	@Override
	public boolean shouldRender(EntityNukeEffects livingEntity, Frustum camera, double camX, double camY, double camZ) {
		return true;
	}

	public void render(EntityNukeEffects ent, float p_114600_, float p_114601_, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
		VertexConsumer cloudletVertexConsumer = bufferSource.getBuffer(CLOUDLET_RENDER_TYPE);

		for (Cloudlet c : ent.cloudlets) {

			int alpha = (c.maxLife - c.life) < c.alphaFade
					? (int) (c.a * (double) (c.maxLife - c.life) / c.alphaFade)
					: c.a;

			billboard(cloudletVertexConsumer, poseStack, (float) c.pos.x, (float) c.pos.y, (float) c.pos.z, c.r, c.g, c.b, alpha, c.getScale());
		}
		VertexConsumer flashVertexConsumer = bufferSource.getBuffer(FLASH_RENDER_TYPE);
		BlockHitResult hit = ent.level().clip(new ClipContext(entityRenderDispatcher.camera.getPosition(), ent.position(), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty()));
		if (hit.getType() == HitResult.Type.MISS && ent.type != null)
			billboard(flashVertexConsumer, poseStack, 0, 0, 0, 255, 255, 255, Math.max(255 - (int) (ent.age * 255f / ent.type.getHandler().getFlashDuration()), 0), ent.type.getHandler().getFlashSize());

		Player player = Minecraft.getInstance().player;
		if (player != null) {
			if (player.distanceTo(ent) < MiArmory.speedOfSound(ent.age) && !ent.playedShockSound) {
				ent.level().playLocalSound(ent.getOnPos(), MiArmorySounds.NUCLEAR_EXPLOSION.get(), SoundSource.BLOCKS, 1f, 1f, false);
				ent.playedShockSound = true;
			}
			if (!ent.playedEMISound) {
				ent.level().playLocalSound(ent.getOnPos(), MiArmorySounds.EMI.get(), SoundSource.BLOCKS, 2f, 1f, false);
				ent.playedEMISound = true;
			}
		}
	}

	private void billboard(VertexConsumer consumer, PoseStack poseStack, float x, float y, float z, int r, int g, int b, int a, float size) {
		poseStack.pushPose();
		poseStack.translate(x, y, z);
		poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
		PoseStack.Pose pose = poseStack.last();
		vertex(consumer, pose, -0.5F * size, -0.5F * size, r, g, b, a, 0, 1, 15728880);
		vertex(consumer, pose, 0.5F * size, -0.5F * size, r, g, b, a, 1, 1, 15728880);
		vertex(consumer, pose, 0.5F * size, 0.5F * size, r, g, b, a, 1, 0, 15728880);
		vertex(consumer, pose, -0.5F * size, 0.5F * size, r, g, b, a, 0, 0, 15728880);
		poseStack.popPose();
	}

	private static void vertex(
			VertexConsumer consumer,
			PoseStack.Pose pose,
			float x,
			float y,
			int red,
			int green,
			int blue,
			int alpha,
			float u,
			float v,
			int packedLight
	) {
		consumer.addVertex(pose, x, y, 0.0F)
				.setColor(red, green, blue, alpha)
				.setUv(u, v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(packedLight)
				.setNormal(pose, 0.0F, 1.0F, 0.0F);
	}
}
