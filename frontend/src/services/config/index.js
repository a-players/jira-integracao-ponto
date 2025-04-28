import axios from 'axios';
import * as utils from '../../utils';

export function getConfig() {
    const url = utils.getRestURL('/config');
    return axios.get(url);
}

export function updateConfig(config) {
    const url = utils.getRestURL('/config');
    return axios.put(url, config);
}