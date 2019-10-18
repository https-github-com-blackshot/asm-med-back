package kz.beeset.med.dmp.model.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CommentAttachment {
    private String attachmentId;
    private String fileName;
    private String mimeType;
}
