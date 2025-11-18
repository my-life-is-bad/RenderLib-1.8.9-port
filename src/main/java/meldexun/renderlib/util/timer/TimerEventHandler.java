package meldexun.renderlib.util.timer;

import java.util.ArrayList;
import java.util.List;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.util.GLUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
//import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

//@EventBusSubscriber(modid = RenderLib.MODID, value = Side.CLIENT)
public class TimerEventHandler {

	private static final ITimer gpuRenderTimer = tryCreateGLTimer("GPU", 100);
	private static final ITimer cpuRenderTimer = new CPUTimer("CPU (Render)", 100);
	private static final ITimer cpuTickTimer = new CPUTimer("CPU (Tick)", 100);
	public static final List<ITimer> timers = new ArrayList<>();
	static {
		timers.add(gpuRenderTimer);
		timers.add(cpuRenderTimer);
		timers.add(cpuTickTimer);
	}

	public static ITimer tryCreateGLTimer(String name, int maxResultCount) {
		if (!GLUtil.CAPS.OpenGL33 && !GLUtil.CAPS.GL_ARB_timer_query) {
			return new DummyTimer(name, "?");
		}
		return new GLTimer(name, maxResultCount);
	}

	@SubscribeEvent
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
		if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		if (!RenderLibConfig.showFrameTimes) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaled = new ScaledResolution(mc);
		int x = scaled.getScaledWidth() - 2;
		int y = 2;
		drawLeftAligned(mc.fontRendererObj, "avg", x - 40, y);
		drawLeftAligned(mc.fontRendererObj, "max", x, y);
		for (int i = 0; i < timers.size(); i++) {
			drawDebug(mc.fontRendererObj, timers.get(i), x, y + (i + 1) * 10);
		}
	}

	private void drawDebug(FontRenderer font, ITimer timer, int x, int y) {
		if (timer == null) {
			return;
		}

		int nameWidth = font.getStringWidth(timer.getName());
		int avgWidth = font.getStringWidth(timer.avgString());
		int maxWidth = font.getStringWidth(timer.maxString());

		int avgOffset = Math.max(maxWidth + 4, 40);
		int nameOffset = Math.max(avgOffset + avgWidth + 4, 80);

		draw(font, timer.getName(), x - nameWidth - nameOffset, y);
		draw(font, timer.avgString(), x - avgWidth - avgOffset, y);
		draw(font, timer.maxString(), x - maxWidth, y);
	}

	private void drawLeftAligned(FontRenderer font, String string, int x, int y) {
		draw(font, string, x - font.getStringWidth(string), y);
	}

	private void draw(FontRenderer font, String string, int x, int y) {
		font.drawStringWithShadow(string, x, y, 0xFFFFFFFF);
	}

	@SubscribeEvent
	public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
		if (event.phase == Phase.START) {
			cpuRenderTimer.update();
			cpuRenderTimer.start();
			gpuRenderTimer.update();
			gpuRenderTimer.start();
		} else {
			gpuRenderTimer.stop();
			cpuRenderTimer.stop();
		}
	}

	@SubscribeEvent
	public void onRenderTickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			cpuTickTimer.update();
			cpuTickTimer.start();
		} else {
			cpuTickTimer.stop();
		}
	}

}
