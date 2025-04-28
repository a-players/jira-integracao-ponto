import React, { useState, useEffect } from 'react';
import Button from '@atlaskit/button';
import './index.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faList } from '@fortawesome/free-solid-svg-icons';
import { faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import Tooltip from '@atlaskit/tooltip';

export default function TblCadastro(props) {
    const [data, setData] = useState([]);

    useEffect(() => {
        setData(props.data);
    }, [props])

    function setCurrentPage(e) {
        props.onPageChange(e.target.innerText);
    }

    function updateEmail(item, e) {
        item.informedEmail = e.target.value;
    }
    
    
    return(
        <div>
        <table id="tblCadastro" className="pages-tbl">
        <thead>
            <tr>
                <th>{props.texts && props.texts['cadastro.page.jirauser'] ? props.texts['cadastro.page.jirauser'] : 'Jira User'}</th>
                <th>{props.texts && props.texts['cadastro.page.emailjira'] ? props.texts['cadastro.page.emailjira'] : 'Jira Email'}</th>
                <th>{props.texts && props.texts['cadastro.page.emailponto'] ? props.texts['cadastro.page.emailponto'] : 'Check-in Email'}</th>
                <th>{props.texts && props.texts['cadastro.page.fromto'] ? props.texts['cadastro.page.fromto'] : 'De / Para'}</th>
                <th>{props.texts && props.texts['cadastro.page.historyheading']? props.texts['cadastro.page.historyheading'] : 'Change history'}</th>
            </tr>
        </thead>
        <tbody>
            {
                (data.list && data.list.length > 0 && (
                    data.list.map((item) => {
                        return (
                            <tr key={item.userKey}>
                                <td>{item.username}</td>
                                <td>{item.email}</td>
                                <td>{item.pontoEmail ? item.pontoEmail : props.texts ? props.texts['cadastro.page.notfound'] : 'Not Found'}</td>
                                <td>
                                    <input type="text" 
                                        onChange={(e) => { updateEmail(item, e)}}
                                        defaultValue={item.informedEmail} />
                                </td>
                                <td>
                                    {
                                        item.history && item.history.length > 0 && (
                                            <Tooltip 
                                                content={props.texts && props.texts['cadastro.page.histtooltip']? props.texts['cadastro.page.histtooltip'] : 'View changes history'}
                                            >
                                                <Button 
                                                    appearance="primary" 
                                                    onClick={() => props.onViewHistory(item)}
                                                >
                                                        <FontAwesomeIcon icon={faList} />
                                                </Button>
                                                </Tooltip>
                                        )
                                    }
                                    {
                                        ((!item.history || item.history.length < 1) && (
                                            <Tooltip 
                                                content={props.texts && props.texts['cadastro.page.nohistory']? props.texts['cadastro.page.nohistory'] : 'No changes for this user'}
                                            >
                                                <FontAwesomeIcon icon={faTimesCircle} />
                                             </Tooltip>
                                            )
                                        )
                                    }
                                </td>
                            </tr>
                        );
                    })
                )) || (
                data.list && data.list.length === 0 && (
                    <tr>
                        <td colSpan="5"><h1>{props.texts ? props.texts['cadastro.page.noshowdata'] : 'No users to list'}</h1></td>
                    </tr>
                ))

            }
        </tbody>
    </table>
    <table className="pages-tbl">
            <tbody>
                <tr>
                    <td className="td-right">
                        {
                            (data.list && data.list.length > 0 && (
                                `${props.texts ? props.texts['cadastro.page.records'] : 'Records'} ${data.list[0].rn} - ${data.list[data.list.length - 1].rn} ${props.texts ? props.texts['cadastro.page.of'] : 'of'} ${data.totalCount}`
                            ))
                        }
                    </td>
                </tr>
                <tr>
                    <td  className="td-left">
                        {
                            ((data.pagesMap && data.pagesMap.length > 1) && (props.texts ? props.texts['cadastro.page.pages'] : 'Pages: ')) ||
                            ((data.pagesMap && data.pagesMap.length === 1) && (props.texts ? props.texts['cadastro.page.page'] : 'Page: '))
                        }
                        {
                            (data.pagesMap && (
                                data.pagesMap.map((page) => {
                                    if (''+ page[0] === '-1') return ('\u22ef');
                                    return( 
                                        (''+ page[0] !== ''+props.currentPage &&
                                        <Button key={page[0]} appearance="subtle" autoFocus="false" onClick={setCurrentPage}>{page[0]}</Button>
                                        ) ||
                                        (''+page[0] === ''+props.currentPage &&
                                            <Button key={page[0]} isDisabled appearance="subtle" autoFocus="false" onClick={setCurrentPage}>{page[0]}</Button>
                                        )
                                    );
                                })
                                
                            )) 
                        }
                    </td>
                </tr>
                <tr>
                    <td className="td-right">
                    <Button appearance="primary" onClick={props.onSave}>{props.texts ? props.texts['cadastro.page.save'] : 'Save'}</Button>
                    </td>
                </tr>
            </tbody>
    </table>
    </div>
    );
}