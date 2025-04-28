import React, { useState, useEffect } from 'react';
import TblAuditPeriod from '../../components/tblAuditPeriod';
import HdrAuditPeriod from '../../components/hdrAuditPeriod';
import Alert from '../../components/shared/alert';

import * as utils from '../../utils';
import { getLogsList } from '../../services/logs';
import { getContent } from '../../services/i18n';

export default function AuditPeriod() {
    const [texts, setTexts] = useState();
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [list, setList] = useState([]);

    const [isAlertOpen, setAlertOpen] = useState(false);
    const [msgHeader, setMsgHeader] = useState('');
    const [msgText, setMsgText] = useState('');
    const showAlert = () => setAlertOpen(true);
    const hideAlert = () => setAlertOpen(false);

    useEffect(initialize, []);
    
    useEffect(getLista, [startDate, endDate, texts]);

    function initialize() {
        getContent('logs').then(
            res => {
                setTexts(res.data);
            }
        )
    }

    function getLista() {
        utils.showBlock(true);
        getLogsList(startDate, endDate).then(
            res => {
                setList(res.data);
                utils.showBlock(false);
            })
            .catch(error => {
                setMsgHeader(texts && texts['logs.page.error'] ? texts['logs.page.error'] : 'Error');
                setMsgText(texts && texts['logs.page.genericerror'] ? texts['logs.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                utils.showBlock(false);
                showAlert();
            });

    }

    function handleSearchStart(startDate, endDate) {
        setStartDate(startDate);
        setEndDate(endDate);
    }

    function handleExport(selected) {
        if(!list || !list.length) {
            setMsgHeader(texts['logs.page.warning']);
                setMsgText(texts['logs.page.noexportdata']);
                showAlert();
                return false;
        }
        const sd = startDate ? startDate : new Date().toISOString().split('T')[0];
        const ed = endDate ? endDate : new Date().toISOString().split('T')[0];
        const to = texts ? texts['logs.page.to'] : 'to';
        const formattedPeriod = utils.formatISODate(sd, '_') + to + utils.formatISODate(ed, '_');
        const filename = texts['logs.page.title'].replaceAll(' ', '') + formattedPeriod;
        if (selected.value === 'xls') {
            const headers = [[
                texts && texts['logs.page.period'] ? texts['logs.page.period'] : 'Period',
                texts && texts['logs.page.previousstate'] ? texts['logs.page.previousstate'] : 'Previous Status',
                texts && texts['logs.page.newstate'] ? texts['logs.page.newstate'] : 'Actual Status',
                texts && texts['logs.page.datetime'] ? texts['logs.page.datetime'] : 'Date and Time of Change'

            ]];
             utils.exportToCSV(list, filename, formattedPeriod, headers);
        } else if(selected.value === 'pdf') {
            const title = texts['logs.page.title'] + ': ' + utils.formatISODate(sd, '/') + ' ' + texts['logs.page.to'] + ' ' + utils.formatISODate(ed, '/');
            utils.exportToPdf(filename, title, ['tblAudit']);
        }
    }

    return (
        <div className='pages-container'>
            <Alert open={isAlertOpen} text={msgText} heading={msgHeader}  onhide={hideAlert} />
            <div className='pages-title'>
                <h2>{texts && texts['logs.page.title']}</h2>
            </div>
            <HdrAuditPeriod handleSearchStart={handleSearchStart} handleExport={handleExport} texts={texts}/>
            <TblAuditPeriod data={list} texts={texts}/>
        </div>
    );
}
