import React, { Fragment } from 'react';
/* tslint:disable-next-line */
import Button from '@material-ui/core/Button';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Col, Row, Table, Label } from 'reactstrap';
import { ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import { getEntity, getEntities, reset } from './currency.reducer';
import { ICurrency } from 'app/shared/model/currency.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getSession } from 'app/shared/reducers/authentication';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import currencyDetail from './currency-detail';

export interface ICurrencyProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

/* tslint:disable-next-line */
export type ICurrencyState = {
  itemsPerPage: number;
  sort: string;
  order: string;
  activePage: number;
  exchangeAmount: number;
  exchangeCurrency: string;
  newCurrency: string;
  newAmount: number;
  transactionFee: number;
};

export class Currency extends React.Component<ICurrencyProps, ICurrencyState> {
  state: ICurrencyState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE),
    exchangeAmount: null,
    exchangeCurrency: null,
    newCurrency: null,
    newAmount: 0,
    transactionFee: null
  };

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  // Function to handle the changing of any form input. (Currency conversions)
  handleChange(event) {
    const value = event.target.value;
    this.setState({ ...this.state, [event.target.name]: value });
  }

  componentDidMount() {
    this.getEntities();
    this.props.getSession();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  convertCurrency() {
    const initialAmount = this.state.exchangeAmount;
    let tempAmount: number;
    let finalAmount: number;

    // 1 - convert from 'any' currency to CAD
    if (this.state.exchangeCurrency !== 'CAD') {
      // Find foreign currency exchange rate in currencyList
      const exchangeCurrencyList = this.props.currencyList.filter(e => e.currencyID === this.state.exchangeCurrency);
      const transFee = this.props.currencyList.filter(e => e.currencyID === 'CAD')[0].transactionFee;
      tempAmount = initialAmount * exchangeCurrencyList[0].cadPerUnit * (1 + transFee);
    } else {
      tempAmount = initialAmount;
    }
    // 2 - convert from CAD to 'any' currency
    if (this.state.newCurrency !== 'CAD') {
      // Find foreign currency exchange rate in currencyList
      const newCurrencyList = this.props.currencyList.filter(e => e.currencyID === this.state.newCurrency);
      const transFee = this.props.currencyList.filter(e => e.currencyID === 'CAD')[0].transactionFee;
      finalAmount = tempAmount * newCurrencyList[0].unitsPerCad * (1 + transFee);
    } else {
      finalAmount = tempAmount;
    }

    this.setState({ ...this.state, newAmount: finalAmount });
  }

  render() {
    const { currencyList, match, totalItems, isManager } = this.props;
    return (
      <div>
        <h2 id="currency-heading">Foreign Exchange Rates</h2>
        <h6>
          {' '}
          The buy & sell exchange rates can be calculated as: R * (1 + 0.015), where R is a rate seen in the table below, and our fee is
          added to the exchange transaction.
        </h6>
        <div className="table-responsive">
          {currencyList && currencyList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('currencyID')}>
                    Currency ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('currencyName')}>
                    Currency Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('unitsPerCad')}>
                    Units/CAD <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('cadPerUnit')}>
                    CAD/Unit <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand">Transaction Fee</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {currencyList.map((currency, index) => (
                  <tr key={`entity-${index}`}>
                    <td>{currency.currencyID}</td>
                    <td>{currency.currencyName}</td>
                    <td>{currency.unitsPerCad}</td>
                    <td>{currency.cadPerUnit}</td>
                    <td id="transactionFee">{currency.transactionFee}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Currencies found</div>
          )}
        </div>
        <div className={currencyList && currencyList.length > 0 ? '' : 'd-none'} />
        <h3 id="conversion-heading">Currency Conversions</h3>
        <h6>
          {/* tslint:disable-next-line jsx-no-lambda */}
          <AvForm onChange={this.handleChange} onSubmit={() => this.convertCurrency()}>
            <Row>
              <Col>
                <AvGroup>
                  <Label id="amountToExchangeLabel" for="exchangeAmount">
                    Amount To Exchange
                  </Label>
                  <AvInput
                    value={this.state.exchangeAmount}
                    id="exchangeAmount"
                    type="number"
                    className="form-control"
                    name="exchangeAmount"
                    onChange={this.handleChange}
                    validate={{
                      required: { value: true },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
              </Col>
              <Col>
                <AvGroup>
                  <Label id="exchangeCurrencyLabel" for="exchangeCurrency">
                    Exchange Currency
                  </Label>
                  <AvInput
                    id="exchangeCurrency"
                    value={this.state.exchangeCurrency}
                    type="select"
                    className="form-control"
                    name="exchangeCurrency"
                    onChange={this.handleChange}
                    required
                  >
                    <option value="">-- Select --</option>
                    {currencyList
                      ? currencyList.map(exchangeCurrencyEntity => (
                          <option value={exchangeCurrencyEntity.currencyID} key={exchangeCurrencyEntity.currencyID}>
                            {exchangeCurrencyEntity.currencyID}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
              </Col>
              <Col />
              <Col>
                <AvGroup>
                  <Label id="newCurrencyLabel" for="newCurrency">
                    New Currency
                  </Label>
                  <AvInput
                    id="newCurrency"
                    value={this.state.newCurrency}
                    type="select"
                    className="form-control"
                    name="newCurrency"
                    onChange={this.handleChange}
                    required
                  >
                    <option value="">-- Select --</option>
                    {currencyList
                      ? currencyList.map(newCurrencyEntity => (
                          <option value={newCurrencyEntity.currencyID} key={newCurrencyEntity.currencyID}>
                            {newCurrencyEntity.currencyID}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
              </Col>
              <Col>
                <AvGroup>
                  <Label id="newAmountLabel" for="newAmount">
                    New Amount
                  </Label>
                  <AvField
                    type="text"
                    name="newAmount"
                    id="newAmount"
                    className="form-control"
                    disabled="true"
                    style={{ fontWeight: 'bold' }}
                    value={this.state.newAmount.toFixed(2)}
                  />
                </AvGroup>
              </Col>
              <Button variant="contained" type="submit" size="small">
                Calculate
              </Button>
            </Row>
          </AvForm>
          <h6>
            ** Each conversion uses the Canadian Dollar (CAD) as intermediary currency prior to converting to the new currency selected.
            Calculations include the Neptunebank transaction fee.
          </h6>
        </h6>
      </div>
    );
  }
}

const mapStateToProps = ({ currency, authentication }: IRootState) => ({
  currencyList: currency.entities,
  totalItems: currency.totalItems,
  isManager: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.MANAGER])
});

const mapDispatchToProps = {
  getEntity,
  getEntities,
  getSession
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Currency);
