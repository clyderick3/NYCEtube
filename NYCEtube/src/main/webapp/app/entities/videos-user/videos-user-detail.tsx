import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './videos-user.reducer';
import VideoApp from 'app/modules/home/videoapp';
import VideoPlayer from 'react-video-js-player';


export const VideosDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const videosEntity = useAppSelector(state => state.videos.entity);
  return (
    <Row>
      <VideoApp vid = {videosEntity} />
      <Col md="8">
        <h2 data-cy="videosDetailsHeading">{videosEntity.name}</h2>
        <dl className="jh-entity-details">
          <dd>
            {videosEntity.video ? (
              <div>
                {videosEntity.videoContentType ? (
                  <a onClick={openFile(videosEntity.videoContentType, videosEntity.video)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {videosEntity.videoContentType}, {byteSize(videosEntity.video)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="categories">Categories</span>
          </dt>
          <dd>{videosEntity.categories}</dd>
          <dt>Created by:</dt>
          <dd>{videosEntity.user ? videosEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/videos/${videosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VideosDetail;
