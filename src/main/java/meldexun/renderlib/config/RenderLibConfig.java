package meldexun.renderlib.config;

import java.util.Arrays;
import java.io.File;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.opengl.debug.GLDebugMessageFilter;
import meldexun.renderlib.opengl.debug.LogStackTraceMode;
import meldexun.renderlib.opengl.debug.Severity;
import meldexun.renderlib.opengl.debug.Source;
import meldexun.renderlib.opengl.debug.Type;
import meldexun.renderlib.util.ResourceLocationMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;


import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import net.minecraft.util.ResourceLocation;

public class RenderLibConfig {

    public static Configuration config;

	public static OpenGLDebugConfiguration openGLDebugOutput = new OpenGLDebugConfiguration();
	public static final String[] options = Arrays.stream(LogStackTraceMode.values()).map(Enum::name).toArray(String[]::new);	// for LogStackTraceMode

	public static boolean debugRenderBoxes;
	public static int mainMenuFPS;
	public static boolean mainMenuFPSSynced;
	public static boolean openGLLogExtensions;
	public static boolean showFrameTimes;

	public static String[] entityBoundingBoxGrowthList;
	public static String[] tileEntityBoundingBoxGrowthList;
	public static String[] tileEntityCachedBoundingBoxBlacklist;
	public static boolean tileEntityCachedBoundingBoxEnabled;
	public static int tileEntityCachedBoundingBoxUpdateInterval;

	public static ResourceLocationMap<TileEntity, Vec3> tileEntityBoundingBoxGrowthListImpl = new ResourceLocationMap<>(te -> new ResourceLocation(TileEntity.classToNameMap.get(te.getClass())), null, s -> {
		double x = s.length >= 1 ? Double.parseDouble(s[0]) : 0.0D;
		double y = s.length >= 2 ? Double.parseDouble(s[1]) : 0.0D;
		double z = s.length >= 3 ? Double.parseDouble(s[2]) : 0.0D;
		return x != 0.0D || y != 0.0D || z != 0.0D ? new Vec3(x, y, z) : null;
	});

	public static ResourceLocationMap<TileEntity, Boolean> tileEntityCachedBoundingBoxBlacklistImpl = new ResourceLocationMap<>(te -> new ResourceLocation(TileEntity.classToNameMap.get(te.getClass())), false, s -> true);


	public static ResourceLocationMap<Entity, Vec3> entityBoundingBoxGrowthListImpl = new ResourceLocationMap<>((Class<Entity> clazz) -> {
            String name = EntityList.classToStringMapping.get(clazz);
            if (name == null) return null;
            return new ResourceLocation("minecraft", name.toLowerCase());
        },
		null, s -> {
		double x = s.length >= 1 ? Double.parseDouble(s[0]) : 0.0D;
		double y = s.length >= 2 ? Double.parseDouble(s[1]) : 0.0D;
		double z = s.length >= 3 ? Double.parseDouble(s[2]) : 0.0D;
		return x != 0.0D || y != 0.0D || z != 0.0D ? new Vec3(x, y, z) : null;
	});


