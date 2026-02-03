package nl.avflexologic.wbje.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

/**
 * FileSystem-based implementation storing template files under a configured root directory.
 * <p>
 * Files are stored using the template id as part of the filename to ensure stable addressing.
 */
@Component
public class TemplateStorageImplementation implements TemplateStorage {

    private final Path storageRoot;

    /**
     * Constructs the storage implementation using the configured root directory.
     *
     * @param root configuration property specifying the root folder for job templates
     */
    public TemplateStorageImplementation(
            @Value("${wbje.templates.storage-path:uploads/job-templates}") String root
    ) {
        this.storageRoot = Path.of(root);
        try {
            Files.createDirectories(storageRoot);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize template storage folder", e);
        }
    }

    @Override
    public void saveFile(Long id, MultipartFile file) throws IOException {
        String extension = resolveExtension(file.getOriginalFilename());
        Path target = storageRoot.resolve(id + extension);
        Files.write(target, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public Resource loadFile(Long id) {
        // We do not know the extension here; scan for matches.
        try {
            try (var stream = Files.list(storageRoot)) {
                return stream
                        .filter(path -> path.getFileName().toString().startsWith(id + "."))
                        .findFirst()
                        .map(FileSystemResource::new)
                        .orElseGet(() -> new FileSystemResource(storageRoot.resolve(id + ".bin")));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to resolve template file for id " + id, e);
        }
    }

    @Override
    public void deleteFile(Long id) {
        try (var stream = Files.list(storageRoot)) {
            stream
                    .filter(path -> path.getFileName().toString().startsWith(id + "."))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new IllegalStateException("Failed to delete template file " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("Failed to enumerate template files for deletion", e);
        }
    }

    private String resolveExtension(String originalFileName) {
        if (originalFileName == null || !originalFileName.contains(".")) {
            return ".bin";
        }
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }
}
