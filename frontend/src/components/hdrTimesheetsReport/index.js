import React, { useState } from 'react'
import Button from '@atlaskit/button';
//import { DatePicker } from '@atlaskit/datetime-picker';
//import { gridSize } from '@atlaskit/theme/constants';
import DatePicker, { registerLocale, setDefaultLocale } from "react-datepicker";
import ptBR from 'date-fns/locale/pt-BR';

import "react-datepicker/dist/react-datepicker.css";
import './index.css'

registerLocale('ptBR', ptBR);
setDefaultLocale('ptBR');

export default function HdrTimesheetsReport(props) {

    const [suggestions, setSuggestions] = useState([]);
    const [text, setText] = useState('');

    const [accountsuggestions, setaccountSuggestions] = useState([]);
    const [accounttext, setaccountText] = useState('');

    const [categoryaccountsuggestions, setcategoryaccountSuggestions] = useState([]);
    const [categoryaccounttext, setcategoryaccountText] = useState('');

    // Team
    function onTextChange(e) {
        let suggestions = [];
        const value = e.target.value;
        if (value.length > 0) {
            const regex = new RegExp(`^${value}`, `i`);
            suggestions = props.teamList.filter(v => regex.test(v.teamName));
        } else {
            props.onTeamSelected(null);
        }

        setSuggestions(suggestions);
        setText(value);
    }


    function suggestionSelected(value) {
        setText(value.teamName);
        setSuggestions([]);
        props.onTeamSelected(value);
    }

    function renderSuggestions() {

        if (suggestions.length === 0) {
            return null;
        }
        return (
            <div className="typeahead">
                <ul>
                    {suggestions.map(team => <li key={team.teamId} onClick={(e) => suggestionSelected(team)}>{team.teamName}</li>)}
                </ul>
            </div>

        )
    }
      // Account

    function onAccountTextChange(e) {
        let accountsuggestions = [];
        const value = e.target.value;
        if (value.length > 0) {
          const regex = new RegExp(`^${value}`, `i`);
          accountsuggestions = props.accountList.filter(v => regex.test(v.accountName));
        } else {
            props.onAccountSelected(null);
        }
     
        setaccountSuggestions(accountsuggestions);
        setaccountText(value);
    }
      
      
    function suggestionAccountSelected(value) {
          setaccountText(value.accountName);
          setaccountSuggestions([]);
          props.onAccountSelected(value);
    }

    function renderAccountSuggestions() {
        if (accountsuggestions.length === 0) {
          return null;
        }
        return (
            <div className="typeahead">
                <ul>
                    {accountsuggestions.map(account => <li key={account.accountId} onClick={(e)=>suggestionAccountSelected(account)}>{account.accountName}</li>)}
                </ul>
            </div>
          
        )
    }

      // Category Account

    function onCategoryAccountTextChange(e) {
        let categoryaccountsuggestions = [];
        const value = e.target.value;
        if (value.length > 0) {
          const regex = new RegExp(`^${value}`, `i`);
          categoryaccountsuggestions = props.categoryaccountList.filter(v => regex.test(v.categoryaccountName));
        } else {
            props.onCategoryAccountSelected(null);
        }
     
        setcategoryaccountSuggestions(categoryaccountsuggestions);
        setcategoryaccountText(value);
    }
      
      
    function suggestionategoryAccountSelected(value) {
          setcategoryaccountText(value.categoryaccountName);
          setcategoryaccountSuggestions([]);
          props.onCategoryAccountSelected(value);
    }

    function renderCategoryAccountSuggestions() {
        if (categoryaccountsuggestions.length === 0) {
          return null;
        }
        return (
            <div className="typeahead">
                <ul>
                    {categoryaccountsuggestions.map(categoryaccount => <li key={categoryaccount.categoryaccountId} onClick={(e)=>suggestionategoryAccountSelected(categoryaccount)}>{categoryaccount.categoryaccountName}</li>)}
                </ul>
            </div>
          
        )
    }

    return (
        <div className="hdrtimesheets-container">
            <div className="hdrtimesheets-left-elements">
                <div className="hdrtimesheets-element">
                    <div  className="titulo-campo">
                        <label htmlFor="stDate">
                            {props.texts && props.texts['relatorio.page.startdate']
                            ? props.texts['relatorio.page.startdate']
                            : 'Start Date'}:
                        </label>
                    </div>
                    <div className="TypeAheadDropDown">
                        <DatePicker
                            id="stDate"
                            selected={props.startDate}
                            onChange={props.onSetStartDate}
                            selectsStart
                            startDate={props.startDate}
                            endDate={props.endDate}
                            dateFormat="MM/yyyy"
                            showMonthYearPicker
                        />
                    </div>
                </div>
                <div className="hdrtimesheets-element">
                    <div  className="titulo-campo">
                        <label htmlFor="edDate">
                            {props.texts && props.texts['relatorio.page.enddate']
                            ? props.texts['relatorio.page.enddate']
                            : 'End Date'}:
                        </label>
                    </div>
                    <div className="TypeAheadDropDown">
                        <DatePicker
                            id="edDate"
                            selected={props.endDate}
                            onChange={props.onSetEndDate}
                            selectsEnd
                            startDate={props.startDate}
                            endDate={props.endDate}
                            dateFormat="MM/yyyy"
                            showMonthYearPicker
                        />
                    </div>
                </div>
                <div className="searchButton">
                    <label htmlFor="searchButton">&nbsp;</label>
                    <Button onClick={props.handleSearchStart}>
                        {props.texts && props.texts['relatorio.page.search']
                        ? props.texts['relatorio.page.search']
                        : 'Search'}
                    </Button>
                </div>
                <div className="hdrtimesheets-right-elements">
                    <Button onClick={props.handleExport}>
                        {props.texts && props.texts['relatorio.page.export']
                            ? props.texts['relatorio.page.export']
                            : 'Export to PDF'}
                    </Button>
                </div>
            </div>
            <div className='hdrtimesheets-campos'>         
                <div className="hdrtimesheets-new-row">
                    {props.selectedTab === 4 && (
                        <div className="hdrtimesheets-element">
                            <div className="titulo-campo">
                                {props.texts && props.texts['relatorio.page.accountsearch']
                                    ? props.texts['relatorio.page.accountsearch']
                                    : 'Account'}: &nbsp;
                            </div>
                            <div className="TypeAheadDropDown">
                                <input
                                onChange={onAccountTextChange}
                                placeholder={
                                    props.texts &&
                                    props.texts['relatorio.page.accountsearchplaceholder']
                                    ? props.texts['relatorio.page.accountsearchplaceholder']
                                    : 'Account'
                                }
                                value={accounttext}
                                type="text"
                                />
                                {renderAccountSuggestions()}
                            </div>
                        </div>
                    )}

                    {props.selectedTab === 4 && (
                        <div className="hdrtimesheets-element">
                            <div className="titulo-campo">
                                {props.texts &&
                                props.texts['relatorio.page.categoryaccountsearch']
                                    ? props.texts['relatorio.page.categoryaccountsearch']
                                    : 'Category Account'}: &nbsp;
                            </div>
                            <div className=" TypeAheadDropDown">
                                <input
                                onChange={onCategoryAccountTextChange}
                                placeholder={props.texts && props.texts['relatorio.page.categoryaccountsearchplaceholder']
                                            ? props.texts['relatorio.page.categoryaccountsearchplaceholder'] : 'Category Account'}
                                value={categoryaccounttext}
                                type="text"
                                />
                                {renderCategoryAccountSuggestions()}
                            </div>
                        </div>
                    )}

                    {props.selectedTab !== 4 && (
                        <div className="hdrtimesheets-element">
                            <div  className="titulo-campo">
                                {props.texts && props.texts['relatorio.page.teamsearch']
                                    ? props.texts['relatorio.page.teamsearch']
                                    : 'Team'}: &nbsp;
                            </div>
                            <div className="TypeAheadDropDown">
                                <input
                                onChange={onTextChange}
                                placeholder={
                                    props.texts &&
                                    props.texts['relatorio.page.searchplaceholder']
                                    ? props.texts['relatorio.page.searchplaceholder']
                                    : 'Team'
                                }
                                value={text}
                                type="text"
                                />
                                {renderSuggestions()}
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );

}