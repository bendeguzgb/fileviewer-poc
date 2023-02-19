package hu.codeup.fileviewerpoc.controller;

import hu.codeup.fileviewerpoc.model.FileModel;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Controller
public class FileViewerController {
    private static final String INDEX_HTML = "index";

    @Value("${root.path}")
    private String rootPath;

    @Value("${error.message.accessDenied}")
    private String accessDeniedErrorMessage;

    @GetMapping({"/files"})
    public String index(@RequestParam(name = "path", required = false, defaultValue = "") String pathParam,
                        @ModelAttribute("model") ModelMap model) throws AccessDeniedException {

        val normalizedPath = normalizePath(pathParam);

        try (Stream<Path> stream = Files.list(normalizedPath)) {
            List<FileModel> fileModelList =
                    stream.map(path1 ->
                                    FileModel.builder()
                                    .isDirectory(Files.isDirectory(path1))
                                    .fileName(path1.toFile().getName())
                                    .build())
                        .sorted((o1, o2) -> Boolean.compare(o2.isDirectory(), o1.isDirectory()))
                        .collect(Collectors.toUnmodifiableList());

            model.addAttribute("files", fileModelList);
            model.addAttribute("path", normalizedPath.toString());
        } catch (NoSuchFileException nsfe) {
            log.error("File not found: " + nsfe.getFile());
            throw new AccessDeniedException(accessDeniedErrorMessage);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new AccessDeniedException(accessDeniedErrorMessage);
        }

        return INDEX_HTML;
    }

    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(@RequestParam("path") String pathParam) throws AccessDeniedException {
        val file = normalizePath(pathParam).toFile();
        ByteArrayResource resource;

        try {
            resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            //FIXME: This has to be handled based on business logic
            throw new RuntimeException(e);
        }

        val header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private Path normalizePath(String path) throws AccessDeniedException {
        Path normalizedPath;
        try {
            path = rootPath + path;
            normalizedPath = Paths.get(path).normalize();

            if (!normalizedPath.startsWith(rootPath)) {
                throw new AccessDeniedException(accessDeniedErrorMessage);
            }
        } catch (InvalidPathException ipe) {
            ipe.printStackTrace();
            throw new AccessDeniedException(accessDeniedErrorMessage);
        }

        return normalizedPath;
    }
}
