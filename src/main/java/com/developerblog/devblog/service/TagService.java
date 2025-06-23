package com.developerblog.devblog.service;

import com.developerblog.devblog.dto.TagDto;
import com.developerblog.devblog.entity.Post;
import com.developerblog.devblog.entity.Tag;
import com.developerblog.devblog.repository.PostRepository;
import com.developerblog.devblog.repository.TagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final EntityManager entityManager;

    public TagService(TagRepository tagRepository, EntityManager entityManager) {
        this.tagRepository = tagRepository;
        this.entityManager = entityManager;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public void createTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag not found with id: " + id);
        }

        entityManager.createNativeQuery("DELETE FROM post_tags WHERE tag_id = :tagId")
                .setParameter("tagId", id)
                .executeUpdate();

        tagRepository.deleteById(id);
    }
}