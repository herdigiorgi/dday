package dday.assets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Manifest manifest = Manifest.getManifest();
        for(Map.Entry<String, String> entry: manifest.assets().entrySet()) {
            String path = entry.getKey();
            String file = entry.getValue();
            RequestMappingInfo reqMap = RequestMappingInfo
                    .paths(path)
                    .methods(RequestMethod.GET)
                    .build();

            mRequestMappingHandlerMapping.registerMapping(reqMap, new AssetController(file), AssetController.getHandleMethod());
            logger.info(String.format("%s -> %s", path, file));
        }
    }


    private Logger logger = LoggerFactory.getLogger(StaticResourceConfiguration.class);
    @Autowired
    private RequestMappingHandlerMapping mRequestMappingHandlerMapping;
}

