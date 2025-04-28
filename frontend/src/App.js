
import React from 'react';
import Routes from './routes'
import { BrowserRouter } from 'react-router-dom';
import './App.css';

function App() {
  return (
    
    <BrowserRouter>
    <div id="integracaoponto-block" className="block-hidden">
      <div className="loader"></div>
    </div>
      <Routes />
    </BrowserRouter>
    
  );
}

export default App;
