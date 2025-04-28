import axios from 'axios';
import { getRestURL, getFirstDateOfMonth, getLastDateOfMonth } from '../../utils';

export function getApprovedTimesheetsReport(startDate, endDate, team, account, categoryaccount) {
    const url = getRestURL('/reports/timesheets/approved');
    if(startDate === '') {
        startDate = getFirstDateOfMonth(new Date());
    }
    if(endDate === '') {
        endDate = getLastDateOfMonth(new Date());
    }
    let params = `startDate=${encodeURIComponent(startDate.toISOString().split('T')[0])}&endDate=${encodeURIComponent(endDate.toISOString().split('T')[0])}`;
    params += team ? '&teamId=' + team : '';
    params += account ? '&accountId=' + account : '';
    params += categoryaccount ? '&categoryaccountId=' + categoryaccount : '';
    return axios.get(url + '?' + params);
}

export function getSubmittedTimesheetsReport(startDate, endDate, team, account, categoryaccount) {
    const url = getRestURL('/reports/timesheets/submitted');
    if(startDate === '') {
        startDate = getFirstDateOfMonth(new Date());
    }
    if(endDate === '') {
        endDate = getLastDateOfMonth(new Date());
    }
    let params = `startDate=${encodeURIComponent(startDate.toISOString().split('T')[0])}&endDate=${encodeURIComponent(endDate.toISOString().split('T')[0])}`;
    params += team ? '&teamId=' + team : '';
    params += account ? '&accountId=' + account : '';
    params += categoryaccount ? '&categoryaccountId=' + categoryaccount : '';
    return axios.get(url + '?' + params);
}

export function getUndersubmittedHoursReport(startDate, endDate, team) {
    const url = getRestURL('/reports/hours/undersubmitted');
    if(startDate === '') {
        startDate = getFirstDateOfMonth(new Date());
    }
    if(endDate === '') {
        endDate = getLastDateOfMonth(new Date());
    }

    const data = {
        startDate: startDate.toISOString().split('T')[0],
        endDate: endDate.toISOString().split('T')[0],
        teamId : team ? team : null
    }

    return axios.post(url, data);
}

export function getUnsubmittedHoursReport(periods, team, account, categoryaccount) {
    const url = getRestURL('/reports/hours/unsubmitted');
    
    const data = {
        periods: periods,
        teamId : team ? team : null,
        accountId : account ? account : null,
        categoryaccountId : categoryaccount ? categoryaccount : null
    }

    return axios.post(url, data);
}

export function getAccountHoursReport(startDate, endDate, account, categoryaccount) {
    
  const url = getRestURL('/reports/hours/accounts');

  if(startDate === '') {
    startDate = getFirstDateOfMonth(new Date());
    }
  if(endDate === '') {
    endDate = getLastDateOfMonth(new Date());
    }

const data = {
    startDate: startDate.toISOString().split('T')[0],
    endDate: endDate.toISOString().split('T')[0],
    accountId : account ? account : null,
    categoryaccountId : categoryaccount ? categoryaccount : null
}


  return axios.post(url, data);
}

export function getAllTeams() {
    const url = getRestURL('/reports/teams');
    return axios.get(url);
}

export function getAllAccounts() {
  const url = getRestURL('/reports/accounts');
  return axios.get(url);
}

export function getAllCategoryAccounts() {
  const url = getRestURL('/reports/categoryaccounts');
  return axios.get(url);
}