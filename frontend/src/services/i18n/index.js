import axios from 'axios';
import * as utils from '../../utils';

export function getContent(page) {
    const url = utils.getRestURL('/i18n?page=' + page);
    return axios.get(url);
}