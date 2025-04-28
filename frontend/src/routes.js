import React from 'react';
import { Switch, Route } from 'react-router-dom';
import AuditPeriod from './pages/auditperiod';
import Aprovacao from './pages/aprovacao'
import Cadastro from './pages/cadastro';
import Settings from './pages/settings';
import Reports from './pages/reports';

export default function Routes() {
    return(
        <Switch>
            <Route path='*/Logs!default.jspa' component={AuditPeriod} />
            <Route path='*/Aprovacao!default.jspa' component={Aprovacao} />
            <Route path='*/Cadastro!default.jspa' component={Cadastro} />
            <Route path='*/Relatorio!default.jspa' component={Reports} />
            <Route path='*/ITSettings!default.jspa' component={Settings} />
        </Switch>
    );
}