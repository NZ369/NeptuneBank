import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Mortgage from './mortgage';
import PrivateRoute from 'app/shared/auth/private-route';

import { AUTHORITIES } from 'app/config/constants';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute path={match.url} component={Mortgage} />
    </Switch>
  </>
);

export default Routes;
