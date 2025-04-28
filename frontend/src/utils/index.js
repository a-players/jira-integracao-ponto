import * as XLSX from 'xlsx';
import { jsPDF } from 'jspdf';
import 'jspdf-autotable';
import * as moment from 'moment'
//import 'moment/min/moment-with-locales'
import axios from 'axios';

moment.locale('pt-br');

export function getBaseURL() {
  const location = getLocation();
  let pathname = '';
  if (location.pathname && location.pathname.indexOf('/secure') > 0) {
    pathname = location.pathname.substring(0, location.pathname.indexOf('/secure'));
  }
  return `${location.protocol}//${location.hostname}${(location.port ? `:${location.port}` : '')}${pathname}`;
}

export function getRestURL(path) {
  if (!path) {
    path = '/';
  }
  const baseUrl = getBaseURL();
  return `${baseUrl}/rest/integracaoponto/1.0${path}`;
}
export function getLocation() {
  return window.location;
}

export function exportToCSV(csvData, fileName, sheetName, headers) {
  const fileExtension = '.xls';
  const wb = XLSX.utils.book_new();
  wb.SheetNames.push(sheetName);
  const ws = XLSX.utils.aoa_to_sheet(headers);
  ws['!cols'] = [{ wch: 20 }, { wch: 6 }, { wch: 20 }, { wch: 20 }];
  XLSX.utils.sheet_add_json(ws, transformLogsCsvDataIntoXLSFJson(csvData), { origin: 'A2', skipHeader: true });
  wb.Sheets[sheetName] = ws;
  XLSX.writeFile(wb, fileName + fileExtension, { bookType: 'xlsx', type: 'buffer' });
}

function transformLogsCsvDataIntoXLSFJson(rawCsvData) {
  return rawCsvData.map((item) => {
    return (
      {
        'Date and Time': item.updatedDate,
        'Period': item.periodKey,
        'Previous State': item.previousState,
        'New State': item.currentState
      }
    );
  });
}

export function exportToPdf(fileName, title, tableId, orientation) {
  if(!orientation) {
    orientation = 'p';
  }
  const doc = new jsPDF({'orientation': orientation});
  doc.text(15, 10, title);
  for(let i in tableId) {
    doc.autoTable({ html: '#' + tableId[i] });
  }
  
  doc.save(fileName + '.pdf');
}

export function formatISODate(isoDate, separator) {
  const format = `DD${separator}MM${separator}yyyy`;
  return moment(isoDate, 'yyyy-MM-DD').format(format);
}

export function formatPeriodDate(date) {
  return moment(date).format('DD/MMM/YYYY');
}

export function validateEmail(email) {
  const mailformat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  if (email && email.match(mailformat)) {
    return true;
  }
  return false;
}

export function showBlock(show) {
  if(show) {
    document.getElementById("integracaoponto-block").className = "block-visible";
  } else {
    document.getElementById("integracaoponto-block").className = "block-hidden";
  }
  window.dispatchEvent(new Event('resize'));
}

export function getAllPeriods(dateFrom, dateTo) {
  if(!dateFrom) {
    dateFrom = new Date('2017-12-01');
  }
  if(!dateTo) {
    dateTo = new Date('2023-12-01');
  }
  const url = getBaseURL() + '/rest/tempo-timesheets/5/period/approval';
  const params = `dateFrom=${dateFrom.toISOString().split('T')[0]}&dateTo=${dateTo.toISOString().split('T')[0]}`;
  return axios.get(url + '?' + params);
}

export function getFirstDateOfMonth(dt) {
  return new Date(dt.getFullYear(), dt.getMonth(), 1);
}

export function getLastDateOfMonth(dt) {
  return new Date(dt.getFullYear(), dt.getMonth()+1, 0);
}
