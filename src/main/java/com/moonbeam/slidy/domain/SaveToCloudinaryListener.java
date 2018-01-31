package com.moonbeam.slidy.domain;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.moonbeam.slidy.config.CloudinaryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class SaveToCloudinaryListener {

    private final Logger log = LoggerFactory.getLogger(SaveToCloudinaryListener.class);

    @Autowired
    private CloudinaryProperties cloudinaryProperties;


    @PrePersist
    @PreUpdate
    public void methodExecuteBeforeSave(final Slide slide) throws IOException {
        AutowireHelper.autowire(this, this.cloudinaryProperties);
        //upload photo to cloudinary
        if (slide.getData() != null && slide.getData().length > 0) {
            Map map = uploadPhoto(slide);
            slide.setUrl((String) map.get("secure_url"));
            slide.setData(null);
        }
    }


    private Map uploadPhoto(Slide slide) throws IOException {

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudinaryProperties.getCloudName(),
            "api_key", cloudinaryProperties.getApiKey(),
            "api_secret", cloudinaryProperties.getApiSecret()));
        File file = new File("temp.txt");
        try (FileOutputStream fop = new FileOutputStream(file)) {

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            fop.write(slide.getData());
            fop.flush();
            fop.close();

        } catch (IOException e) {
            log.debug("could not save slide data to file" + e.getMessage());
        }

        Map photoInfo =  cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        log.debug("file was uploaded: " + photoInfo.get("secure_url"));
        // we don't to persist the photo to data.
        //TODO: remove data column
        slide.setData(null);
        return photoInfo;
    }

}
