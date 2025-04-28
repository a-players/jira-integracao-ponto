import React, { useState, useEffect } from 'react';
import * as utils from '../../utils';
import Button from '@atlaskit/button';
import ArrowDownIcon from '@atlaskit/icon/glyph/arrow-down'
import ArrowUpIcon from '@atlaskit/icon/glyph/arrow-up'
import ChevronDownIcon from '@atlaskit/icon/glyph/chevron-down';
import { PopupSelect, CheckboxOption } from '@atlaskit/select';

export default function TblUnsubHoursReport(props) {

    const headers = [
      { id: 0, dataType: "Date", name: props.texts && props.texts['relatorio.hours.page.period'] ? props.texts['relatorio.hours.page.period'] : 'Period', value: 'period' },
      { id: 1, dataType: "String", name: props.texts && props.texts['relatorio.hours.page.user'] ? props.texts['relatorio.hours.page.user'] : 'Employee', value: 'user' },
      { id: 2, dataType: "String", name: props.texts && props.texts['relatorio.hours.page.team'] ? props.texts['relatorio.hours.page.team'] : 'Team', value: 'team' },
      { id: 3, dataType: "String", name: props.texts && props.texts['relatorio.hours.page.approver'] ? props.texts['relatorio.hours.page.approver'] : 'Leader', value: 'approver' },
      { id: 4, dataType: "Float", name: props.texts && props.texts['relatorio.hours.page.totalhours'] ? props.texts['relatorio.hours.page.totalhours'] : 'Total Hours', value: 'totalhours' },
      { id: 5, dataType: "Date", name: props.texts && props.texts['relatorio.hours.page.hoursponto'] ? props.texts['relatorio.hours.page.hoursponto'] : 'Check-in Hours', value: 'hoursponto' },
    ]

    const [hours, setHours] = useState([]);
    const [selectedColumns, setSelectedColumns] = useState(headers)
    const [sortColumn, setSortColumn] = useState(null);
    const [sortDirection, setSortDirection] = useState('asc');

    useEffect(() => {
        setHours(props.data.unsubmittedHoursDTOList);
    }, [props])

    const [selectedOptionsData, setSelectedOptionsData] = useState(
      headers.map(header => ({ label: header.name, value: header.value }))
    );

    function hasToShow(columnIdx) {
      return selectedColumns.some(({ id }) => id === columnIdx);
    }

    function handleColumnSelection(selectedOptions) {
      const selectedColumns = headers.filter(header =>
        selectedOptions.some(option => option.value === header.value)
      );
      setSelectedColumns(selectedColumns);
      setSelectedOptionsData(selectedOptions);
    }

    function getColumnIndexByName(columnName) {
      const column = headers.find(header => header.name === columnName);
      return column ? column.id : -1;
    }

    return (
        <div>
            <div className="multiselect-container">
                <PopupSelect
                    components={{ Option: CheckboxOption }}
                    options={headers.map(header => ({ label: header.name, value: header.value }))}
                    closeMenuOnSelect={false}
                    hideSelectedOptions={false}
                    isMulti
                    aria-label={props.texts && props.texts['relatorio.page.filtercolumns']
                        ? props.texts['relatorio.page.filtercolumns']
                        : 'Filter columns'}
                    placeholder={props.texts && props.texts['relatorio.page.filtercolumnsplaceholder']
                        ? props.texts['relatorio.page.filtercolumnsplaceholder']
                        : 'Filter columns...'}
                    target={({ isOpen, ...triggerProps }) => (
                      <Button
                        {...triggerProps}
                        isSelected={isOpen}
                        iconAfter={<ChevronDownIcon label="" />}
                      >
                        {props.texts && props.texts['relatorio.page.columns']
                        ? props.texts['relatorio.page.columns']
                        : 'Columns'}
                      </Button>
                    )}
                    value={selectedOptionsData}
                    onChange={handleColumnSelection}
                />
            </div>
            <table id="tblUnsubHoursReport" className="pages-tbl">
                <thead>
                 <h2>{props.texts && props.texts['relatorio.hours.page.unsubmittedhours'] ? props.texts['relatorio.hours.page.unsubmittedhours'] : 'Unsubmitted Timesheets'}</h2>
                    <tr>
                      {headers.map((item) => {    
                        return hasToShow(item.id) && (
                          <th key={item.id} className={`sortable ${sortColumn === item.id ? 'sorted' : ''}
                              ${sortColumn === item.id && sortDirection === 'asc' ? 'asc' : 'desc'}`}
                              onClick={() => sortTableUnsub(item.id, item.dataType)}
                          >
                            {item.name}
                            {sortColumn === item.id && (
                              <>
                                {sortDirection === 'asc' ? <ArrowUpIcon className="asc-icon" /> : <ArrowDownIcon className="desc-icon" />}
                              </>
                            )}
                          </th>
                        );
                      })}
                    </tr>
                </thead>
                <tbody>
                    {
                        hours && hours.length > 0 ? (
                          hours.map((item) => (
                            <tr key={item.periodStartDate + item.usuario}>
                              {hasToShow(0) && <td>{utils.formatPeriodDate(new Date(item.periodStartDateISO + ' 00:00:00'))} - {utils.formatPeriodDate(new Date(item.periodEndDateISO + ' 00:00:00'))}</td>}
                              {hasToShow(1) && <td>{item.usuario}</td>}
                              {hasToShow(2) && <td>{item.team}</td>}
                              {hasToShow(3) && <td>{item.approver}</td>}
                              {hasToShow(4) && <td>{Number(item.totalHours).toFixed(2)}</td>}
                              {hasToShow(5) && <td>{item.horasPonto === '?' ? item.horasPonto : Number(item.horasPonto).toFixed(2)}</td>}
                            </tr>
                          ))
                        ) : (
                          <tr>
                            <td colSpan={`${selectedColumns.length || 0}`}>
                              <h1>{props.texts ? props.texts['relatorio.hours.page.noshowdata'] : 'No data for this interval'}</h1>
                            </td>
                          </tr>
                        )

                    }
                </tbody>
            </table>
        </div>
    );

    function sortTableUnsub(n, dataType) {
      utils.showBlock(true);
      console.log("Ordenando: " + n);
      console.log("DataType: " +dataType);
      console.log("Carregando");
      var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
      table = document.getElementById("tblUnsubHoursReport");
      switching = true;
      dir = "asc";

      if (sortColumn === n) {
        setSortDirection((prevDirection) => (prevDirection === 'asc' ? 'desc' : 'asc'));
      } else {
        setSortColumn(n);
        setSortDirection('asc');
      }

      while (switching) {
          switching = false;
          rows = table.rows;
          for (i = 1; i < (rows.length - 1); i++) {
              shouldSwitch = false;
              x = rows[i].getElementsByTagName("TD")[n];
              y = rows[i + 1].getElementsByTagName("TD")[n];
  
              var cmpX = getValueToCompare(x.innerHTML, dataType);
              var cmpY = getValueToCompare(y.innerHTML, dataType);
  
              if (dir == "asc") {
                  if (cmpX > cmpY) {
                      shouldSwitch = true;
                      break;
                  }
              } else if (dir == "desc") {
                  if (cmpX < cmpY) {
                      shouldSwitch = true;
                      break;
                  }
              }
          }
  
          if (shouldSwitch) {
              rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
              switching = true;
              switchcount++;
          } else {
              if (switchcount == 0 && dir == "asc") {
                  dir = "desc";
                  switching = true;
              }
          }
      }
      console.log("Carregamento Finalizado");
      utils.showBlock(false);
  }

  function getValueToCompare(value, dataType) {
    if (dataType === "String") {
        return value.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
    } else if (dataType === "Float") {
        return isNaN(parseFloat(value)) ? 0 : parseFloat(value);
    } else if (dataType === "Date") {
        if (value.includes(" - ")) {
            const dates = value.split(" - ");
            const startDate = parseDate(dates[0]);
            const endDate = parseDate(dates[1]);
            return startDate;
        } else {
            const dateParts = value.split(' ');
            const date = parseDate(dateParts[0]);
            if (dateParts.length > 1) {
                const time = parseTime(dateParts[1]);
                date.setHours(time.hours, time.minutes, time.seconds);
            }
            return date;
        }
    }
    return value.toLowerCase();
}

function parseDate(dateString) {
  const months = {
      'jan': 0, 'fev': 1, 'mar': 2, 'abr': 3, 'mai': 4, 'jun': 5,
      'jul': 6, 'ago': 7, 'set': 8, 'out': 9, 'nov': 10, 'dez': 11
  };

  const dateTimeParts = dateString.split(' ');
  const datePart = dateTimeParts[0];
  const timePart = dateTimeParts[1];

  const dateParts = datePart.split('/');
  if (dateParts.length === 3) {
      const day = parseInt(dateParts[0], 10);
      let month = null;
      if (isNaN(parseInt(dateParts[1], 10))) {
          
          month = months[dateParts[1].toLowerCase()];
      } else {
          
          month = parseInt(dateParts[1], 10) - 1; 
      }
      const year = parseInt(dateParts[2], 10);

      if (month !== null) {
          const date = new Date(year, month, day);

          if (timePart) {
              const timeParts = timePart.split(':');
              const hours = parseInt(timeParts[0], 10);
              const minutes = parseInt(timeParts[1], 10);
              const seconds = parseInt(timeParts[2], 10) || 0; 
              date.setHours(hours, minutes, seconds);
          }

          return date;
      }
  }
  return null;
}

function parseTime(timeString) {
    const timeParts = timeString.split(':');
    const hours = parseInt(timeParts[0], 10);
    const minutes = parseInt(timeParts[1], 10);
    const seconds = parseInt(timeParts[2], 10) || 0; 
    return { hours, minutes, seconds };
}
/*
    function sortTableUnsub(n) {
      console.log("Ordenando: " +n);
      debugger;
        var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
        table = document.getElementById("tblUnsubHoursReport");
        switching = true;
        dir = "asc";

        while (switching) {
          switching = false;
          rows = table.rows;

          for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
                    var cmpX=isNaN(parseInt(x.innerHTML))?x.innerHTML.toLowerCase():parseInt(x.innerHTML);
                    var cmpY=isNaN(parseInt(y.innerHTML))?y.innerHTML.toLowerCase():parseInt(y.innerHTML);
                    cmpX=(cmpX=='-')?0:cmpX;
                    cmpY=(cmpY=='-')?0:cmpY;
            if (dir == "asc") {
                if (cmpX > cmpY) {
                    shouldSwitch= true;
                    break;
                }
            } else if (dir == "desc") {
                if (cmpX < cmpY) {
                    shouldSwitch= true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount ++;      
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
    }
    */
}