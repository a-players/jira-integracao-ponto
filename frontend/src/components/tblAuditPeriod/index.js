import React, { useState, useEffect } from 'react';
import * as utils from '../../utils';

export default function TblApproval(props) {

    const [data, setData] = useState([]);

    useEffect(() => {
        setData(props.data);
    }, [props])


    return (
        <table id="tblAudit" className="pages-tbl">
            <thead>
                <tr>
                    <th>{props.texts ? props.texts['logs.page.period'] : 'Period'}</th>
                    <th>{props.texts ? props.texts['logs.page.previousstate'] : 'Previous Status'}</th>
                    <th>{props.texts ? props.texts['logs.page.newstate'] : 'Actual Status'}</th>
                    <th>{props.texts ? props.texts['logs.page.datetime'] : 'Date and Time of Change'}</th>
                </tr>
            </thead>
            <tbody>
                {
                    (data.length > 0 && (
                        data.map((item) => {
                            return (
                                <tr key={item.id}>
                                    <td>{utils.formatPeriodDate(item.periodStart)} - {utils.formatPeriodDate(item.periodEnd)}</td>
                                    <td>{item.previousState}</td>
                                    <td>{item.currentState}</td>
                                    <td>{item.updatedDate}</td>
                                </tr>
                            );
                        })
                    )) || (
                    data.length === 0 && (
                        <tr>
                            <td colSpan="4"><h1>{props.texts ? props.texts['logs.page.noshowdata'] : 'No data for this interval'}</h1></td>
                        </tr>
                    ))

                }
            </tbody>
        </table>
    );

}