	public static void loadConfig(File file) {
        config = new Configuration(file);
        config.load();
        Property mainMenuFPSProp = config.get("general", "mainMenuFPS", 60, "The Fps Cap in main menu.");
		mainMenuFPSProp.setMinValue(30);
        mainMenuFPSProp.setMaxValue(240);							//max and min values
		mainMenuFPS = mainMenuFPSProp.getInt();
		mainMenuFPSSynced = config.get("general", "mainMenuFPSSynced", false, "If set to true the main menu FPS is synced to the in game FPS (but clamped between 30 and 240).").getBoolean();
		showFrameTimes = config.get("general", "showFrameTimes", false, "If set to true. Frame times will be shown on screen.").getBoolean();
		debugRenderBoxes = config.get("general", "debugRenderBoxes", false, "If set to true Debug boxes will render around entities.").getBoolean();
		openGLLogExtensions = config.get("general", "openGLLogExtensions", false, "Log OpenGL Extenstions.").getBoolean();
		entityBoundingBoxGrowthList = config.getStringList("entityBoundingBoxGrowthList", "general", new String[0], "Allows you to increase the render bounding boxes of entities (or all entities of a mod). Width increases the size on the X and Z axis. Top increases the size in the positive Y direction. Bottom increases the size in the negative Y direction. (Accepts 'modid=width,top,bottom' or 'modid:entity=width,top,bottom').");
		tileEntityBoundingBoxGrowthList = config.getStringList("tileEntityBoundingBoxGrowthList", "general", new String[0], "Allows you to increase the render bounding boxes of tile entities (or all entities of a mod). Width increases the size on the X and Z axis. Top increases the size in the positive Y direction. Bottom increases the size in the negative Y direction. (Accepts 'modid=width,top,bottom' or 'modid:tileentity=width,top,bottom').");
		tileEntityCachedBoundingBoxBlacklist = config.getStringList("tileEntityCachedBoundingBoxBlacklist", "general", new String[0], "Tile entities whose bounding boxes won't be cached (Accepts 'modid' or 'modid:tileentity').");
		tileEntityCachedBoundingBoxEnabled = config.get("general", "tileEntityCachedBoundingBoxEnabled", true, "Most tile entities have static bounding boxes and thus they can be cached. Tile entities whose bounding boxes are likely to change every frame or so should be added to the blacklist. Tile entities whose bounding only change every once in a while should be covered by cache updates (update speed adjustable through tileEntityCachedBoundingBoxUpdateInterval)").getBoolean();
		Property tileEntityCachedBoundingBoxUpdateIntervalProp = config.get("general", "tileEntityCachedBoundingBoxUpdateInterval", 100, "Every frame there is a 1 in x chance to update the cached bounding box. Higher = better performance, Lower = tile entities with dynamic bounding boxes get updated faster."+"\n"+"Min: 1"+"\n"+"Max: 1000000");
		tileEntityCachedBoundingBoxUpdateIntervalProp.setMinValue(1);
        tileEntityCachedBoundingBoxUpdateIntervalProp.setMaxValue(1_000_000);							//max and min values
		tileEntityCachedBoundingBoxUpdateInterval = tileEntityCachedBoundingBoxUpdateIntervalProp.getInt();

		//Open GL Debug Stuff

		openGLDebugOutput.crashOnError = config.get("opengldebugoutput", "crashOnError", false, "Enable/Disable crashing when an OpenGL error occurs. If disabled OpenGL errors are likely to go unnoticed unless the log is checked manually.").getBoolean();
		openGLDebugOutput.enabled = config.get("opengldebugoutput", "enabled", false, "Better debugging of OpenGL errors. Might not be supported by your hardware/driver.").getBoolean();
		openGLDebugOutput.logStackTrace = LogStackTraceMode.valueOf(config.get("opengldebugoutput", "logStackTrace", "ERRORS_ONLY", "Enable/Disable appending of the stack trace when logging a debug message."+"\nOptions: " + String.join(", ", options)).getString());
		openGLDebugOutput.messageFilters = config.getStringList("messageFilters", "opengldebugoutput",openGLDebugOutput.messageFilters,"Enable/Disable debug messages matching the specified filters."
				+ "\n" + "Format: 'source, type, severity, enabled'"
				+ "\n" + "Valid source values: [ ANY, API, WINDOW_SYSTEM, SHADER_COMPILER, THIRD_PARTY, APPLICATION, OTHER ]"
				+ "\n" + "Valid type values: [ ANY, ERROR, DEPRECATED_BEHAVIOR, UNDEFINED_BEHAVIOR, PORTABILITY, PERFORMANCE, MARKER, PUSH_GROUP, POP_GROUP, OTHER ]"
				+ "\n" + "Valid severity values: [ ANY, HIGH, MEDIUM, LOW, NOTIFICATION ]");

		openGLDebugOutput.setContextDebugBit = config.get("opengldebugoutput", "setContextDebugBit", false, "May be required by some systems to generate OpenGL debug output. (Enabling might have a negative impact on performance)").getBoolean();


		tileEntityCachedBoundingBoxBlacklistImpl.load(tileEntityCachedBoundingBoxBlacklist);
		entityBoundingBoxGrowthListImpl.load(entityBoundingBoxGrowthList);
		tileEntityBoundingBoxGrowthListImpl.load(tileEntityBoundingBoxGrowthList);
	}

