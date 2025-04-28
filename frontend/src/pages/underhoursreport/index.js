import React, { useState, useEffect } from 'react';
import { getContent } from '../../services/i18n';
import TblUnderHoursReport from '../../components/tblUnderHoursReport';

export default function UnderSubmittedHoursReport(props) {
    const [texts, setTexts] = useState();

    useEffect(initialize, []);

    function initialize() {
        getContent('relatorio.hours').then(
            res => {
                setTexts(res.data);
            }
        )
    }

    return(
        <div className='fullwidth'>
            <TblUnderHoursReport data={props.list} texts={texts} />
        </div>
    );
}