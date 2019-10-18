package kz.beeset.med.dmp.model.common;

import kz.beeset.med.dmp.model.DMPPatientAppealComment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class CommentAndAttachmentInfo {
    private DMPPatientAppealComment comment;
    private List<CommentAttachment> attachmentList;
}
