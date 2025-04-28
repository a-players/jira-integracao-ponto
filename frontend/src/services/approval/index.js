import axios from 'axios';
import * as utils from '../../utils';

export function getApprovalsList(startDate, endDate) {
    const url = utils.getRestURL('/approvehours/list/' + startDate + '/' + endDate);
    return axios.get(url);
}

export function approveTimesheet(team, previousTimesheetActionId) {
    const url = utils.getRestURL(`/approvehours/approve/${team}/${previousTimesheetActionId}`);
    return axios.post(url, {});
}

export function submitTimesheet(item) {
    const url = utils.getRestURL(`/approvehours/submit/${item.teamId}`);
    let submit = {"userKey" : item.workerKey, "period" : item.period, "workedTime" : item.horasTempo * 3600, "submittedTime" : item.horasTempo * 3600, "requiredTime" : item.requiredTime * 3600 };
    return axios.post(url, submit);
}

export function rejectTimesheet(team, previousTimesheetActionId, reason) {
    const url = utils.getRestURL(`/approvehours/reject/${team}/${previousTimesheetActionId}`);
    return axios.post(url, {"reason" : reason});
}

export function reopenTimesheet(team, previousTimesheetActionId, reason) {
    const url = utils.getRestURL(`/approvehours/reopen/${team}/${previousTimesheetActionId}`);
    return axios.post(url, {"reason" : reason});
}