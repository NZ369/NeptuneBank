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
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getSession } from 'app/shared/reducers/authentication';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { runInThisContext } from 'vm';

/* tslint:disable-next-line */
export type IMortgageState = {
  interestRate: number;
  termPeriod: number;
  principleAmount: number;
  monthlyPayment: number;
  allInterestRates: number[];
  allTermPeriods: number[];
};

export default class Mortgage extends React.Component<IMortgageState> {
  state: IMortgageState = {
    interestRate: 0,
    termPeriod: 0,
    principleAmount: 0,
    monthlyPayment: 0,
    allInterestRates: [1, 2, 3, 5],
    allTermPeriods: [10, 15, 20, 25, 30]
  };

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  calculateMortgage() {
    let m: number;
    let P: number;
    let n: number;
    let r: number;
    let t: number;

    P = this.state.principleAmount;
    n = this.state.termPeriod * 12;
    r = this.state.interestRate / (12 * 100); // monthly rate

    t = Math.pow(1 + r, n);

    // P[r(1+r)^n/((1+r)^n)-1)]
    m = P * ((r * t) / (t - 1));

    this.setState({ ...this.state, monthlyPayment: m.toFixed(2) });
  }

  // Function to handle the changing of any form input
  handleChange(event) {
    const value = event.target.value;
    this.setState({ ...this.state, [event.target.name]: value });
  }

  render() {
    return (
      <div>
        <h1>Mortgage Calculator</h1>
        <br />
        <div>
          {/* tslint:disable-next-line jsx-no-lambda */}
          <AvForm onChange={this.handleChange} onSubmit={() => this.calculateMortgage()}>
            <Col>
              <Row>
                <AvGroup>
                  <Label id="amountLabel" for="amount">
                    Loan Amount (in $)
                  </Label>
                  <AvInput
                    value={this.state.principleAmount}
                    id="principleAmount"
                    type="number"
                    className="form-control"
                    name="principleAmount"
                    onChange={this.handleChange}
                    validate={{
                      required: { value: true },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
              </Row>

              <Row>
                <AvGroup>
                  <Label id="interestLabel" for="interest">
                    Interest rate (% per annum)
                  </Label>
                  <AvInput
                    value={this.state.interestRate}
                    id="interestRate"
                    type="select"
                    className="form-control"
                    name="interestRate"
                    onChange={this.handleChange}
                    required
                  >
                    <option value="">-- Select --</option>
                    {this.state.allInterestRates
                      ? this.state.allInterestRates.map(t => (
                          <option value={t} key={t}>
                            {t}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
              </Row>

              <Row>
                <AvGroup>
                  <Label id="termLabel" for="term">
                    Term Period (years)
                  </Label>
                  <AvInput
                    value={this.state.termPeriod}
                    id="termPeriod"
                    type="select"
                    className="form-control"
                    name="termPeriod"
                    onChange={this.handleChange}
                    required
                  >
                    <option value="">-- Select --</option>
                    {this.state.allTermPeriods
                      ? this.state.allTermPeriods.map(t => (
                          <option value={t} key={t}>
                            {t}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
              </Row>

              <Button variant="contained" type="submit" size="small">
                Calculate
              </Button>
              <br />
            </Col>
          </AvForm>

          <br />
          <h4>Monthly Payment is: ${this.state.monthlyPayment}</h4>
          <h6>*All rates/amounts are in CAD. To explore options in different currencies please visit our Foreign Exchange calculator.</h6>
        </div>
      </div>
    );
  }
}
