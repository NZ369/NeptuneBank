import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import { getEntity, getEntities, reset } from './currency.reducer';
import { ICurrency } from 'app/shared/model/currency.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICurrencyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CurrencyDetail extends React.Component<ICurrencyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  callGoBack = () => {
    this.props.history.goBack();
  };

  render() {
    const { currencyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Currency [<b>{currencyEntity.currencyID}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="currencyName">Currency Name</span>
            </dt>
            <dd>{currencyEntity.currencyName}</dd>
            <dt>
              <span id="unitsPerCad">Units/CAD</span>
            </dt>
            <dd>{currencyEntity.unitsPerCad}</dd>
            <dt>
              <span id="cadPerUnit">CAD/Unit</span>
            </dt>
            <dd>{currencyEntity.cadPerUnit}</dd>
            <dt>
              <span id="transactionFee">Transaction Fee</span>
            </dt>
            <dd>{currencyEntity.transactionFee}</dd>
          </dl>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ currency }: IRootState) => ({
  currencyEntity: currency.entity
});

const mapDispatchToProps = { getEntity, getEntities, reset };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CurrencyDetail);
