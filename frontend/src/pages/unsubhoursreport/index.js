import React, { useState, useEffect } from 'react';
import { getContent } from '../../services/i18n';
import TblUnsubHoursReport from '../../components/tblUnsubHoursReport';

export default function UnsubmittedHoursReport(props) {
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
            <TblUnsubHoursReport data={props.list} texts={texts} />
        </div>
    );
}