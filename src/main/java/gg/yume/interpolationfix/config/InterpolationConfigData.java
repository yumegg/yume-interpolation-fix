/*
 * yume-interpolation-fix by yume.gg
 *
 * To the extent possible under law, the person who associated CC0 with
 * yume-interpolation-fix has waived all copyright and related or neighboring rights
 * to yume-interpolation-fix.
 *
 * You should have received a copy of the CC0 legalcode along with this
 * work.  If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package gg.yume.interpolationfix.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class InterpolationConfigData {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(InterpolationConfigData.class);
    private static final @NotNull Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final @NotNull String INTERPOLATION_MODE_KEY = "interpolationMode";
    private static final @NotNull InterpolationMode INTERPOLATION_MODE_DEFAULT = InterpolationMode.VANILLA;

    @Contract(value = "_ -> new", pure = true)
    static @NotNull InterpolationConfigData getDefaultConfig(@NotNull Path path) {
        return new InterpolationConfigData(
            path,
            INTERPOLATION_MODE_DEFAULT
        );
    }

    @Contract(value = "_ -> new")
    static @NotNull InterpolationConfigData load(@NotNull Path path) {
        try {
            final @NotNull JsonElement document = JsonParser.parseString(Files.readString(path, StandardCharsets.UTF_8));
            if (!document.isJsonObject()) {
                throw new JsonParseException("Expected a JSON object");
            }

            final InterpolationMode interpolationMode;

            final JsonElement jsonInterpolationMode = document.getAsJsonObject().get(INTERPOLATION_MODE_KEY);
            if (jsonInterpolationMode != null) {
                if (!jsonInterpolationMode.isJsonPrimitive() || !jsonInterpolationMode.getAsJsonPrimitive().isString()) {
                    throw new JsonParseException(INTERPOLATION_MODE_KEY + " must be a string");
                }
                final var value = jsonInterpolationMode.getAsString();
                try {
                    interpolationMode = InterpolationMode.valueOf(value);
                } catch (IllegalArgumentException exception) {
                    throw new JsonParseException("Unable to parse interpolation mode: " + value);
                }
            } else {
                interpolationMode = INTERPOLATION_MODE_DEFAULT;
            }

            return new InterpolationConfigData(path, interpolationMode);
        } catch (NoSuchFileException exception) {
            final var config = getDefaultConfig(path);
            try {
                config.save();
            } catch (IOException saveException) {
                LOGGER.error("Could not create interpolation settings at {}", path, saveException);
            }
            return config;
        } catch (IOException | JsonParseException exception) {
            // Keep an unreadable file intact. A later explicit user change may replace it.
            LOGGER.error("Could not read {}; using Minecraft 26.3 interpolation", path, exception);
            return getDefaultConfig(path);
        }
    }

    private final @NotNull Path path;
    private @NotNull InterpolationMode interpolationMode;

    @Contract(pure = true)
    private InterpolationConfigData(@NotNull Path path, @NotNull InterpolationMode interpolationMode) {
        this.path = path.toAbsolutePath();
        this.interpolationMode = interpolationMode;
    }

    @NotNull InterpolationMode getInterpolationMode() {
        return this.interpolationMode;
    }

    void setInterpolationMode(@NotNull InterpolationMode interpolationMode) {
        this.interpolationMode = interpolationMode;
    }

    void save() throws IOException {
        final JsonObject document = new JsonObject();
        document.addProperty(INTERPOLATION_MODE_KEY, this.interpolationMode.name());
        Path directory = this.path.getParent();
        Files.createDirectories(directory);

        final Path temporary = Files.createTempFile(directory, "yume-interpolation-fix-", ".tmp");
        try {
            Files.writeString(temporary, GSON.toJson(document) + "\n", StandardCharsets.UTF_8);
            try {
                Files.move(temporary, this.path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, this.path, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

}
