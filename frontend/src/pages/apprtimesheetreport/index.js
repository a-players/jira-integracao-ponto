import React, { useState, useEffect } from 'react';
import { getContent } from '../../services/i18n';
import TblApprovedTimesheetReport from '../../components/tblApprovedTimesheetReport';

export default function ApprovedTimesheetReport(props) {
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
            <TblApprovedTimesheetReport data={props.list} texts={texts}/>
        </div>
    );
}