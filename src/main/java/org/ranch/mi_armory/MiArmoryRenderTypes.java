package org.ranch.mi_armory;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class MiArmoryRenderTypes {

	public static RenderType cloudlet(ResourceLocation texture) {
		return CLOUDLET.apply(texture);
	}

	private static final Function<ResourceLocation, RenderType> CLOUDLET = texture -> {
		RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
				.setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
				.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
				.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
				.setLightmapState(RenderStateShard.LIGHTMAP)
				.setOverlayState(RenderStateShard.OVERLAY)
				.setWriteMaskState(RenderStateShard.COLOR_WRITE)
				.createCompositeState(false);
		return RenderType.create(
				"cloudlet",
				DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
				VertexFormat.Mode.QUADS,
				1536,
				true,
				false,
				rendertype$compositestate
		);
	};
}
