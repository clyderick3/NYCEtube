import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';


import VideosUpdate from './videos-update';
import VideosDeleteDialog from './videos-delete-dialog';
import VideosDetail from '../videos-user/videos-user-detail';
// import Videos from './videos';
// import VideosDetail from './videos-detail';
import { VideosUser } from '../videos-user/videos-user';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VideosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VideosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VideosDetail} />
      <ErrorBoundaryRoute path={match.url} component={VideosUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VideosDeleteDialog} />
  </>
);

export default Routes;
