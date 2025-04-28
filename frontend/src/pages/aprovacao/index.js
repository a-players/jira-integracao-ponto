import React, { useState, useEffect } from 'react';
import Alert from '../../components/shared/alert';
import ModalApproval from '../../components/mdlApproval';
import * as ApprovalService from '../../services/approval';
import TblApproval from '../../components/tblApproval';
import { approveTimesheet, rejectTimesheet, reopenTimesheet, submitTimesheet } from '../../services/approval';
import { showBlock, getAllPeriods, formatPeriodDate } from '../../utils';
import { getContent } from '../../services/i18n';
import Button from '@atlaskit/button';
import './index.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleLeft } from '@fortawesome/free-solid-svg-icons'
import { faAngleRight } from '@fortawesome/free-solid-svg-icons'

export default function Aprovacao() {
    const [texts, setTexts] = useState();
    const [list, setList] = useState([]);
    const [periods, setPeriods] = useState([]);
    const [currentPeriod, setCurrentPeriod] = useState();
    const [currentPeriodIndex, setCurrentPeriodIndex] = useState();

    const [isAlertOpen, setAlertOpen] = useState(false);
    const [msgHeader, setMsgHeader] = useState('');
    const [msgText, setMsgText] = useState('');

    const showAlert = () => setAlertOpen(true);
    const hideAlert = () => setAlertOpen(false);

    const [selectedItem, setSelectedItem] = useState({});
    const [modalOpen, setModalOpen] = useState(false);
    const [modalHeading, setModalHeading] = useState();
    const [btnConfirmText, setBtnConfirmText] = useState();
    const [modalAction, setModalAction] = useState(null);

    useEffect(() => {
        initialize();
        loadPeriods()
    }, []);
    useEffect(getLista, [texts, periods, currentPeriod]);

    function initialize() {
        getContent('aprovacao').then(
            res => {
                setTexts(res.data);
            }
        )
        
    }

    function loadPeriods() {
        getAllPeriods().then(
            res => {
                setPeriods(res.data.periods);
            }
        )
    }

    function getLista() {
        
        let curPeriod;
        if(!currentPeriod) {
            curPeriod = findCurrentPeriod();
        } else {
            curPeriod = currentPeriod;
        }
        if(curPeriod) {
            showBlock(true);
            setList([]);
            ApprovalService.getApprovalsList(curPeriod.startDate, curPeriod.endDate).then(
                res => {
                    setList(res.data);
                    showBlock(false);
                })
                .catch(error => {
                    setMsgHeader(texts && texts['aprovacao.page.error'] ? texts['aprovacao.page.error'] : 'Error');
                    setMsgText(texts && texts['aprovacao.page.genericerror'] ? texts['aprovacao.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                    showBlock(false);
                    showAlert();
                });
        }

    }

    function modalCallback(team, id, item) {
        if(modalAction && modalAction === 'reject') {
            doReject(team, id, item);
        } else if(modalAction && modalAction === 'reopen') {
            doReopen(team, id, item);
        }
    }

    function doApprove(team, id) {
        approveTimesheet(team, id)
            .then(() => {
                getLista();
            })
            .catch(error => {
                setMsgHeader(texts && texts['aprovacao.page.error'] ? texts['aprovacao.page.error'] : 'Error');
                setMsgText(texts && texts['aprovacao.page.genericerror'] ? texts['aprovacao.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                showAlert();
            });
    }

    function doSubmit(item) {
        submitTimesheet(item)
            .then(() => {
                getLista();
            })
            .catch(error => {
                setMsgHeader(texts && texts['aprovacao.page.error'] ? texts['aprovacao.page.error'] : 'Error');
                setMsgText(texts && texts['aprovacao.page.genericerror'] ? texts['aprovacao.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                showAlert();
            });
    }

    function openRejectModal(item) {
        setSelectedItem(item);
        setModalAction('reject');
        setModalHeading(
            texts && texts['aprovacao.page.rejectheader'] ? 
            texts['aprovacao.page.rejectheader']
            :
            'Reject Timesheet'
        );
        setBtnConfirmText(
            texts && texts['aprovacao.page.reject'] ? 
            texts['aprovacao.page.reject']
            :
            'Reject'
        );
        setModalOpen(true);
    }

    function validateReject(reason) {
        if (!reason || reason.length < 1) {
            setMsgHeader(texts && texts['aprovacao.page.error'] ? texts['aprovacao.page.error'] : 'Error');
            setMsgText(texts && texts['aprovacao.page.nocomment'] ? texts['aprovacao.page.nocomment'] : 'You must inform a comment to reject a timesheet');
            showAlert();
            return false;
        }
        return true;
    }

    function openReopenModal(item) {
        setSelectedItem(item);
        setModalAction('reopen');
        setModalHeading(
            texts && texts['aprovacao.page.reopenheader'] ? 
            texts['aprovacao.page.reopenheader']
            :
            'Reopen Timesheet'
        );
        setBtnConfirmText(
            texts && texts['aprovacao.page.reopen'] ? 
            texts['aprovacao.page.reopen']
            :
            'Reopen'
        );
        setModalOpen(true);
    }

    function closeModal() {
        setSelectedItem({});
        setModalOpen(false);
    }

    function doReject(team, id, reason) {
        if (validateReject(reason)) {
            rejectTimesheet(team, id, reason)
                .then(() => {
                    getLista();
                })
                .catch(error => {
                setMsgHeader(texts && texts['aprovacao.page.error'] ? texts['aprovacao.page.error'] : 'Error');
                setMsgText(texts && texts['aprovacao.page.genericerror'] ? texts['aprovacao.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                showAlert();
                });
            closeModal();
        }
    }

    function doReopen(team, id, reason) {
            reopenTimesheet(team, id, reason)
                .then(() => {
                    getLista();
                })
                .catch(error => {
                setMsgHeader(texts && texts['aprovacao.page.error'] ? texts['aprovacao.page.error'] : 'Error');
                setMsgText(texts && texts['aprovacao.page.genericerror'] ? texts['aprovacao.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                showAlert();
                });
            closeModal();
    }

    function findCurrentPeriod() {
        const curDate = new Date().toISOString().split('T')[0];
        if(periods) {
            for(let i in periods) {
                if(periods[i].startDate <= curDate && periods[i].endDate >= curDate) {
                    setCurrentPeriod(periods[i]);
                    setCurrentPeriodIndex(i);
                    return periods[i];
                }
            }
        }
    }

    function previousPeriod() {
        setCurrentPeriod(periods[currentPeriodIndex - 1]);
        setCurrentPeriodIndex(currentPeriodIndex - 1);
    }

    function nextPeriod() {
        setCurrentPeriod(periods[+currentPeriodIndex + 1]);
        setCurrentPeriodIndex(+currentPeriodIndex + 1);
    }

    function refresh() {
        getLista();
    }

    return (
        <div className='pages-container'>

            <Alert open={isAlertOpen} text={msgText} heading={msgHeader} onhide={hideAlert} />
            <ModalApproval 
                open={modalOpen} 
                heading={modalHeading} 
                onclose={closeModal} 
                item={selectedItem} 
                callback={modalCallback}  
                btnConfirm={btnConfirmText}
                texts={texts} 
            />
            <div className='pages-title'>
                <h2>{texts && texts['aprovacao.page.title']}</h2>
            </div>
            
            {
                currentPeriod && (
                <div className='controls-container'>
                    {
                        texts && texts['aprovacao.page.period'] ? 
                        texts['aprovacao.page.period'] 
                        : 
                        'Period' 
                    }
                    {
                        currentPeriodIndex > 0 &&
                        <Button appearance="subtle" onClick={previousPeriod}>
                            <FontAwesomeIcon icon={faAngleLeft} />
                        </Button> 
                    }
                    <div>
                     {formatPeriodDate(currentPeriod.startDate)} - {formatPeriodDate(currentPeriod.endDate)}
                     </div>
                    {
                        currentPeriodIndex < +periods.length - 1 && 
                        <Button appearance="subtle" onClick={nextPeriod}>
                            <FontAwesomeIcon icon={faAngleRight} />
                        </Button> 
                    }
                     <Button appearance="primary" spacing="compact" onClick={refresh}>
                     {texts && texts['aprovacao.page.refresh'] ? 
                        texts['aprovacao.page.refresh'] 
                        : 
                        'Refresh' }
                     </Button>
                </div>
                
                )
            }
            
             <TblApproval data={list} onApprove={doApprove} onReject={openRejectModal} onReopen={openReopenModal} onSubmit={doSubmit} period={currentPeriod} texts={texts} />
        </div>
    );

}