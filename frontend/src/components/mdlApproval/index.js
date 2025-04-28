import React, { useState, useEffect } from 'react';
import Modal, { ModalTransition } from '@atlaskit/modal-dialog';
import TextArea from '@atlaskit/textarea';

import './index.css';

export default function ModalApproval(props) {
    const [isOpen, setOpen] = useState(false);
    const [item, setItem] = useState({});
    const [reason, setReason] = useState('');

    const close = () => {
        props.onclose();
        setOpen(false);
    }

    useEffect(() => {
        setOpen(props.open);
        setItem(props.item);
        setReason('');
    }, [props]);

    function reasonChange(e) {
        setReason(e.target.value);
    }

    function confirm() {
        props.callback(item.teamId, item.timeSheetApprovalId, reason);
    }

    return (
        <ModalTransition>
            {isOpen && (
                <Modal
                    actions={[
                        {  
                            appearance : 'danger', 
                            text: props.btnConfirm, 
                            onClick: confirm 
                        }, 
                        { 
                            text: props.texts && props.texts['aprovacao.page.cancel'] ?
                            props.texts['aprovacao.page.cancel'] : 'Cancel', 
                            onClick: close 
                        }]}
                    onClose={close}
                    heading={props.heading ? props.heading : 'Do something'}
                >
                    <div className="reject-container">
                        <div className="reject-header">
                            <div className="reject-header-field">
                                <span className="reject-header-field-title">{props.texts && props.texts['aprovacao.page.team'] ? props.texts['aprovacao.page.team'] : 'Team'}</span>
                                <span className="reject-header-field-value">{item.team}</span>
                            </div>
                            <div className="reject-header-field">
                                <span className="reject-header-field-title">{props.texts && props.texts['aprovacao.page.teammember'] ? props.texts['aprovacao.page.teammember'] : 'Member'}</span>
                                <span className="reject-header-field-value">{item.username}</span>
                            </div>
                            <div className="reject-header-field">
                                <span className="reject-header-field-title">{props.texts && props.texts['aprovacao.page.hourstimesheet'] ? props.texts['aprovacao.page.hourstimesheet'] : 'Jira Hours'}</span>
                                <span className="reject-header-field-value">{item.horasTempo}</span>
                            </div>
                            <div className="reject-header-field">
                                <span className="reject-header-field-title">{props.texts && props.texts['aprovacao.page.hoursponto'] ? props.texts['aprovacao.page.hoursponto'] : 'Check-in Hours'}</span>
                                <span className="reject-header-field-value">{Number(item.horasPonto).toFixed(2)}</span>
                            </div>
                        </div>
                        <div className="reject-header-field">
                        <span className="reject-body-field-title">{props.texts && props.texts['aprovacao.page.comment'] ? props.texts['aprovacao.page.comment'] : 'Comment'}</span>
                       <TextArea name="taReject" 
                            value={reason} 
                            placeholder={props.texts && props.texts['aprovacao.page.placeholder'] ? props.texts['aprovacao.page.placeholder'] : 'Write a comment'}
                            onChange={reasonChange} 
                            maxLength="300"
                       />
                       </div>
                    </div>
                </Modal>
            )}
        </ModalTransition>
    );
}