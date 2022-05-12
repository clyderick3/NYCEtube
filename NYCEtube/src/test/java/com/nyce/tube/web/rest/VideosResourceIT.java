package com.nyce.tube.web.rest;

import static com.nyce.tube.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nyce.tube.IntegrationTest;
import com.nyce.tube.domain.Videos;
import com.nyce.tube.domain.enumeration.Categories;
import com.nyce.tube.repository.VideosRepository;
import com.nyce.tube.service.VideosService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link VideosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VideosResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_VIDEO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VIDEO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_CONTENT_TYPE = "image/png";

    private static final Categories DEFAULT_CATEGORIES = Categories.ARTSCRAFTS;
    private static final Categories UPDATED_CATEGORIES = Categories.FASHIONBEAUTY;

    private static final String ENTITY_API_URL = "/api/videos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VideosRepository videosRepository;

    @Mock
    private VideosRepository videosRepositoryMock;

    @Mock
    private VideosService videosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideosMockMvc;

    private Videos videos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Videos createEntity(EntityManager em) {
        Videos videos = new Videos()
            .name(DEFAULT_NAME)
            .date(DEFAULT_DATE)
            .video(DEFAULT_VIDEO)
            .videoContentType(DEFAULT_VIDEO_CONTENT_TYPE)
            .categories(DEFAULT_CATEGORIES);
        return videos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Videos createUpdatedEntity(EntityManager em) {
        Videos videos = new Videos()
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .categories(UPDATED_CATEGORIES);
        return videos;
    }

    @BeforeEach
    public void initTest() {
        videos = createEntity(em);
    }

    @Test
    @Transactional
    void createVideos() throws Exception {
        int databaseSizeBeforeCreate = videosRepository.findAll().size();
        // Create the Videos
        restVideosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videos)))
            .andExpect(status().isCreated());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeCreate + 1);
        Videos testVideos = videosList.get(videosList.size() - 1);
        assertThat(testVideos.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVideos.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVideos.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testVideos.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testVideos.getCategories()).isEqualTo(DEFAULT_CATEGORIES);
    }

    @Test
    @Transactional
    void createVideosWithExistingId() throws Exception {
        // Create the Videos with an existing ID
        videos.setId(1L);

        int databaseSizeBeforeCreate = videosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videos)))
            .andExpect(status().isBadRequest());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = videosRepository.findAll().size();
        // set the field null
        videos.setName(null);

        // Create the Videos, which fails.

        restVideosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videos)))
            .andExpect(status().isBadRequest());

        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVideos() throws Exception {
        // Initialize the database
        videosRepository.saveAndFlush(videos);

        // Get all the videosList
        restVideosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videos.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))))
            .andExpect(jsonPath("$.[*].categories").value(hasItem(DEFAULT_CATEGORIES.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVideosWithEagerRelationshipsIsEnabled() throws Exception {
        when(videosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVideosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(videosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVideosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(videosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVideosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(videosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVideos() throws Exception {
        // Initialize the database
        videosRepository.saveAndFlush(videos);

        // Get the videos
        restVideosMockMvc
            .perform(get(ENTITY_API_URL_ID, videos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videos.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.videoContentType").value(DEFAULT_VIDEO_CONTENT_TYPE))
            .andExpect(jsonPath("$.video").value(Base64Utils.encodeToString(DEFAULT_VIDEO)))
            .andExpect(jsonPath("$.categories").value(DEFAULT_CATEGORIES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVideos() throws Exception {
        // Get the videos
        restVideosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVideos() throws Exception {
        // Initialize the database
        videosRepository.saveAndFlush(videos);

        int databaseSizeBeforeUpdate = videosRepository.findAll().size();

        // Update the videos
        Videos updatedVideos = videosRepository.findById(videos.getId()).get();
        // Disconnect from session so that the updates on updatedVideos are not directly saved in db
        em.detach(updatedVideos);
        updatedVideos
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .categories(UPDATED_CATEGORIES);

        restVideosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVideos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVideos))
            )
            .andExpect(status().isOk());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
        Videos testVideos = videosList.get(videosList.size() - 1);
        assertThat(testVideos.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVideos.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVideos.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testVideos.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testVideos.getCategories()).isEqualTo(UPDATED_CATEGORIES);
    }

    @Test
    @Transactional
    void putNonExistingVideos() throws Exception {
        int databaseSizeBeforeUpdate = videosRepository.findAll().size();
        videos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, videos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVideos() throws Exception {
        int databaseSizeBeforeUpdate = videosRepository.findAll().size();
        videos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVideos() throws Exception {
        int databaseSizeBeforeUpdate = videosRepository.findAll().size();
        videos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVideosWithPatch() throws Exception {
        // Initialize the database
        videosRepository.saveAndFlush(videos);

        int databaseSizeBeforeUpdate = videosRepository.findAll().size();

        // Update the videos using partial update
        Videos partialUpdatedVideos = new Videos();
        partialUpdatedVideos.setId(videos.getId());

        partialUpdatedVideos.name(UPDATED_NAME).date(UPDATED_DATE).video(UPDATED_VIDEO).videoContentType(UPDATED_VIDEO_CONTENT_TYPE);

        restVideosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideos))
            )
            .andExpect(status().isOk());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
        Videos testVideos = videosList.get(videosList.size() - 1);
        assertThat(testVideos.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVideos.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVideos.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testVideos.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testVideos.getCategories()).isEqualTo(DEFAULT_CATEGORIES);
    }

    @Test
    @Transactional
    void fullUpdateVideosWithPatch() throws Exception {
        // Initialize the database
        videosRepository.saveAndFlush(videos);

        int databaseSizeBeforeUpdate = videosRepository.findAll().size();

        // Update the videos using partial update
        Videos partialUpdatedVideos = new Videos();
        partialUpdatedVideos.setId(videos.getId());

        partialUpdatedVideos
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .categories(UPDATED_CATEGORIES);

        restVideosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideos))
            )
            .andExpect(status().isOk());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
        Videos testVideos = videosList.get(videosList.size() - 1);
        assertThat(testVideos.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVideos.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVideos.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testVideos.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testVideos.getCategories()).isEqualTo(UPDATED_CATEGORIES);
    }

    @Test
    @Transactional
    void patchNonExistingVideos() throws Exception {
        int databaseSizeBeforeUpdate = videosRepository.findAll().size();
        videos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, videos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVideos() throws Exception {
        int databaseSizeBeforeUpdate = videosRepository.findAll().size();
        videos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVideos() throws Exception {
        int databaseSizeBeforeUpdate = videosRepository.findAll().size();
        videos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(videos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Videos in the database
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVideos() throws Exception {
        // Initialize the database
        videosRepository.saveAndFlush(videos);

        int databaseSizeBeforeDelete = videosRepository.findAll().size();

        // Delete the videos
        restVideosMockMvc
            .perform(delete(ENTITY_API_URL_ID, videos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Videos> videosList = videosRepository.findAll();
        assertThat(videosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
