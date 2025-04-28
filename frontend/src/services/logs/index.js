import axios from 'axios';
import * as utils from '../../utils';

export function getLogsList(startDate, endDate) {
    const url = utils.getRestURL('/auditperiod/list');
    if(startDate === '') {
        startDate = new Date().toISOString().split('T')[0];
    }
    if(endDate === '') {
        endDate = new Date().toISOString().split('T')[0];
    }
    const params = `startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`;
    return axios.get(url + '?' + params);
}