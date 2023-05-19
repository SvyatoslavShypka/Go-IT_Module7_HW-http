package com.goit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class FileModel {
    private String id;
    private String name;
    private String extension;
    private Long creationDate;
    private byte[] data;
}
