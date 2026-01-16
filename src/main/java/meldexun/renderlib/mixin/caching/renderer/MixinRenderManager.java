package meldexun.renderlib.mixin.caching.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import meldexun.renderlib.api.IEntityRendererCache;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager {

	// /** {@link RenderManager#isRenderMultipass(Entity)} */
	// @Redirect(method = "isRenderMultipass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
	// public Render<Entity> isRenderMultipass_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {
	// 	return ((IEntityRendererCache) entityIn).getRenderer();
	// }	:/

	/** {@link RenderManager#shouldRender(Entity, ICamera, double, double, double)} */
	@Redirect(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
	public Render shouldRender_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {	//Render<Entity>
		return ((IEntityRendererCache) entityIn).getRenderer();
	}

	/** {@link RenderManager#doRenderEntity(Entity, double, double, double, float, float, boolean)} */
	@Redirect(method = { "doRenderEntity", "laggoggles_trueRender(Lnet/minecraft/entity/Entity;DDDFFZ)Z"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
	public Render renderEntity_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {	//Render<Entity>	//doRenderEntity instead of renderEntity in 1.8.9
		return ((IEntityRendererCache) entityIn).getRenderer();											//, "laggoggles_trueRender(Lnet/minecraft/entity/Entity;DDDFFZ)V" no laggoggles ig
	}

	// /** {@link RenderManager#renderMultipass(Entity, float)} */
	// @Redirect(method = "renderMultipass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
	// public Render<Entity> renderMultipass_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {
	// 	return ((IEntityRendererCache) entityIn).getRenderer();
	// }	:/

}
