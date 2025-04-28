import React, { useState, useEffect } from 'react';
import Alert from '../../components/shared/alert';
import TblCadastro from '../../components/tblCadastro';
import * as CadastroService from '../../services/cadastro';
import { getContent } from '../../services/i18n';
import { validateEmail, showBlock } from '../../utils';
import HdrCadastro from '../../components/hdrCadastro';
import ModalHistorico from '../../components/mdlHistorico';

export default function Cadastro() {
    const [texts, setTexts] = useState();
    const [dados, setDados] = useState({ list: [] });
    const [currentPage, setCurrentPage] = useState("1");
    const [DPType, setDPType] = useState('1');
    const [rowsPerPage, setRowsPerPage] = useState(30);

    const [isAlertOpen, setAlertOpen] = useState(false);
    const [msgHeader, setMsgHeader] = useState('');
    const [msgText, setMsgText] = useState('');
    const showAlert = () => setAlertOpen(true);
    const hideAlert = () => setAlertOpen(false);

    const [userKeysList, setUserKeysList] = useState([]);
    const [groupsList, setGroupsList] = useState([]);
    const [selectedGroups, setSelectedGroups] = useState([]);


    const [selectedItem, setSelectedItem] = useState({});
    const [modalOpen, setModalOpen] = useState(false);


    useEffect(initialize, []);

    useEffect(() => {
        getLista(selectedGroups);
    }, [currentPage, DPType, rowsPerPage])

    function initialize() {
        getContent('cadastro').then(
            res => {
                setTexts(res.data);
            }
        );

        CadastroService.getAllUserKeysList().then(
            res => {
                setUserKeysList(res.data);
            }
        );

        CadastroService.getAllGroupsList().then(
            res => {
                setGroupsList(res.data);
            }
        )
    }

    function getLista(groups) {
        showBlock(true);
        const found = dados && dados.pagesMap && dados.pagesMap.find(element => element[0] == currentPage);
        const start = found ? found[1] : 1;
        const previous = (dados && dados.deParaTypeOnRequest) ? dados.deParaTypeOnRequest : 1;
        CadastroService.getList(DPType, start, previous, rowsPerPage, groups).then(
                res => {
                    setDados(res.data);
                    showBlock(false);

                })
            .catch(error => {
                setMsgHeader(texts && texts['cadastro.page.error'] ? texts['cadastro.page.error'] : 'Error');
                setMsgText(texts && texts['cadastro.page.genericerror'] ? texts['cadastro.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                showBlock(false);
                showAlert();
            });

    }

    function getListaByKey(key) {
        showBlock(true);
        CadastroService.getListByKey(key).then(
                res => {
                    setDados(res.data);
                    showBlock(false);

                })
            .catch(error => {
                setMsgHeader(texts && texts['cadastro.page.error'] ? texts['cadastro.page.error'] : 'Error');
                setMsgText(texts && texts['cadastro.page.genericerror'] ? texts['cadastro.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');
                showBlock(false);
                showAlert();
            });

    }


    function onPageChange(page) {
        if (page !== currentPage) {
            setCurrentPage(page);
        }
    }

    function onFilterChange(filter) {
        setDPType(filter);
        setCurrentPage(1);
    }

    function onRowsPerPageChange(rowsPerPage) {
        setRowsPerPage(rowsPerPage);
        setCurrentPage(1);
    }

    function save() {
        if (validateEmails()) {
            showBlock(true);
            CadastroService.updateUsers(dados.list).then(
                    () => {
                        setMsgHeader(texts['cadastro.page.success']);
                        setMsgText(texts['cadastro.page.userslistsaved']);
                        showBlock(false);
                        showAlert();
                    })
                .catch(error => {
                    setMsgHeader(texts && texts['cadastro.page.error'] ? texts['cadastro.page.error'] : 'Error');
                    setMsgText(texts && texts['cadastro.page.genericerror'] ? texts['cadastro.page.genericerror'] : 'An error occurred. Please try again later, or contact service support');    
                    showBlock(false);
                    showAlert();
                });
        }
    }

    function validateEmails() {
        for (let i in dados.list) {
            if (dados.list[i].informedEmail && dados.list[i].informedEmail !== '') {
                if (!validateEmail(dados.list[i].informedEmail)) {
                    setMsgHeader(texts && texts['cadastro.page.error'] ? texts['cadastro.page.error'] : 'Error');    
                    setMsgText(`${dados.list[i].informedEmail} ${texts && texts['cadastro.page.invalidemail'] ? texts['cadastro.page.invalidemail'] : 'is not a valid email'}`);
                    showBlock(false);
                    showAlert();
                    return false;
                }
            } else {
                dados.list[i].informedEmail = null;
            }
        }
        return true;
    }

    function onUserSelected(key) {
        if (key) {
            getListaByKey(key.id);
        } else {
            getLista();
        }

    }

    function closeModal() {
        setSelectedItem({});
        setModalOpen(false);
    }

    function onViewHistory(item) {
        setSelectedItem(item);
        setModalOpen(true);
    }

    function onGroupSelectionChange(groups) {
        setSelectedGroups(
            groups.map((group) => {
                return "'" + group.value + "'";
            })
        );
        setCurrentPage(1);
    }

    function doSearch() {
        const selectedGroupNames = selectedGroups && selectedGroups.length > 0 ?
            selectedGroups.join() : null;
        if (selectedGroupNames) {
            getLista(selectedGroupNames);
        } else {
            getLista();
        }
    }

    return ( 
        <div className='pages-container'>
            <Alert open={isAlertOpen} text={msgText} heading={msgHeader} onhide={hideAlert} />
            <ModalHistorico 
                open={modalOpen} 
                onclose={closeModal} 
                item={selectedItem} 
                texts={texts} 
            />
            <div className='pages-title'>
                <h2>{texts && texts['cadastro.page.title']}</h2>
            </div>
               <HdrCadastro 
                onFilterChange={onFilterChange} 
                onRowsPerPageChange={onRowsPerPageChange} 
                texts={texts} 
                userKeysList={userKeysList} 
                onUserSelected={onUserSelected} 
                onGroupSelectionChange = { onGroupSelectionChange }
                groupsList = { groupsList }
                doSearch = { doSearch }
               />
              <TblCadastro data={dados} onPageChange={onPageChange} onSave={save} texts={texts} currentPage={currentPage} onViewHistory={onViewHistory}/>
        </div>
    );
}