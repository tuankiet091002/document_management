package com.java.java_proj.entities;

import com.java.java_proj.entities.enums.DocumentVersionCompositeKey;
import com.java.java_proj.exceptions.HttpException;
import lombok.*;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_versions")
@IdClass(DocumentVersionCompositeKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DocumentVersion {

    @Id
    @Column(name = "version")
    private String version = "1.0";

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document")
    private Document document;

    @Column(name = "content")
    private String content;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "generated_name", nullable = false)
    private String generatedName;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public String updateMainVer(int mainVer) {
        // to array of string
        String[] currentVersion = this.version.split("\\.");

        // check for legit version
        if (mainVer <= Integer.parseInt(currentVersion[0]))
            throw new HttpException(HttpStatus.BAD_REQUEST, "New version must be higher than current version.");

        // reset i2 and i3
        return mainVer + ".0";
    }

    public String nextPublish() {

        String[] currentVersion = this.version.split("\\.");
        // increase by 1
        return currentVersion[0] + "." + (Integer.parseInt(currentVersion[1]) + 1);
    }

    @Override
    public String toString() {
        return "DocumentVersion{" +
                "version='" + version + '\'' +
                ", content='" + content + '\'' +
                ", filename='" + filename + '\'' +
                ", generatedName='" + generatedName + '\'' +
                ", url='" + url + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
