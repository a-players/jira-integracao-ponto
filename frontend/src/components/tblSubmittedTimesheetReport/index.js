import React, { useState, useEffect } from 'react';
import * as utils from '../../utils';
import Button from '@atlaskit/button';
import ChevronDownIcon from '@atlaskit/icon/glyph/chevron-down';
import ArrowDownIcon from '@atlaskit/icon/glyph/arrow-down'
import ArrowUpIcon from '@atlaskit/icon/glyph/arrow-up'
import { PopupSelect, CheckboxOption } from '@atlaskit/select';

export default function TblSubmittedTimesheetReport(props) {

    const headers = [
      { id: 0, dataType: "Date", name: props.texts && props.texts['relatorio.timesheets.page.period'] ? props.texts['relatorio.timesheets.page.period'] : 'Period', value: 'period' },
      { id: 1, dataType: "String", name: props.texts && props.texts['relatorio.timesheets.page.user'] ? props.texts['relatorio.timesheets.page.user'] : 'Employee', value: 'employee' },
      { id: 2, dataType: "String", name: props.texts && props.texts['relatorio.timesheets.page.team'] ? props.texts['relatorio.timesheets.page.team'] : 'Team', value: 'team' },
      { id: 3, dataType: "String", name: props.texts && props.texts['relatorio.timesheets.page.approver'] ? props.texts['relatorio.timesheets.page.approver'] : 'Leader', value: 'approver' },
      { id: 4, dataType: "Float", name: props.texts && props.texts['relatorio.timesheets.page.hourstempo'] ? props.texts['relatorio.timesheets.page.hourstempo'] : 'Jira Hours', value: 'hourstempo' },
      { id: 5, dataType: "Float", name: props.texts && props.texts['relatorio.timesheets.page.hoursponto'] ? props.texts['relatorio.timesheets.page.hoursponto'] : 'Check-in Hours', value: 'hoursponto' },
      { id: 6, dataType: "String", name: props.texts && props.texts['relatorio.timesheets.page.reason'] ? props.texts['relatorio.timesheets.page.reason'] : 'Comment', value: 'reason' },
      { id: 7, dataType: "Date", name: props.texts && props.texts['relatorio.timesheets.page.submitteddatetime'] ? props.texts['relatorio.timesheets.page.submitteddatetime'] : 'Submitted Date and Time', value: 'approvaldatetime' },
    ]

    const [submittedList, setSubmittedList] = useState([]);
    const [selectedColumns, setSelectedColumns] = useState(headers)
    const [sortColumn, setSortColumn] = useState(null);
    const [sortDirection, setSortDirection] = useState(true);
    
    useEffect(() => {
        setSubmittedList(props.data.submittedTimesheetsList);
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
              <table id="tblSubmTimesheetsReport" className="pages-tbl">
                  <thead>
                    <h2 className='h2-table'>{props.texts && props.texts['relatorio.timesheets.page.submittedts'] ? props.texts['relatorio.timesheets.page.submittedts'] : 'Pending of Approval Timesheets'}</h2>
                      <tr>
                        {headers.map((item) => {
                          const isSortable = hasToShow(item.id);
                          const isSorted = sortColumn === item.value;
                          const arrowIcon = isSorted ? (
                            sortDirection ? (
                              <ArrowDownIcon label="" />
                            ) : (
                              <ArrowUpIcon label="" />
                            )
                          ) : null;

                          return isSortable ? (
                            <th onClick={() => sortTableSub(item.value, item.dataType)}>
                              {item.name} {arrowIcon}
                            </th>
                          ) : null;
                        })}
                      </tr>
                  </thead>
                  <tbody>
                      {
                          (submittedList && submittedList.length > 0 && (
                              submittedList.map((item) => {
                                  return (
                                      <tr key={item.id}>
                                          { hasToShow(0) && ( <td>{utils.formatPeriodDate(new Date(item.periodStartDateISO + ' 00:00:00'))} - {utils.formatPeriodDate(new Date(item.periodEndDateISO + ' 00:00:00'))}</td> ) }
                                          { hasToShow(1) && ( <td>{item.usuario}</td> ) }
                                          { hasToShow(2) && ( <td>{item.team}</td> ) }
                                          { hasToShow(3) && ( <td>{item.approver}</td> ) }
                                          { hasToShow(4) && ( <td>{Number(item.hoursTempo).toFixed(2)}</td> ) }
                                          { hasToShow(5) && ( <td>{((item.horasPonto == '?') ? item.horasPonto : Number(item.horasPonto).toFixed(2))}</td> ) }
                                          { hasToShow(6) && ( <td>{item.reason}</td> ) }
                                          { hasToShow(7) && ( <td>{item.actionDate}</td> ) }
                                      </tr>
                                  );
                              })
                          )) || (
                              (!submittedList || submittedList.length === 0) && (
                                <tr>
                                  <td colSpan={selectedColumns.length} className="no-data-cell">
                                    <h1>{props.texts ? props.texts['relatorio.timesheets.page.noshowdata'] : 'No data for this interval'}</h1>
                                  </td>
                                </tr>
                              ))

                      }
                  </tbody>
              </table>
            </div>
    );

    function sortTableSub(column, dataType) {
      utils.showBlock(true);
      console.log("Ordenando: " + column);
      console.log("DataType: " + dataType);
      console.log("Carregando");
    
      const table = document.getElementById("tblSubmTimesheetsReport");
      const rows = Array.from(table.rows).slice(1);
      const sortedRows = rows.sort((rowA, rowB) => {
        const cellA = rowA.cells[headers.findIndex(header => header.value === column)];
        const cellB = rowB.cells[headers.findIndex(header => header.value === column)];
    
        const cmpA = getValueToCompare(cellA.innerHTML, dataType);
        const cmpB = getValueToCompare(cellB.innerHTML, dataType);
    
        if (sortDirection) {
          return cmpA > cmpB ? 1 : -1;
        } else {
          return cmpA < cmpB ? 1 : -1;
        }
      });
    
      sortedRows.forEach((row) => {
        table.tBodies[0].appendChild(row);
      });
    
      setSortColumn(column);
      setSortDirection(!sortDirection);
    
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

    function sortTableSub(column) {
      console.log("Ordenando: " + column);
      debugger;
      var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
      table = document.getElementById("tblSubmTimesheetsReport");
      switching = true;

      if (sortColumn === column) {
        setSortDirection(!sortDirection);
      } else {
        setSortDirection(true);
      }

      dir = sortDirection ? "asc" : "desc";

      while (switching) {
        switching = false;
        rows = table.rows;

        for (i = 1; i < (rows.length - 1); i++) {
          shouldSwitch = false;

          x = rows[i].getElementsByTagName("TD")[headers.findIndex(header => header.value === column)];
          y = rows[i + 1].getElementsByTagName("TD")[headers.findIndex(header => header.value === column)];
          if (dir === "asc") {
            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
              shouldSwitch = true;
              break;
            }
          } else if (dir === "desc") {
            if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
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
          if (switchcount === 0 && dir === "asc") {
            dir = "desc";
            switching = true;
          }
        }
      }

      setSortColumn(column);
      setSortDirection(!sortDirection);
    }
*/
}