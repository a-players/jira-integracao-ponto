import React, { useState } from 'react'
import './index.css'
import { Radio } from '@atlaskit/radio';
import Button from '@atlaskit/button';
import { Multiselect } from 'multiselect-react-dropdown';

export default function HdrCadastro(props) {

    const [DPType, setDPType] = useState('1');
    const [rowsPerPage, setRowsPerPage] = useState("30");

    const [suggestions, setSuggestions] = useState([]);
    const [text, setText] = useState('');

    const options = [
        {
            id: 1,
            value: '1',
            label: props.texts ? props.texts['cadastro.page.withoutfromto'] : 'Without From/To'
        },
        {
            id: 2,
            value: '2',
            label: props.texts ? props.texts['cadastro.page.manualfromto'] : 'Manual From/To'
        },
        {
            id: 3,
            value: '3',
            label: props.texts ? props.texts['cadastro.page.all'] : 'All'
        }
    ];

    function onFilterChange(e) {
        setText('');
        setDPType(e.target.value);
        props.onFilterChange(e.target.value);
    }

    function onRowsPerPageChange(e) {
        setText('');
        setRowsPerPage(e.target.value);
        props.onRowsPerPageChange(e.target.value);
    }

    function onTextChange(e) {
        let suggestions = [];
        const value = e.target.value;
        if (value.length > 0) {
          const regex = new RegExp(`^${value}`, `i`);
          suggestions = props.userKeysList.filter(v => regex.test(v.label));
        } else {
            props.onUserSelected(null);
        }
     
        setSuggestions(suggestions);
        setText(value);
      }
      
      
      function suggestionSelected(value) {
          setText(value.label);
          setSuggestions([]);
          props.onUserSelected(value);
      }

      function renderSuggestions() {
        if (suggestions.length === 0) {
          return null;
        }
        return (
            <div className="typeahead">
                <ul>
                    {suggestions.map(user => <li key={user.id} onClick={(e)=>suggestionSelected(user)}>{user.label}</li>)}
                </ul>
            </div>
          
        )
      }

    return (
        <div className="hdrcadastro-container">
            <div className="hdrcadastro-left-elements">
                
                    {
                        options.map((option) => {
                            return(
                                <div key={option.id} className="hdrcadastro-element">
                                <Radio 
                                key={option.id}
                                isChecked={option.value === DPType}
                                onChange={(e) => {onFilterChange(e)}}
                                value={option.value}
                                label={option.label}
                                />
                                </div>
                            );
                        })
                    }
                    
                
                <div className="hdrcadastro-element">
                {props.texts ? props.texts['cadastro.page.rowsperpage'] : 'Rows per Page'}:&nbsp; 
                <select value={rowsPerPage} onChange={onRowsPerPageChange}>
                    <option value="30">30</option>
                    <option value="50">50</option>
                    <option value="100">100</option>
                </select>
                </div>
                <div className="hdrcadastro-element">
                {props.texts && props.texts['cadastro.page.usernamesearch'] ? props.texts['cadastro.page.usernamesearch'] : 'Search user'}:&nbsp;
                    <div className="TypeAheadDropDown">
                        <input onChange={onTextChange} 
                        placeholder={props.texts && props.texts['cadastro.page.searchplaceholder'] ? props.texts['cadastro.page.searchplaceholder'] : 'User'} 
                        value={text} 
                        type="text" />
                        {renderSuggestions()}
                    </div>
                </div>
                <div className="hdrcadastro-element">
                {props.texts && props.texts['cadastro.page.groups'] ? props.texts['cadastro.page.groups'] : 'Groups'}:&nbsp;
                    <Multiselect
                        options={props.groupsList} 
                        onSelect={props.onGroupSelectionChange} 
                        onRemove={props.onGroupSelectionChange}
                        displayValue="label" 
                        showCheckbox={true} 
                        placeholder=""
                    />
                    <Button appearance="primary" onClick={props.doSearch}>{props.texts && props.texts['cadastro.page.filtergroups'] ? props.texts['cadastro.page.filtergroups'] : 'Filter'}</Button>
                </div>
            </div>
            
        </div>
    );

}