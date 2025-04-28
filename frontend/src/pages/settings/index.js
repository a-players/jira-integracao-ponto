import React, { useState, useEffect } from 'react';
import Alert from '../../components/shared/alert';
import Button from '@atlaskit/button';
import * as ConfigService from '../../services/config';
import { showBlock } from '../../utils';
import { getContent } from '../../services/i18n';
import './index.css';

export default function Settings() {
    const [texts, setTexts] = useState();
    const [config, setConfig] = useState({});

    const [isAlertOpen, setAlertOpen] = useState(false);
    const [msgHeader, setMsgHeader] = useState('');
    const [msgText, setMsgText] = useState('');
    const showAlert = () => setAlertOpen(true);
    const hideAlert = () => setAlertOpen(false);

    useEffect(initialize, []);

    function initialize() {
        getContent('settings').then(
            res => {
                setTexts(res.data);
            }
        )
        getConfig();
    }

    function getConfig() {
        showBlock(true);
        ConfigService.getConfig().then(
            res => {
                setConfig(res.data);
                showBlock(false);
            })
            .catch(error => {
                setMsgHeader(texts && texts['settings.page.error'] ? texts['settings.page.error'] : 'Error');
                setMsgText(texts && texts['settings.page.genericerror'] ? texts['settings.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                showBlock(false);
                showAlert();
            });

    }

    function onSave() {
        showBlock(true);
        ConfigService.updateConfig(config).then(
            res => {
                setMsgHeader(texts && texts['settings.page.success'] ? texts['settings.page.success'] : 'Success!');
                setMsgText(texts && texts['settings.page.settingssaved'] ? texts['settings.page.settingssaved'] : 'Settings successfully saved');
                showBlock(false);
                showAlert();

            })
            .catch(error => {
                setMsgHeader(texts && texts['settings.page.error'] ? texts['settings.page.error'] : 'Error');
                setMsgText(texts && texts['settings.page.genericerror'] ? texts['settings.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                showBlock(false);
                showAlert();
            });

    }

    function updateUrlLogin(e) {
        config.urlLogin = e.target.value;
    }

    function updateApiKey(e) {
        config.apiKey = e.target.value;
    }

    function updateUrlPonto(e) {
        config.urlPonto = e.target.value;
    }    

    return (
        <div className='pages-container'>
            <Alert open={isAlertOpen} text={msgText} heading={msgHeader} onhide={hideAlert} />
            <div className='pages-title'>
                <h2>{texts && texts['settings.page.title']}</h2>
            </div>
            <table className="pages-tbl">
                <tbody>
                    <tr>
                        <td className="td-settings-left">
                        {texts && texts['settings.page.loginurl']}:
                            <input id="txtUrlLogin" type="text"
                                onChange={(e) => { updateUrlLogin(e) }}
                                defaultValue={config.urlLogin}
                                size="80"
                                maxLength="255" />
                        </td>
                    </tr>
                    <tr>
                        <td className="td-settings-left">
                        {texts && texts['settings.page.apikey']}:
                            <input id="txtApiKey" type="text"
                                onChange={(e) => { updateApiKey(e) }}
                                defaultValue={config.apiKey}
                                size="80"
                                maxLength="255" />
                        </td>
                    </tr>
                    <tr>
                        <td className="td-settings-left">
                        {texts && texts['settings.page.checkinurl']}:
                            <input id="txtUrl" type="text"
                                onChange={(e) => { updateUrlPonto(e) }}
                                defaultValue={config.urlPonto}
                                size="80"
                                maxLength="255" />
                        </td>
                    </tr>
                    <tr>
                        <td className="td-settings-right">
                            <Button appearance="primary" onClick={onSave}>{texts && texts['settings.page.save']}</Button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
}