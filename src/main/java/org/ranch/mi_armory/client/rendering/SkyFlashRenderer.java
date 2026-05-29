package org.ranch.mi_armory.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.client.rendering.nuke.NukeExplosionType;
import org.ranch.mi_armory.network.PacketDetonation;
import org.ranch.mi_armory.util.UnfunMath;

import java.util.ArrayList;
import java.util.Set;

public class SkyFlashRenderer {


	private static final ResourceLocation FLASH_TEXTURE = MiArmory.location("textures/flash.png");
	private static final ResourceLocation FLARE_TEXTURE = MiArmory.location("textures/flare.png");

	private final float SIZE = 50;
	private final float MAX_RANGE = 5000;

	private ArrayList<Tuple<PacketDetonation, Long>> detonations = new ArrayList<>();

	public void addFlash(PacketDetonation detonation, IPayloadContext ctx) {
		detonations.add(new Tuple<>(detonation, ctx.player().level().getGameTime()));
	}

	public void renderFlashes(PoseStack poseStack, Level level, Camera camera) {

		ArrayList<Tuple<PacketDetonation, Long>> copy = new ArrayList<>(detonations);

		for (Tuple<PacketDetonation, Long> detonation : copy) {
			BlockPos pos = detonation.getA().location();
			NukeExplosionType type = detonation.getA().explosionType();
			double distance = pos.getCenter().distanceTo(camera.getPosition());
			int strength = detonation.getA().strength();
			long birth = detonation.getB();
			int age = (int) (level.getGameTime() - birth);
			int maxAge = strength * 2;
			if (age > maxAge) {
				detonations.remove(detonation);
				continue;
			}

			float ageFraction = (float) age / maxAge;

			Vector2d spherical = UnfunMath.CartesianToSpherical(new Vector3d(pos.getX(), pos.getY(), pos.getZ()).sub(camera.getPosition().toVector3f()));

			poseStack.pushPose();

			poseStack.mulPose(Axis.YP.rotation((float) spherical.x));
			poseStack.mulPose(Axis.ZP.rotation((float) spherical.y));

			boolean sky = type == NukeExplosionType.EXOATMOSPHERIC || type == NukeExplosionType.ATMOSPHERIC;

			float xSize = SIZE;
			float ySize = SIZE * (sky ? 1 : 2);

			Matrix4f matrix = poseStack.last().pose();
			Tesselator tesselator = Tesselator.getInstance();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, sky ? FLARE_TEXTURE : FLASH_TEXTURE);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, -(ageFraction - 1) * 0.5f);
			BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			bufferBuilder.addVertex(matrix, -xSize, 90, -ySize).setUv(0.0F, 0.0F);
			bufferBuilder.addVertex(matrix, xSize, 90, -ySize).setUv(1.0F, 0.0F);
			bufferBuilder.addVertex(matrix, xSize, 90, ySize).setUv(1.0F, 1.0F);
			bufferBuilder.addVertex(matrix, -xSize, 90, ySize).setUv(0.0F, 1.0F);
			BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());

			poseStack.popPose();
		}
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
