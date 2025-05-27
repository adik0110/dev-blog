package com.developerblog.devblog.service;

import com.developerblog.devblog.entity.Tag;
import com.developerblog.devblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));
    }

    @Transactional
    public Tag createTag(String name) {
        if (tagRepository.existsByName(name)) {
            throw new RuntimeException("Tag with name '" + name + "' already exists");
        }

        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag updateTag(Long id, String newName) {
        Tag tag = getTagById(id);
        tag.setName(newName);
        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = getTagById(id);
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    public List<Tag> getTagsByIds(List<Long> ids) {
        return tagRepository.findAllById(ids);
    }
}