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
<div className = "container" >
  <div className="row">
    <div className="col">video1</div>
    <div className="col">video2</div>
    <div className="col">video3</div>

  </div>
  <div className="row">
    <div className="col">video4</div>
    <div className="col">video5</div>
    <div className="col">video6</div>

  </div>
  <div className="row">
    <div className="col">video7</div>
    <div className="col">video8</div>
    <div className="col">video9</div>

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
           <VideoApp/>
        </div>
      </Col>
    </Row>
    </div>
  );
};
// }

export default Home;
