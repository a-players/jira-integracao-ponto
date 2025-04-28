import React, { useState, useEffect } from 'react';
import Button from '@atlaskit/button';
import './index.css';
export default function TblAuditPeriod(props) {

    const [data, setData] = useState([]);
    const [period, setPeriod] = useState([]);

    useEffect(() => {
        setData(props.data);
        setPeriod(props.period);
    }, [props])

    return (
        <div>
            { data.submittedTimesheets && data.submittedTimesheets.length > 0 && (
            <table id="tblWaitingApproval" className="pages-tbl">
                <thead>
                    <tr>
                        <th colSpan="6">
                            <h3>
                                {
                                    props.texts && props.texts['aprovacao.page.submittedts'] ? 
                                    props.texts['aprovacao.page.submittedts'] 
                                    : 
                                    'Timesheets waiting for approval' 

                                }
                                &nbsp;({data.submittedTimesheets.length})
                            </h3>
                        </th>
                    </tr>
                    <tr>
                        <th width="20%">{props.texts ? props.texts['aprovacao.page.team'] : 'Team'}</th>
                        <th width="30%">{props.texts ? props.texts['aprovacao.page.teammember'] : 'Member'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.hourstimesheet'] : 'Jira Hours'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.hoursponto'] : 'Check-in Hours'}</th>
                        <th width="10%"></th>
                        <th width="20%">{props.texts ? props.texts['aprovacao.page.actions'] : 'Actions'}</th>
                    </tr>
                </thead>
                <tbody>

                       {     data.submittedTimesheets.map((item) => {
                                return (
                                    <tr key={item.timeSheetApprovalId}>
                                        <td>{item.team}</td>
                                        <td>{item.username}</td>
                                        <td>
                                            <a href={`/secure/Tempo.jspa#/my-work/timesheet?columns=WORKED_COLUMN&dateDisplayType=days&from=${period.startDate}&groupBy=issue&periodType=FIXED&to=${period.endDate}&worker=${item.workerKey}`} rel="noreferrer" target="_blank">
                                                {Number(item.horasTempo).toFixed(2)}
                                            </a>
                                        </td>
                                        <td>{((item.horasPonto == '?') ? item.horasPonto : Number(item.horasPonto).toFixed(2))}</td>
                                        <td></td>
                                        <td>
                                            { 
                                                item.canApprove &&
                                                <span className="span-button">
                                                    {
                                                        item.horasTempo > item.horasPonto &&
                                                        <Button appearance="primary" spacing="compact" isDisabled>{props.texts ? props.texts['aprovacao.page.approve'] : 'Approve'}</Button>
                                                    }
                                                    {

                                                        item.canApprove && (item.horasTempo <= item.horasPonto || isNaN(item.horasPonto)) &&
                                                        <Button appearance="primary" spacing="compact" onClick={() => props.onApprove(item.teamId, item.timeSheetApprovalId)}>{props.texts ? props.texts['aprovacao.page.approve'] : 'Approve'}</Button>
                                                    }

                                                </span>
                                            }
                                            { 
                                                item.canApprove &&
                                                <span className="span-button">
                                                    <Button appearance="danger" spacing="compact" onClick={() => props.onReject(item)}>{props.texts ? props.texts['aprovacao.page.reject'] : 'Reject'}</Button>
                                                </span>
                                            }
                                        </td>
                                    </tr>
                                );
                            })
                        }
                    
                </tbody>
            </table>
            )}
            { data.readyToSubmitTimesheets && data.readyToSubmitTimesheets.length > 0 && (
            <table id="tblReady" className="pages-tbl">
                <thead>
                    <tr>
                        <th colSpan="5">
                            <h3>
                                {
                                    props.texts && props.texts['aprovacao.page.readyts'] ? 
                                    props.texts['aprovacao.page.readyts'] 
                                    : 
                                    'Timesheets ready to submit' 
                                }
                                &nbsp;({data.readyToSubmitTimesheets.length})
                            </h3>
                        </th>
                    </tr>
                    <tr>
                        <th width="20%">{props.texts ? props.texts['aprovacao.page.team'] : 'Team'}</th>
                        <th width="30%">{props.texts ? props.texts['aprovacao.page.teammember'] : 'Member'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.hourstimesheet'] : 'Jira Hours'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.hoursponto'] : 'Check-in Hours'}</th>
                        <th width="10%"></th>
                        <th width="20%">{props.texts ? props.texts['aprovacao.page.actions'] : 'Actions'}</th>
                    </tr>
                </thead>
                <tbody>
                    {
                            data.readyToSubmitTimesheets.map((item) => {
                                return (
                                    <tr key={item.username}>
                                        <td>{item.team}</td>
                                        <td>{item.username}</td>
                                        <td>
                                            <a href={`/secure/Tempo.jspa#/my-work/timesheet?columns=WORKED_COLUMN&dateDisplayType=days&from=${period.startDate}&groupBy=issue&periodType=FIXED&to=${period.endDate}&worker=${item.workerKey}`} rel="noreferrer" target="_blank">
                                                {Number(item.horasTempo).toFixed(2)}
                                            </a>
                                        </td>
                                        <td>{((item.horasPonto == '?') ? item.horasPonto : Number(item.horasPonto).toFixed(2))}</td>
                                        <td></td>
                                        <td>
                                            { 
                                                item.canApprove &&
                                                <span className="span-button">
                                                    <Button appearance="primary" spacing="compact" onClick={() => props.onSubmit(item)}>{props.texts ? props.texts['aprovacao.page.submit'] : 'Submit'}</Button>
                                                </span>
                                            }
                                        </td>
                                    </tr>
                                );
                            })

                    }
                </tbody>
            </table>
            )}
            { data.openTimesheets && data.openTimesheets.length > 0 && (
            <table id="tblOpen" className="pages-tbl">
                <thead>
                    <tr>
                        <th colSpan="5">
                            <h3>
                                {
                                    props.texts && props.texts['aprovacao.page.opents'] ? 
                                    props.texts['aprovacao.page.opents'] 
                                    : 
                                    'Timesheets open' 
                                }
                                &nbsp;({data.openTimesheets.length})
                            </h3>
                        </th>
                    </tr>
                    <tr>
                        <th width="20%">{props.texts ? props.texts['aprovacao.page.team'] : 'Team'}</th>
                        <th width="30%">{props.texts ? props.texts['aprovacao.page.teammember'] : 'Member'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.hourstimesheet'] : 'Jira Hours'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.hoursponto'] : 'Check-in Hours'}</th>
                        <th width="10%"></th>
                        <th width="20%">{props.texts ? props.texts['aprovacao.page.actions'] : 'Actions'}</th>
                    </tr>
                </thead>
                <tbody>
                    {
                            data.openTimesheets.map((item) => {
                                return (
                                    <tr key={item.username}>
                                        <td>{item.team}</td>
                                        <td>{item.username}</td>
                                        <td>
                                            <a href={`/secure/Tempo.jspa#/my-work/timesheet?columns=WORKED_COLUMN&dateDisplayType=days&from=${period.startDate}&groupBy=issue&periodType=FIXED&to=${period.endDate}&worker=${item.workerKey}`} rel="noreferrer" target="_blank">
                                                {Number(item.horasTempo).toFixed(2)}
                                            </a>
                                        </td>
                                        <td>{((item.horasPonto == '?') ? item.horasPonto : Number(item.horasPonto).toFixed(2))}</td>
                                        <td></td>
                                        <td>
                                            { 
                                                item.canApprove &&
                                                <span className="span-button">
                                                    <Button appearance="primary" spacing="compact" onClick={() => props.onSubmit(item)}>{props.texts ? props.texts['aprovacao.page.submit'] : 'Submit'}</Button>
                                                </span>
                                            }
                                        </td>
                                    </tr>
                                );
                            })

                    }
                </tbody>
            </table>
            )}
            { data.approvedTimesheets && data.approvedTimesheets.length > 0 && (
            <table id="tblApproved" className="pages-tbl">
                <thead>
                    <tr>
                        <th colSpan="6">
                            <h3>
                                {
                                    props.texts && props.texts['aprovacao.page.approvedts'] ? 
                                    props.texts['aprovacao.page.approvedts'] 
                                    : 
                                    'Timesheets approved' 
                                }
                                &nbsp;({data.approvedTimesheets.length})
                            </h3>
                        </th>
                    </tr>
                    <tr>
                        <th width="20%">{props.texts ? props.texts['aprovacao.page.team'] : 'Team'}</th>
                        <th width="30%">{props.texts ? props.texts['aprovacao.page.teammember'] : 'Member'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.hourstimesheet'] : 'Jira Hours'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.hoursponto'] : 'Check-in Hours'}</th>
                        <th width="10%">{props.texts ? props.texts['aprovacao.page.approvedvia'] : 'Approved via'}</th>
                        <th width="20%">{props.texts ? props.texts['aprovacao.page.actions'] : 'Actions'}</th>
                    </tr>
                </thead>
                <tbody>
                    {
                            data.approvedTimesheets.map((item) => {
                                return (
                                    <tr key={item.timeSheetApprovalId}>
                                        <td>{item.team}</td>
                                        <td>{item.username}</td>
                                        <td>
                                            <a href={`/secure/Tempo.jspa#/my-work/timesheet?columns=WORKED_COLUMN&dateDisplayType=days&from=${period.startDate}&groupBy=issue&periodType=FIXED&to=${period.endDate}&worker=${item.workerKey}`} rel="noreferrer" target="_blank">
                                                {Number(item.horasTempo).toFixed(2)}
                                            </a>
                                        </td>
                                        <td>{((item.horasPonto == '?') ? item.horasPonto : Number(item.horasPonto).toFixed(2))}</td>
                                        <td>
                                            {
                                                 (+item.origin === 0 && 
                                                    (props.texts && props.texts['aprovacao.page.approvedviaundefined'] ? 
                                                    props.texts['aprovacao.page.approvedviaundefined'] 
                                                    : 
                                                    'Unknown' )) || 
                                                (+item.origin === 1 && 
                                                    (props.texts && props.texts['aprovacao.page.approvedviaapp'] ? 
                                                    props.texts['aprovacao.page.approvedviaapp'] 
                                                    : 
                                                    'Check-in Integration' )) || 
                                                (+item.origin === 2 && 
                                                    (props.texts && props.texts['aprovacao.page.approvedviatempo'] ? 
                                                    props.texts['aprovacao.page.approvedviatempo'] 
                                                    : 
                                                    'Timesheets' ))
                                            }
                                        </td>
                                        <td>
                                            { 
                                                item.canApprove &&
                                                <span className="span-button">
                                                    <Button appearance="primary" spacing="compact" onClick={() => props.onReopen(item)}>{props.texts ? props.texts['aprovacao.page.reopen'] : 'Reopen'}</Button>
                                                </span>
                                            }
                                        </td>
                                    </tr>
                                );
                            })
 
                    }
                </tbody>
            </table>
            )}
        </div>
    );

}