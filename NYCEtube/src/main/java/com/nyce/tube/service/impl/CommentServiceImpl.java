package com.nyce.tube.service.impl;

import com.nyce.tube.domain.Comment;
import com.nyce.tube.repository.CommentRepository;
import com.nyce.tube.service.CommentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comment}.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        log.debug("Request to save Comment : {}", comment);
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        log.debug("Request to save Comment : {}", comment);
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> partialUpdate(Comment comment) {
        log.debug("Request to partially update Comment : {}", comment);

        return commentRepository
            .findById(comment.getId())
            .map(existingComment -> {
                if (comment.getDate() != null) {
                    existingComment.setDate(comment.getDate());
                }
                if (comment.getText() != null) {
                    existingComment.setText(comment.getText());
                }

                return existingComment;
            })
            .map(commentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        log.debug("Request to get all Comments");
        return commentRepository.findAllWithEagerRelationships();
    }

    public Page<Comment> findAllWithEagerRelationships(Pageable pageable) {
        return commentRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
    }
}
