import React, { useState, useEffect } from 'react';
import { getContent } from '../../services/i18n';
import TblAccountHoursReport from '../../components/tblAccountHoursReport';

export default function AccountHoursReport(props) {
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
            <TblAccountHoursReport data={props.list} texts={texts} />
        </div>
    );
}