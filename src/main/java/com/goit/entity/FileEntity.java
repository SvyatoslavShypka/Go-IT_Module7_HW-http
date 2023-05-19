package com.goit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class FileEntity {
    private String id;
    private String name;
    private String extension;
    private Long creationDate;
}
