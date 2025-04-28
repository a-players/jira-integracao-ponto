import axios from 'axios';
import * as utils from '../../utils';

export function getList(DPType, startRecord, previous, rowsPerPage, groups) {
    const url = utils.getRestURL('/user/list');
    let params = `records=${rowsPerPage}&startRecord=${startRecord ? startRecord : ''}&DPType=${DPType ? DPType : '1'}&previous=${previous}`;
    if (groups && groups.length > 0) {
        params += '&groups=' + groups;
    }
    return axios.get(url + '?' + params);
}

export function getListByKey(key) {
    const url = utils.getRestURL('/user/list/' + key);
    return axios.get(url);
}

export function getAllUserKeysList() {
    const url = utils.getRestURL('/user/list/username/all');
    return axios.get(url);
}

export function getAllGroupsList() {
    const url = utils.getRestURL('/user/list/groups/all');
    return axios.get(url);
}

export function updateUsers(users) {
    const url = utils.getRestURL('/user');
    return axios.put(url, users);
}