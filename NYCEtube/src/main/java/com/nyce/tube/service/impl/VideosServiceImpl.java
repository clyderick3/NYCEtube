package com.nyce.tube.service.impl;

import com.nyce.tube.domain.Videos;
import com.nyce.tube.repository.VideosRepository;
import com.nyce.tube.service.VideosService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Videos}.
 */
@Service
@Transactional
public class VideosServiceImpl implements VideosService {

    private final Logger log = LoggerFactory.getLogger(VideosServiceImpl.class);

    private final VideosRepository videosRepository;

    public VideosServiceImpl(VideosRepository videosRepository) {
        this.videosRepository = videosRepository;
    }

    @Override
    public Videos save(Videos videos) {
        log.debug("Request to save Videos : {}", videos);
        return videosRepository.save(videos);
    }

    @Override
    public Videos update(Videos videos) {
        log.debug("Request to save Videos : {}", videos);
        return videosRepository.save(videos);
    }

    @Override
    public Optional<Videos> partialUpdate(Videos videos) {
        log.debug("Request to partially update Videos : {}", videos);

        return videosRepository
            .findById(videos.getId())
            .map(existingVideos -> {
                if (videos.getName() != null) {
                    existingVideos.setName(videos.getName());
                }
                if (videos.getUrl() != null) {
                    existingVideos.setUrl(videos.getUrl());
                }
                if (videos.getDate() != null) {
                    existingVideos.setDate(videos.getDate());
                }
                if (videos.getVideo() != null) {
                    existingVideos.setVideo(videos.getVideo());
                }
                if (videos.getVideoContentType() != null) {
                    existingVideos.setVideoContentType(videos.getVideoContentType());
                }
                if (videos.getCategories() != null) {
                    existingVideos.setCategories(videos.getCategories());
                }
                if (videos.getThumbnail() != null) {
                    existingVideos.setThumbnail(videos.getThumbnail());
                }
                if (videos.getThumbnailContentType() != null) {
                    existingVideos.setThumbnailContentType(videos.getThumbnailContentType());
                }

                return existingVideos;
            })
            .map(videosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Videos> findAll() {
        log.debug("Request to get all Videos");
        return videosRepository.findAllWithEagerRelationships();
    }

    public Page<Videos> findAllWithEagerRelationships(Pageable pageable) {
        return videosRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Videos> findOne(Long id) {
        log.debug("Request to get Videos : {}", id);
        return videosRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Videos : {}", id);
        videosRepository.deleteById(id);
    }
}
