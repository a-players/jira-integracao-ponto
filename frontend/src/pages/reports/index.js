import React, { useState, useEffect } from 'react';
import Tabs from '@atlaskit/tabs';
import ApprovedTimesheetReport from '../apprtimesheetreport';
import SubmittedTimesheetReport from '../submtimesheetreport';
import UnderSubmittedHoursReport from '../underhoursreport';
import UnsubmittedHoursReport from '../unsubhoursreport';
import AccountHoursReport from '../accounthoursreport';
import { getContent } from '../../services/i18n';
import HdrTimesheetsReport from '../../components/hdrTimesheetsReport';
import Alert from '../../components/shared/alert';
import * as utils from '../../utils';
import { 
    getApprovedTimesheetsReport, 
    getSubmittedTimesheetsReport, 
    getUndersubmittedHoursReport, 
    getUnsubmittedHoursReport,
    getAccountHoursReport,
    getAllTeams,
    getAllAccounts,
    getAllCategoryAccounts 
} from '../../services/reports';

export default function Reports() {
    const [texts, setTexts] = useState();
    const [startDate, setStartDate] = useState(utils.getFirstDateOfMonth(new Date()));
    const [endDate, setEndDate] = useState(utils.getLastDateOfMonth(new Date()));
    
    const [teamList, setTeamList] = useState([]);
    const [selectedTeam, setSelectedTeam] = useState('');

    const [accountList, setAccountList] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState('');

    const [categoryaccountList, setCategoryAccountList] = useState([]);
    const [selectedCategoryAccount, setSelectedCategoryAccount] = useState('');
    
    const [isAlertOpen, setAlertOpen] = useState(false);
    const [msgHeader, setMsgHeader] = useState('');
    const [msgText, setMsgText] = useState('');
    const showAlert = () => setAlertOpen(true);
    const hideAlert = () => setAlertOpen(false);

    const [selectedTab, setSelectedTab] = useState();

    const [approvedTimesheetsList, setApprovedTimesheetsList] = useState([]);
    const [submittedTimesheetsList, setSubmittedTimesheetsList] = useState([]);
    const [undersubmittedHoursList, setUndersubmittedHoursList] = useState([]);
    const [unsubmittedHoursList, setUnsubmittedHoursList] = useState([]);
    const [accountHoursList, setAccountHoursList] = useState([]);

    /*
    const headers = [
        { id: 1, name: "Period", value: 'period' },
        { id: 2, name: "User", value: 'user' },
        { id: 3, name: "Team", value: 'team' },
        { id: 4, name: "Approver", value: 'approver' },
        { id: 5, name: "Hours Tempo", value: 'hourstempo' },
        { id: 6, name: "Hours Ponto", value: 'hoursponto' },
        { id: 7, name: "Origin", value: 'origin' },
        { id: 8, name: "Reason", value: 'reason' },
        { id: 9, name: "Approval Date Time", value: 'approvaldatetime' },
    ]
    
    const [selectedColums, setSelectedColumns] = useState(headers)
*/

    const tabs = [
        {
            label: (texts && texts['relatorio.page.apprtimesheets.tab.title']) || 'Approved Timesheets',
            content: <ApprovedTimesheetReport list={approvedTimesheetsList} />
        },
        {
            label: (texts && texts['relatorio.page.submtimesheets.tab.title']) || 'Pending of Approval Timesheets',
            content: <SubmittedTimesheetReport  list={submittedTimesheetsList} />
        },
        {
            label: (texts && texts['relatorio.page.underhours.tab.title']) || 'Partial Submitted Timesheets',
            content: <UnderSubmittedHoursReport  list={undersubmittedHoursList} />
        },
        {
            label: (texts && texts['relatorio.page.unsubhours.tab.title']) || 'Unsubmitted Timesheets',
            content: <UnsubmittedHoursReport   list={unsubmittedHoursList} />
        },
        {
            label: (texts && texts['relatorio.page.account.tab.title']) || 'Account Timesheets',
            content: <AccountHoursReport   list={accountHoursList} />
        }
    ];

    useEffect(initialize, []);
    useEffect(doGetList, [selectedTab, selectedTeam, selectedAccount, selectedCategoryAccount]);

    function initialize() {
        getContent('relatorio').then(
            res => {
                setTexts(res.data);
            }
        )
        getAllTeams().then(
            res => {
                setTeamList(res.data);
            }
        )
        getAllAccounts().then(
            res => {
                setAccountList(res.data);
            }
        )
        getAllCategoryAccounts().then(
            res => {
                setCategoryAccountList(res.data);
            }
        )
    }

    function onTabChange(selected) {
        setSelectedTab(selected);
    }

    function onStartDateChange(newStartDate) {
        setStartDate(utils.getFirstDateOfMonth(newStartDate));
    }

    function onEndDateChange(newEndDate) {
        setEndDate(utils.getLastDateOfMonth(newEndDate));
    }

    function handleExport() {
        const list = getSelectedList();
        if(!list || !list.length) {
            setMsgHeader(texts['relatorio.page.warning']);
            setMsgText(texts['relatorio.page.noexportdata']);
            showAlert();
            return false;
        }
        const tabName = getReportTitle();
        const sd = startDate ? startDate : utils.getFirstDateOfMonth(new Date());
        const ed = endDate ? endDate : utils.getLastDateOfMonth(new Date());
        const to = texts ? texts['relatorio.page.to'] : 'to';
        const formattedPeriod = (utils.formatPeriodDate(sd) + to + utils.formatPeriodDate(ed)).replaceAll('/', '_');
        const filename = tabName.replaceAll(' ', '') + formattedPeriod;
        const title = tabName + ': ' + utils.formatPeriodDate(sd) + ' ' + texts['relatorio.page.to'] + ' ' + utils.formatPeriodDate(ed);
        utils.exportToPdf(filename, title, [getReportTableId()], 'l');
        // utils.exportToCSV(filename, title, [getReportTableId()], 'l');
        // if (selected.value === 'xls') {
        //      utils.exportToCSV(list, filename, [getReportTableId()], 'l');
        // } else if(selected.value === 'pdf') {
        //    utils.exportToPdf(filename, title, [getReportTableId()], 'l');
        // }
    }

    function handleSearchStart() {
        if(validateSearch) {
            doGetList();
        }
    }

    function validateSearch() {
        if (startDate > endDate) {
            setMsgHeader(texts ? texts['relatorio.page.error'] : 'Error');
            setMsgText(texts ? texts['relatorio.page.wronginterval'] : 'Start Date cannot be higher than End Date');
            showAlert();
            return false;
        }
        return true;
    }

    function getSelectedList() {
        switch(selectedTab) {
            case 0:
                return approvedTimesheetsList.approvedTimesheetsList;
            case 1:
                return submittedTimesheetsList.submittedTimesheetsList;
            case 2:
                return undersubmittedHoursList.partialTimesheetReportsDTOList;
            case 3:
                return unsubmittedHoursList.unsubmittedHoursDTOList;
            case 4:
                return accountHoursList.accountHoursDTOList;
            default:
                return approvedTimesheetsList.approvedTimesheetsList;
        }
    }

    function getReportTitle() {
        switch(selectedTab) {
            case 0:
                return (texts && texts['relatorio.page.apprtimesheets.tab.title']) || 'Approved Timesheets';
            case 1:
                return (texts && texts['relatorio.page.submtimesheets.tab.title']) || 'Pending of Approval Timesheets';
            case 2:
                return (texts && texts['relatorio.page.underhours.tab.title']) || 'Partial Submitted Timesheets';
            case 3:
                return (texts && texts['relatorio.page.unsubhours.tab.title']) || 'Unsubmitted Timesheets';
            case 4:
                return (texts && texts['relatorio.page.account.tab.title']) || 'Account Timesheets';
            default:
                return (texts && texts['relatorio.page.apprtimesheets.tab.title']) || 'Approved Timesheets';
        }
    }

    function getReportTableId() {
        switch(selectedTab) {
            case 0:
                return 'tblApprTimesheetsReport';
            case 1:
                return 'tblSubmTimesheetsReport';
            case 2:
                return 'tblUnderHoursReport';
            case 3:
                return 'tblUnsubHoursReport';
            case 4:
                console.log('Definido report table')
                return 'tblAccountHoursReport';
            default:
                return 'tblApprTimesheetsReport';
        }
    }

    function doGetList() {
        if(validateSearch()) {
            switch(selectedTab) {
                case 0:
                    getApprovedTimesheetsLista();
                    break;
                case 1:
                    getSubmittedTimesheetsLista();
                    break;
                case 2:
                    getUndersubmittedHoursLista();
                    break;
                case 3:
                    getUnsubmittedHoursLista();
                    break;
                case 4:
                    getAccountHoursLista();
                    break;
                default:
                    getApprovedTimesheetsLista();
            }
        }
    }

    function getApprovedTimesheetsLista() {
        utils.showBlock(true);
        getApprovedTimesheetsReport(startDate, endDate, selectedTeam, selectedAccount, selectedCategoryAccount).then(
            res => {
                setApprovedTimesheetsList(res.data);
                utils.showBlock(false);
            })
            .catch(error => {
                setMsgHeader(texts && texts['relatorio.page.error'] ? texts['relatorio.page.error'] : 'Error');
                setMsgText(texts && texts['relatorio.page.genericerror'] ? texts['relatorio.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                utils.showBlock(false);
                showAlert();
            });

    }

    function getSubmittedTimesheetsLista() {
        utils.showBlock(true);
        getSubmittedTimesheetsReport(startDate, endDate, selectedTeam, selectedAccount, selectedCategoryAccount).then(
            res => {
                setSubmittedTimesheetsList(res.data);
                utils.showBlock(false);
            })
            .catch(error => {
                setMsgHeader(texts && texts['relatorio.page.error'] ? texts['relatorio.page.error'] : 'Error');
                setMsgText(texts && texts['relatorio.page.genericerror'] ? texts['relatorio.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                utils.showBlock(false);
                showAlert();
            });

    }

    function getUndersubmittedHoursLista() {
        utils.showBlock(true);
        getUndersubmittedHoursReport(startDate, endDate, selectedTeam).then(
            res => {
                setUndersubmittedHoursList(res.data);
                utils.showBlock(false);
            })
            .catch(error => {
                setMsgHeader(texts && texts['relatorio.page.error'] ? texts['relatorio.page.error'] : 'Error');
                setMsgText(texts && texts['relatorio.page.genericerror'] ? texts['relatorio.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                utils.showBlock(false);
                showAlert();
            }
        );

    }

    function getUnsubmittedHoursLista() {
        utils.showBlock(true);
        utils.getAllPeriods(startDate, endDate).then(
            res => {
                getUnsubmittedHoursReport(res.data.periods, selectedTeam, selectedAccount, selectedCategoryAccount).then(
                    res => {
                        setUnsubmittedHoursList(res.data);
                        utils.showBlock(false);
                    })
                    .catch(error => {
                        setMsgHeader(texts && texts['relatorio.page.error'] ? texts['relatorio.page.error'] : 'Error');
                        setMsgText(texts && texts['relatorio.page.genericerror'] ? texts['relatorio.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                        utils.showBlock(false);
                        showAlert();
                    });
            })
            .catch(error => {
                setMsgHeader(texts && texts['relatorio.page.error'] ? texts['relatorio.page.error'] : 'Error');
                setMsgText(texts && texts['relatorio.page.genericerror'] ? texts['relatorio.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                utils.showBlock(false);
                showAlert();
            });
        

    }

    function getAccountHoursLista() {
        utils.showBlock(true);
        getAccountHoursReport(startDate, endDate, selectedAccount, selectedCategoryAccount).then(
            res => {
                setAccountHoursList(res.data);
                utils.showBlock(false);
            })
            .catch(error => {
                setMsgHeader(texts && texts['relatorio.page.error'] ? texts['relatorio.page.error'] : 'Error');
                setMsgText(texts && texts['relatorio.page.genericerror'] ? texts['relatorio.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                utils.showBlock(false);
                showAlert();
            }
        );

    }



    /*
    function onColumnSelect(selectedList) {
        (selectedList && setSelectedColumns(selectedList));
    }
    */

    function onTeamSelected(team) {
        if(team && team != null) {
            setSelectedTeam(team.teamId);
        } else {
            setSelectedTeam(null);
        }
        
    }

    function onAccountSelected(account) {
        if(account && account != null) {
            setSelectedAccount(account.accountId);
        } else {
            setSelectedAccount(null);
        }
        
    }

    function onCategoryAccountSelected(categoryaccount) {
        if(categoryaccount && categoryaccount != null) {
            setSelectedCategoryAccount(categoryaccount.categoryaccountId);
        } else {
            setSelectedCategoryAccount(null);
        }
        
    }
    return (
        <div className='pages-container'>
            <Alert open={isAlertOpen} text={msgText} heading={msgHeader} onhide={hideAlert} />
            <div className='pages-title'>
                <h2>{texts && texts['relatorio.page.title']}</h2>
            </div>
            <HdrTimesheetsReport 
                onSetStartDate={onStartDateChange} 
                onSetEndDate={onEndDateChange} 
                startDate={startDate} 
                endDate={endDate} 
                handleSearchStart={handleSearchStart}
                handleExport={handleExport} 
                //setSelectedColumns={setSelectedColumns}
                teamList={teamList}
                onTeamSelected={onTeamSelected}
                accountList={accountList} 
                categoryaccountList={categoryaccountList}
                onAccountSelected={onAccountSelected}
                onCategoryAccountSelected={onCategoryAccountSelected}
                texts={texts}
                //headers={headers}
                //onColumnSelect={onColumnSelect}
                selectedTab={selectedTab}
            />
            <Tabs 
                tabs={tabs} 
                onSelect={(selected,
                    selectedIndex) => {
                    onTabChange(selectedIndex);
                }}
                selected={selectedTab}
            />
        </div>
    );


}