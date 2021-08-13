import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Currency from './currency';
import PrivateRoute from 'app/shared/auth/private-route';
import CurrencyUpdate from './currency-update';
import { AUTHORITIES } from 'app/config/constants';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={CurrencyUpdate} hasAnyAuthorities={[AUTHORITIES.MANAGER]} />
      <ErrorBoundaryRoute path={match.url} component={Currency} />
    </Switch>
  </>
);

export default Routes;
