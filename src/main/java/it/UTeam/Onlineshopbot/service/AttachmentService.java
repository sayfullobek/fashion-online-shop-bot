package it.UTeam.Onlineshopbot.service;

import it.UTeam.Onlineshopbot.entity.Attachment;
import it.UTeam.Onlineshopbot.entity.AttachmentContent;
import it.UTeam.Onlineshopbot.payload.ApiResponse;
import it.UTeam.Onlineshopbot.repository.AttachmentContentRepository;
import it.UTeam.Onlineshopbot.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;

    public UUID upload(MultipartHttpServletRequest request) {
        try {
            Iterator<String> fileNames = request.getFileNames();
            MultipartFile file = request.getFile(fileNames.next());
            Attachment attachment = new Attachment(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize());

            Attachment savedAttachment = attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent(
                    savedAttachment,
                    file.getBytes());
            attachmentContentRepository.save(attachmentContent);
            return savedAttachment.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpEntity<?> getFile(UUID id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attachment"));
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(attachment.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
                .body(attachmentContent.getBytes());
    }

    public HttpEntity<?> deletePhoto(UUID id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attachment"));
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(attachment.getId());
        attachmentContentRepository.delete(attachmentContent);
        attachmentRepository.deleteById(id);
        return ResponseEntity.ok().body(new ApiResponse("deleted", true));
    }
}
