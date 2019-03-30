package xyz.gianlu.librespot.mercury.model;

import io.seruco.encoding.base62.Base62;
import org.jetbrains.annotations.NotNull;
import xyz.gianlu.librespot.common.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gianlu
 */
public final class EpisodeId implements SpotifyId, PlayableId {
    private static final Pattern PATTERN = Pattern.compile("spotify:episode:(.{22})");
    private static final Base62 BASE62 = Base62.createInstanceWithInvertedCharacterSet();
    private final String hexId;

    private EpisodeId(@NotNull String hex) {
        this.hexId = hex;
    }

    @NotNull
    public static EpisodeId fromUri(@NotNull String uri) {
        Matcher matcher = PATTERN.matcher(uri);
        if (matcher.find()) {
            String id = matcher.group(1);
            return new EpisodeId(Utils.bytesToHex(BASE62.decode(id.getBytes())));
        } else {
            throw new IllegalArgumentException("Not a Spotify episode ID: " + uri);
        }
    }

    @NotNull
    public static EpisodeId fromBase62(@NotNull String base62) {
        return new EpisodeId(Utils.bytesToHex(BASE62.decode(base62.getBytes())));
    }

    @NotNull
    public static EpisodeId fromHex(@NotNull String hex) {
        return new EpisodeId(hex);
    }

    @Override
    public @NotNull String toMercuryUri() {
        return "hm://metadata/4/episode/" + hexId;
    }

    @Override
    public @NotNull String toSpotifyUri() {
        return "spotify:episode:" + new String(BASE62.encode(Utils.hexToBytes(hexId)));
    }

    @Override
    public @NotNull String hexId() {
        return hexId;
    }

    @Override
    @NotNull
    public byte[] getGid() {
        return Utils.hexToBytes(hexId);
    }
}
