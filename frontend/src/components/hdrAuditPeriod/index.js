import React, { useState } from 'react'
import Button from '@atlaskit/button';
import { DatePicker } from '@atlaskit/datetime-picker';
import { gridSize } from '@atlaskit/theme/constants';
import { PopupSelect } from '@atlaskit/select';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleDown } from '@fortawesome/free-solid-svg-icons'

import './index.css'
import Alert from '../shared/alert';
export default function HdrAuditPeriod(props) {

    const [isAlertOpen, setAlertOpen] = useState(false);
    const [startDate, setStartDate] = useState(new Date().toISOString().split('T')[0]);
    const [endDate, setEndDate] = useState(new Date().toISOString().split('T')[0]);
    const showAlert = () => setAlertOpen(true);
    const hideAlert = () => setAlertOpen(false);


    const options = [
        { label: 'PDF', value: 'pdf' },
        { label: 'XLS', value: 'xls' },
    ];

    const onChange = props.handleExport;
    const defaults = {
        options,
        placeholder: '',
        onChange,
    };


    function searchStart() {
        if (validateSearch()) {
            props.handleSearchStart(startDate, endDate);
        }

    }

    function validateSearch() {
        if (startDate > endDate) {
            showAlert();
            return false;
        }
        return true;
    }



    return (
        <div className="hdraudit-container">
            <Alert open={isAlertOpen} text={props.texts ? props.texts['logs.page.wronginterval'] : 'Start Date cannot be higher than End Date'} heading={props.text ? props.texts['logs.page.error'] : 'Error'} onhide={hideAlert} />
            <div className="hdraudit-left-elements">
                <div className="hdraudit-element">
                    <label htmlFor="stDate">{props.texts ? props.texts['logs.page.startdate'] : 'Start Date'}:</label>
                    <DatePicker
                        id="stDate"
                        dateFormat="DD/MM/YYYY"
                        value={startDate}
                        onChange={setStartDate}
                        innerProps={{ style: { width: gridSize() * 20 } }} />
                </div>
                <div className="hdraudit-element">
                    <label htmlFor="edDate">{props.texts ? props.texts['logs.page.enddate'] : 'End Date'}:</label>
                    <DatePicker
                        id="edDate"
                        dateFormat="DD/MM/YYYY"
                        value={endDate}
                        onChange={setEndDate}
                        innerProps={{ style: { width: gridSize() * 20 } }} />
                </div>
                <div className="hdraudit-element">
                    <Button appearance="primary" onClick={searchStart}>{props.texts ? props.texts['logs.page.search'] : 'Search'}</Button>
                </div>
            </div>
            <div className="hdraudit-right-elements">
                <PopupSelect
                    {...defaults}
    target={({ ref }) => <Button appearance="primary" ref={ref}>{props.texts ? props.texts['logs.page.export'] : 'Export'}&nbsp;&nbsp;&nbsp;&nbsp;
    <FontAwesomeIcon icon={faAngleDown} /></Button>}
                    popperProps={{ placement: 'bottom', strategy: 'fixed' }}
                    searchThreshold={10}
                />
            </div>
        </div>
    );

}