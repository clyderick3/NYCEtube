import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert, Button, Table } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import VideoApp from '../home/videoapp';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { TextFormat, openFile, byteSize } from 'react-jhipster';
import { getEntities } from 'app/entities/videos/videos.reducer';

export const Home = (ReactElement) => {
  const account = useAppSelector(state => state.authentication.account);
  const videosList = useAppSelector(state => state.videos.entities);
  const loading = useAppSelector(state => state.videos.loading);
  const dispatch = useAppDispatch();
  const url = 'https://zcw-cohort8zero.s3.amazonaws.com/videoapp/sora.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220509T183032Z&X-Amz-SignedHeaders=host&X-Amz-Expires=604799&X-Amz-Credential=AKIAUTLWJ537YFVAWF46%2F20220509%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=69c99954726330f49a4a5bc58f975fd236de3a010e615feb10d7ad2b8cc31428';
  

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  

  // const { match } = props;

  // const VideoUser = (props: RouteComponentProps<{ url: string }>) => {
  return (
    <div className="centered">
      <Row>
      <Col>
        {account?.login ? (
          <div>
          </div>
        ) : (
          <div>
           
          </div>
        )}
         <div>
         <div>
         <h2 className="centered">Where you`re always watching ⊙=⊙</h2>
      <h3 className="centered" id="videos-heading" data-cy="VideosHeading">
        Viewfinder
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="red" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/videos/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Videos
          </Link>
        </div>
      </h3>
      <div className="table-responsive">
        {videosList && videosList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th></th> {/* Thumbnail */}
                <th></th> {/* Name */}
                <th></th> {/* Date */}
                {/* <th>Video</th> */}
                <th></th> {/* Category */}
                <th></th> {/* User */}
                <th />
              </tr>
            </thead>
            <tbody>
              {videosList.map((videos, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/* <td>
                    <Button tag={Link} to={`/videos/${videos.id}`} color="link" size="sm">
                      {videos.id}
                    </Button>
                  </td> */}
                  <Button tag={Link} to={`/videos/${videos.id}`} color="white" size="lg" data-cy="entityDetailsButton">
                       <span className="d-none d-md-inline"><img src={`data:${videos.thumbnailContentType};base64,${videos.thumbnail}`} style={{ maxHeight: '200px' }} /></span>
                      </Button>
                  <td>{videos.name}</td>
                  {/* <td>
                    {videos.video ? (
                      
                      <div>
                        
                        {videos.videoContentType ? <a onClick={openFile(videos.videoContentType, videos.video)}>Open &nbsp;</a> : null}
                        <span>
                          {videos.videoContentType}, {byteSize(videos.video)}
                        </span>
                      </div>
                    ) : null}
                  </td> */}
                  <td>{videos.categories}</td>
                  <td>{videos.user ? videos.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                    
                      <Button tag={Link} to={`/videos/${videos.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/videos/${videos.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Videos found</div>
        )}
      </div>
    </div>
        </div>
      </Col>
    </Row>
    </div>
  );
};
// }

export default Home;
