import React, { useState, useEffect } from 'react';
import { getContent } from '../../services/i18n';
import TblSubmittedTimesheetReport from '../../components/tblSubmittedTimesheetReport';

export default function SubmittedTimesheetReport(props) {
    const [texts, setTexts] = useState();

    useEffect(initialize, []);

    function initialize() {
        getContent('relatorio.timesheets').then(
            res => {
                setTexts(res.data);
            }
        )
    }

    return(
        <div className='fullwidth'>
            <TblSubmittedTimesheetReport data={props.list} texts={texts} />
        </div>
    );
}