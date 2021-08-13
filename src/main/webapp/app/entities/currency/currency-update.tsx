import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import { getEntity, getEntities, reset } from './currency.reducer';
import { ICurrency } from 'app/shared/model/currency.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICurrencyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICurrencyUpdateState {
  isNew: boolean;
}

export class CurrencyUpdate extends React.Component<ICurrencyUpdateProps, ICurrencyUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  callGoBack = () => {
    this.props.history.goBack();
  };

  handleClose = () => {
    this.props.history.push('/entity/currency');
  };

  render() {
    const { currencyEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="neptunebank.currency.home.createOrEditLabel">Create or edit a currency</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : currencyEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="currency-id">Currency ID</Label>
                    <AvInput id="currency-id" type="text" className="form-control" name="currencyID" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="currency-name">
                    Currency Name
                  </Label>
                  <AvField
                    id="currency-name"
                    type="text"
                    name="currencyName"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="unitsPerCadLabel" for="currency-unitsPerCad">
                    Units/CAD
                  </Label>
                  <AvField
                    id="currency-unitsPerCad"
                    type="text"
                    name="unitsPerCad"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="currencyCadPerUnitLabel" for="currency-cadPerUnit">
                    CAD/Unit
                  </Label>
                  <AvField
                    id="currency-cadPerUnit"
                    type="text"
                    name="cadPerUnit"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="transactionFeeLabel" for="currency-transactionFee">
                    Transaction Fee
                  </Label>
                  <AvField id="currency-transactionFee" type="text" name="transactionFee" />
                </AvGroup>
                <Button onClick={this.callGoBack} color="info">
                  <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  currencyEntity: storeState.currency.entity,
  loading: storeState.currency.loading,
  updating: storeState.currency.updating,
  updateSuccess: storeState.currency.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CurrencyUpdate);
