// package meldexun.renderlib.util;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.util.function.Supplier;
// import java.util.stream.Stream;

// import net.minecraft.client.Minecraft;
// import net.minecraft.client.resources.IResource;
// import net.minecraft.util.ResourceLocation;

// public class ResourceSupplier implements Supplier<String> {

// 	private final ResourceLocation file;

// 	public ResourceSupplier(ResourceLocation file) {
// 		this.file = file;
// 	}

// 	@Override
// 	public String get() {
// 		StringBuilder sb = new StringBuilder();

// 		try (IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(this.file)) {
// 			try (Stream<String> stream = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines()) {
// 				stream.forEach(s -> {
// 					sb.append(s);
// 					sb.append('\n');
// 				});
// 			}
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}

// 		return sb.toString();
// 	}

// }


package meldexun.renderlib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class ResourceSupplier implements Supplier<String> {

    private final ResourceLocation file;

    public ResourceSupplier(ResourceLocation file) {
        this.file = file;
    }

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(this.file);
            reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close only the reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
        }

        return sb.toString();
    }
}
