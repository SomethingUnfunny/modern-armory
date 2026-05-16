package org.ranch.mi_armory.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryRenderTypes;

public class EntityNukeEffectsRenderer extends EntityRenderer<EntityNukeEffects> {

	private static final ResourceLocation CLOUDLET_TEXTURE = ResourceLocation.fromNamespaceAndPath(MiArmory.MODID, "textures/particle_base.png");
	private static final RenderType CLOUDLET_RENDER_TYPE = MiArmoryRenderTypes.cloudlet(CLOUDLET_TEXTURE);

	public EntityNukeEffectsRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityNukeEffects entityNukeEffects) {
		return null;
	}

	public void render(EntityNukeEffects ent, float p_114600_, float p_114601_, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
		poseStack.pushPose();
		poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
		VertexConsumer vertexconsumer = bufferSource.getBuffer(CLOUDLET_RENDER_TYPE);
		for (Cloudlet c : ent.cloudlets) {
			cloudlet(vertexconsumer, poseStack, (float) c.pos.x, (float) c.pos.y, (float) c.pos.z, 255, 255, 255, 100, 1);
		}
		poseStack.popPose();
	}

	private static void cloudlet(VertexConsumer consumer, PoseStack poseStack, float x, float y, float z, int r, int g, int b, int a, float size) {
		poseStack.pushPose();
		poseStack.translate(x, y, z);
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
