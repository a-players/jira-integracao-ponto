import React, { useState, useEffect } from 'react';
import Modal, { ModalTransition } from '@atlaskit/modal-dialog';

export default function ModalHistorico(props) {
    const [isOpen, setOpen] = useState(false);
    const [item, setItem] = useState({});

    const close = () => {
        props.onclose();
        setOpen(false);
    }

    useEffect(() => {
        setOpen(props.open);
        setItem(props.item);
    }, [props]);

    return (
        <ModalTransition>
            {isOpen && (
                <Modal
                    actions={[
                        { 
                            text: props.texts && props.texts['cadastro.page.ok'] ?
                            props.texts['cadastro.page.ok'] : 'OK', 
                            onClick: close 
                        }]}
                    onClose={close}
                    heading={
                        props.texts && props.texts['cadastro.page.historyheading'] ?
                        props.texts['cadastro.page.historyheading'] : 'Changes history'
                    }
                >
                    <div>
                        <table id="tblCadastro" className="pages-tbl">
                            <thead>
                                <tr>
                                    <th>{props.texts && props.texts['cadastro.page.historydate'] ? props.texts['cadastro.page.historydate'] : 'Date and Time'}</th>
                                    <th>{props.texts && props.texts['cadastro.page.historyprevious'] ? props.texts['cadastro.page.historyprevious'] : 'Previous Value'}</th>
                                    <th>{props.texts && props.texts['cadastro.page.historycurrent'] ? props.texts['cadastro.page.historycurrent'] : 'New Value'}</th>
                                    <th>{props.texts && props.texts['cadastro.page.historyactor'] ? props.texts['cadastro.page.historyactor'] : 'User'}</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    item.history.map(hist => {
                                        return(
                                            <tr key={hist.id}>
                                                <td>{hist.updatedDate}</td>
                                                <td>{hist.previousInformedEmail}</td>
                                                <td>{hist.informedEmail}</td>
                                                <td>{hist.actorName}</td>
                                            </tr>
                                        );
                                    })
                                }
                            </tbody>
                        </table>
                    </div>
                </Modal>
            )}
        </ModalTransition>
    );
}