package hu.codeup.fileviewerpoc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class FileModel {
    private boolean isDirectory;
    private String fileName;
}
