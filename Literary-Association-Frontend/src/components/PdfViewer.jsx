import React, { useState } from 'react'
import { Document, Page } from 'react-pdf/dist/esm/entry.webpack'
import 'react-pdf/dist/esm/Page/AnnotationLayer.css'
import { Button, ButtonToolbar } from 'react-bootstrap'
import { ArrowLeft, ArrowRight } from 'react-bootstrap-icons'

const PdfViewer = ({ pdf }) => {
    const [numPages, setNumPages] = useState(null)
    const [pageNumber, setPageNumber] = useState(1)

    const changePage = (offset) => {
        let newPage = pageNumber + offset

        if (newPage > numPages) {
            newPage = numPages
        } else if (newPage < 1) {
            newPage = 1
        }
        setPageNumber(newPage)
    }

    return (
        <div>
            <Document file={'data:application/pdf;base64,' + pdf}
                      onLoadSuccess={({ numPages }) => setNumPages(numPages)}>
                <Page pageNumber={pageNumber} width={600}/>
            </Document>
            <ButtonToolbar>
                <Button type='button' variant='secondary' onClick={() => changePage(-1)}><ArrowLeft/></Button>
                &nbsp;
                <p>Page {pageNumber} of {numPages}</p>
                &nbsp;
                <Button type='button' variant='secondary' onClick={() => changePage(1)}><ArrowRight/></Button>
            </ButtonToolbar>
        </div>
    )
}

export default PdfViewer