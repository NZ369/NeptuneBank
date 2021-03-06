import './header.scss';
import React, { useState } from 'react';
import { Navbar, Nav, NavbarToggler, NavbarBrand, Collapse } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavLink as Link } from 'react-router-dom';
import LoadingBar from 'react-redux-loading-bar';
import {
  Home,
  Login,
  Register,
  Brand,
  AboutUs,
  ContactUs,
  LoanApproval,
  OurBranches,
  Mortgage,
  ForeignExchange,
  ManagePayees,
  NewsUpdates,
  DocVerification
} from './header-components';
import { AdminMenu, EntitiesMenu, AccountMenu } from '../menus';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isSwaggerEnabled: boolean;
  isNotUser: boolean;
  isManager: boolean;
  isStaff: boolean;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">Development</a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div id="app-header">
      <LoadingBar className="loading-bar" />
      <Navbar dark expand="sm" fixed="top" className="jh-navbar">
        <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
        <Brand />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="header-tabs" className="ml-auto" navbar>
            <Home />
            {!props.isAuthenticated && <Login />}
            {!props.isAuthenticated && <Register />}
            {props.isAuthenticated && <OurBranches />}
            {props.isAuthenticated && <Mortgage />}
            {props.isAuthenticated && <ForeignExchange />}
            {props.isManager && <LoanApproval />}
            {props.isManager && <DocVerification />}
            {props.isAuthenticated && <EntitiesMenu />}
            {props.isAuthenticated && !props.isNotUser && <ManagePayees />}
            {props.isAuthenticated && props.isAdmin && (
              <AdminMenu showSwagger={props.isSwaggerEnabled} showDatabase={!props.isInProduction} />
            )}
            {props.isAuthenticated && <AccountMenu />}
            <AboutUs />
            <ContactUs />
            <NewsUpdates />
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
