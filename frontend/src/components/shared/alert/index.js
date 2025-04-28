import React, { useState, useEffect } from 'react';
import Modal, { ModalTransition } from '@atlaskit/modal-dialog';

export default function Alert(props) {
    const [isAlertOpen, setAlertOpen] = useState(false);
    const [alertHeading, setAlertHeading] = useState('');
    const [alertText, setAlertText] = useState('');
    const closeAlert = () => {
        props.onhide();
        setAlertOpen(false);
    }
    
    useEffect(() => {
        setAlertHeading(props.heading);
        setAlertText(props.text);
        setAlertOpen(props.open);
    }, [props])

    return(
        <ModalTransition>
                {isAlertOpen && (
                    <Modal
                        actions={[{ text: 'OK', onClick: closeAlert }]}
                        onClose={closeAlert}
                        heading={alertHeading}
                    >
                        {alertText}
                    </Modal>
                )}
            </ModalTransition>
    );
}