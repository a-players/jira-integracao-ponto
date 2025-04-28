import React from 'react';
import { PDFDownloadLink, Page, Text, Document, StyleSheet } from '@react-pdf/renderer';

// <PDFExport title = 'pesquisa'/>
export default function PDFExport({title}) {
    const styles = StyleSheet.create({
        page: {
          flexDirection: 'column',
          alignItems: 'center',
          margin: 50,
        },
        title: {
          fontSize: 24,
          marginBottom: 30,
        },
      });
    
    const PdfDocument = () => (
        <Document>
            <Page style={styles.page}>
                <Text style={styles.title}>
                    {title}
                </Text>
            </Page>
        </Document> 
    );
}

import React from 'react';

const Table = ({ data }) => {
  return (
    <table>
      <thead>
        <tr>
          <th>Coluna 1</th>
          <th>Coluna 2</th>
          <th>Coluna 3</th>
        </tr>
      </thead>
      <tbody>
        {data.map((row, index) => (
          <tr key={index}>
            <td>{row.column1}</td>
            <td>{row.column2}</td>
            <td>{row.column3}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export { Table };