package dday.assets;

import java.util.Map;

public interface Manifest {

    /**
     * Absolute path to the index.html file
     */
    String index();

    /**
     * Returns a map where the keys are url path. They all start with '/'. Values are absolute paths to the associated
     * resource.
     */
    Map<String, String> assets();

    /**
     * Gets default manifest file
     */
    static Manifest getManifest() {
        return ManifestDefault.MANIFEST_INSTANCE;
    }
    class ManifestDefault {
        static final Manifest MANIFEST_INSTANCE = new AssetManifestJson();
    }

}