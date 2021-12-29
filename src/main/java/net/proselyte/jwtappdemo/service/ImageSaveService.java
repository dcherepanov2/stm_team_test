package net.proselyte.jwtappdemo.service;

import net.proselyte.jwtappdemo.exception.comicsException.ComicsException;
import net.proselyte.jwtappdemo.exception.imageException.ImageExceptionBadRequest;
import net.proselyte.jwtappdemo.model.Comics;
import net.proselyte.jwtappdemo.repository.ComicsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageSaveService {

    private final ComicsRepo comicsRepo;

    @Value("${upload.path}")
    private String PATH_UPLOAD;

    @Autowired
    public ImageSaveService(ComicsRepo comicsRepo) {
        this.comicsRepo = comicsRepo;
    }

    public boolean saveImage(MultipartFile file, String slug) throws ImageExceptionBadRequest, IOException, ComicsException {
        Comics comics = comicsRepo.findComicsBySlug(slug);
        if (!file.isEmpty() && comics != null &&
                (Objects.requireNonNull(file.getOriginalFilename()).contains(".jpg")
                        || Objects.requireNonNull(file.getOriginalFilename()).contains(".png")//можно загрузить картинки определенного формата
                )) {
            String filename = file.hashCode() + UUID.randomUUID().toString() + ".jpg";
            if (!new File(PATH_UPLOAD).exists()) {
                Files.createDirectories(Paths.get(filename));
            }
            Path path = Paths.get(PATH_UPLOAD, filename);
            file.transferTo(path);
            comics.setImagePath(PATH_UPLOAD + filename);
            comicsRepo.save(comics);
            return true;
        } else if (file.isEmpty() || (!Objects.requireNonNull(file.getOriginalFilename()).contains(".jpg")
                || !Objects.requireNonNull(file.getOriginalFilename()).contains(".png")
        ))
            throw new ImageExceptionBadRequest("The file: " + file.getOriginalFilename() + " format transferred to the server is not supported");
        else if (comics == null)
            throw new ComicsException("Ooo...It seems that no such item has been found. Try to refine your filtering options");
        return false;
    }
}
