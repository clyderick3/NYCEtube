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
      {/* <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col> */}
<div className="card-deck">
  <div className="card">
    <VideoApp vid = {url}/>
    <div className="card-body">
      <h5 className="card-title">video1</h5>
      <p className="card-text">This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
      <p className="card-text"><small className="text-muted">Last updated 3 mins ago</small></p>
    </div>
  </div>
  <div className="card">
  <VideoApp vid = {url} />
    <div className="card-body">
      <h5 className="card-title">Card title</h5>
      <p className="card-text">This card has supporting text below as a natural lead-in to additional content.</p>
      <p className="card-text"><small className="text-muted">Last updated 3 mins ago</small></p>
    </div>
  </div>
  <div className="card">
  <VideoApp  vid = {url}/>
    <div className="card-body">
      <h5 className="card-title">Card title</h5>
      <p className="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
      <p className="card-text"><small className="text-muted">Last updated 3 mins ago</small></p>
    </div>
  </div>
</div>





      <Col>
        <p>
        </p>
        <h2>Welcome to NYCEtube!</h2>
        <p className="lead">NYCE to see you ^_^</p>
        {account?.login ? (
          <div>
            <Alert color="success">You are logged in as user {account.login}.</Alert>
          </div>
        ) : (
          <div>
          </div>
        )}
         <div>
           <VideoApp vid = {url}/>
        </div>
      </Col>
    </Row>
    </div>
  );
};
// }

export default Home;
