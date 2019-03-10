package dday.assets;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class AssetController {

    public AssetController(String absPath) throws AssetException {
        try {
            this.init(absPath);
        } catch (IOException e) {
            throw new AssetException(absPath, e);
        }
    }

    private void init(String absPath) throws  IOException {
        File file = new File(absPath);
        String body = FileUtils.readFileToString(file, "UTF-8");
        mResponse = ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .contentType(getMediaType(file))
                .body(body);
    }

    public MediaType getMediaType(File file) throws IOException {
        String stype = new Tika().detect(file);
        return MediaType.valueOf(stype);
    }

    public ResponseEntity<String> handle() {
        return mResponse;
    }

    public static Method getHandleMethod() {
        try {
            return AssetController.class.getDeclaredMethod("handle");
        } catch (NoSuchMethodException e) {
            throw new AssetException("no handle method", e);
        }
    }

    private ResponseEntity<String> mResponse;
}
