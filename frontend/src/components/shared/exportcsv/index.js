import React, { useEffect, useState } from 'react';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';

export default function ExportCSV(props)  {

    const [csvData, setCsvData] = useState([]);
    const [fileName, setFilename] = useState('');

    useEffect(() => {
        setCsvData(props.csvData);
        setFilename(props.fileName);
    }, [props]);

    const fileType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
    const fileExtension = '.xls';

    function exportToCSV () {
        const ws = XLSX.utils.json_to_sheet(csvData);
        const wb = { Sheets: { 'data': ws }, SheetNames: ['data'] };
        const excelBuffer = XLSX.write(wb, { bookType: 'xls', type: 'array' });
        const data = new Blob([excelBuffer], { type: fileType });
        FileSaver.saveAs(data, fileName + fileExtension);
    }

    return(
        <button type="button" onClick={ exportToCSV }>Vamo</button>
    );
}