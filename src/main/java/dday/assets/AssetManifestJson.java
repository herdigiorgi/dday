package dday.assets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;


public class AssetManifestJson implements Manifest {
    private Map<String, String> mServableAssets;
    private String mIndexFile;

    public AssetManifestJson() {
        Map<String, String> assets = readAssetManifestJsonFile();
        mServableAssets = getServableAssets(assets);
        mIndexFile = getIndex(assets);
    }

    @Override
    public String index() {
        return mIndexFile;
    }

    @Override
    public Map<String, String> assets() {
        return mServableAssets;
    }

    private Map<String,String> readAssetManifestJsonFile() {
        File manifestFile = this.getAssetManifestJsonFile();
        try {
            Map<String, String> resultMap = new HashMap<>();
            JsonNode obj = new ObjectMapper().readTree(manifestFile);
            Iterator<String> keys =  obj.fieldNames();
            while(keys.hasNext()) {
                String keyName = keys.next();
                resultMap.put(keyName, obj.get(keyName).textValue());
            }
            return resultMap;
        } catch (IOException e) {
            throw new AssetException("while reading " + manifestFile.getAbsolutePath(), e);
        }
    }

    private Map<String, String> getServableAssets(Map<String, String> in) {
        return in.entrySet().stream().filter(entry -> {
            String extension = FilenameUtils.getExtension(entry.getKey());
            return !(extension.equalsIgnoreCase("map") || extension.equalsIgnoreCase("html"));
        }).map(entry ->
            new AbstractMap.SimpleEntry<>(ensureKeyStartSlash(entry.getKey()), getAbsolutePath(entry.getValue()))
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getIndex(Map<String, String> in) {
        if(!in.containsKey("index.html")) {
            throw new AssetException("index.html not found");
        }
        return getAbsolutePath(in.get("index.html"));
    }

    private String getAbsolutePath(String path) {
        return Paths.get(Paths.get("").toAbsolutePath().toString(), WEB_BUILD_DIR, path).toAbsolutePath().toString();
    }

    private String ensureKeyStartSlash(String key) {
        //if(!key.startsWith("/")) return "/" +  key;
        return key;
    }

    private File getAssetManifestJsonFile() {
        return new File(getAbsolutePath(ASSET_MANIFEST_FILE_NAME));
    }

    private static final String WEB_BUILD_DIR = "/web/build/";
    private static final String ASSET_MANIFEST_FILE_NAME = "asset-manifest.json";
}