	public static void saveConfig(File file) {
        config = new Configuration(file);
		config.get("general", "mainMenuFPS", 60, "The Fps Cap in main menu.").setValue(mainMenuFPS);
		config.get("general", "mainMenuFPSSynced", false, "If set to true the main menu FPS is synced to the in game FPS (but clamped between 30 and 240).").setValue(mainMenuFPSSynced);
		config.get("general", "showFrameTimes", false, "If set to true. Frame times will be shown on screen.").setValue(showFrameTimes);
		config.get("general", "debugRenderBoxes", false, "If set to true Debug boxes will render around entities.").setValue(debugRenderBoxes);	
		config.get("general", "openGLLogExtensions", false, "Log OpenGL Extenstions.").setValue(openGLLogExtensions);
		config.get("general", "entityBoundingBoxGrowthList", entityBoundingBoxGrowthList, "Allows you to increase the render bounding boxes of entities (or all entities of a mod). Width increases the size on the X and Z axis. Top increases the size in the positive Y direction. Bottom increases the size in the negative Y direction. (Accepts 'modid=width,top,bottom' or 'modid:entity=width,top,bottom').").set(entityBoundingBoxGrowthList);
		config.get("general", "tileEntityBoundingBoxGrowthList", tileEntityBoundingBoxGrowthList, "Allows you to increase the render bounding boxes of tile entities (or all entities of a mod). Width increases the size on the X and Z axis. Top increases the size in the positive Y direction. Bottom increases the size in the negative Y direction. (Accepts 'modid=width,top,bottom' or 'modid:tileentity=width,top,bottom').").set(tileEntityBoundingBoxGrowthList);
		config.get("general", "tileEntityCachedBoundingBoxBlacklist", tileEntityCachedBoundingBoxBlacklist, "Tile entities whose bounding boxes won't be cached (Accepts 'modid' or 'modid:tileentity').").set(tileEntityCachedBoundingBoxBlacklist);
		config.get("general", "tileEntityCachedBoundingBoxEnabled", true, "Most tile entities have static bounding boxes and thus they can be cached. Tile entities whose bounding boxes are likely to change every frame or so should be added to the blacklist. Tile entities whose bounding only change every once in a while should be covered by cache updates (update speed adjustable through tileEntityCachedBoundingBoxUpdateInterval)").setValue(tileEntityCachedBoundingBoxEnabled);
		config.get("general", "tileEntityCachedBoundingBoxUpdateInterval", 100, "Every frame there is a 1 in x chance to update the cached bounding box. Higher = better performance, Lower = tile entities with dynamic bounding boxes get updated faster."+"\n"+"Min: 1"+"\n"+"Max: 1000000").setValue(tileEntityCachedBoundingBoxUpdateInterval);

		//Open GL Debug Stuff

		config.get("opengldebugoutput", "crashOnError", false, "Enable/Disable crashing when an OpenGL error occurs. If disabled OpenGL errors are likely to go unnoticed unless the log is checked manually.").setValue(openGLDebugOutput.crashOnError);
		config.get("opengldebugoutput", "enabled", true, "Better debugging of OpenGL errors. Might not be supported by your hardware/driver.").setValue(openGLDebugOutput.enabled);
		config.get("opengldebugoutput", "logStackTrace", "ERRORS_ONLY", "Enable/Disable appending of the stack trace when logging a debug message.").setValue(openGLDebugOutput.logStackTrace.name());
		config.get("opengldebugoutput", "messageFilters", openGLDebugOutput.messageFilters,"Enable/Disable debug messages matching the specified filters."
				+ "\n" + "Format: 'source, type, severity, enabled'"
				+ "\n" + "Valid source values: [ ANY, API, WINDOW_SYSTEM, SHADER_COMPILER, THIRD_PARTY, APPLICATION, OTHER ]"
				+ "\n" + "Valid type values: [ ANY, ERROR, DEPRECATED_BEHAVIOR, UNDEFINED_BEHAVIOR, PORTABILITY, PERFORMANCE, MARKER, PUSH_GROUP, POP_GROUP, OTHER ]"
				+ "\n" + "Valid severity values: [ ANY, HIGH, MEDIUM, LOW, NOTIFICATION ]").set(openGLDebugOutput.messageFilters);

		config.get("opengldebugoutput", "setContextDebugBit", false, "May be required by some systems to generate OpenGL debug output. (Enabling might have a negative impact on performance)").setValue(openGLDebugOutput.setContextDebugBit);

		
		if (config.hasChanged()) {
            config.save();
        }
    }


	public static class OpenGLDebugConfiguration {

    public boolean crashOnError;
    public boolean enabled;
    public LogStackTraceMode logStackTrace;
    public String[] messageFilters = { new GLDebugMessageFilter(Source.ANY, Type.ERROR, Severity.ANY, true).toString() };
    public boolean setContextDebugBit;

    public GLDebugMessageFilter[] getMessageFilters() {
        return Arrays.stream(messageFilters).map(GLDebugMessageFilter::new).toArray(GLDebugMessageFilter[]::new);
    	}
	}
}