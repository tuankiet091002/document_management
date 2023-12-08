package com.java.java_proj.entities.enums;

import com.java.java_proj.entities.Document;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DocumentVersionCompositeKey implements Serializable {

    private Document document;
    private String version;
}
